package com.dec.decisland.entity.projectile

import com.dec.decisland.entity.ModEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class ThunderBall(entityType: EntityType<ThunderBall>, level: Level) :
    VisibleMagicBallProjectile(entityType, level, "thunder_wake_particle") {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.THUNDER_BALL.get(), level) {
        setSpawnPositionFromOwner(owner)
    }

    override val baseDamage: Float = 12.0f
    override val airInertia: Double = 0.8
    override val waterInertia: Double = 0.5
    override val spawnForwardOffset: Double = 1.0

    override fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
        maybeStrikeLightning(serverLevel, target.position())
    }

    override fun onBlockHit(serverLevel: ServerLevel, pos: Vec3) {
        maybeStrikeLightning(serverLevel, pos)
    }

    private fun maybeStrikeLightning(serverLevel: ServerLevel, pos: Vec3) {
        if (RandomSource.create().nextInt(4) != 0) {
            return
        }

        EntityType.LIGHTNING_BOLT.create(serverLevel, EntitySpawnReason.TRIGGERED)?.let { lightning ->
            lightning.setPos(pos.x, pos.y, pos.z)
            serverLevel.addFreshEntity(lightning)
        }
    }
}
