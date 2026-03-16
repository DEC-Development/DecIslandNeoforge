package com.dec.decisland.entity.projectile.dart

import com.dec.decisland.api.CustomInertia
import com.dec.decisland.DecIsland
import com.dec.decisland.network.Networking
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

open class DartEntity(entityType: EntityType<out DartEntity>, level: Level) :
    ThrowableItemProjectile(entityType, level), CustomInertia {
    var spinRotationO: Float = 0.0f
        private set
    var spinRotation: Float = 0.0f
        private set
    private var impactHandled: Boolean = false

    init {
        setItem(ItemStack(definition.item()))
        setRandomTilt(generateRandomTilt())
    }

    constructor(entityType: EntityType<out DartEntity>, level: Level, owner: LivingEntity, item: ItemStack) : this(entityType, level) {
        setOwner(owner)
        setItem(item)
        setRandomTilt(generateRandomTilt())
    }

    constructor(
        entityType: EntityType<out DartEntity>,
        level: Level,
        x: Double,
        y: Double,
        z: Double,
        item: ItemStack,
    ) : this(entityType, level) {
        setPos(x, y, z)
        setItem(item)
        setRandomTilt(generateRandomTilt())
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(DATA_RANDOM_TILT, 0.0f)
    }

    final override fun getDefaultItem(): Item = definition.item()

    val randomTilt: Float
        get() = entityData.get(DATA_RANDOM_TILT)

    override fun tick() {
        super.tick()
        updateRotationFromVelocity()
        updateSpinRotation()
    }

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        if (level().isClientSide || impactHandled) {
            return
        }

        val entity: Entity = result.entity
        entity.hurt(damageSources().thrown(this, getOwner()), definition.entitySettings.baseDamage)
        if (definition.entitySettings.igniteSecondsOnHit > 0.0f) {
            entity.igniteForSeconds(definition.entitySettings.igniteSecondsOnHit)
        }
        if (entity is LivingEntity) {
            if (definition.entitySettings.applyKnockback) {
                val direction = deltaMovement
                if (direction.horizontalDistanceSqr() > 1.0E-7) {
                    entity.knockback(definition.entitySettings.knockbackStrength, -direction.x, -direction.z)
                }
            }
            definition.entitySettings.effectOnHit?.invoke()?.let(entity::addEffect)
        }
        definition.onHitEntityServer(this, result)
        handleImpact(result)
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        if (level().isClientSide || impactHandled) {
            return
        }

        definition.onHitBlockServer(this, result)
        handleImpact(result)
    }

    override fun getDefaultGravity(): Double = definition.entitySettings.gravity

    override val airInertia: Float
        get() = definition.entitySettings.bedrockInertia.toFloat() // (VANILLA_AIR_INERTIA + ((1.0 - VANILLA_AIR_INERTIA) * (1.0 - (1.0 / definition.entitySettings.bedrockInertia)))).toFloat()

    override val waterInertia: Float
        get() = definition.entitySettings.waterInertia

    private fun updateRotationFromVelocity() {
        val motion = deltaMovement
        if (motion.lengthSqr() < 1.0E-7) {
            return
        }

        val horizontal = motion.horizontalDistance()
        val targetYRot = (Mth.atan2(motion.x, motion.z) * (180.0 / Math.PI)).toFloat()
        val targetXRot = (Mth.atan2(motion.y, horizontal) * (180.0 / Math.PI)).toFloat()

        if (xRotO == 0.0f && yRotO == 0.0f) {
            xRotO = targetXRot
            yRotO = targetYRot
            setXRot(targetXRot)
            setYRot(targetYRot)
            return
        }

        xRotO = xRot
        yRotO = yRot
        setXRot(Mth.rotLerp(0.2f, xRot, targetXRot))
        setYRot(Mth.rotLerp(0.2f, yRot, targetYRot))
    }

    private fun updateSpinRotation() {
        spinRotationO = spinRotation
        spinRotation = (spinRotation + (deltaMovement.length().toFloat() * definition.entitySettings.spinDegreesPerSpeed)) % 360.0f
    }

    private fun setRandomTilt(value: Float) {
        entityData.set(DATA_RANDOM_TILT, value)
    }

    private fun generateRandomTilt(): Float = (random.nextFloat() * 2.0f - 1.0f) * definition.entitySettings.randomTiltDegrees

    protected open val definition: DartDefinition
        get() = definitionFor(type)

    fun sendHitEmitters(particleIds: Iterable<Identifier>, radius: Double = 64.0, durationTicks: Int = 6) {
        val serverLevel = serverLevel() ?: return
        val pos = position()
        particleIds.forEach { id ->
            Networking.sendBedrockEmitterToNearby(serverLevel, id, pos, radius, durationTicks)
        }
    }

    fun playHitSound(
        sound: SoundEvent,
        source: SoundSource = SoundSource.PLAYERS,
        volume: Float = 1.0f,
        pitch: Float = 1.0f,
    ) {
        serverLevel()?.playSound(null, x, y, z, sound, source, volume, pitch)
    }

    fun explodeDamage(radius: Double, damage: Float, excludeOwner: Boolean = false) {
        val serverLevel = serverLevel() ?: return
        val owner = getOwner()
        val box = AABB.ofSize(position(), radius * 2.0, radius * 2.0, radius * 2.0)
        val source = serverLevel.damageSources().explosion(this, owner)

        serverLevel.getEntities(this, box) { entity ->
            entity.isAlive && entity !is ItemEntity
        }.forEach { entity ->
            if (excludeOwner && owner != null && entity == owner) {
                return@forEach
            }
            entity.hurt(source, damage)
        }
    }

    fun sendVanillaParticles(
        particle: ParticleOptions,
        count: Int,
        xSpread: Double = 0.15,
        ySpread: Double = 0.15,
        zSpread: Double = 0.15,
        speed: Double = 0.0,
    ) {
        serverLevel()?.sendParticles(particle, x, y, z, count, xSpread, ySpread, zSpread, speed)
    }

    fun sendSnowballPoof(count: Int) {
        sendVanillaParticles(ItemParticleOption(ParticleTypes.ITEM, ItemStack(Items.SNOWBALL)), count)
    }

    private fun handleImpact(result: HitResult) {
        if (impactHandled) {
            return
        }
        impactHandled = true
        definition.onHitServer(this, result)
        discard()
    }

    private fun serverLevel(): ServerLevel? = level() as? ServerLevel

    companion object {
        private val DATA_RANDOM_TILT: EntityDataAccessor<Float> =
            SynchedEntityData.defineId(DartEntity::class.java, EntityDataSerializers.FLOAT)
        private const val VANILLA_AIR_INERTIA: Double = 0.99
        private val DEFINITIONS_BY_ID: MutableMap<Identifier, DartDefinition> = linkedMapOf()

        @JvmStatic
        fun registerDefinition(definition: DartDefinition) {
            DEFINITIONS_BY_ID[Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, definition.path)] = definition
        }

        @JvmStatic
        fun definitionFor(type: EntityType<*>): DartDefinition =
            DEFINITIONS_BY_ID[BuiltInRegistries.ENTITY_TYPE.getKey(type)]
                ?: error("Missing dart definition for entity type ${BuiltInRegistries.ENTITY_TYPE.getKey(type)}")
    }
}
