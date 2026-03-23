package com.dec.decisland.entity.projectile

import com.dec.decisland.api.CustomInertia
import com.dec.decisland.DecIsland
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.ModItems
import com.dec.decisland.network.Networking
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3

class LapisBullet(entityType: EntityType<LapisBullet>, level: Level) : ThrowableItemProjectile(entityType, level), CustomInertia {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.LAPIS_BULLET.get(), level) {
        setOwner(owner)
        setItem(ItemStack(ModItems.LAPIS_BULLET_RENDER.get()))
        val view = owner.getViewVector(0.0f)
        val spawnPos = owner.eyePosition.add(view.scale(SPAWN_FORWARD_OFFSET))
        setPos(spawnPos.x, spawnPos.y, spawnPos.z)
    }

    override fun getDefaultItem(): Item = ModItems.LAPIS_BULLET_RENDER.get()

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val serverLevel = level() as? ServerLevel ?: return
        val target = result.entity
        target.hurtServer(serverLevel, damageSources().thrown(this, getOwner()), BASE_DAMAGE)
        explode(serverLevel, target.position())
        discard()
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        val serverLevel = level() as? ServerLevel ?: return
        val pos = result.location
        explode(serverLevel, pos)
        discard()
    }

    override fun getDefaultGravity(): Double = 0.0

    override val airInertia: Float
        get() = 1.1f

    override val waterInertia: Float
        get() = 1.0f

    private fun explode(serverLevel: ServerLevel, center: Vec3) {
        val particlePos = center.add(0.0, GROUND_PARTICLE_Y_OFFSET, 0.0)
        Networking.sendBedrockEmitterToNearby(serverLevel, BOOM_PARTICLE_ID, particlePos, 64.0, 6)
        val damageSource = damageSources().thrown(this, getOwner())
        serverLevel.getEntities(this, AABB.ofSize(center, EXPLOSION_RADIUS * 2, EXPLOSION_RADIUS * 2, EXPLOSION_RADIUS * 2)) { target ->
            target.isAlive && target !is ItemEntity
        }.forEach { target ->
            target.hurtServer(serverLevel, damageSource, SPLASH_DAMAGE)
        }
    }

    companion object {
        private const val BASE_DAMAGE: Float = 5.0f
        private const val SPAWN_FORWARD_OFFSET: Double = 0.85
        private const val EXPLOSION_RADIUS: Double = 2.0
        private const val SPLASH_DAMAGE: Float = 4.0f
        private const val GROUND_PARTICLE_Y_OFFSET: Double = 0.05
        private val BOOM_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "lapis_bullet_boom_particle")
    }
}
