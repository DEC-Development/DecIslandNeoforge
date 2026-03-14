package com.dec.decisland.entity.projectile

import com.dec.decisland.api.CustomInertia
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.ModItems
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

class ThrownAshPufferfish(entityType: EntityType<ThrownAshPufferfish>, level: Level) :
    ThrowableItemProjectile(entityType, level), CustomInertia {
    constructor(level: Level, owner: LivingEntity, item: ItemStack) : this(ModEntities.THROWN_ASH_PUFFERFISH.get(), level) {
        setOwner(owner)
        setItem(item)
    }

    constructor(level: Level, x: Double, y: Double, z: Double, item: ItemStack) : this(ModEntities.THROWN_ASH_PUFFERFISH.get(), level) {
        setPos(x, y, z)
        setItem(item)
    }

    override fun getDefaultItem(): Item = ModItems.ASH_PUFFERFISH.get()

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val entity: Entity = result.entity
        entity.hurt(damageSources().thrown(this, getOwner()), 4.0f)
        if (!level().isClientSide) {
            level().explode(this, x, y, z, 1.5f, Level.ExplosionInteraction.MOB)
            discard()
        }
    }

    override fun onHit(result: HitResult) {
        super.onHit(result)
        if (!level().isClientSide) {
            level().explode(this, x, y, z, 2.0f, true, Level.ExplosionInteraction.MOB)
            discard()
        }
    }

    override fun getDefaultGravity(): Double = 0.04

    override val waterInertia: Float
        get() = 0.99f
}
