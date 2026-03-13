package com.dec.decisland.item.custom;

import com.dec.decisland.api.ModItemEventTrigger;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.IItemExtension;

import java.util.List;

public abstract class Katana extends Item implements IItemExtension, ModItemEventTrigger {
    protected static final String ATTACK_COUNTER_KEY = "AttackCounter";
    protected static final String LAST_ATTACK_TIME_KEY = "LastAttackTime";
    public Katana(Properties properties) {
        super(properties);
    }
    protected int getMaxAttackCount() {
        return 7;
    }
    protected long getResetTimeMs() {
        return 5000;
    }

    public float getUseSkillRadius() {
        return 2.0f;
    }

    public int getUseSkillBreakAmount() {
        return 5;
    }

    public float getUseSkillKnockback() {
        return 0.4f;
    }

    /**
     * 在视野前方生成粒子
     *
     * @param serverLevel
     * @param x
     * @param y
     * @param z
     */
    public void useSpawnParticle(ServerLevel serverLevel, double x, double y, double z) {
        // 具体的粒子逻辑在这里
    }

    /**
     * 服务端逻辑
     *
     * @param serverLevel
     * @param source
     */
    public void useServer(ServerLevel serverLevel, LivingEntity source) {
        // 具体的服务端逻辑在这里
    }

    public float getAttackSkillRadius() {
        return 1.1f;
    }

    public int getAttackSkillBreakAmount() {
        return 3;
    }

    public float getAttackSkillKnockback() {
        return 0.4f;
    }

    /**
     * 在视野前方生成粒子
     *
     * @param serverLevel
     * @param x
     * @param y
     * @param z
     */
    public void attackSpawnParticle(ServerLevel serverLevel, double x, double y, double z) {
        // 具体的粒子逻辑在这里
    }

    /**
     * 服务端逻辑
     *
     * @param serverLevel
     * @param source
     */
    public void attackServer(ServerLevel serverLevel, LivingEntity source) {
        // 具体的服务端逻辑在这里
    }

    public boolean onAttackTriggerSweep(ItemStack stack) {
        return true;
    }

