package com.dec.decisland.item.custom.accessory

import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HeartRingItem(properties: Properties) : Item(properties), AccessoryProcItem {
    override val accessoryProcChance: Double = 0.2
    override val accessoryProcCooldownTicks: Long = 100L

    override fun triggerAccessoryProc(serverLevel: ServerLevel, player: Player, weaponStack: ItemStack, accessoryStack: ItemStack) {
        player.heal(4.0f)
        if (player.random.nextDouble() < 0.5) {
            player.addEffect(MobEffectInstance(MobEffects.STRENGTH, 100, 0))
        }

        serverLevel.sendParticles(
            ParticleTypes.HEART,
            player.x,
            player.y + 1.0,
            player.z,
            6,
            0.35,
            0.2,
            0.35,
            0.01,
        )
        serverLevel.sendParticles(
            ParticleTypes.ENCHANT,
            player.x,
            player.y + 1.0,
            player.z,
            12,
            0.4,
            0.25,
            0.4,
            0.02,
        )
    }
}
