package com.dec.decisland.entity.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.network.Networking
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import java.util.UUID

class WitherCloudEntity(entityType: EntityType<WitherCloudEntity>, level: Level) : Entity(entityType, level) {
    private var lifetime = DEFAULT_LIFETIME
    private var ownerUUID: UUID? = null

    constructor(level: Level, owner: LivingEntity?, x: Double, y: Double, z: Double) : this(ModEntities.WITHER_CLOUD.get(), level) {
        setPos(x, y, z)
        ownerUUID = owner?.uuid
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        lifetime = input.getIntOr(LIFETIME_KEY, DEFAULT_LIFETIME)
        val owner = input.getStringOr(OWNER_KEY, "")
        ownerUUID = if (owner.isEmpty()) null else UUID.fromString(owner)
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        output.putInt(LIFETIME_KEY, lifetime)
        ownerUUID?.let { output.putString(OWNER_KEY, it.toString()) }
    }

    override fun tick() {
        super.tick()
        if (level().isClientSide) {
            return
        }
        val serverLevel = level() as ServerLevel
        if (tickCount % PARTICLE_INTERVAL == 1) {
            Networking.sendBedrockEmitterToNearby(serverLevel, WAKE_PARTICLE_ID, position(), 64.0, PARTICLE_INTERVAL)
        }
        if (lifetime-- <= 0) {
            discard()
            return
        }
        val owner = ownerUUID?.let(serverLevel::getPlayerByUUID)
        for (entity in serverLevel.getEntities(this, boundingBox) { it is LivingEntity }) {
            val target = entity as LivingEntity
            target.addEffect(MobEffectInstance(MobEffects.WITHER, WITHER_DURATION_TICKS, WITHER_AMPLIFIER), owner)
            target.hurtServer(serverLevel, serverLevel.damageSources().indirectMagic(owner ?: this, this), IMPACT_DAMAGE)
        }
    }

    override fun hurtServer(level: ServerLevel, source: DamageSource, amount: Float): Boolean = false

    companion object {
        private val WAKE_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "wither_cloud_wake_particle")
        private const val LIFETIME_KEY = "Lifetime"
        private const val OWNER_KEY = "Owner"
        private const val DEFAULT_LIFETIME = 100
        private const val PARTICLE_INTERVAL = 4
        private const val WITHER_DURATION_TICKS = 200
        private const val WITHER_AMPLIFIER = 2
        private const val IMPACT_DAMAGE = 2.0f
    }
}