    public float getBaseDamage(LivingEntity entity) {
        float finalDamage = (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
//        entity.level().players().get(0).displayClientMessage(Component.literal("level type is server:" + (entity.level() instanceof ServerLevel)), false);
//        entity.level().players().get(0).displayClientMessage(Component.literal("Damage: " + finalDamage), false);
        return finalDamage;
    }

    public float getUseSkillBonusDamage() {
        return 0.0f;
    }

    public float getAttackSkillBonusDamage() {
        return 0.0f;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        this.sweepAttack(level, player, hand,
                this.getUseSkillRadius(), this.getUseSkillKnockback(), this.getUseSkillBreakAmount(),
                this.getBaseDamage(player), this.getUseSkillBonusDamage(),
                (lvl, x, y, z) -> this.useSpawnParticle(lvl, x, y, z), // 粒子回调
                (svr, ply) -> this.useServer(svr, ply));               // 服务端回调
        return InteractionResult.SUCCESS;
    }

//    @Override
//    public boolean onAttack(LivingEntity source, Entity target, ItemStack stack, InteractionHand hand) {
//        boolean isClient = source.level().isClientSide();
//        boolean triggerSweep;
//
//        if (isClient) {
//            // 客户端：从 CUSTOM_DATA 组件中读取服务器写入的标志
//            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
//            if (customData != null) {
//                CompoundTag tag = customData.copyTag(); // 获取 NBT 副本
//                if (tag.contains("decisland_sweep_trigger")) {
//                    triggerSweep = tag.getBoolean("decisland_sweep_trigger").get();
//                    // 使用后立即删除该组件，避免残留
//                    stack.set(DataComponents.CUSTOM_DATA, null);
//                } else {
//                    // 理论上不应发生，作为后备调用原方法
//                    triggerSweep = this.onAttackTriggerSweep();
//                }
//            } else {
//                triggerSweep = this.onAttackTriggerSweep(); // 后备
//            }
//        } else {
//            // 服务器端：调用原方法决定结果，并将结果写入 CUSTOM_DATA
//            triggerSweep = this.onAttackTriggerSweep();
//            CompoundTag tag = new CompoundTag();
//            tag.putBoolean("decisland_sweep_trigger", triggerSweep);
//            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
//        }
//
//        // 如果决定不触发横扫，直接返回
//        if (!triggerSweep) return false;
//
////        // 调试消息（可选）
////        if (source instanceof Player player) {
////            player.displayClientMessage(Component.literal("level type is server:" + (player.level() instanceof ServerLevel)), false);
////        }
//
//        // 执行横扫攻击
//        this.sweepAttack(source.level(), source, hand,
//                this.getAttackSkillRadius(), this.getAttackSkillKnockback(), this.getAttackSkillBreakAmount(),
//                this.getBaseDamage(source), this.getAttackSkillBonusDamage(),
//                (lvl, x, y, z) -> this.attackSpawnParticle(lvl, x, y, z),
//                (svr, ply) -> this.attackServer(svr, ply));
//
//        return true;
//    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // 获取或创建CustomData组件
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag tag;

        if (customData == null) {
            tag = new CompoundTag();
        } else {
            tag = customData.copyTag();
        }

        // 获取当前时间
        long currentTime = System.currentTimeMillis();

        // 检查是否需要重置计数器
        boolean shouldReset = false;
        if (tag.contains(LAST_ATTACK_TIME_KEY)) {
            long lastAttackTime = tag.getLong(LAST_ATTACK_TIME_KEY).get();
            if (currentTime - lastAttackTime > this.getResetTimeMs()) {
                shouldReset = true;
            }
        }

        // 获取当前攻击计数
        int attackCount = shouldReset ? 0 : (tag.contains(ATTACK_COUNTER_KEY) ? tag.getInt(ATTACK_COUNTER_KEY).get() : 0);

        // 增加攻击计数
        attackCount++;
        tag.putInt(ATTACK_COUNTER_KEY, attackCount);
        tag.putLong(LAST_ATTACK_TIME_KEY, currentTime);

        // 创建新的CustomData并设置回物品
        CustomData updatedData = CustomData.of(tag);
        stack.set(DataComponents.CUSTOM_DATA, updatedData);

//        if (attacker instanceof Player player) {
//            player.displayClientMessage(Component.literal("攻击次数：" + attackCount), true);
//        }

        // 检查是否达到特殊攻击条件
        boolean triggerSweep;

        // 如果决定不触发横扫，直接返回
        triggerSweep = this.onAttackTriggerSweep(stack);
        if (!triggerSweep) return;

//        // 调试消息（可选）
//        if (source instanceof Player player) {
//            player.displayClientMessage(Component.literal("level type is server:" + (player.level() instanceof ServerLevel)), false);
//        }

        // 执行横扫攻击
        InteractionHand hand = null;
        if (attacker instanceof Player player) {
            hand = player.getUsedItemHand();
        }
        this.sweepAttack(attacker.level(), attacker, hand,
                this.getAttackSkillRadius(), this.getAttackSkillKnockback(), this.getAttackSkillBreakAmount(),
                this.getBaseDamage(attacker), this.getAttackSkillBonusDamage(),
                (lvl, x, y, z) -> this.attackSpawnParticle(lvl, x, y, z),
                (svr, ply) -> this.attackServer(svr, ply));


        // 检查是否达到最大次
        if (attackCount >= this.getMaxAttackCount()) {
            // 重置计数器
            stack.set(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        }
    }

    public void sweepAttack(Level level, LivingEntity source, InteractionHand hand,
                            float radius, float knockbackStrength, int breakAmount, float baseDamage, float bonusDamage,
                            ParticleAction particleAction, ServerAction serverAction) {

        if (level.isClientSide()) {
            source.playSound(SoundEvents.PLAYER_ATTACK_SWEEP);
        } else {
            if (source instanceof Player player) {
                ItemStack itemStack = player.getItemInHand(hand); // 修正: 使用 getItemInHand 替代已弃用的 getUseItem
                itemStack.hurtAndBreak(breakAmount, player, hand.asEquipmentSlot());
            }

            // 1. 粒子效果 (客户端和服务端都会执行，视具体实现而定)
            double x = source.getX() + source.getViewVector(0.0f).x;
            double y = source.getEyeY() + source.getViewVector(0.0f).y;
            double z = source.getZ() + source.getViewVector(0.0f).z;

            // 调用粒子回调
            if (particleAction != null && level instanceof ServerLevel serverLevel) {
                particleAction.spawn(serverLevel, x, y, z);
            }

            // 调用服务端回调
            if (level instanceof ServerLevel serverLevel) {
                if (serverAction != null) {
                    serverAction.execute(serverLevel, source);
                }


                // 3. 伤害判定逻辑 (这部分通常只在服务端计算并同步，或者两边都算)
                double entityReachSq = 0;
                if (source instanceof Player player) {
                    entityReachSq = Mth.square(player.entityInteractionRange());
                } else {
                    entityReachSq = Mth.square(4);
                }
                float a = radius;

                // 获取范围内的实体
                // 注意：原版AABB构造通常涉及min和max，这里的构造看起来是为了获取前方的一个点然后膨胀
                AABB aabb = new AABB(
                        source.getX() + a * source.getViewVector(0.0f).x,
                        source.getY() + a * source.getViewVector(0.0f).y,
                        source.getZ() + a * source.getViewVector(0.0f).z,
                        source.getX() + a * source.getViewVector(0.0f).x,
                        source.getY() + a * source.getViewVector(0.0f).y,
                        source.getZ() + a * source.getViewVector(0.0f).z
                ).inflate(a);

                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);

                Vec3 center = new Vec3(
                        source.getX() + a * source.getViewVector(0.0f).x,
                        source.getY() + a * source.getViewVector(0.0f).y,
                        source.getZ() + a * source.getViewVector(0.0f).z);

                for (LivingEntity livingentity : entities) {
                    // 再次检查距离，因为inflate可能包含了一些远处的实体
                    if (livingentity.position().distanceToSqr(center) <= a * a) {
                        if (livingentity != source
                                && !source.isAlliedTo(livingentity)
                                && !(livingentity instanceof ArmorStand armorstand && armorstand.isMarker())
                                && source.distanceToSqr(livingentity) < entityReachSq) {


                            // 1. 获取 AttributeInstance，可能为 null
                            AttributeInstance sweepingAttr = source.getAttribute(Attributes.SWEEPING_DAMAGE_RATIO);

                            // 2. 如果不为 null 则获取值，否则使用默认值 0.0F
                            float sweeping_damage_ratio = sweepingAttr != null ? (float) sweepingAttr.getValue() : 0.0F;

                            float f1 = 1.0F + sweeping_damage_ratio * baseDamage + bonusDamage;
                            DamageSource damageSource = level.damageSources().mobAttack(source);
                            if (source instanceof Player player) {
                                damageSource = level.damageSources().playerAttack(player);
                            }

                            if (livingentity.hurtServer(serverLevel, damageSource, f1)) {
                                livingentity.knockback(knockbackStrength, Mth.sin(source.getYRot() * (float) (Math.PI / 180.0)), -Mth.cos(source.getYRot() * (float) (Math.PI / 180.0)));
                                EnchantmentHelper.doPostAttackEffects(serverLevel, livingentity, damageSource);
                            }
                        }
                    }
                }

                // 计算方向向量（原代码末尾部分，未使用，保留以防）
                double d0 = -Mth.sin(source.getYRot() * (float) (Math.PI / 180.0));
                double d1 = Mth.cos(source.getYRot() * (float) (Math.PI / 180.0));

            }
        }
    }
}

@FunctionalInterface
interface ParticleAction {
    void spawn(ServerLevel serverLevel, double x, double y, double z);
}

@FunctionalInterface
interface ServerAction {
    void execute(ServerLevel serverLevel, LivingEntity source);
}
