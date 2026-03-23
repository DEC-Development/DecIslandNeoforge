package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.network.Networking
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class SpotsOverflow(entityType: EntityType<SpotsOverflow>, level: Level) : ParticleRayProjectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.SPOTS_OVERFLOW.get(), level) {
        setOwner(owner)
        val view = owner.getViewVector(0.0f)
        val spawnPos = owner.eyePosition.add(view.scale(SPAWN_FORWARD_OFFSET))
        setPos(
            spawnPos.x,
            spawnPos.y,
            spawnPos.z,
        )
    }

    override val baseDamage: Float = 5.0f
    override val maxLifetimeTicks: Int = 80
    override val airInertia: Double = 0.95
    override val waterInertia: Double = 0.8
    override val trailDurationTicks: Int = 6
    override val trailIntervalTicks: Int = 4
    override val hitParticleDurationTicks: Int = 6
    override val trailParticleId: Identifier =
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "small_fire_wake_particle")
    override val hitParticleIds: List<Identifier> = listOf(
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "small_fire_wake_particle"),
    )

    override fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
        target.igniteForSeconds(4.0f)
    }

    override fun spawnAdditionalTrailParticles(serverLevel: ServerLevel, pos: Vec3) {
        val motion = deltaMovement
        if (motion.lengthSqr() <= 1.0E-6) {
            return
        }

        val direction = motion.normalize()
        TRAIL_OFFSETS.forEach { offset ->
            Networking.sendBedrockEmitterToNearby(
                serverLevel,
                trailParticleId,
                pos.subtract(direction.scale(offset)),
                64.0,
                trailDurationTicks,
            )
        }
    }

    override fun spawnAdditionalHitParticles(serverLevel: ServerLevel, pos: Vec3) {
        Networking.sendBedrockEmitterToNearby(
            serverLevel,
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "small_fire_wake_particle"),
            pos,
            64.0,
            hitParticleDurationTicks,
        )
    }

    companion object {
        private const val SPAWN_FORWARD_OFFSET: Double = 1.1
        private val TRAIL_OFFSETS: DoubleArray = doubleArrayOf(0.18, 0.36, 0.54)
    }
}
