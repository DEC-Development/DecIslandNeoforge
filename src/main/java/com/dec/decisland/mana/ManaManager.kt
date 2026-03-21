package com.dec.decisland.mana

import com.dec.decisland.attachment.ModAttachments
import com.dec.decisland.network.ManaSyncPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.min

object ManaManager {
    @JvmStatic
    fun getCurrentMana(player: Player): Float = player.getData(ModAttachments.CURRENT_MANA.get())

    @JvmStatic
    fun getMaxMana(player: Player): Float = player.getData(ModAttachments.MAX_MANA.get())

    @JvmStatic
    fun setMana(player: Player, mana: Float): Boolean {
        var newMana = mana
        var adjusted = false
        if (newMana < 0.0f) {
            newMana = 0.0f
            adjusted = true
        }
        player.setData(ModAttachments.CURRENT_MANA.get(), newMana)
        return adjusted
    }

    @JvmStatic
    fun reduceMana(player: Player, amount: Float): Float {
        if (player.level().isClientSide) {
            return 0.0f
        }

        val mana = getCurrentMana(player)
        var newMana = mana - amount
        var adjustedMana = amount
        if (newMana < 0.0f) {
            newMana = 0.0f
            adjustedMana = mana
        }

        setMana(player, newMana)
        PacketDistributor.sendToPlayer(
            player as ServerPlayer,
            ManaSyncPayload(newMana, getMaxMana(player)),
        )
        return adjustedMana
    }

    @JvmStatic
    fun addMana(player: Player, amount: Float): Float {
        if (player.level().isClientSide) {
            return 0.0f
        }

        val currentMana = getCurrentMana(player)
        val maxMana = getMaxMana(player)
        val newMana = min(currentMana + amount, maxMana)
        val addedMana = newMana - currentMana
        if (addedMana <= 0.0f) {
            return 0.0f
        }

        setMana(player, newMana)
        PacketDistributor.sendToPlayer(
            player as ServerPlayer,
            ManaSyncPayload(newMana, maxMana),
        )
        return addedMana
    }

    @JvmStatic
    fun addManaIgnoringMax(player: Player, amount: Float): Float {
        if (player.level().isClientSide) {
            return 0.0f
        }

        if (amount <= 0.0f) {
            return 0.0f
        }

        val currentMana = getCurrentMana(player)
        val newMana = currentMana + amount
        setMana(player, newMana)
        PacketDistributor.sendToPlayer(
            player as ServerPlayer,
            ManaSyncPayload(newMana, getMaxMana(player)),
        )
        return amount
    }
}
