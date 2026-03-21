package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import com.dec.decisland.api.CustomInertia
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.category.Weapon
import com.dec.decisland.network.Networking
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.FluidTags
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult

class ThrownStickyAsh(entityType: EntityType<ThrownStickyAsh>, level: Level) :
    ThrowableItemProjectile(entityType, level), CustomInertia {
    constructor(level: Level, owner: LivingEntity, item: ItemStack) : this(ModEntities.STICKY_ASH.get(), level) {
        setOwner(owner)
        setItem(item.copyWithCount(1))
    }

    constructor(level: Level, x: Double, y: Double, z: Double, item: ItemStack) : this(ModEntities.STICKY_ASH.get(), level) {
        setPos(x, y, z)
        setItem(item.copyWithCount(1))
    }

    override fun getDefaultItem(): Item = Weapon.STICKY_ASH.get()

    override fun tick() {
        super.tick()
        val serverLevel = level() as? ServerLevel ?: return
        if (tickCount % 2 == 0) {
            serverLevel.sendParticles(ParticleTypes.ASH, x, y, z, 1, 0.02, 0.02, 0.02, 0.0)
        }
    }

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val entity: Entity = result.entity
        val projectileOwner = getOwner()
        entity.hurt(damageSources().thrown(this, projectileOwner), 6.0f)
        if (entity is LivingEntity) {
            entity.addEffect(MobEffectInstance(MobEffects.WITHER, 200, 1), projectileOwner)
        }
    }

    override fun onHit(result: HitResult) {
        super.onHit(result)
        if (!level().isClientSide) {
            applyImpactEffects(level() as ServerLevel)
        }
    }

    override fun getDefaultGravity(): Double = 0.03

    override val airInertia: Float
        get() = 1.1f

    private fun applyImpactEffects(level: ServerLevel) {
        replaceNearbyLava(level)
        Networking.sendBedrockEmitterToNearby(level, HIT_PARTICLE_ID, position(), 64.0, 10)
        discard()
    }

    private fun replaceNearbyLava(level: ServerLevel) {
        val center = blockPosition()
        val min = center.offset(-2, -2, -2)
        val max = center.offset(2, 2, 2)
        for (pos in BlockPos.betweenClosed(min, max)) {
            val state = level.getBlockState(pos)
            val fluidState = state.fluidState
            if (!fluidState.`is`(FluidTags.LAVA) || fluidState.type !== Fluids.LAVA || fluidState.amount != 8) {
                continue
            }
            level.setBlockAndUpdate(pos, ModBlocks.ASH.get().defaultBlockState())
        }
    }

    companion object {
        private val HIT_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "sticky_ash_particle")
    }
}
