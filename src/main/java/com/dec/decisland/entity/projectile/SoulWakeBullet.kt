package com.dec.decisland.entity.projectile

import com.dec.decisland.api.CustomInertia
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.ModItems
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class SoulWakeBullet(entityType: EntityType<SoulWakeBullet>, level: Level) : ThrowableItemProjectile(entityType, level), CustomInertia {
    constructor(level: Level, owner: LivingEntity?, pos: Vec3, motion: Vec3) : this(ModEntities.SOUL_WAKE_BULLET.get(), level) {
        owner?.let(::setOwner)
        setItem(ItemStack(ModItems.SOUL_WAKE_BULLET_RENDER.get()))
        setPos(pos.x, pos.y, pos.z)
        setDeltaMovement(motion)
    }

    override fun getDefaultItem(): Item = ModItems.SOUL_WAKE_BULLET_RENDER.get()

    override fun tick() {
        super.tick()
        if (level().isClientSide) {
            return
        }

        val serverLevel = level() as? ServerLevel ?: return
        val touchedTarget = serverLevel.getEntitiesOfClass(LivingEntity::class.java, contactBox()) { target ->
            target.isAlive && target != getOwner()
        }.firstOrNull()
        if (touchedTarget != null) {
            applyBlindnessAndDiscard(touchedTarget)
            return
        }

        if (tickCount > MAX_LIFETIME_TICKS) {
            discard()
        }
    }

    override fun onHitEntity(result: EntityHitResult) {
        (result.entity as? LivingEntity)?.let(::applyBlindnessAndDiscard) ?: discard()
    }

    override fun onHit(result: HitResult) {
        super.onHit(result)
        if (!level().isClientSide) {
            discard()
        }
    }

    override fun getDefaultGravity(): Double = 0.0

    override val airInertia: Float
        get() = 1.1f

    override val waterInertia: Float
        get() = 1.0f

    private fun contactBox(): AABB = boundingBox.inflate(CONTACT_PADDING)

    private fun applyBlindnessAndDiscard(target: LivingEntity) {
        target.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 40, 0))
        discard()
    }

    companion object {
        private const val CONTACT_PADDING: Double = 0.05
        private const val MAX_LIFETIME_TICKS: Int = 100
    }
}
