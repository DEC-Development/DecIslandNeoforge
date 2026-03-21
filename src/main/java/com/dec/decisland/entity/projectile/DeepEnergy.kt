package com.dec.decisland.entity.projectile

import com.dec.decisland.entity.ModEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class DeepEnergy(entityType: EntityType<DeepEnergy>, level: Level) :
    VisibleMagicBallProjectile(
        entityType,
        level,
        "deep_energy_wake_particle",
        "deep_energy_hit_particle",
    ) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.DEEP_ENERGY.get(), level) {
        setSpawnPositionFromOwner(owner)
    }

    override val baseDamage: Float = 10.0f
    override val airInertia: Double = 1.1
    override val waterInertia: Double = 1.0

    override fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
        spawnSlownessCloud(serverLevel, target.position())
    }

    override fun onBlockHit(serverLevel: ServerLevel, pos: Vec3) {
        spawnSlownessCloud(serverLevel, pos)
    }

    private fun spawnSlownessCloud(serverLevel: ServerLevel, center: Vec3) {
        val cloud = AreaEffectCloud(serverLevel, center.x, center.y, center.z)
        cloud.owner = owner as? LivingEntity
        cloud.radius = 4.0f
        cloud.duration = 40
        cloud.waitTime = 0
        cloud.radiusOnUse = 0.0f
        cloud.durationOnUse = 0
        cloud.radiusPerTick = 0.0f
        cloud.addEffect(MobEffectInstance(MobEffects.SLOWNESS, 20 * 5, 1))
        serverLevel.addFreshEntity(cloud)
    }
}
