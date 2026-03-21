package com.dec.decisland.client.bedrock

import com.dec.decisland.particles.ModParticles
import com.dec.decisland.particles.bedrock.BedrockParticleCollisionEvent
import com.dec.decisland.particles.bedrock.BedrockCurves
import com.dec.decisland.particles.bedrock.BedrockParticleEffectDefinition
import com.dec.decisland.particles.bedrock.BedrockTintColor
import com.dec.decisland.particles.bedrock.ColorStop
import com.dec.decisland.particles.bedrock.Molang
import com.dec.decisland.particles.bedrock.MolangContext
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.SingleQuadParticle
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.phys.Vec3
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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
    emitterVariables: Map<String, Double> = emptyMap(),
    private val emitterAgeAtSpawn: Double = 0.0,
    private val emitterRandom1: Double = Math.random(),
    private val emitterRandom2: Double = Math.random(),
    private val emitterRandom3: Double = Math.random(),
    private val emitterRandom4: Double = Math.random(),
) : SingleQuadParticle(level, x, y, z, sprite), EmitterBoundParticle {
    companion object {
        // Bedrock `particles_add` behaves closer to an emissive overlay than a lit translucent quad.
        private val ADDITIVE_PARTICLE_LAYER = Layer(true, TextureAtlas.LOCATION_PARTICLES, RenderPipelines.TRANSLUCENT_PARTICLE)
    }

    private var emitterOrigin = Vec3(x, y, z)
    private val emitterVariables = LinkedHashMap(emitterVariables)
    private val curveVariables = LinkedHashMap<String, Double>()
    private val particleVariables = LinkedHashMap<String, Double>()
    private val particleRandom1 = Math.random()
    private val particleRandom2 = Math.random()
    private val particleRandom3 = Math.random()
    private val particleRandom4 = Math.random()
    private val ticksPerSecond = 20.0

    private var halfWidth = 0.1f
    private var halfHeight = 0.1f

    private val uv = definition.particleAppearanceBillboard?.uv
    private val flipbook = uv?.flipbook

    init {
        setSprite(sprite)
        setParticleSpeed(xSpeed, ySpeed, zSpeed)

        refreshCurveVariables()
        runParticleInitialization(definition.particleInitialization?.creationExpression)
        refreshCurveVariables()

        definition.particleInitialSpin?.rotation?.let { expr ->
            val degrees = Molang.evalDouble(expr, createContext(), particleVariables) ?: expr.toDoubleOrNull()
            if (degrees != null) {
                val radians = ((degrees / 180.0) * PI).toFloat()
                roll = radians
                oRoll = radians
            }
        }

        definition.particleLifetimeExpression?.maxLifetime?.let { expr ->
            val seconds = Molang.evalDouble(expr, createContext(), particleVariables) ?: expr.toDoubleOrNull()
            if (seconds != null) {
                setLifetime(max(1, (seconds * ticksPerSecond).toInt()))
            }
        }

        definition.particleMotionCollision?.collisionRadius?.let { expr ->
            val radius = Molang.evalDouble(expr, createContext(), particleVariables) ?: expr.toDoubleOrNull()
            if (radius != null) {
                hasPhysics = true
                val size = (radius * 2.0).toFloat().coerceAtLeast(0.0f)
                setSize(size, size)
            }
        }

        applyDynamicDrag()
        runParticleInitialization(definition.particleInitialization?.perRenderExpression)
        refreshCurveVariables()
        applyTint()
        applySize()
    }

    override fun setEmitterOrigin(origin: Vec3) {
        emitterOrigin = origin
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z

        if (age++ >= lifetime) {
            remove()
            return
        }

        refreshCurveVariables()
        runParticleInitialization(definition.particleInitialization?.perUpdateExpression)
        refreshCurveVariables()
        applyDynamicDrag()

        val ctx = createContext()
        definition.particleMotionDynamic?.linearAcceleration?.let { acc ->
            val accelerationScale = 1.0 / (ticksPerSecond * ticksPerSecond)
            xd += acc.getOrElse(0) { 0.0 } * accelerationScale
            yd += acc.getOrElse(1) { 0.0 } * accelerationScale
            zd += acc.getOrElse(2) { 0.0 } * accelerationScale
        }

        definition.particleInitialSpin?.rotationRate?.let { expr ->
            val degreesPerSecond = Molang.evalDouble(expr, ctx, particleVariables) ?: expr.toDoubleOrNull()
            if (degreesPerSecond != null) {
                oRoll = roll
                roll += ((degreesPerSecond / ticksPerSecond) * (PI / 180.0)).toFloat()
            }
        }

        val requestedMove = Vec3(xd, yd, zd)
        val previousPos = Vec3(x, y, z)
        move(xd, yd, zd)
        val actualMove = Vec3(x - previousPos.x, y - previousPos.y, z - previousPos.z)

        if (hasPhysics && didCollide(requestedMove, actualMove)) {
            handleCollision(requestedMove)
            if (!isAlive) {
                return
            }
        }

        xd *= friction.toDouble()
        yd *= friction.toDouble()
        zd *= friction.toDouble()

        runParticleInitialization(definition.particleInitialization?.perRenderExpression)
        refreshCurveVariables()
        applyTint()
        applySize()
    }

    override fun getFacingCameraMode(): FacingCameraMode =
        when (definition.particleAppearanceBillboard?.facingCameraMode?.lowercase()) {
            "lookat_y", "rotate_y" -> FacingCameraMode.LOOKAT_Y
            else -> FacingCameraMode.LOOKAT_XYZ
        }

    override fun getLayer(): Layer =
        when (definition.renderMaterial?.lowercase()) {
            "particles_add" -> ADDITIVE_PARTICLE_LAYER
            else -> Layer.TRANSLUCENT
        }

    override fun getLightColor(partialTick: Float): Int {
        if (definition.renderMaterial.equals("particles_add", ignoreCase = true)) {
            return 0xF000F0
        }
        if (definition.particleAppearanceLighting?.isLit == true) {
            return super.getLightColor(partialTick)
        }
        return 0xF000F0
    }

    override fun getU0(): Float = computeUv().u0
    override fun getU1(): Float = computeUv().u1
    override fun getV0(): Float = computeUv().v0
    override fun getV1(): Float = computeUv().v1

    private fun runParticleInitialization(expression: String?) {
        Molang.evalDouble(expression, createContext(), particleVariables)
    }

    private fun refreshCurveVariables() {
        curveVariables.clear()
        curveVariables.putAll(
            BedrockCurves.evaluateAll(
                definition.curves,
                createRawContext(),
                particleVariables,
            ),
        )
    }

    private fun applyDynamicDrag() {
        definition.particleMotionDynamic?.linearDragCoefficient?.let { expr ->
            val drag = Molang.evalDouble(expr, createContext(), particleVariables) ?: expr.toDoubleOrNull()
            if (drag != null) {
                friction = (1.0 - (drag / ticksPerSecond)).toFloat().coerceIn(0.0f, 1.0f)
            }
        }
    }

    private fun didCollide(requested: Vec3, actual: Vec3): Boolean {
        val epsilon = 1.0E-6
        return abs(requested.x - actual.x) > epsilon ||
            abs(requested.y - actual.y) > epsilon ||
            abs(requested.z - actual.z) > epsilon
    }

    private fun handleCollision(requestedMove: Vec3) {
        val collision = definition.particleMotionCollision ?: return
        val speedPerSecond = requestedMove.length() * ticksPerSecond
        collision.events.forEach { event ->
            triggerCollisionEvent(event, speedPerSecond)
        }
        if (collision.expireOnContact == true) {
            remove()
        }
    }

    private fun triggerCollisionEvent(event: BedrockParticleCollisionEvent, speedPerSecond: Double) {
        val minSpeed = Molang.evalDouble(event.minSpeed, createContext(), particleVariables) ?: event.minSpeed?.toDoubleOrNull() ?: 0.0
        if (speedPerSecond < minSpeed) {
            return
        }

        val definitionEvent = definition.events[event.event] ?: return
        val particleEffect = definitionEvent.particleEffect ?: return
        if (particleEffect.type != null && particleEffect.type.lowercase() != "particle") {
            return
        }

        val effectId = Identifier.tryParse(particleEffect.effect ?: return) ?: return
        if (ModParticles.resolveBedrockDefinition(effectId) != null && ModParticles.resolveBedrockSpriteId(effectId) != null) {
            BedrockEmitterManager.spawnAt(effectId, Vec3(x, y, z), 2)
            return
        }

        val particleType = BuiltInRegistries.PARTICLE_TYPE.getValue(effectId)
        if (particleType is SimpleParticleType) {
            level.addParticle(particleType, x, y, z, 0.0, 0.0, 0.0)
        }
    }

    private fun applySize() {
        val sizeField = definition.particleAppearanceBillboard?.size ?: return
        val ctx = createContext()

        val (width, height) = when (sizeField) {
            is Number -> sizeField.toFloat() to sizeField.toFloat()
            is DoubleArray -> {
                val w = sizeField.getOrElse(0) { 0.2 }.toFloat()
                val h = sizeField.getOrElse(1) { w.toDouble() }.toFloat()
                w to h
            }
            is Array<*> -> {
                val w = evalSizePart(sizeField.getOrNull(0), ctx, 0.2)
                val h = evalSizePart(sizeField.getOrNull(1), ctx, w)
                w.toFloat() to h.toFloat()
            }
            else -> return
        }

        halfWidth = max(0.0f, width) * 0.5f
        halfHeight = max(0.0f, height) * 0.5f
        // SingleQuadParticle renders from quadSize directly, so Bedrock billboard sizes
        // should map to the full size value rather than a half-size radius.
        quadSize = max(0.0f, max(width, height))
    }

    private fun applyTint() {
        val tint = definition.particleAppearanceTinting ?: return
        val ctx = createContext()

        when (val color = tint.color) {
            is BedrockTintColor.ConstantRgb -> {
                val rgb = color.rgb
                if (rgb.size >= 3) {
                    setColor(rgb[0].toFloat(), rgb[1].toFloat(), rgb[2].toFloat())
                }
            }
            is BedrockTintColor.ConstantHex -> applyArgb(color.argb)
            is BedrockTintColor.Gradient -> {
                val t = (Molang.evalDouble(color.interpolant, ctx, particleVariables) ?: 0.0).coerceIn(0.0, 1.0)
                applyArgb(sampleGradientArgb(color.stops, t))
            }
            null -> Unit
        }

        when (val alphaValue = tint.alpha) {
            is Number -> setAlpha(alphaValue.toFloat())
            is String -> {
                val alpha = Molang.evalDouble(alphaValue, ctx, particleVariables) ?: alphaValue.toDoubleOrNull()
                if (alpha != null) {
                    setAlpha(alpha.toFloat())
                }
            }
        }
    }

    private fun evalSizePart(value: Any?, ctx: MolangContext, fallback: Double): Double =
        when (value) {
            is Number -> value.toDouble()
            is String -> Molang.evalDouble(value, ctx, particleVariables) ?: value.toDoubleOrNull() ?: fallback
            else -> fallback
        }

    private fun sampleGradientArgb(stops: List<ColorStop>, t: Double): Int {
        if (stops.isEmpty()) {
            return 0xFFFFFFFF.toInt()
        }
        if (t <= stops.first().t) {
            return stops.first().argb
        }
        if (t >= stops.last().t) {
            return stops.last().argb
        }

        var left = stops.first()
        for (index in 1 until stops.size) {
            val right = stops[index]
            if (t <= right.t) {
                val span = (right.t - left.t).coerceAtLeast(1.0E-9)
                return lerpArgb(left.argb, right.argb, ((t - left.t) / span).coerceIn(0.0, 1.0))
            }
            left = right
        }
        return stops.last().argb
    }

    private fun lerpArgb(a: Int, b: Int, t: Double): Int {
        val aA = (a ushr 24) and 0xFF
        val aR = (a ushr 16) and 0xFF
        val aG = (a ushr 8) and 0xFF
        val aB = a and 0xFF

        val bA = (b ushr 24) and 0xFF
        val bR = (b ushr 16) and 0xFF
        val bG = (b ushr 8) and 0xFF
        val bB = b and 0xFF

        val outA = (aA + (bA - aA) * t).toInt().coerceIn(0, 255)
        val outR = (aR + (bR - aR) * t).toInt().coerceIn(0, 255)
        val outG = (aG + (bG - aG) * t).toInt().coerceIn(0, 255)
        val outB = (aB + (bB - aB) * t).toInt().coerceIn(0, 255)
        return (outA shl 24) or (outR shl 16) or (outG shl 8) or outB
    }

    private fun applyArgb(argb: Int) {
        val alpha = ((argb ushr 24) and 0xFF) / 255.0f
        val red = ((argb ushr 16) and 0xFF) / 255.0f
        val green = ((argb ushr 8) and 0xFF) / 255.0f
        val blue = (argb and 0xFF) / 255.0f
        setColor(red, green, blue)
        setAlpha(alpha)
    }

    private data class UvRect(val u0: Float, val u1: Float, val v0: Float, val v1: Float)

    private fun computeUv(): UvRect {
        val sprite = this.sprite
        val textureWidth = uv?.textureWidth?.takeIf { it > 0 } ?: sprite.contents().width()
        val textureHeight = uv?.textureHeight?.takeIf { it > 0 } ?: sprite.contents().height()

        val baseU: Int
        val baseV: Int
        val sizeU: Int
        val sizeV: Int

        if (flipbook != null) {
            val base = flipbook.baseUV ?: intArrayOf(0, 0)
            val step = flipbook.stepUV ?: intArrayOf(0, 0)
            val size = flipbook.sizeUV ?: intArrayOf(textureWidth, textureHeight)
            val stepU = step.getOrElse(0) { 0 }
            val stepV = step.getOrElse(1) { 0 }

            val maxFrameSafe = computeSafeMaxFrame(
                textureWidth = textureWidth,
                textureHeight = textureHeight,
                baseU = base.getOrElse(0) { 0 },
                baseV = base.getOrElse(1) { 0 },
                sizeU = size.getOrElse(0) { textureWidth },
                sizeV = size.getOrElse(1) { textureHeight },
                stepU = stepU,
                stepV = stepV,
                maxFrameDeclared = flipbook.maxFrame,
            )

            val intendedFrame = if (flipbook.stretchToLifetime == true) {
                val frameCount = ((flipbook.maxFrame ?: maxFrameSafe) + 1).coerceAtLeast(1)
                ((age.toDouble() / max(1, lifetime).toDouble()) * frameCount.toDouble()).toInt()
            } else {
                val fps = flipbook.framesPerSecond ?: 0.0
                ((age.toDouble() / ticksPerSecond) * fps).toInt()
            }

            val frame = if (flipbook.loop == true && maxFrameSafe > 0) {
                val modulo = maxFrameSafe + 1
                ((intendedFrame % modulo) + modulo) % modulo
            } else {
                intendedFrame.coerceIn(0, maxFrameSafe)
            }

            baseU = base.getOrElse(0) { 0 } + stepU * frame
            baseV = base.getOrElse(1) { 0 } + stepV * frame
            sizeU = size.getOrElse(0) { textureWidth }
            sizeV = size.getOrElse(1) { textureHeight }
        } else {
            val base = uv?.uv ?: intArrayOf(0, 0)
            val size = uv?.uvSize ?: intArrayOf(textureWidth, textureHeight)
            baseU = base.getOrElse(0) { 0 }
            baseV = base.getOrElse(1) { 0 }
            sizeU = size.getOrElse(0) { textureWidth }
            sizeV = size.getOrElse(1) { textureHeight }
        }

        val u0 = sprite.getU0() + (sprite.getU1() - sprite.getU0()) * (baseU.toFloat() / textureWidth.toFloat())
        val u1 = sprite.getU0() + (sprite.getU1() - sprite.getU0()) * ((baseU + sizeU).toFloat() / textureWidth.toFloat())
        val v0 = sprite.getV0() + (sprite.getV1() - sprite.getV0()) * (baseV.toFloat() / textureHeight.toFloat())
        val v1 = sprite.getV0() + (sprite.getV1() - sprite.getV0()) * ((baseV + sizeV).toFloat() / textureHeight.toFloat())
        return UvRect(u0, u1, v0, v1)
    }

    private fun computeSafeMaxFrame(
        textureWidth: Int,
        textureHeight: Int,
        baseU: Int,
        baseV: Int,
        sizeU: Int,
        sizeV: Int,
        stepU: Int,
        stepV: Int,
        maxFrameDeclared: Int?,
    ): Int {
        var maxFrame = maxFrameDeclared ?: Int.MAX_VALUE

        if (stepU > 0) {
            maxFrame = min(maxFrame, ((textureWidth - baseU - sizeU).toDouble() / stepU.toDouble()).toInt())
        }
        if (stepV > 0) {
            maxFrame = min(maxFrame, ((textureHeight - baseV - sizeV).toDouble() / stepV.toDouble()).toInt())
        }

        if (maxFrame == Int.MAX_VALUE) {
            maxFrame = 0
        }
        return maxFrame.coerceAtLeast(0)
    }

    private fun createContext(): MolangContext =
        createRawContext().copyWithVariables(
            buildMap {
                putAll(emitterVariables)
                putAll(curveVariables)
                putAll(particleVariables)
                put("emitter_x", emitterOrigin.x)
                put("emitter_y", emitterOrigin.y)
                put("emitter_z", emitterOrigin.z)
            },
        )

    private fun createRawContext(): MolangContext =
        MolangContext(
            emitterAge = emitterAgeAtSpawn + (age.toDouble() / ticksPerSecond),
            particleAge = age.toDouble() / ticksPerSecond,
            particleLifetime = lifetime.toDouble() / ticksPerSecond,
            particleRandom1 = particleRandom1,
            particleRandom2 = particleRandom2,
            particleRandom3 = particleRandom3,
            particleRandom4 = particleRandom4,
            emitterRandom1 = emitterRandom1,
            emitterRandom2 = emitterRandom2,
            emitterRandom3 = emitterRandom3,
            emitterRandom4 = emitterRandom4,
            variables = buildMap {
                putAll(emitterVariables)
                putAll(curveVariables)
                putAll(particleVariables)
                put("emitter_x", emitterOrigin.x)
                put("emitter_y", emitterOrigin.y)
                put("emitter_z", emitterOrigin.z)
            },
        )
}
