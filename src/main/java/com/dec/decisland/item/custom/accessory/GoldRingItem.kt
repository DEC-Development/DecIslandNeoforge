package com.dec.decisland.item.custom.accessory

import com.dec.decisland.entity.projectile.GoldenEnergyBall
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class GoldRingItem(properties: Properties) : Item(properties), AccessoryProcItem {
    override val accessoryProcChance: Double = 0.4
    override val accessoryProcCooldownTicks: Long = 20L

    override fun triggerAccessoryProc(serverLevel: ServerLevel, player: Player, weaponStack: ItemStack, accessoryStack: ItemStack) {
        spawnAccessoryProjectile(serverLevel, player, GoldenEnergyBall(serverLevel, player, weaponStack))
    }
}
