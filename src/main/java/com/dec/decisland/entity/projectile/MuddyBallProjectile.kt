package com.dec.decisland.entity.projectile

import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.category.Weapon
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

class MuddyBallProjectile(entityType: EntityType<MuddyBallProjectile>, level: Level) :
    ThrowableItemProjectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, item: ItemStack) : this(ModEntities.MUDDY_BALL.get(), level) {
        setOwner(owner)
        setItem(item.copyWithCount(1))
    }

    constructor(level: Level, x: Double, y: Double, z: Double, item: ItemStack) : this(ModEntities.MUDDY_BALL.get(), level) {
        setPos(x, y, z)
        setItem(item.copyWithCount(1))
    }

    override fun getDefaultItem(): Item = Weapon.MUDDY_BALL.get()

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val entity: Entity = result.entity
        val projectileOwner = getOwner()
        entity.hurt(damageSources().thrown(this, projectileOwner), 2.0f)
        if (entity is LivingEntity) {
            entity.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 150, 0), projectileOwner)
        }
    }

    override fun onHit(result: HitResult) {
        super.onHit(result)
        val serverLevel = level() as? ServerLevel ?: return
        serverLevel.playSound(
            null,
            blockPosition(),
            SoundEvents.MUD_BREAK,
            SoundSource.NEUTRAL,
            0.8f,
            1.0f,
        )
        serverLevel.sendParticles(
            BlockParticleOption(ParticleTypes.BLOCK, Blocks.MUD.defaultBlockState()),
            x,
            y,
            z,
            8,
            0.12,
            0.12,
            0.12,
            0.0,
        )
        discard()
    }

    override fun getDefaultGravity(): Double = 0.04
}
