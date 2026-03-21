package com.dec.decisland.particles.bedrock

data class BedrockEmitterLocalSpace(
    val position: Boolean? = null,
    val rotation: Boolean? = null,
    val velocity: Boolean? = null,
)

data class BedrockEmitterInitialization(
    val creationExpression: String? = null,
)

data class BedrockBezierNode(
    val position: Double,
    val value: Double,
    val slope: Double = 0.0,
)

sealed class BedrockCurve {
    abstract val input: String?
    abstract val horizontalRange: String?

    data class Linear(
        override val input: String? = null,
        override val horizontalRange: String? = null,
        val nodes: List<Double> = emptyList(),
    ) : BedrockCurve()

    data class CatmullRom(
        override val input: String? = null,
        override val horizontalRange: String? = null,
        val nodes: List<Double> = emptyList(),
    ) : BedrockCurve()

    data class BezierChain(
        override val input: String? = null,
        override val horizontalRange: String? = null,
        val nodes: List<BedrockBezierNode> = emptyList(),
    ) : BedrockCurve()
}

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

    data class Manual(
        val maxParticles: String? = null,
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

    data class Box(
        val halfDimensions: DoubleArray? = null,
        val offset: DoubleArray? = null,
        val surfaceOnly: Boolean? = null,
        val direction: Any? = null,
    ) : BedrockEmitterShape()

    data class Disc(
        val radius: String? = null,
        val normal: DoubleArray? = null,
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

data class BedrockParticleAppearanceTinting(
    val color: BedrockTintColor? = null,
    val alpha: Any? = null,
)

data class BedrockParticleAppearanceLighting(
    val isLit: Boolean = true,
)

sealed class BedrockTintColor {
    data class ConstantRgb(val rgb: DoubleArray) : BedrockTintColor()

    data class ConstantHex(val argb: Int) : BedrockTintColor()

    data class Gradient(val interpolant: String, val stops: List<ColorStop>) : BedrockTintColor()
}

data class ColorStop(
    val t: Double,
    val argb: Int,
)

data class BedrockParticleLifetimeExpression(
    val maxLifetime: String? = null,
)

data class BedrockParticleInitialization(
    val creationExpression: String? = null,
    val perUpdateExpression: String? = null,
    val perRenderExpression: String? = null,
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

data class BedrockParticleCollisionEvent(
    val event: String,
    val minSpeed: String? = null,
)

data class BedrockParticleMotionCollision(
    val collisionRadius: String? = null,
    val expireOnContact: Boolean? = null,
    val events: List<BedrockParticleCollisionEvent> = emptyList(),
)

data class BedrockParticleEventEffect(
    val effect: String? = null,
    val type: String? = null,
)

data class BedrockParticleEventDefinition(
    val particleEffect: BedrockParticleEventEffect? = null,
)

data class BedrockParticleEffectDefinition(
    val renderMaterial: String? = null,
    val emitterInitialization: BedrockEmitterInitialization? = null,
    val emitterLocalSpace: BedrockEmitterLocalSpace? = null,
    val emitterLifetimeExpression: BedrockEmitterLifetimeExpression? = null,
    val emitterLifetimeOnce: BedrockEmitterLifetimeOnce? = null,
    val emitterLifetimeLooping: BedrockEmitterLifetimeLooping? = null,
    val emitterRate: BedrockEmitterRate? = null,
    val emitterShape: BedrockEmitterShape? = null,
    val particleLifetimeExpression: BedrockParticleLifetimeExpression? = null,
    val particleInitialization: BedrockParticleInitialization? = null,
    val particleInitialSpeed: BedrockParticleInitialSpeed? = null,
    val particleInitialSpin: BedrockParticleInitialSpin? = null,
    val particleMotionDynamic: BedrockParticleMotionDynamic? = null,
    val particleMotionCollision: BedrockParticleMotionCollision? = null,
    val particleAppearanceBillboard: BedrockParticleAppearanceBillboard? = null,
    val particleAppearanceTinting: BedrockParticleAppearanceTinting? = null,
    val particleAppearanceLighting: BedrockParticleAppearanceLighting? = null,
    val events: Map<String, BedrockParticleEventDefinition> = emptyMap(),
    val curves: Map<String, BedrockCurve> = emptyMap(),
)
