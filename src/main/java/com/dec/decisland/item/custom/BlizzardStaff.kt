package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.BlizzardEnergy
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class BlizzardStaff(properties: Properties) : ProjectileStaff(properties) {
    override val manaCost: Float = 7.0f

    override fun getCastSound(): SoundEvent = SoundEvents.SNOW_BREAK

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        repeat(7) {
            spawnProjectile(serverLevel, source, stack, ::BlizzardEnergy, 0.02f, 30.0f)
        }
    }
}
