package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.EnergyBall
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class WoodenStaff(properties: Properties) : ProjectileStaff(properties) {
    override val manaCost: Float = 2.0f

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        spawnProjectile(serverLevel, source, stack, ::EnergyBall, 0.9f, 2.0f)
    }
}
