package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.EnergyRay
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class EnergyRayStaff(properties: Properties) : ProjectileStaff(properties) {
    override val manaCost: Float = 3.0f

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        spawnProjectile(serverLevel, source, stack, ::EnergyRay, 1.4f, 2.0f)
    }
}
