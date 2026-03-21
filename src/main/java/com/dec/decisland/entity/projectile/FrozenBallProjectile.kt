package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import com.dec.decisland.api.CustomInertia
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.category.Weapon
import com.dec.decisland.network.Networking
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

class FrozenBallProjectile(entityType: EntityType<FrozenBallProjectile>, level: Level) :
    ThrowableItemProjectile(entityType, level), CustomInertia {
    constructor(level: Level, owner: LivingEntity, item: ItemStack) : this(ModEntities.FROZEN_BALL.get(), level) {
        setOwner(owner)
        setItem(item.copyWithCount(1))
    }

    constructor(level: Level, x: Double, y: Double, z: Double, item: ItemStack) : this(ModEntities.FROZEN_BALL.get(), level) {
        setPos(x, y, z)
        setItem(item.copyWithCount(1))
    }

    override fun getDefaultItem(): Item = Weapon.FROZEN_BALL.get()

    override fun tick() {
        super.tick()
        val serverLevel = level() as? ServerLevel ?: return
        Networking.sendBedrockEmitterToNearby(serverLevel, TRAIL_PARTICLE_ID, position(), 64.0, 2)
    }

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val entity: Entity = result.entity
        entity.hurt(damageSources().thrown(this, getOwner()), 5.0f)
    }

    override fun onHit(result: HitResult) {
        super.onHit(result)
        if (level().isClientSide) {
            return
        }

        val serverLevel = level() as? ServerLevel ?: return
        serverLevel.sendParticles(
            ItemParticleOption(ParticleTypes.ITEM, ItemStack(Items.SNOWBALL)),
            x,
            y,
            z,
            6,
            0.15,
            0.15,
            0.15,
            0.0,
        )
        discard()
    }

    override fun getDefaultGravity(): Double = 0.0

    override val airInertia: Float
        get() = 1.0f

    override val waterInertia: Float
        get() = 1.0f

    companion object {
        private val TRAIL_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "everlasting_winter_wake_particle")
    }
}
