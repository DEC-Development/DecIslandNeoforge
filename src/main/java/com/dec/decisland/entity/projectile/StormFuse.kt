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
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class StormFuse(entityType: EntityType<StormFuse>, level: Level) : ParticleRayProjectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.STORM_FUSE.get(), level) {
        setOwner(owner)
        setPos(
            owner.x + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).x,
            owner.eyeY + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).y,
            owner.z + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).z,
        )
    }

    override val baseDamage: Float = 12.0f
    override val maxLifetimeTicks: Int = 60
    override val airInertia: Double = 0.9
    override val waterInertia: Double = 1.4
    override val hitParticleIds: List<Identifier> =
        listOf(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bubble_spurt_particle"))

    override fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
        explodeArea(serverLevel, target.position())
    }

    override fun onBlockHit(serverLevel: ServerLevel, pos: Vec3) {
        explodeArea(serverLevel, pos)
    }

    override fun spawnAdditionalHitParticles(serverLevel: ServerLevel, pos: Vec3) {
        Networking.sendBedrockEmitterToNearby(
            serverLevel,
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bubble_spurt_particle"),
            pos.add(0.0, -0.5, 0.0),
            64.0,
            6,
        )
    }

    private fun explodeArea(serverLevel: ServerLevel, pos: Vec3) {
        val projectileOwner = getOwner()
        val damageSource = if (projectileOwner is LivingEntity) {
            damageSources().spit(this, projectileOwner)
        } else {
            damageSources().thrown(this, projectileOwner)
        }
        val box = AABB(
            pos.x - 2.0,
            pos.y - 1.5,
            pos.z - 2.0,
            pos.x + 2.0,
            pos.y + 2.5,
            pos.z + 2.0,
        )

        serverLevel.getEntitiesOfClass(LivingEntity::class.java, box).forEach { living ->
            living.hurtServer(serverLevel, damageSource, 6.0f)
        }
    }
}
