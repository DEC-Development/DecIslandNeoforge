package com.dec.decisland.entity.projectile

import com.dec.decisland.entity.ModEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class FrozenEnergyBall(entityType: EntityType<FrozenEnergyBall>, level: Level) :
    GeoParticleRayProjectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.FROZEN_ENERGY_BALL.get(), level) {
        setSpawnPositionFromOwner(owner)
    }

    override val baseDamage: Float = 3.0f
    override val projectileGravity: Double = 0.04
    override val airInertia: Double = 1.3
    override val waterInertia: Double = 1.0
    override val spawnForwardOffset: Double = 0.8

    override fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
        if (target is LivingEntity) {
            target.addEffect(MobEffectInstance(MobEffects.SLOWNESS, 200, 1))
        }
    }
}
