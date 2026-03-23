package com.dec.decisland.item.custom

import com.dec.decisland.events.AccessoryCombatEffects
import com.dec.decisland.entity.projectile.ThrownAshPufferfish
import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.Level

class AshPufferfish(props: Item.Properties) : Item(props), ProjectileItem {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val itemStack = player.getItemInHand(hand)
        level.playSound(
            null,
            player.x,
            player.y,
            player.z,
            SoundEvents.SNOWBALL_THROW,
            SoundSource.NEUTRAL,
            0.5f,
            0.4f / (level.random.nextFloat() * 0.4f + 0.8f),
        )
        if (level is ServerLevel) {
            val projectile = ThrownAshPufferfish(level, player, itemStack.copyWithCount(1))
            projectile.setPos(player.x, player.eyeY - 0.1, player.z)
            projectile.shootFromRotation(
                player,
                player.xRot,
                player.yRot,
                0.0f,
                PROJECTILE_SHOOT_POWER,
                PROJECTILE_INACCURACY,
            )
            if (!level.addFreshEntity(projectile)) {
                return InteractionResult.FAIL
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this))
        AccessoryCombatEffects.onSuccessfulWeaponUse(player, itemStack)
        player.swing(hand, true)
        itemStack.consume(1, player)
        return if (level.isClientSide) InteractionResult.SUCCESS else InteractionResult.SUCCESS_SERVER
    }

    override fun asProjectile(level: Level, position: Position, itemStack: ItemStack, direction: Direction): Projectile =
        ThrownAshPufferfish(level, position.x(), position.y(), position.z(), itemStack)

    companion object {
        @JvmField
        val PROJECTILE_SHOOT_POWER: Float = 0.9f

        @JvmField
        val PROJECTILE_INACCURACY: Float = 1.0f
    }
}
