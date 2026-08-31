package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.dart.DartEntity
import com.dec.decisland.entity.projectile.dart.ModDarts
import com.dec.decisland.events.AccessoryCombatEffects
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

// Void Whispering: right click consumes mana, thrusts, and throws the dagger as a projectile.
class VoidWhisperingDaggerItem(
    properties: Properties,
    config: DaggerConfig,
) : DaggerItem(properties, config) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.getItemInHand(hand)
        if (player.cooldowns.isOnCooldown(stack)) {
            return InteractionResult.FAIL
        }
        if (ManaManager.getCurrentMana(player) <= MANA_COST) {
            return InteractionResult.FAIL
        }

        if (level.isClientSide) {
            player.swing(hand, true)
            return InteractionResult.SUCCESS
        }

        val serverLevel = level as ServerLevel
        serverLevel.playSound(null, player.x, player.y, player.z, SoundEvents.SNOW_GOLEM_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f)
        performThrust(serverLevel, player, stack)
        ManaManager.reduceMana(player, MANA_COST)

        val projectile = DartEntity(ModDarts.VOID_WHISPERING_DAGGER.entityType(), serverLevel, player, stack.copyWithCount(1))
        projectile.setPos(player.x, player.eyeY - 0.1, player.z)
        projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0f, THROW_POWER, THROW_UNCERTAINTY)
        serverLevel.addFreshEntity(projectile)

        player.awardStat(Stats.ITEM_USED.get(this))
        AccessoryCombatEffects.onSuccessfulWeaponUse(player, stack)
        player.swing(hand, true)
        return InteractionResult.SUCCESS_SERVER
    }

    companion object {
        private const val MANA_COST: Float = 4.0f
        private const val THROW_POWER: Float = 1.5f
        private const val THROW_UNCERTAINTY: Float = 8.0f
    }
}
