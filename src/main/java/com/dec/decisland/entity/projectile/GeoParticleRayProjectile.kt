package com.dec.decisland.entity.projectile

import com.dec.decisland.network.Networking
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import software.bernie.geckolib.animatable.GeoEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.manager.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.RawAnimation
import software.bernie.geckolib.util.GeckoLibUtil

abstract class GeoParticleRayProjectile(
    entityType: EntityType<out GeoParticleRayProjectile>,
    level: Level,
) : ParticleRayProjectile(entityType, level), GeoEntity {
    private val geoCache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)

    protected open val spawnForwardOffset: Double = 0.9
    protected open val extraTrailOffsets: DoubleArray = doubleArrayOf(0.18, 0.36)

    protected fun setSpawnPositionFromOwner(owner: LivingEntity) {
        setOwner(owner)
        val view = owner.getViewVector(0.0f)
        val spawnPos = owner.eyePosition.add(view.scale(spawnForwardOffset))
        setPos(spawnPos.x, spawnPos.y, spawnPos.z)
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.add(AnimationController<GeoParticleRayProjectile>("fly", 0) { state ->
            state.setAndContinue(FLY_ANIMATION)
        })
    }

    override fun spawnAdditionalTrailParticles(serverLevel: ServerLevel, pos: Vec3) {
        val particleId = trailParticleId ?: return
        val motion = deltaMovement
        if (motion.lengthSqr() <= 1.0E-6) {
            return
        }

        val direction = motion.normalize()
        extraTrailOffsets.forEach { offset ->
            Networking.sendBedrockEmitterToNearby(
                serverLevel,
                particleId,
                pos.subtract(direction.scale(offset)),
                64.0,
                trailDurationTicks,
            )
        }
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = geoCache

    companion object {
        private val FLY_ANIMATION: RawAnimation = RawAnimation.begin().thenLoop("animation.energy_ball.fly")
    }
}
