package com.dec.decisland.entity.projectile
import com.dec.decisland.DecIsland
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.network.Networking
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class SnowEnergy(entityType: EntityType<SnowEnergy>, level: Level) : Projectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity?, x: Double, y: Double, z: Double) : this(ModEntities.SNOW_ENERGY.get(), level) {
        setOwner(owner)
        setPos(x, y, z)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    }

    override fun getDefaultGravity(): Double = 0.03

    override fun tick() {
        if (tickCount > 80) {
            discard()
            return
        }

        super.tick()

        val motion = deltaMovement
        val hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity)
        if (hitResult.type != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitResult)) {
            hitTargetOrDeflectSelf(hitResult)
            return
        }

        val nextPos = position().add(motion)
        if (level().getBlockStates(boundingBox).anyMatch { !it.isAir }) {
            discard()
            return
        }

        if (!level().isClientSide) {
            val updatedMotion = motion.add(0.0, -getDefaultGravity(), 0.0).scale(1.0 / 1.01)
            setDeltaMovement(updatedMotion)
            if (tickCount % 4 == 0) {
                Networking.sendBedrockEmitterToNearby(
                    level() as ServerLevel,
                    Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "everlasting_winter_wake_particle"),
                    position(),
                    64.0,
                    4,
                )
            }
        }

        setPos(nextPos.x, nextPos.y, nextPos.z)
    }

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        if (level().isClientSide) {
            return
        }

        val target: Entity = result.entity
        target.hurt(damageSources().thrown(this, getOwner()), 4.0f)
        if (target is LivingEntity) {
            target.addEffect(MobEffectInstance(MobEffects.SLOWNESS, 200, 1))
        }
        spawnHitParticles()
        discard()
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        if (level().isClientSide) {
            return
        }

        spawnHitParticles()
        discard()
    }

    private fun spawnHitParticles() {
        (level() as? ServerLevel)?.sendParticles(
            ItemParticleOption(ParticleTypes.ITEM, ItemStack(Items.SNOWBALL)),
            x,
            y,
            z,
            3,
            0.1,
            0.1,
            0.1,
            0.0,
        )
    }
}
