package com.dec.decisland.client.bedrock

import com.dec.decisland.particles.bedrock.BedrockParticleEffectDefinition
import com.dec.decisland.particles.bedrock.Molang
import com.dec.decisland.particles.bedrock.MolangContext
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import kotlin.math.PI
import kotlin.math.max

class BedrockBillboardParticle(
    private val definition: BedrockParticleEffectDefinition,
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double,
    ySpeed: Double,
    zSpeed: Double,
    sprite: TextureAtlasSprite,
) : SingleQuadParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite) {
    private val particleRandom1 = Math.random()
    private val particleRandom2 = Math.random()

    init {
        setParticleSpeed(xSpeed, ySpeed, zSpeed)

        definition.particleLifetimeExpression?.maxLifetime?.let { expr ->
            val seconds = Molang.evalDouble(expr, createContext()) ?: 1.0
            setLifetime(max(1, (seconds * 20.0).toInt()))
        }

        definition.particleInitialSpin?.rotation?.let { expr ->
            val degrees = Molang.evalDouble(expr, createContext()) ?: 0.0
            roll = ((degrees / 180.0) * PI).toFloat()
            oRoll = roll
        }

        definition.particleMotionDynamic?.linearDragCoefficient?.let { expr ->
            val drag = Molang.evalDouble(expr, createContext()) ?: 0.0
            friction = (1.0 - (drag / 20.0)).toFloat().coerceIn(0.0f, 1.0f)
        }

        definition.particleMotionCollision?.collisionRadius?.let { expr ->
            val radius = Molang.evalDouble(expr, createContext()) ?: 0.0
            hasPhysics = true
            setSize((radius * 2.0).toFloat(), (radius * 2.0).toFloat())
        }

        applySize()
    }

    override fun tick() {
        super.tick()
        if (!isAlive) {
            return
        }

        val ctx = createContext()
        definition.particleMotionDynamic?.linearAcceleration?.let { acc ->
            xd += acc.getOrElse(0) { 0.0 } / 400.0
            yd += acc.getOrElse(1) { 0.0 } / 400.0
            zd += acc.getOrElse(2) { 0.0 } / 400.0
        }

        definition.particleInitialSpin?.rotationRate?.let { expr ->
            val degreesPerSecond = Molang.evalDouble(expr, ctx) ?: 0.0
            oRoll = roll
            roll += ((degreesPerSecond / 20.0) * (PI / 180.0)).toFloat()
        }

        applySize()
    }

    override fun getFacingCameraMode(): FacingCameraMode =
        when (definition.particleAppearanceBillboard?.facingCameraMode?.lowercase()) {
            "lookat_y", "rotate_y" -> FacingCameraMode.LOOKAT_Y
            else -> FacingCameraMode.LOOKAT_XYZ
        }

    override fun getLayer(): Layer = Layer.TRANSLUCENT

    override fun getLightColor(partialTick: Float): Int {
        if (definition.particleAppearanceLighting?.isLit == true) {
            return super.getLightColor(partialTick)
        }
        return 0xF000F0
    }

    override fun getU0(): Float = computeUv().u0
    override fun getU1(): Float = computeUv().u1
    override fun getV0(): Float = computeUv().v0
    override fun getV1(): Float = computeUv().v1

    private fun applySize() {
        val size = definition.particleAppearanceBillboard?.size ?: return
        val ctx = createContext()
        val width: Double
        val height: Double
        when (size) {
            is Number -> {
                width = size.toDouble()
                height = size.toDouble()
            }
            is DoubleArray -> {
                width = size.getOrElse(0) { 0.2 }
                height = size.getOrElse(1) { width }
            }
            is Array<*> -> {
                width = evalSizePart(size.getOrNull(0), ctx, 0.2)
                height = evalSizePart(size.getOrNull(1), ctx, width)
            }
            else -> return
        }
        quadSize = max(width.toFloat(), height.toFloat()) * 0.5f
    }

    private fun evalSizePart(value: Any?, ctx: MolangContext, fallback: Double): Double =
        when (value) {
            is Number -> value.toDouble()
            is String -> Molang.evalDouble(value, ctx) ?: fallback
            else -> fallback
        }

    private data class UvRect(val u0: Float, val u1: Float, val v0: Float, val v1: Float)

    private fun computeUv(): UvRect {
        val uv = definition.particleAppearanceBillboard?.uv ?: return UvRect(super.getU0(), super.getU1(), super.getV0(), super.getV1())
        val sprite = this.sprite
        val textureWidth = uv.textureWidth?.takeIf { it > 0 } ?: sprite.contents().width()
        val textureHeight = uv.textureHeight?.takeIf { it > 0 } ?: sprite.contents().height()

        val baseU: Int
        val baseV: Int
        val sizeU: Int
        val sizeV: Int

        if (uv.flipbook != null) {
            val flipbook = uv.flipbook
            val base = flipbook.baseUV ?: intArrayOf(0, 0)
            val step = flipbook.stepUV ?: intArrayOf(0, 0)
            val size = flipbook.sizeUV ?: intArrayOf(textureWidth, textureHeight)
            val maxFrame = resolveFlipbookMaxFrameIndex(
                textureWidth = textureWidth,
                textureHeight = textureHeight,
                baseU = base.getOrElse(0) { 0 },
                baseV = base.getOrElse(1) { 0 },
                stepU = step.getOrElse(0) { 0 },
                stepV = step.getOrElse(1) { 0 },
                sizeU = size.getOrElse(0) { textureWidth },
                sizeV = size.getOrElse(1) { textureHeight },
                configuredMaxFrame = flipbook.maxFrame,
            )
            val frame = if (flipbook.stretchToLifetime == true) {
                (((age.toDouble() / lifetime.toDouble()) * (maxFrame + 1)).toInt()).coerceIn(0, maxFrame)
            } else {
                val fps = flipbook.framesPerSecond ?: 0.0
                (((age.toDouble() / 20.0) * fps).toInt()).let { raw ->
                    if (flipbook.loop == true && maxFrame > 0) raw % (maxFrame + 1) else raw.coerceIn(0, maxFrame)
                }
            }
            baseU = base.getOrElse(0) { 0 } + (step.getOrElse(0) { 0 } * frame)
            baseV = base.getOrElse(1) { 0 } + (step.getOrElse(1) { 0 } * frame)
            sizeU = size.getOrElse(0) { textureWidth }
            sizeV = size.getOrElse(1) { textureHeight }
        } else {
            val base = uv.uv ?: intArrayOf(0, 0)
            val size = uv.uvSize ?: intArrayOf(textureWidth, textureHeight)
            baseU = base.getOrElse(0) { 0 }
            baseV = base.getOrElse(1) { 0 }
            sizeU = size.getOrElse(0) { textureWidth }
            sizeV = size.getOrElse(1) { textureHeight }
        }

        val u0 = sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * (baseU.toFloat() / textureWidth.toFloat()))
        val u1 = sprite.getU0() + ((sprite.getU1() - sprite.getU0()) * ((baseU + sizeU).toFloat() / textureWidth.toFloat()))
        val v0 = sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * (baseV.toFloat() / textureHeight.toFloat()))
        val v1 = sprite.getV0() + ((sprite.getV1() - sprite.getV0()) * ((baseV + sizeV).toFloat() / textureHeight.toFloat()))
        return UvRect(u0, u1, v0, v1)
    }

    private fun resolveFlipbookMaxFrameIndex(
        textureWidth: Int,
        textureHeight: Int,
        baseU: Int,
        baseV: Int,
        stepU: Int,
        stepV: Int,
        sizeU: Int,
        sizeV: Int,
        configuredMaxFrame: Int?,
    ): Int {
        val maxByWidth = maxFramesForAxis(textureWidth, baseU, sizeU, stepU)
        val maxByHeight = maxFramesForAxis(textureHeight, baseV, sizeV, stepV)
        val maxThatFits = minOf(maxByWidth, maxByHeight).coerceAtLeast(0)
        return minOf(configuredMaxFrame ?: maxThatFits, maxThatFits).coerceAtLeast(0)
    }

    private fun maxFramesForAxis(textureSize: Int, base: Int, frameSize: Int, step: Int): Int =
        when {
            step > 0 -> ((textureSize - frameSize - base) / step).coerceAtLeast(0)
            step < 0 -> (base / -step).coerceAtLeast(0)
            else -> 0
        }

    private fun createContext(): MolangContext =
        MolangContext(
            particleAge = age.toDouble() / 20.0,
            particleLifetime = lifetime.toDouble() / 20.0,
            particleRandom1 = particleRandom1,
            particleRandom2 = particleRandom2,
        )
}
