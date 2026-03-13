package com.dec.decisland.mana;

import com.dec.decisland.DecIsland;
import com.dec.decisland.attachment.ModAttachments;
import com.dec.decisland.network.ManaSyncPayload;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;

@EventBusSubscriber(modid = DecIsland.MOD_ID)
public class ManaSystem {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        initManaData(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        initManaData(event.getEntity());
    }

    private static void initManaData(Player player) {
        if (!player.level().isClientSide()) {
            float maxMana = player.getData(ModAttachments.MAX_MANA.get());
            float currentMana = player.getData(ModAttachments.CURRENT_MANA.get());
            if (currentMana > maxMana) {
                player.setData(ModAttachments.CURRENT_MANA.get(), maxMana);
            }
            // 初始化 prev_magic 为当前魔法值
            player.setData(ModAttachments.PREV_MAGIC.get(), currentMana);
            // 初始化 magic_gap 为默认等待时间
            player.setData(ModAttachments.MAGIC_GAP.get(), WAIT_TICK);

            // 发送初始mana值到客户端
            PacketDistributor.sendToPlayer((ServerPlayer) player,
                    new ManaSyncPayload(currentMana, maxMana));
        }
    }

    // ---------- 常量配置（与你的 TypeScript 一致）----------
    public static final int WAIT_TICK = 60;                     // 基础等待 tick 数
    private static final double K2 = 0.001;
    private static final double K1 = 1 - K2 + 0.01;             // = 1.009
    private static final double G_MIN = (1 - K1) / K2;          // = -9
    private static final double MAGICGAIN_MAP_K = 1.05;
    private static final double MAGICGAIN_MAP_K2 = (-G_MIN + 1) / MAGICGAIN_MAP_K; // ≈ 9.5238

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return; // 只在服务端执行

        // 读取附件数据
        float maxMana = player.getData(ModAttachments.MAX_MANA.get());
        float magicGain = player.getData(ModAttachments.MANA_GAIN_LEVEL.get());       // 原 magicgain
        int magicReckon = player.getData(ModAttachments.MANA_RECKON.get()); // 原 magicreckon
        float currentMana = player.getData(ModAttachments.CURRENT_MANA.get());   // 原 magicpoint
        int magicGap = player.getData(ModAttachments.MAGIC_GAP.get());           // 原 magic_gap
        float prevMagic = player.getData(ModAttachments.PREV_MAGIC.get());       // 原 dec_magic_store

        player.displayClientMessage(
                Component.literal("Mana: " + currentMana + "/" + maxMana),
                true
        );

        // ========== 魔法恢复逻辑 ==========
        if (currentMana < maxMana) {
            if (magicGap <= 0) {
                // 恢复 1 点魔法值
                float newMana = Math.min(currentMana + 1, maxMana);
                player.setData(ModAttachments.CURRENT_MANA.get(), newMana);

                // 同步mana值到客户端
                PacketDistributor.sendToPlayer((ServerPlayer) player,
                        new ManaSyncPayload(newMana, maxMana));

                // 生成增加粒子（原 this.getExDimension().spawnParticle）
                spawnManaParticle(player, "increase");

                // 计算新的 magic_gap
                double mappedGain = magicGainMap(magicGain);
                double base = 1.0 / (K1 + K2 * mappedGain);
                double exponent = magicReckon; // 注意指数就是 magicreckon
                int newGap = (int) (WAIT_TICK * Math.pow(base, exponent));
                if (newGap < 1) newGap = 1;
                player.setData(ModAttachments.MAGIC_GAP.get(), newGap);
            } else {
                // 减少等待计数
                player.setData(ModAttachments.MAGIC_GAP.get(), magicGap - 1);
            }

            // 每 tick 增加 magicreckon
            player.setData(ModAttachments.MANA_RECKON.get(), magicReckon + 1);
        } else if (magicReckon != 0) {
            // 魔法值已满，重置计时器
            player.setData(ModAttachments.MANA_RECKON.get(), 0);
            player.setData(ModAttachments.MAGIC_GAP.get(), WAIT_TICK);
        }

        // ========== 检测魔法值减少 ==========
        // 这里是关键：如果prevMagic > currentMana，说明魔法值被外部操作减少了（如使用法杖）
        if (prevMagic > currentMana && Math.abs(prevMagic - currentMana) > 0.001) { // 使用小的容差避免浮点精度问题
            // 同步mana值到客户端
            PacketDistributor.sendToPlayer((ServerPlayer) player,
                    new ManaSyncPayload(currentMana, maxMana));

            // 重置 magicreckon
            player.setData(ModAttachments.MANA_RECKON.get(), 0);

            // 如果之前的 magicreckon 达到等待时间，播放减少粒子
            if (magicReckon >= WAIT_TICK) {
                spawnManaParticle(player, "decrease");
            }
            // 原代码未重置 magic_gap，这里保持
        }

        // 更新上一 tick 的魔法值
        player.setData(ModAttachments.PREV_MAGIC.get(), currentMana);
    }

    // ---------- 辅助方法 ----------

    /**
     * 映射 magicgain 的函数，与你的 TypeScript 版本一致
     */
    private static double magicGainMap(float magicGain) {
        if (magicGain < 1) {
            return MAGICGAIN_MAP_K2 * Math.pow(MAGICGAIN_MAP_K, magicGain) + G_MIN;
        } else {
            return magicGain;
        }
    }

    /**
     * 生成粒子效果（暂时使用原版粒子代替自定义粒子）
     * 你可以后续注册自己的粒子并替换这里的 ParticleTypes
     */
    private static void spawnManaParticle(Player player, String type) {
        if (player.level() instanceof ServerLevel serverLevel) {
            var particle = type.equals("increase")
                    ? ParticleTypes.HAPPY_VILLAGER   // 原 dec:magic_increase_particle
                    : ParticleTypes.SMOKE;           // 原 dec:magic_decrease_particle
            serverLevel.sendParticles(particle,
                    player.getX(), player.getY() + 1.5, player.getZ(),
                    5, 0.2, 0.2, 0.2, 0.1);
        }
    }
}