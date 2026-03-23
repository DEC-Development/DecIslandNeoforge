package com.dec.decisland.entity.projectile

import com.dec.decisland.api.CustomInertia
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.ModItems
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

class JellyfishStaffProjectile(entityType: EntityType<JellyfishStaffProjectile>, level: Level) :
    ThrowableItemProjectile(entityType, level), CustomInertia {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.JELLYFISH_BY_JELLYFISH_STAFF.get(), level) {
        setOwner(owner)
        setItem(ItemStack(ModItems.JELLYFISH_STAFF_PROJECTILE_RENDER.get()))
        val view = owner.getViewVector(0.0f)
        val spawnPos = owner.eyePosition.add(view.scale(SPAWN_FORWARD_OFFSET))
        setPos(spawnPos.x, spawnPos.y, spawnPos.z)
    }

    override fun getDefaultItem(): Item = ModItems.JELLYFISH_STAFF_PROJECTILE_RENDER.get()

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        result.entity.hurt(damageSources().thrown(this, getOwner()), BASE_DAMAGE)
    }

    override fun onHit(result: HitResult) {
        super.onHit(result)
        if (!level().isClientSide) {
            discard()
        }
    }

    override fun getDefaultGravity(): Double = 0.0

    override val airInertia: Float
        get() = 1.0f

    override val waterInertia: Float
        get() = 1.1f

    companion object {
        private const val BASE_DAMAGE: Float = 12.0f
        private const val SPAWN_FORWARD_OFFSET: Double = 0.85
    }
}
