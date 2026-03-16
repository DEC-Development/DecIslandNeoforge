package com.dec.decisland.entity.projectile
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.particles.ModParticles
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class EnergyRay(entityType: EntityType<EnergyRay>, level: Level) : Projectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.ENERGY_RAY.get(), level) {
        setOwner(owner)
        setPos(
            owner.x + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).x,
            owner.eyeY + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).y,
            owner.z + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).z,
        )
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    }

    override fun getDefaultGravity(): Double = 0.0

    override fun tick() {
        if (tickCount > 60) discard()
        super.tick()
        val vec3: Vec3 = deltaMovement
        val hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity)
        if (hitResult.type != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitResult)) {
            hitTargetOrDeflectSelf(hitResult)
        }
        val d0 = x + vec3.x
        val d1 = y + vec3.y
        val d2 = z + vec3.z
        updateRotation()
        if (level().getBlockStates(boundingBox).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            discard()
        } else {
            setDeltaMovement(vec3)
            applyGravity()
            setPos(d0, d1, d2)
        }
        level().addParticle(ModParticles.BLIZZARD_WAKE_PARTICLE.get(), position().x, position().y, position().z, 0.0, 0.0, 0.0)
    }

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val owner = getOwner()
        if (owner is LivingEntity) {
            val entity: Entity = result.entity
            val damageSource: DamageSource = damageSources().spit(this, owner)
            val currentLevel = level()
            if (currentLevel is ServerLevel && entity.hurtServer(currentLevel, damageSource, 7.0f)) {
                EnchantmentHelper.doPostAttackEffects(currentLevel, entity, damageSource)
            }
        }
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        if (!level().isClientSide) {
            discard()
        }
    }
}
