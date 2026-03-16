package com.dec.decisland.item.custom.dart

import com.dec.decisland.entity.projectile.dart.DartDefinition
import com.dec.decisland.entity.projectile.dart.DartEntity
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
import java.util.function.Function

class Dart(
    properties: Item.Properties,
    private val definition: DartDefinition,
) : Item(properties), ProjectileItem {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val itemStack = player.getItemInHand(hand)
        if (player.cooldowns.isOnCooldown(itemStack)) {
            return InteractionResult.FAIL
        }

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
            val projectile = DartEntity(definition.entityType(), level, player, itemStack.copyWithCount(1))
            projectile.setPos(player.x, player.eyeY - 0.1, player.z)
            projectile.shootFromRotation(
                player,
                player.xRot,
                player.yRot,
                0.0f,
                definition.itemSettings.power,
                definition.itemSettings.uncertainty,
            )
            level.addFreshEntity(projectile)
        }

        player.awardStat(Stats.ITEM_USED.get(this))
        player.cooldowns.addCooldown(itemStack, definition.itemSettings.cooldownTicks)
        player.swing(hand, true)
        itemStack.consume(1, player)
        return if (level.isClientSide) InteractionResult.SUCCESS else InteractionResult.SUCCESS_SERVER
    }

    override fun asProjectile(level: Level, position: Position, itemStack: ItemStack, direction: Direction): Projectile =
        DartEntity(definition.entityType(), level, position.x(), position.y(), position.z(), itemStack)

    companion object {
        @JvmStatic
        fun factory(definition: DartDefinition): Function<Item.Properties, out Item> =
            Function { properties -> Dart(properties, definition) }
    }
}
