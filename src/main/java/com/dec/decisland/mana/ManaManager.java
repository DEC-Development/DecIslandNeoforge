package com.dec.decisland.mana;

import com.dec.decisland.attachment.ModAttachments;
import com.dec.decisland.network.ManaSyncPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class ManaManager {
    public static float getCurrentMana(Player player) {
        return player.getData(ModAttachments.CURRENT_MANA.get());
    }
    public static float getMaxMana(Player player) {
        return player.getData(ModAttachments.MAX_MANA.get());
    }
    public static boolean setMana(Player player, float mana) {
        float newMana = mana;
        boolean adjusted = false;
        if (newMana < 0) {
            newMana = 0;
            adjusted = true;
        }
        player.setData(ModAttachments.CURRENT_MANA.get(), newMana);
        return adjusted;
    }
    public static float reduceMana(Player player, float amount) {
        if (player.level().isClientSide()) return 0; // 只在服务端执行同步
        float mana = getCurrentMana(player);
        float newMana = mana - amount;
        float adjustedMana = amount;
        if (newMana < 0) {
            newMana = 0;
            adjustedMana = mana;
        }
        setMana(player, newMana);
        PacketDistributor.sendToPlayer((net.minecraft.server.level.ServerPlayer) player,
                new ManaSyncPayload(newMana, getMaxMana(player)));
        return adjustedMana;
    }
}
