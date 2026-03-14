package com.dec.decisland.particles.custom

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource

class KatanaParticle(level: ClientLevel, x: Double, y: Double, z: Double, private val sprites: SpriteSet) :
    SingleQuadParticle(level, x, y, z, sprites.first()) {
    init {
        xd = 0.0
        yd = 0.0
        zd = 0.0
        lifetime = 6
        quadSize = 1.0f
        setSpriteFromAge(sprites)
    }

    override fun tick() {
        super.tick()
        setSpriteFromAge(sprites)
    }

    override fun getLayer(): Layer = Layer.OPAQUE

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
        ): Particle = KatanaParticle(level, x, y, z, sprite)
    }
}
