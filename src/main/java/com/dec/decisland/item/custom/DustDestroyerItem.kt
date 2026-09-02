package com.dec.decisland.item.custom

import com.dec.decisland.entity.custom.WitherCloudEntity
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class DustDestroyerItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }
        val serverLevel = level as? ServerLevel ?: return InteractionResult.PASS
        if (ManaManager.getCurrentMana(player) <= MANA_COST) {
            return InteractionResult.FAIL
        }
        ManaManager.reduceMana(player, MANA_COST)
        val view = player.getViewVector(0.0f)
        for (distance in CLOUD_DISTANCES) {
            val pos = player.position().add(view.x * distance, view.y * distance, view.z * distance)
            serverLevel.addFreshEntity(WitherCloudEntity(serverLevel, player, pos.x, pos.y, pos.z))
        }
        player.swing(hand, true)
        return InteractionResult.SUCCESS_SERVER
    }

    companion object {
        private const val MANA_COST: Float = 1.0f
        private val CLOUD_DISTANCES = listOf(5.0, 3.0)
    }
}