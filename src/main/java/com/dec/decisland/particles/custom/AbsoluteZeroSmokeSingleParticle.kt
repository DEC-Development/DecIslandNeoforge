package com.dec.decisland.particles.custom

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource
import kotlin.math.max

class AbsoluteZeroSmokeSingleParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double,
    private val sprites: SpriteSet,
) : SingleQuadParticle(level, x, y, z, sprites.get(0, 1)) {
    private val rotationRateRadPerTick: Float

    init {
        quadSize = 0.0f
        xd = xSpeed
        yd = ySpeed
        zd = zSpeed
        lifetime = (MAX_LIFETIME_SEC * 20).toInt()
        roll = level.random.nextFloat() * (2 * Math.PI).toFloat()
        val rotRateDegPerSec = level.random.nextFloat() * 60 - 30
        rotationRateRadPerTick = Math.toRadians(rotRateDegPerSec.toDouble()).toFloat()
        setSprite(sprites.get(0, lifetime))
    }

    override fun tick() {
        super.tick()
        oRoll = roll
        yd += GRAVITY / 20.0
        roll += rotationRateRadPerTick
        val ageSec = age / 20.0f
        val t = ageSec / MAX_LIFETIME_SEC
        val sizeFactor = cubicBezier(t, P0, P1, P2, P3)
        quadSize = max(0.0f, 0.3f * sizeFactor)
    }

    override fun getLayer(): Layer = Layer.TRANSLUCENT

    override fun getFacingCameraMode(): FacingCameraMode = FacingCameraMode.LOOKAT_XYZ

    class Provider(private val sprite: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            particleType: SimpleParticleType,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double,
            random: RandomSource,
        ): Particle = AbsoluteZeroSmokeSingleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite)
    }

    companion object {
        private const val MAX_LIFETIME_SEC: Float = 3.0f
        private const val GRAVITY: Float = -0.2f
        private const val P0: Float = -0.05f
        private const val P1: Float = 2.09f
        private const val P2: Float = 0.69f
        private const val P3: Float = 0.0f

        private fun cubicBezier(t: Float, p0: Float, p1: Float, p2: Float, p3: Float): Float {
            val u = 1 - t
            return u * u * u * p0 + 3 * u * u * t * p1 + 3 * u * t * t * p2 + t * t * t * p3
        }
    }
}
