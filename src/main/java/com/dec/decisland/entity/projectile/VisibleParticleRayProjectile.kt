package com.dec.decisland.entity.projectile

import com.dec.decisland.network.Networking
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import kotlin.math.abs

abstract class VisibleParticleRayProjectile(
    entityType: EntityType<out VisibleParticleRayProjectile>,
    level: Level,
) : ParticleRayProjectile(entityType, level) {
    protected open val spawnForwardOffset: Double = 0.9
    protected open val trailSampleSpacing: Double? = null
    protected open val extraTrailOffsets: DoubleArray = doubleArrayOf(0.18, 0.36)

    protected fun setSpawnPositionFromOwner(owner: LivingEntity) {
        setOwner(owner)
        val view = owner.getViewVector(0.0f)
        val spawnPos = owner.eyePosition.add(view.scale(spawnForwardOffset))
        setPos(spawnPos.x, spawnPos.y, spawnPos.z)
    }

    override fun spawnAdditionalTrailParticles(serverLevel: ServerLevel, pos: Vec3) {
        val particleId = trailParticleId ?: return
        val motion = deltaMovement
        val motionLength = motion.length()
        if (motionLength <= 1.0E-6) {
            return
        }

        val direction = motion.normalize()
        val offsets = ArrayList<Double>()
        val sampleSpacing = trailSampleSpacing
        if (sampleSpacing != null && sampleSpacing > 0.0) {
            var offset = sampleSpacing
            while (offset <= motionLength + 1.0E-6) {
                offsets.add(offset)
                offset += sampleSpacing
            }
        }

        extraTrailOffsets.forEach { offset ->
            if (offset > 0.0 && offsets.none { abs(it - offset) <= 1.0E-6 }) {
                offsets.add(offset)
            }
        }

        offsets.forEach { offset ->
            Networking.sendBedrockEmitterToNearby(
                serverLevel,
                particleId,
                pos.subtract(direction.scale(offset)),
                64.0,
                trailDurationTicks,
            )
        }
    }
}
