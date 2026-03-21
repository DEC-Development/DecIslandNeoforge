package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.NightmareRay
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class Nightmare(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        val serverLevel = level as? ServerLevel ?: return InteractionResult.FAIL
        if (ManaManager.getCurrentMana(player) <= MANA_COST) {
            return InteractionResult.FAIL
        }

        ManaManager.reduceMana(player, MANA_COST)
        serverLevel.playSound(null, player.x, player.y, player.z, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f)

        Projectile.spawnProjectileUsingShoot(
            ::NightmareRay,
            serverLevel,
            player.getItemInHand(hand),
            player,
            player.getViewVector(0.0f).x,
            player.getViewVector(0.0f).y,
            player.getViewVector(0.0f).z,
            RAY_BASE_SPEED,
            RAY_INACCURACY,
        )

        player.getItemInHand(hand).hurtAndBreak(1, player, hand.asEquipmentSlot())
        player.swing(hand, true)
        return InteractionResult.SUCCESS_SERVER
    }

    companion object {
        private const val MANA_COST: Float = 10.0f
        private const val RAY_BASE_SPEED: Float = 0.7f
        private const val RAY_INACCURACY: Float = 1.3f
    }
}
