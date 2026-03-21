package com.dec.decisland.entity.projectile

import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.category.Weapon
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

class FireflyBottleProjectile(entityType: EntityType<FireflyBottleProjectile>, level: Level) :
    ThrowableItemProjectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, item: ItemStack) : this(ModEntities.FIREFLY_BOTTLE.get(), level) {
        setOwner(owner)
        setItem(item.copyWithCount(1))
    }

    constructor(level: Level, x: Double, y: Double, z: Double, item: ItemStack) : this(ModEntities.FIREFLY_BOTTLE.get(), level) {
        setPos(x, y, z)
        setItem(item.copyWithCount(1))
    }

    override fun getDefaultItem(): Item = Weapon.FIREFLY_BOTTLE.get()

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val entity: Entity = result.entity
        entity.hurt(damageSources().thrown(this, getOwner()), 1.0f)
    }

    override fun onHit(result: HitResult) {
        super.onHit(result)
        val serverLevel = level() as? ServerLevel ?: return
        val pos = result.location
        val xp = 6 + random.nextInt(13)

        ExperienceOrb.award(serverLevel, pos, xp)
        serverLevel.playSound(
            null,
            blockPosition(),
            SoundEvents.GLASS_BREAK,
            SoundSource.NEUTRAL,
            0.9f,
            1.1f + random.nextFloat() * 0.2f,
        )
        serverLevel.sendParticles(
            ParticleTypes.FIREFLY,
            pos.x,
            pos.y,
            pos.z,
            21,
            0.45,
            0.3,
            0.45,
            0.015,
        )
        serverLevel.sendParticles(
            ParticleTypes.FIREFLY,
            pos.x,
            pos.y + 0.1,
            pos.z,
            8,
            0.2,
            0.15,
            0.2,
            0.0,
        )
        discard()
    }

    override fun getDefaultGravity(): Double = 0.05
}
