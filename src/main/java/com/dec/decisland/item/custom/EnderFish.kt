package com.dec.decisland.item.custom

import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class EnderFish(properties: Properties) : Item(properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val result = super.finishUsingItem(stack, level, livingEntity)
        val serverLevel = level as? ServerLevel ?: return result

        var teleported = false
        for (attempt in 0 until MAX_TELEPORT_ATTEMPTS) {
            val targetX = livingEntity.x + ((livingEntity.random.nextDouble() - 0.5) * HORIZONTAL_RANGE * 2.0)
            val targetY = Mth.clamp(
                livingEntity.y + ((livingEntity.random.nextDouble() - 0.5) * VERTICAL_RANGE * 2.0),
                serverLevel.minY.toDouble(),
                (serverLevel.maxY - 1).toDouble(),
            )
            val targetZ = livingEntity.z + ((livingEntity.random.nextDouble() - 0.5) * HORIZONTAL_RANGE * 2.0)
            if (livingEntity.randomTeleport(targetX, targetY, targetZ, true)) {
                teleported = true
                serverLevel.playSound(
                    null,
                    livingEntity.x,
                    livingEntity.y,
                    livingEntity.z,
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS,
                    1.0f,
                    1.0f,
                )
                break
            }
        }

        if (teleported && livingEntity is Player) {
            livingEntity.cooldowns.addCooldown(stack, COOLDOWN_TICKS)
        }

        return result
    }

    companion object {
        private const val HORIZONTAL_RANGE = 512.0
        private const val VERTICAL_RANGE = 8.0
        private const val COOLDOWN_TICKS = 5 * 20
        private const val MAX_TELEPORT_ATTEMPTS = 16
    }
}
