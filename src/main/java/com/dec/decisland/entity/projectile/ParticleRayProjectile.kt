package com.dec.decisland.entity.projectile

import com.dec.decisland.network.Networking
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

abstract class ParticleRayProjectile(
    entityType: EntityType<out ParticleRayProjectile>,
    level: Level,
) : Projectile(entityType, level) {
    private var impactHandled = false

    protected abstract val baseDamage: Float
    protected open val maxLifetimeTicks: Int = 60
    protected open val projectileGravity: Double = 0.0
    protected open val airInertia: Double = 1.0
    protected open val waterInertia: Double = 1.0
    protected open val trailParticleId: Identifier? = null
    protected open val trailDurationTicks: Int = 2
    protected open val trailIntervalTicks: Int = 1
    protected open val hitParticleIds: List<Identifier> = emptyList()
    protected open val hitParticleDurationTicks: Int = 6

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    }

    override fun getDefaultGravity(): Double = projectileGravity

    override fun tick() {
        if (tickCount > maxLifetimeTicks) {
            discard()
            return
        }

        super.tick()

        val motion = deltaMovement
        val hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity)
        if (hitResult.type != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitResult)) {
            hitTargetOrDeflectSelf(hitResult)
            if (!isAlive) {
                return
            }
        }

        if (level().getBlockStates(boundingBox).anyMatch { !it.isAir }) {
            discard()
            return
        }

        if (!level().isClientSide) {
            emitTrail(level() as ServerLevel, position())
        }

        val nextPos = position().add(motion)
        val inertia = if (isInWater) waterInertia else airInertia
        setDeltaMovement(motion.scale(inertia))
        applyGravity()
        setPos(nextPos.x, nextPos.y, nextPos.z)
    }

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        if (impactHandled || level().isClientSide) {
            return
        }

        impactHandled = true
        val serverLevel = level() as? ServerLevel ?: return
        val target = result.entity
        val projectileOwner = getOwner()
        val damageSource = if (projectileOwner is LivingEntity) {
            damageSources().spit(this, projectileOwner)
        } else {
            damageSources().thrown(this, projectileOwner)
        }

        if (target.hurtServer(serverLevel, damageSource, baseDamage)) {
            if (projectileOwner is LivingEntity) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource)
            }
            onEntityDamaged(serverLevel, target)
        }

        playShulkerBulletImpactEffects(serverLevel, result.location)
        spawnHitParticles(serverLevel, result.location)
        discard()
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        if (impactHandled || level().isClientSide) {
            return
        }

        impactHandled = true
        val serverLevel = level() as? ServerLevel ?: return
        playShulkerBulletImpactEffects(serverLevel, result.location)
        spawnHitParticles(serverLevel, result.location)
        onBlockHit(serverLevel, result.location)
        discard()
    }

    protected open fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
    }

    protected open fun onBlockHit(serverLevel: ServerLevel, pos: Vec3) {
    }

    protected open fun spawnAdditionalTrailParticles(serverLevel: ServerLevel, pos: Vec3) {
    }

    protected open fun spawnAdditionalHitParticles(serverLevel: ServerLevel, pos: Vec3) {
    }

    private fun emitTrail(serverLevel: ServerLevel, pos: Vec3) {
        val particleId = trailParticleId ?: return
        if (trailIntervalTicks > 1 && tickCount % trailIntervalTicks != 0) {
            return
        }
        Networking.sendBedrockEmitterToNearby(serverLevel, particleId, pos, 64.0, trailDurationTicks)
        spawnAdditionalTrailParticles(serverLevel, pos)
    }

    private fun spawnHitParticles(serverLevel: ServerLevel, pos: Vec3) {
        hitParticleIds.forEach { particleId ->
            Networking.sendBedrockEmitterToNearby(serverLevel, particleId, pos, 64.0, hitParticleDurationTicks)
        }
        spawnAdditionalHitParticles(serverLevel, pos)
    }

    private fun playShulkerBulletImpactEffects(serverLevel: ServerLevel, pos: Vec3) {
        serverLevel.playSound(
            null,
            pos.x,
            pos.y,
            pos.z,
            SoundEvents.SHULKER_BULLET_HIT,
            SoundSource.HOSTILE,
            1.0f,
            1.0f,
        )
        serverLevel.sendParticles(
            ParticleTypes.EXPLOSION,
            pos.x,
            pos.y,
            pos.z,
            1,
            0.1,
            0.1,
            0.1,
            0.0,
        )
    }
}
