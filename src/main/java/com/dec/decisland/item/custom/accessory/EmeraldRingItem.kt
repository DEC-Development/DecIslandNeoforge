package com.dec.decisland.item.custom.accessory

import com.dec.decisland.entity.projectile.PureEnergyBall
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class EmeraldRingItem(properties: Properties) : Item(properties), AccessoryProcItem {
    override val accessoryProcChance: Double = 0.2
    override val accessoryProcCooldownTicks: Long = 10L

    override fun triggerAccessoryProc(serverLevel: ServerLevel, player: Player, weaponStack: ItemStack, accessoryStack: ItemStack) {
        spawnAccessoryProjectile(serverLevel, player, PureEnergyBall(serverLevel, player, weaponStack))
    }
}
