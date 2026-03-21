package com.dec.decisland.item.custom.accessory

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack

interface AccessoryProcItem {
    val accessoryProcChance: Double
    val accessoryProcCooldownTicks: Long
    val accessoryProcCooldownKey: String
        get() = "ring"

    fun triggerAccessoryProc(serverLevel: ServerLevel, player: Player, weaponStack: ItemStack, accessoryStack: ItemStack)

    fun spawnAccessoryProjectile(
        serverLevel: ServerLevel,
        player: Player,
        projectile: Projectile,
        velocity: Float = 0.8f,
        inaccuracy: Float = 1.0f,
    ) {
        projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0f, velocity, inaccuracy)
        serverLevel.addFreshEntity(projectile)
    }
}
