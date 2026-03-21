package com.dec.decisland.entity.projectile

import com.dec.decisland.api.CustomInertia
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.category.Weapon
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

class ThrowableBombProjectile(entityType: EntityType<ThrowableBombProjectile>, level: Level) :
    ThrowableItemProjectile(entityType, level), CustomInertia {
    constructor(level: Level, owner: LivingEntity, item: ItemStack) : this(ModEntities.THROWABLE_BOMB.get(), level) {
        setOwner(owner)
        setItem(item.copyWithCount(1))
    }

    constructor(level: Level, x: Double, y: Double, z: Double, item: ItemStack) : this(ModEntities.THROWABLE_BOMB.get(), level) {
        setPos(x, y, z)
        setItem(item.copyWithCount(1))
    }

    override fun getDefaultItem(): Item = Weapon.THROWABLE_BOMB.get()

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val entity: Entity = result.entity
        entity.hurt(damageSources().thrown(this, getOwner()), 7.0f)
    }

    override fun onHit(result: HitResult) {
        super.onHit(result)
        if (!level().isClientSide) {
            level().explode(this, x, y, z, 2.0f, true, Level.ExplosionInteraction.MOB)
            discard()
        }
    }

    override fun getDefaultGravity(): Double = 0.05

    override val airInertia: Float
        get() = 1.2f

    override val waterInertia: Float
        get() = 1.0f
}
