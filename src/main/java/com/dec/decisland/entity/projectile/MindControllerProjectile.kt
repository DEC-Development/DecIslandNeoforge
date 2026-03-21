package com.dec.decisland.entity.projectile

import com.dec.decisland.api.CustomInertia
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.category.Weapon
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult

class MindControllerProjectile(entityType: EntityType<MindControllerProjectile>, level: Level) :
    ThrowableItemProjectile(entityType, level), CustomInertia {
    constructor(level: Level, owner: LivingEntity, item: ItemStack) : this(ModEntities.MIND_CONTROLLER.get(), level) {
        setOwner(owner)
        setItem(item.copyWithCount(1))
    }

    constructor(level: Level, x: Double, y: Double, z: Double, item: ItemStack) : this(ModEntities.MIND_CONTROLLER.get(), level) {
        setPos(x, y, z)
        setItem(item.copyWithCount(1))
    }

    override fun getDefaultItem(): Item = Weapon.MIND_CONTROLLER.get()

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val entity: Entity = result.entity
        entity.hurt(damageSources().thrown(this, getOwner()), 3.0f)
        if (entity is LivingEntity) {
            entity.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 24, 0), getOwner())
        }
    }

    override fun getDefaultGravity(): Double = 0.03

    override val airInertia: Float
        get() = 1.2f
}
