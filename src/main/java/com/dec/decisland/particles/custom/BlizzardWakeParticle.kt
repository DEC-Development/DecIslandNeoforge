package com.dec.decisland.particles.custom

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource
import kotlin.math.max

class BlizzardWakeParticle(level: ClientLevel, x: Double, y: Double, z: Double, private val sprites: SpriteSet) :
    SingleQuadParticle(level, x, y, z, sprites.get(0, 1)) {
    private val initialSize: Float

    init {
        xd = 0.0
        yd = 0.0
        zd = 0.0
        lifetime = (20.0 * (0.6 + level.random.nextDouble() * 1.4)).toInt()
        initialSize = 0.05f + level.random.nextDouble().toFloat() * 0.05f
        quadSize = initialSize
        setSprite(sprites.get(0, lifetime))
    }

    override fun tick() {
        super.tick()
        val ageSec = age / 20.0f
        quadSize = max(0.0f, initialSize - ageSec * 0.05f)
    }

    override fun getLayer(): Layer = Layer.OPAQUE

    override fun getFacingCameraMode(): FacingCameraMode = FacingCameraMode.LOOKAT_XYZ

    override fun getGroup(): ParticleRenderType = ParticleRenderType.SINGLE_QUADS

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
        ): Particle = BlizzardWakeParticle(level, x, y, z, sprite)
    }
}
