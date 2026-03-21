package com.dec.decisland.item.custom.accessory

import com.dec.decisland.entity.projectile.StreamEnergyBall
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class DiamondRingItem(properties: Properties) : Item(properties), AccessoryProcItem {
    override val accessoryProcChance: Double = 0.3
    override val accessoryProcCooldownTicks: Long = 24L

    override fun triggerAccessoryProc(serverLevel: ServerLevel, player: Player, weaponStack: ItemStack, accessoryStack: ItemStack) {
        repeat(2) {
            spawnAccessoryProjectile(serverLevel, player, StreamEnergyBall(serverLevel, player, weaponStack))
        }
    }
}
