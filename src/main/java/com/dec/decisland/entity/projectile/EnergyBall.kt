package com.dec.decisland.entity.projectile

import com.dec.decisland.entity.ModEntities
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class EnergyBall(entityType: EntityType<EnergyBall>, level: Level) :
    VisibleMagicBallProjectile(
        entityType,
        level,
        "energy_ball_wake_particle",
        "energy_ball_hit_particle",
    ) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.ENERGY_BALL.get(), level) {
        setSpawnPositionFromOwner(owner)
    }

    override val baseDamage: Float = 5.0f
    override val airInertia: Double = 1.0
    override val waterInertia: Double = 1.0
    override val trailDurationTicks: Int = 6
    override val hitParticleDurationTicks: Int = 8
    override val hitParticleIds: List<Identifier> =
        super.hitParticleIds + Identifier.fromNamespaceAndPath("decisland", "shulker_bullet_hit_small_particle")
}
