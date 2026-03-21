package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.AmethystEnergyRay
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class AmethystRayStaff(properties: Properties) : ProjectileStaff(properties) {
    override val manaCost: Float = 5.0f

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        spawnProjectile(serverLevel, source, stack, ::AmethystEnergyRay, 1.4f, 1.6f)
    }
}
