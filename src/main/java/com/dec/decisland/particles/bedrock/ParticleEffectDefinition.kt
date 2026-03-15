package com.dec.decisland.particles.bedrock

data class BedrockEmitterLifetimeExpression(
    val activationExpression: String? = null,
    val expirationExpression: String? = null,
)

data class BedrockEmitterLifetimeOnce(
    val activeTime: String? = null,
)

data class BedrockEmitterLifetimeLooping(
    val activeTime: String? = null,
    val sleepTime: String? = null,
)

sealed class BedrockEmitterRate {
    data class Steady(
        val spawnRate: String? = null,
        val maxParticles: String? = null,
    ) : BedrockEmitterRate()

    data class Instant(
        val numParticles: String? = null,
    ) : BedrockEmitterRate()
}

sealed class BedrockEmitterShape {
    data class Point(
        val offset: DoubleArray? = null,
        val direction: Any? = null,
    ) : BedrockEmitterShape()

    data class Sphere(
        val radius: String? = null,
        val offset: DoubleArray? = null,
        val surfaceOnly: Boolean? = null,
        val direction: Any? = null,
    ) : BedrockEmitterShape()
}

data class BedrockParticleUv(
    val textureWidth: Int? = null,
    val textureHeight: Int? = null,
    val uv: IntArray? = null,
    val uvSize: IntArray? = null,
    val flipbook: BedrockParticleFlipbook? = null,
)

data class BedrockParticleFlipbook(
    val baseUV: IntArray? = null,
    val sizeUV: IntArray? = null,
    val stepUV: IntArray? = null,
    val framesPerSecond: Double? = null,
    val maxFrame: Int? = null,
    val stretchToLifetime: Boolean? = null,
    val loop: Boolean? = null,
)

data class BedrockParticleAppearanceBillboard(
    val size: Any? = null,
    val facingCameraMode: String? = null,
    val uv: BedrockParticleUv? = null,
)

data class BedrockParticleAppearanceLighting(
    val isLit: Boolean = true,
)

data class BedrockParticleLifetimeExpression(
    val maxLifetime: String? = null,
)

data class BedrockParticleInitialSpeed(
    val speed: Any? = null,
)

data class BedrockParticleInitialSpin(
    val rotation: String? = null,
    val rotationRate: String? = null,
)

data class BedrockParticleMotionDynamic(
    val linearAcceleration: DoubleArray? = null,
    val linearDragCoefficient: String? = null,
)

data class BedrockParticleMotionCollision(
    val collisionRadius: String? = null,
)

data class BedrockParticleEffectDefinition(
    val emitterLifetimeExpression: BedrockEmitterLifetimeExpression? = null,
    val emitterLifetimeOnce: BedrockEmitterLifetimeOnce? = null,
    val emitterLifetimeLooping: BedrockEmitterLifetimeLooping? = null,
    val emitterRate: BedrockEmitterRate? = null,
    val emitterShape: BedrockEmitterShape? = null,
    val particleLifetimeExpression: BedrockParticleLifetimeExpression? = null,
    val particleInitialSpeed: BedrockParticleInitialSpeed? = null,
    val particleInitialSpin: BedrockParticleInitialSpin? = null,
    val particleMotionDynamic: BedrockParticleMotionDynamic? = null,
    val particleMotionCollision: BedrockParticleMotionCollision? = null,
    val particleAppearanceBillboard: BedrockParticleAppearanceBillboard? = null,
    val particleAppearanceLighting: BedrockParticleAppearanceLighting? = null,
)
