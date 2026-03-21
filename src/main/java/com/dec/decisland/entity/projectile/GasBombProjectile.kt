package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import com.dec.decisland.api.CustomInertia
import com.dec.decisland.effect.ModEffects
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.category.Weapon
import com.dec.decisland.network.Networking
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class GasBombProjectile(entityType: EntityType<GasBombProjectile>, level: Level) :
    ThrowableItemProjectile(entityType, level), CustomInertia {
    constructor(level: Level, owner: LivingEntity, item: ItemStack) : this(ModEntities.GAS_BOMB.get(), level) {
        setOwner(owner)
        setItem(item.copyWithCount(1))
    }

    constructor(level: Level, x: Double, y: Double, z: Double, item: ItemStack) : this(ModEntities.GAS_BOMB.get(), level) {
        setPos(x, y, z)
        setItem(item.copyWithCount(1))
    }

    override fun getDefaultItem(): Item = Weapon.GAS_BOMB.get()

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val entity: Entity = result.entity
        entity.hurt(damageSources().thrown(this, getOwner()), 6.0f)
    }

    override fun onHit(result: HitResult) {
        super.onHit(result)
        val serverLevel = level() as? ServerLevel ?: return
        explode(serverLevel, result.location)
    }

    override fun getDefaultGravity(): Double = 0.05

    override val airInertia: Float
        get() = 1.1f

    private fun explode(serverLevel: ServerLevel, pos: Vec3) {
        serverLevel.playSound(null, pos.x, pos.y, pos.z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.NEUTRAL, 1.0f, 1.0f)
        Networking.sendBedrockEmitterToNearby(serverLevel, HIT_PARTICLE_ID, pos, 64.0, 10)
        applyStatusEffects(serverLevel, pos)
        discard()
    }

    private fun applyStatusEffects(serverLevel: ServerLevel, pos: Vec3) {
        val area = AABB(pos.x - EFFECT_RADIUS, pos.y - EFFECT_RADIUS, pos.z - EFFECT_RADIUS, pos.x + EFFECT_RADIUS, pos.y + EFFECT_RADIUS, pos.z + EFFECT_RADIUS)
        val projectileOwner = owner as? LivingEntity
        serverLevel.getEntitiesOfClass(LivingEntity::class.java, area).forEach { target ->
            if (!target.isAlive || target.position().distanceToSqr(pos) > EFFECT_RADIUS * EFFECT_RADIUS) {
                return@forEach
            }
            target.addEffect(MobEffectInstance(MobEffects.POISON, 20 * 10, 0), projectileOwner)
            target.addEffect(MobEffectInstance(MobEffects.NAUSEA, 20 * 10, 0), projectileOwner)
            if (target is Player) {
                target.addEffect(MobEffectInstance(ModEffects.DIZZINESS, 20 * 10, 2), projectileOwner)
            }
        }
    }

    companion object {
        private val HIT_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "gas_bomb_particle")

        private const val EFFECT_RADIUS: Double = 4.0
    }
}
