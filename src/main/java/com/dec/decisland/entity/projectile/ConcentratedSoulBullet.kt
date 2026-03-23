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
import net.minecraft.world.phys.Vec3

class ConcentratedSoulBullet(entityType: EntityType<ConcentratedSoulBullet>, level: Level) :
    VisibleMagicBallProjectile(
        entityType,
        level,
        "concentrated_soul_bullet_wake_particle",
        "concentrated_soul_bullet_hit_particle",
    ) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.CONCENTRATED_SOUL_BULLET.get(), level) {
        setSpawnPositionFromOwner(owner)
    }

    override val baseDamage: Float = 8.0f
    override val maxLifetimeTicks: Int = MAX_LIFETIME_TICKS
    override val spawnForwardOffset: Double = 0.85
    override val trailDurationTicks: Int = 2
    override val trailSampleSpacing: Double? = null
    override val extraTrailOffsets: DoubleArray = doubleArrayOf()

    override fun tick() {
        super.tick()

        val serverLevel = level() as? ServerLevel ?: return
        if (!isAlive || tickCount < FIRST_SOUL_WAKE_TICK || (tickCount - FIRST_SOUL_WAKE_TICK) % SOUL_WAKE_INTERVAL_TICKS != 0) {
            return
        }

        val motion = deltaMovement
        val trailPos = if (motion.lengthSqr() > 1.0E-6) {
            position().subtract(motion.scale(SOUL_WAKE_BACKTRACK_FACTOR))
        } else {
            position()
        }
        serverLevel.addFreshEntity(SoulWakeBullet(serverLevel, owner as? LivingEntity, trailPos, Vec3.ZERO))
    }

    override fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
        if (target is LivingEntity) {
            target.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 40, 1))
        }
    }

    companion object {
        private const val MAX_LIFETIME_TICKS: Int = 140
        private const val FIRST_SOUL_WAKE_TICK: Int = 3
        private const val SOUL_WAKE_INTERVAL_TICKS: Int = 5
        private const val SOUL_WAKE_BACKTRACK_FACTOR: Double = 0.5
    }
}
