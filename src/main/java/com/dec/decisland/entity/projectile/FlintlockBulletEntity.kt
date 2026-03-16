package com.dec.decisland.entity.projectile
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.category.Weapon
import com.dec.decisland.network.Networking
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.allay.Allay
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class FlintlockBulletEntity(
    type: EntityType<out FlintlockBulletEntity>,
    level: Level,
) : ThrowableItemProjectile(type, level) {
    init {
        setNoGravity(true)
    }

    var baseDamage = 11.0f
    var knockback = 0.45
    var bouncesRemaining = 0
    var power = 2.5
    var inertia = 1.1
    var liquidInertia = 0.95
    var gravityAmount = 0.02

    private var impactHandled = false
    private var ageTicks = 0
    private var stuckInGround = false

    override fun getDefaultItem(): Item = Weapon.FLINTLOCK_BULLET.get()

    override fun tick() {
        super.tick()
        ageTicks++

        if (!level().isClientSide) {
            if (stuckInGround) {
                deltaMovement = Vec3.ZERO
            } else {
                deltaMovement = deltaMovement.add(0.0, -gravityAmount, 0.0)
                deltaMovement = if (isInWater()) {
                    deltaMovement.scale(liquidInertia)
                } else {
                    deltaMovement.scale(1.0 / inertia)
                }
            }
        }

        if (level().isClientSide) {
            return
        }
        val serverLevel = level() as? ServerLevel ?: return

        if (type == ModEntities.BULLET_BY_STORM_FLINTLOCK.get()) {
            val raining = serverLevel.isRainingAt(blockPosition())
            if (!raining && !isInWater()) {
                if (ageTicks > 200) {
                    discard()
                }
                return
            }
        }

        if (stuckInGround) {
            if (ageTicks > 60) {
                discard()
            }
            return
        }

        if (ageTicks > 200) {
            discard()
        }
    }

    override fun onHitEntity(hitResult: EntityHitResult) {
        val target = hitResult.entity
        val owner = getOwner()
        val damageSource = resolveDamageSource(owner)
        target.hurt(damageSource, baseDamage)

        if (target is LivingEntity) {
            val direction = deltaMovement
            target.knockback(knockback, -direction.x, -direction.z)
        }

        handleImpactEffects()
        if (!level().isClientSide) {
            discard()
        }
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        if (level().isClientSide || impactHandled) {
            return
        }

        if (hitResult.type == HitResult.Type.BLOCK) {
            val serverLevel = level() as? ServerLevel ?: return
            if (bouncesRemaining > 0 && type == ModEntities.BULLET_BY_FLINTLOCK_PRO.get() && hitResult is BlockHitResult) {
                bouncesRemaining--
                val bounced = when (hitResult.direction.axis) {
                    net.minecraft.core.Direction.Axis.X -> deltaMovement.multiply(-0.85, 0.95, 0.95)
                    net.minecraft.core.Direction.Axis.Y -> deltaMovement.multiply(0.95, -0.85, 0.95)
                    net.minecraft.core.Direction.Axis.Z -> deltaMovement.multiply(0.95, 0.95, -0.85)
                }
                deltaMovement = bounced
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, x, y, z, 1, 0.02, 0.02, 0.02, 0.0)
                return
            }

            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, x, y, z, 2, 0.05, 0.05, 0.05, 0.0)
            handleImpactEffects()
            stuckInGround = true
            deltaMovement = Vec3.ZERO
        }
    }

    private fun handleImpactEffects() {
        if (impactHandled) {
            return
        }
        impactHandled = true

        val serverLevel = level() as? ServerLevel ?: return
        val config = ModEntities.BULLET_CONFIG_BY_TYPE.entries.firstOrNull { it.key.get() == type }?.value ?: return

        if (type == ModEntities.BULLET_BY_STORM_FLINTLOCK.get()) {
            val raining = serverLevel.isRainingAt(blockPosition())
            if (!raining && !isInWater()) {
                return
            }
        }

        val chance = config.hitParticleChance.coerceIn(0.0, 1.0)
        if (chance < 1.0 && random.nextDouble() > chance) {
            return
        }

        val pos = position()
        config.hitParticleIds.forEach { id ->
            Networking.sendBedrockEmitterToNearby(serverLevel, id, pos, 64.0, 6)
        }
        applySpecialEffect(serverLevel)
    }

    private fun applySpecialEffect(serverLevel: ServerLevel) {
        when (type) {
            ModEntities.BULLET_BY_EVERLASTING_WINTER_FLINTLOCK.get() -> {
                serverLevel.playSound(null, x, y, z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.9f, 1.15f)
            }
            ModEntities.BULLET_BY_GHOST_FLINTLOCK.get() -> {
                val allay = Allay(EntityType.ALLAY, serverLevel)
                allay.setPos(x, y + 0.2, z)
                allay.deltaMovement = deltaMovement.scale(0.15).add(0.0, 0.08, 0.0)
                serverLevel.addFreshEntity(allay)
            }
            ModEntities.BULLET_BY_LAVA_FLINTLOCK.get() -> explodeDamage(serverLevel, 14.0f)
            ModEntities.BULLET_BY_STAR_FLINTLOCK.get() -> explodeDamage(serverLevel, 16.0f)
            ModEntities.BULLET_BY_STORM_FLINTLOCK.get() -> explodeDamage(serverLevel, 14.0f)
        }
    }

    private fun explodeDamage(serverLevel: ServerLevel, damage: Float) {
        val radius = 2.8
        val box = AABB.ofSize(position(), radius * 2, radius * 2, radius * 2)
        val owner = getOwner()
        val source = serverLevel.damageSources().explosion(this, owner)

        serverLevel.playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.0f, 1.0f)
        serverLevel.getEntitiesOfClass(LivingEntity::class.java, box) { it.isAlive }.forEach { entity ->
            if (owner != null && entity == owner) {
                return@forEach
            }
            entity.hurt(source, damage)
        }
    }

    private fun resolveDamageSource(owner: net.minecraft.world.entity.Entity?): DamageSource =
        when (owner) {
            is Player -> level().damageSources().playerAttack(owner)
            is LivingEntity -> level().damageSources().mobAttack(owner)
            else -> level().damageSources().thrown(this, owner)
        }

}
