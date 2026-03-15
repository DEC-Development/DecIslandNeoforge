package com.dec.decisland.client.bedrock

import com.dec.decisland.particles.ModParticles
import com.dec.decisland.particles.bedrock.BedrockEmitterRate
import com.dec.decisland.particles.bedrock.BedrockEmitterShape
import com.dec.decisland.particles.bedrock.BedrockParticleEffectDefinition
import com.dec.decisland.particles.bedrock.Molang
import com.dec.decisland.particles.bedrock.MolangContext
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.data.AtlasIds
import net.minecraft.world.phys.Vec3
import kotlin.math.cbrt
import kotlin.math.max
import kotlin.math.sqrt

object BedrockEmitterManager {
    private val active = ArrayList<EmitterInstance>()

    fun tick(level: ClientLevel) {
        val iterator = active.iterator()
        while (iterator.hasNext()) {
            val emitter = iterator.next()
            emitter.tick(level)
            if (emitter.done) {
                iterator.remove()
            }
        }
    }

    fun spawnAt(id: net.minecraft.resources.Identifier, position: Vec3, durationTicks: Int = 100): Boolean {
        val definition = ModParticles.resolveBedrockDefinition(id) ?: return false
        val spriteId = ModParticles.resolveBedrockSpriteId(id) ?: return false
        active += EmitterInstance(definition, spriteId, position, durationTicks)
        return true
    }

    private class EmitterInstance(
        private val definition: BedrockParticleEffectDefinition,
        private val spriteId: net.minecraft.resources.Identifier,
        private val origin: Vec3,
        private val maxTicks: Int,
    ) {
        var done = false
            private set

        private var ageTicks = 0
        private var accumulator = 0.0
        private var instantDone = false

        fun tick(level: ClientLevel) {
            if (done) return
            if (ageTicks++ >= maxTicks) {
                done = true
                return
            }

            if (!isActive()) return
            if (isExpired()) {
                done = true
                return
            }

            val toEmit = computeEmitCount()
            if (toEmit <= 0) return

            repeat(toEmit) {
                val spawn = computeSpawn()
                val velocity = computeVelocity(spawn.direction)
                val minecraft = Minecraft.getInstance()
                val engine = minecraft.particleEngine
                val atlas = minecraft.atlasManager.getAtlasOrThrow(AtlasIds.PARTICLES)
                val sprite = atlas.getSprite(spriteId)
                engine.add(BedrockBillboardParticle(definition, level, spawn.pos.x, spawn.pos.y, spawn.pos.z, velocity.x, velocity.y, velocity.z, sprite))
            }
        }

        private fun isActive(): Boolean {
            val expr = definition.emitterLifetimeExpression?.activationExpression ?: return true
            return (Molang.evalDouble(expr, MolangContext()) ?: 1.0) != 0.0
        }

        private fun isExpired(): Boolean {
            val expr = definition.emitterLifetimeExpression?.expirationExpression ?: return false
            return (Molang.evalDouble(expr, MolangContext()) ?: 0.0) != 0.0
        }

        private fun computeEmitCount(): Int {
            return when (val rate = definition.emitterRate) {
                is BedrockEmitterRate.Instant -> {
                    if (instantDone) return 0
                    instantDone = true
                    (Molang.evalDouble(rate.numParticles, MolangContext()) ?: 1.0).toInt().coerceAtLeast(0)
                }
                is BedrockEmitterRate.Steady -> {
                    val perSecond = Molang.evalDouble(rate.spawnRate, MolangContext()) ?: 0.0
                    accumulator += perSecond / 20.0
                    val count = accumulator.toInt()
                    accumulator -= count.toDouble()
                    count
                }
                null -> 0
            }
        }

        private data class Spawn(val pos: Vec3, val direction: Vec3)

        private fun computeSpawn(): Spawn {
            return when (val shape = definition.emitterShape) {
                is BedrockEmitterShape.Sphere -> {
                    val radius = Molang.evalDouble(shape.radius, MolangContext()) ?: 0.0
                    val offset = toVec(shape.offset)
                    val dir = randomUnit()
                    val distance = if (shape.surfaceOnly == true) radius else cbrt(Math.random()) * radius
                    val delta = dir.scale(distance)
                    Spawn(origin.add(offset).add(delta), resolveDirection(shape.direction, dir))
                }
                is BedrockEmitterShape.Point -> Spawn(origin.add(toVec(shape.offset)), resolveDirection(shape.direction, Vec3(0.0, 1.0, 0.0)))
                null -> Spawn(origin, Vec3(0.0, 1.0, 0.0))
            }
        }

        private fun computeVelocity(direction: Vec3): Vec3 {
            val speed = definition.particleInitialSpeed?.speed ?: return Vec3.ZERO
            return when (speed) {
                is Number -> normalizeSafe(direction).scale(speed.toDouble() / 20.0)
                is DoubleArray -> Vec3(
                    speed.getOrElse(0) { 0.0 } / 20.0,
                    speed.getOrElse(1) { 0.0 } / 20.0,
                    speed.getOrElse(2) { 0.0 } / 20.0,
                )
                is String -> normalizeSafe(direction).scale((Molang.evalDouble(speed, MolangContext()) ?: 0.0) / 20.0)
                else -> Vec3.ZERO
            }
        }

        private fun resolveDirection(field: Any?, fallback: Vec3): Vec3 {
            return when (field) {
                is String -> {
                    when (field.lowercase()) {
                        "outwards" -> normalizeSafe(fallback)
                        "inwards" -> normalizeSafe(fallback.scale(-1.0))
                        else -> normalizeSafe(fallback)
                    }
                }
                is DoubleArray -> normalizeSafe(Vec3(field.getOrElse(0) { 0.0 }, field.getOrElse(1) { 0.0 }, field.getOrElse(2) { 0.0 }))
                is Array<*> -> {
                    val ctx = MolangContext()
                    normalizeSafe(
                        Vec3(
                            evalFieldPart(field.getOrNull(0), ctx),
                            evalFieldPart(field.getOrNull(1), ctx),
                            evalFieldPart(field.getOrNull(2), ctx),
                        ),
                    )
                }
                else -> normalizeSafe(fallback)
            }
        }

        private fun evalFieldPart(value: Any?, ctx: MolangContext): Double =
            when (value) {
                is Number -> value.toDouble()
                is String -> Molang.evalDouble(value, ctx) ?: 0.0
                else -> 0.0
            }

        private fun randomUnit(): Vec3 {
            val x = Math.random() * 2.0 - 1.0
            val y = Math.random() * 2.0 - 1.0
            val z = Math.random() * 2.0 - 1.0
            return normalizeSafe(Vec3(x, y, z))
        }

        private fun normalizeSafe(vec: Vec3): Vec3 {
            val length = sqrt((vec.x * vec.x) + (vec.y * vec.y) + (vec.z * vec.z))
            return if (length <= 1.0E-9) Vec3(0.0, 1.0, 0.0) else Vec3(vec.x / length, vec.y / length, vec.z / length)
        }

        private fun toVec(values: DoubleArray?): Vec3 =
            if (values == null) Vec3.ZERO else Vec3(values.getOrElse(0) { 0.0 }, values.getOrElse(1) { 0.0 }, values.getOrElse(2) { 0.0 })
    }
}
