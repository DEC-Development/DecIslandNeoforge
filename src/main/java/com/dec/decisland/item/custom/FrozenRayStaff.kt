package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.FrozenRay
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class FrozenRayStaff(properties: Properties) : ProjectileStaff(properties) {
    override val manaCost: Float = 4.0f

    override fun getCastSound(): SoundEvent = SoundEvents.SNOW_BREAK

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        spawnProjectile(serverLevel, source, stack, ::FrozenRay, 0.3f, 2.0f)
    }
}
