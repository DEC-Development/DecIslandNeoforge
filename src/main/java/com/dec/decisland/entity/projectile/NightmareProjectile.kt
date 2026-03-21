package com.dec.decisland.entity.projectile

import com.dec.decisland.block.ModBlocks
import com.dec.decisland.network.Networking
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import kotlin.math.floor

abstract class NightmareProjectile(entityType: EntityType<out NightmareProjectile>, level: Level) : Projectile(entityType, level) {
    private var exploded = false

    protected abstract val baseDamage: Float
    protected abstract val maxLifetimeTicks: Int
    protected abstract val airInertia: Double
    protected abstract val waterInertia: Double
    protected abstract val blockExtent: Double
    protected abstract val trailParticleId: Identifier
    protected abstract val spawnParticleId: Identifier

    override fun getDefaultGravity(): Double = 0.0

    override fun tick() {
        if (exploded) {
            discard()
            return
        }

        if (tickCount >= maxLifetimeTicks) {
            explodeAt(position())
            return
        }

        super.tick()

        val motion = deltaMovement
        val hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity)
        if (hitResult.type != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitResult)) {
            hitTargetOrDeflectSelf(hitResult)
            if (exploded) {
                return
            }
        }

        val nextPos = position().add(motion)
        if (level().getBlockStates(boundingBox).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            explodeAt(position())
            return
        }

        if (!level().isClientSide) {
            val serverLevel = level() as ServerLevel
            Networking.sendBedrockEmitterToNearby(serverLevel, trailParticleId, position(), 64.0, 2)
        }

        val inertia = if (isInWater) waterInertia else airInertia
        setDeltaMovement(motion.scale(inertia))
        setPos(nextPos.x, nextPos.y, nextPos.z)
    }

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val projectileOwner = getOwner()
        val currentLevel = level()
        if (projectileOwner is LivingEntity && currentLevel is ServerLevel) {
            result.entity.hurtServer(currentLevel, damageSources().spit(this, projectileOwner), baseDamage)
        }
        explodeAt(result.location)
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        explodeAt(result.location)
    }

    protected fun explodeAt(pos: Vec3) {
        if (exploded || level().isClientSide) {
            discard()
            return
        }

        exploded = true
        val serverLevel = level() as? ServerLevel ?: return
        placeNightmareBlocks(serverLevel, pos, blockExtent)
        playShulkerBulletImpactEffects(serverLevel, pos)
        Networking.sendBedrockEmitterToNearby(serverLevel, spawnParticleId, pos, 64.0, 6)
        discard()
    }

    private fun placeNightmareBlocks(level: ServerLevel, center: Vec3, extent: Double) {
        val minX = floor(center.x - extent).toInt()
        val maxX = floor(center.x + extent).toInt()
        val minY = floor(center.y - extent).toInt()
        val maxY = floor(center.y + extent).toInt()
        val minZ = floor(center.z - extent).toInt()
        val maxZ = floor(center.z + extent).toInt()
        val nightmareState = ModBlocks.NIGHTMARE_BLOCK.get().defaultBlockState()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val pos = net.minecraft.core.BlockPos(x, y, z)
                    if (!level.getBlockState(pos).isAir) {
                        continue
                    }
                    level.setBlockAndUpdate(pos, nightmareState)
                }
            }
        }
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
