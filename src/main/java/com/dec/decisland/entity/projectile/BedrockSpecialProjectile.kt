package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import com.dec.decisland.network.Networking
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3

/**
 * Configurable implementation for Bedrock projectile entities that use the
 * vanilla egg/snowball runtime. The entity type selects the Bedrock profile;
 * item skills can additionally select a component-group variant.
 */
class BedrockSpecialProjectile(
    private val entityTypeRef: EntityType<BedrockSpecialProjectile>,
    level: Level,
) : CustomInertiaProjectile(entityTypeRef, level) {
    private var profileVariant: String? = null
    private var stuckInGround: Boolean
        get() = entityData.get(DATA_STUCK_IN_GROUND)
        set(value) {
            entityData.set(DATA_STUCK_IN_GROUND, value)
        }
    private var stuckTicks = 0
    private var bouncesRemaining = 0
    private var traveledDistance = 0.0
    private var fuseTicksRemaining = -1

    private val profile: Profile
        get() = profileFor(entityTypeRef, profileVariant)

    init {
        setNoGravity(false)
        setItem(profile.renderItem?.let(::ItemStack) ?: ItemStack.EMPTY)
        initializeFuse()
    }

    fun selectVariant(variant: String): BedrockSpecialProjectile {
        profileVariant = variant
        bouncesRemaining = profile.bounceCount
        setItem(profile.renderItem?.let(::ItemStack) ?: ItemStack.EMPTY)
        initializeFuse()
        return this
    }

    private fun initializeFuse() {
        val min = profile.fuseMinTicks
        val max = profile.fuseMaxTicks.coerceAtLeast(min)
        fuseTicksRemaining = if (min > 0) {
            min + if (max > min) random.nextInt(max - min + 1) else 0
        } else {
            -1
        }
    }

    override fun getDefaultItem(): Item = profile.renderItem ?: Items.AIR

    override fun getDefaultGravity(): Double = profile.gravity

    override val airInertia: Float
        get() = profile.airInertia

    override val waterInertia: Float
        get() = profile.waterInertia

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(DATA_STUCK_IN_GROUND, false)
    }

    override fun tick() {
        if (!level().isClientSide && fuseTicksRemaining > 0) {
            fuseTicksRemaining--
            if (fuseTicksRemaining == 0) {
                if (profile.explosionPower > 0.0f) {
                    explode(level() as ServerLevel, position())
                }
                discard()
                return
            }
        }
        if (stuckInGround) {
            superEntityTick()
            // The hit state is synchronized from the server. Keep both sides
            // frozen at the impact position instead of allowing the client to
            // continue applying gravity while the data update is arriving.
            noPhysics = false
            if (!level().isClientSide && ++stuckTicks >= profile.stuckLifetime) {
                discard()
            }
            return
        }
        var reachedMaxTravelDistance = false
        profile.maxTravelDistance?.let { maxDistance ->
            if (!level().isClientSide) {
                val remainingDistance = maxDistance - traveledDistance
                if (remainingDistance <= 0.0) {
                    discard()
                    return
                }
                val motion = deltaMovement
                val motionLength = motion.length()
                if (motionLength > remainingDistance) {
                    setDeltaMovement(motion.scale(remainingDistance / motionLength))
                }
            }
        }
        val previousPosition = position()
        super.tick()
        if (!level().isClientSide && isAlive) {
            profile.maxTravelDistance?.let { maxDistance ->
                traveledDistance += previousPosition.distanceTo(position())
                if (traveledDistance >= maxDistance) {
                    reachedMaxTravelDistance = true
                }
            }
        }
        val trailParticleId = profile.trailParticleId
        if (!level().isClientSide && isAlive && trailParticleId != null &&
            (profile.trailIntervalTicks <= 1 || tickCount % profile.trailIntervalTicks == 0)) {
            val serverLevel = level() as? ServerLevel
            if (serverLevel != null) {
                Networking.sendBedrockEmitterToNearby(
                    serverLevel,
                    trailParticleId,
                    position().add(profile.trailPositionOffset),
                    profile.trailRadius,
                    profile.trailDurationTicks,
                )
            }
        }
        if (!level().isClientSide && reachedMaxTravelDistance) {
            discard()
            return
        }
        if (!level().isClientSide && tickCount > profile.maxLifetime) {
            discard()
        }
    }

    private fun superEntityTick() {
        baseTick()
        setDeltaMovement(Vec3.ZERO)
    }

    override fun onHitEntity(result: EntityHitResult) {
        if (level().isClientSide) return
        val serverLevel = level() as? ServerLevel ?: return
        val target = result.entity
        val owner = owner as? LivingEntity
        val source = if (owner != null) {
            damageSources().thrown(this, owner)
        } else {
            damageSources().generic()
        }
        val wasHurt = target.hurt(source, profile.damage)
        if (wasHurt) {
            if (profile.knockback && target is LivingEntity) {
                val direction = deltaMovement.normalize()
                target.knockback(0.35, -direction.x, -direction.z)
            }
            if (profile.ignite && target is LivingEntity) {
                target.igniteForSeconds(5.0f)
            }
            if (profile.poisonTicks > 0 && target is LivingEntity) {
                target.addEffect(MobEffectInstance(MobEffects.POISON, profile.poisonTicks, profile.poisonAmplifier), owner)
            }
        }
        profile.hitSound?.let { sound ->
            level().playSound(null, x, y, z, sound, SoundSource.PLAYERS, 1.0f, 1.0f)
        }
        if (profile.explosionPower > 0.0f) {
            explode(serverLevel, target.position())
        }
        emitHit(serverLevel, target.position())
        if (profile.stopOnHurt && wasHurt) {
            setDeltaMovement(Vec3.ZERO)
            stuckInGround = true
            stuckTicks = 0
        } else if (profile.destroyOnEntityHit) {
            discard()
        }
    }

    override fun onHitBlock(result: BlockHitResult) {
        if (level().isClientSide) return
        val serverLevel = level() as? ServerLevel ?: return
        val pos = result.location
        profile.hitGroundSound?.let { sound ->
            level().playSound(null, pos.x, pos.y, pos.z, sound, SoundSource.PLAYERS, 1.0f, 1.0f)
        }
        if (profile.explosionPower > 0.0f) {
            explode(serverLevel, pos)
        }
        emitHit(serverLevel, pos)
        if (bouncesRemaining > 0) {
            bouncesRemaining--
            val normal = Vec3(
                result.direction.stepX.toDouble(),
                result.direction.stepY.toDouble(),
                result.direction.stepZ.toDouble(),
            )
            val motion = deltaMovement
            val reflected = motion.subtract(normal.scale(2.0 * motion.dot(normal)))
            setDeltaMovement(reflected.scale(0.5))
            setPos(pos.x + normal.x * 0.02, pos.y + normal.y * 0.02, pos.z + normal.z * 0.02)
            return
        }
        if (profile.stuckInGround) {
            stuckInGround = true
            stuckTicks = 0
            setDeltaMovement(Vec3.ZERO)
            setPos(pos.x, pos.y, pos.z)
        } else {
            discard()
        }
    }

    private fun explode(serverLevel: ServerLevel, pos: Vec3) {
        serverLevel.explode(
            owner,
            pos.x,
            pos.y,
            pos.z,
            profile.explosionPower,
            false,
            profile.explosionInteraction,
        )
    }

    private fun emitHit(serverLevel: ServerLevel, pos: Vec3) {
        profile.hitParticle?.let { particle ->
            serverLevel.sendParticles(particle, pos.x, pos.y, pos.z, 6, 0.15, 0.15, 0.15, 0.03)
        }
    }

    data class Profile(
        val damage: Float,
        val gravity: Double,
        val airInertia: Float,
        val waterInertia: Float,
        val maxLifetime: Int = 100,
        val stuckInGround: Boolean = false,
        val stuckLifetime: Int = 30,
        val destroyOnEntityHit: Boolean = true,
        val knockback: Boolean = false,
        val ignite: Boolean = false,
        val poisonTicks: Int = 0,
        val poisonAmplifier: Int = 0,
        val explosionPower: Float = 0.0f,
        val explosionInteraction: Level.ExplosionInteraction = Level.ExplosionInteraction.NONE,
        val bounceCount: Int = 0,
        val renderItem: Item? = Items.SNOWBALL,
        val hitParticle: net.minecraft.core.particles.ParticleOptions? = null,
        val trailParticleId: ResourceLocation? = null,
        val trailRadius: Double = 64.0,
        val trailDurationTicks: Int = 2,
        val trailIntervalTicks: Int = 1,
        val trailPositionOffset: Vec3 = Vec3.ZERO,
        val maxTravelDistance: Double? = null,
        val stopOnHurt: Boolean = false,
        val hitSound: SoundEvent? = null,
        val hitGroundSound: SoundEvent? = null,
        val spawnOffset: Vec3 = Vec3.ZERO,
        val spawnForwardOffset: Double = 0.85,
        val fuseMinTicks: Int = 0,
        val fuseMaxTicks: Int = 0,
    )

    companion object {
        private val DATA_STUCK_IN_GROUND: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(BedrockSpecialProjectile::class.java, EntityDataSerializers.BOOLEAN)

        fun create(
            level: Level,
            owner: LivingEntity,
            stack: ItemStack,
            entityType: EntityType<BedrockSpecialProjectile>,
            variant: String? = null,
        ): BedrockSpecialProjectile {
            val projectile = BedrockSpecialProjectile(entityType, level)
            projectile.owner = owner
            projectile.selectVariant(variant ?: "")
            val view = owner.getViewVector(0.0f)
            val pos = owner.eyePosition
                .add(projectile.profile.spawnOffset)
                .add(view.scale(projectile.profile.spawnForwardOffset))
            projectile.setPos(pos.x, pos.y, pos.z)
            return projectile
        }

        private fun profileFor(type: EntityType<BedrockSpecialProjectile>, variant: String?): Profile {
            val path = BuiltInRegistries.ENTITY_TYPE.getKey(type).path
            return when (path) {
                "angel_purification_blade" -> Profile(
                    damage = 7.0f,
                    gravity = 0.0,
                    airInertia = 1.0f,
                    waterInertia = 1.0f,
                    maxLifetime = Int.MAX_VALUE,
                    destroyOnEntityHit = false,
                    knockback = true,
                    renderItem = null,
                    maxTravelDistance = 5.0,
                )
                "bamboo_yataghan" -> Profile(
                    damage = 7.0f,
                    gravity = 0.1,
                    airInertia = 1.0f,
                    waterInertia = 0.8f,
                    maxLifetime = 100,
                    stuckInGround = true,
                    stuckLifetime = 30,
                    bounceCount = 0,
                    renderItem = Items.BAMBOO,
                    stopOnHurt = true,
                    hitSound = SoundEvents.TRIDENT_HIT,
                    hitGroundSound = SoundEvents.TRIDENT_HIT_GROUND,
                    spawnOffset = Vec3(0.0, -0.1, 0.0),
                    spawnForwardOffset = 0.0,
                    fuseMinTicks = 10,
                    fuseMaxTicks = 40,
                )
                "hard_bubble" -> Profile(4.0f, 0.0, 1.01f, 1.01f, renderItem = Items.SNOWBALL)
                "bullet_by_echo_of_deep" -> Profile(
                    damage = 18.0f,
                    gravity = 0.0,
                    airInertia = 1.0f,
                    waterInertia = 1.0f,
                    renderItem = Items.SNOWBALL,
                    trailParticleId = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "deep_wake_particle"),
                )
                "sun_energy" -> Profile(
                    damage = 8.0f,
                    gravity = 0.0,
                    airInertia = 0.95f,
                    waterInertia = 0.8f,
                    ignite = true,
                    explosionPower = 1.0f,
                    explosionInteraction = Level.ExplosionInteraction.MOB,
                    renderItem = Items.SUNFLOWER,
                    trailParticleId = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "fire_wake_particle"),
                    // Bedrock's fuseLength is measured in seconds; the Java
                    // tick loop uses 20 ticks per second.
                    fuseMinTicks = 100,
                    fuseMaxTicks = 100,
                )
                "radiate_cloud" -> Profile(
                    damage = 19.0f,
                    gravity = 0.0,
                    airInertia = 1.0f,
                    waterInertia = 1.0f,
                    poisonTicks = 200,
                    poisonAmplifier = 2,
                    renderItem = Items.SNOWBALL,
                    trailParticleId = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "radiate_wake_big_particle"),
                )
                "radiate_fog" -> Profile(
                    damage = 1.0f,
                    gravity = 0.0,
                    airInertia = 1.1f,
                    waterInertia = 1.1f,
                    poisonTicks = 100,
                    poisonAmplifier = 2,
                    renderItem = Items.SNOWBALL,
                    trailParticleId = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "radiate_wake_particle"),
                )
                "spots_by_sword" -> Profile(14.0f, 0.0, 1.05f, 0.95f, stuckInGround = true, stuckLifetime = 1, ignite = true, renderItem = Items.FIRE_CHARGE)
                "storm_energy" -> when (variant) {
                    "a" -> Profile(4.0f, 0.01, 1.01f, 1.3f, knockback = true, renderItem = Items.SNOWBALL, trailParticleId = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "wave_wake_particle"))
                    "b" -> Profile(4.0f, 0.01, 1.0f, 1.3f, knockback = true, renderItem = Items.SNOWBALL, trailParticleId = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "wave_wake_particle"))
                    "c" -> Profile(4.0f, 0.01, 1.0f, 1.3f, knockback = true, renderItem = Items.SNOWBALL, trailParticleId = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "wave_wake_particle"))
                    else -> Profile(4.0f, 0.01, 1.0f, 1.3f, knockback = true, renderItem = Items.SNOWBALL, trailParticleId = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "wave_wake_particle"))
                }
                "bullet_by_catapult_normal" -> Profile(7.0f, 0.02, 1.1f, 0.95f, knockback = true, renderItem = Items.COBBLESTONE)
                "bullet_by_catapult_explode" -> Profile(7.0f, 0.02, 1.1f, 0.95f, knockback = true, explosionPower = 1.0f, renderItem = Items.GUNPOWDER)
                else -> Profile(5.0f, 0.0, 1.0f, 1.0f)
            }
        }
    }
}
