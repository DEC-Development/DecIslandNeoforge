package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

abstract class VisibleMagicBallProjectile(
    entityType: EntityType<out VisibleMagicBallProjectile>,
    level: Level,
    wakeParticlePath: String,
    hitParticlePath: String? = null,
) : GeoParticleRayProjectile(entityType, level) {
    override val trailDurationTicks: Int = 8
    override val hitParticleDurationTicks: Int = 8
    override val trailSampleSpacing: Double = 0.12
    override val extraTrailOffsets: DoubleArray = doubleArrayOf(0.12, 0.24, 0.36, 0.48)
    override val trailParticleId: Identifier = id(wakeParticlePath)
    override val hitParticleIds: List<Identifier> = listOfNotNull(hitParticlePath?.let(::id))

    protected fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, path)
}
