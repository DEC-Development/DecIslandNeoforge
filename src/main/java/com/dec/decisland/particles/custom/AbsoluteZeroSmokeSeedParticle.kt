package com.dec.decisland.particles.custom

import com.dec.decisland.particles.ModParticles
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.NoRenderParticle
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource

class AbsoluteZeroSmokeSeedParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    private val numParticles: Int,
    private val radius: Double,
    private val initialSpeed: Double,
) : NoRenderParticle(level, x, y, z, 0.0, 0.0, 0.0) {
    init {
        lifetime = 1
    }

    override fun tick() {
        if (age == 0) {
            val random: RandomSource = this.random
            repeat(numParticles) {
                var dx: Double
                var dy: Double
                var dz: Double
                do {
                    dx = random.nextDouble() * 2 - 1
                    dy = random.nextDouble() * 2 - 1
                    dz = random.nextDouble() * 2 - 1
                } while (dx * dx + dy * dy + dz * dz > 1.0)

                val particleX = x + dx * radius
                val particleY = y + dy * radius + OFFSET_Y
                val particleZ = z + dz * radius
                val xSpeed = (random.nextDouble() * 2 - 1) * initialSpeed
                val ySpeed = DIRECTION_Y * initialSpeed
                val zSpeed = (random.nextDouble() * 2 - 1) * initialSpeed

                level.addParticle(
                    ModParticles.ABSOLUTE_ZERO_SMOKE_SINGLE_PARTICLE.get(),
                    particleX,
                    particleY,
                    particleZ,
                    xSpeed,
                    ySpeed,
                    zSpeed,
                )
            }
        }
        super.tick()
    }

    class Provider(
        private val numParticles: Int,
        private val radius: Double,
        private val initialSpeed: Double,
    ) : ParticleProvider<SimpleParticleType> {
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
        ): Particle = AbsoluteZeroSmokeSeedParticle(level, x, y, z, numParticles, radius, initialSpeed)
    }

    companion object {
        private const val OFFSET_Y: Double = 1.0
        private const val DIRECTION_Y: Double = 2.0
    }
}
