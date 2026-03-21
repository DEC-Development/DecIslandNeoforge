package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.StormFuse
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

class StormStaff(properties: Properties) : ProjectileStaff(properties) {
    override val manaCost: Float = 9.0f

    override fun getCastSound(): SoundEvent = SoundEvents.CONDUIT_ACTIVATE

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        spawnProjectile(serverLevel, source, stack, ::StormFuse, 3.0f, 0.0f)
    }
}
