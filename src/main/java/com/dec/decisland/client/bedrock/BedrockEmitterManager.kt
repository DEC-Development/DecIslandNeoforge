package com.dec.decisland.client.bedrock

import com.dec.decisland.particles.ModParticles
import com.dec.decisland.particles.bedrock.BedrockEmitterRate
import com.dec.decisland.particles.bedrock.BedrockEmitterShape
import com.dec.decisland.particles.bedrock.BedrockCurves
import com.dec.decisland.particles.bedrock.BedrockParticleEffectDefinition
import com.dec.decisland.particles.bedrock.Molang
import com.dec.decisland.particles.bedrock.MolangContext
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.data.AtlasIds
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.cbrt
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
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
        val spriteIds = ModParticles.resolveBedrockSpriteIds(id)
        if (spriteIds.isEmpty()) return false
        active += EmitterInstance(definition, spriteIds, position, durationTicks)
        return true
    }

    private class EmitterInstance(
        private val definition: BedrockParticleEffectDefinition,
        private val spriteIds: List<net.minecraft.resources.Identifier>,
        private val origin: Vec3,
        private val maxTicks: Int,
    ) {
        private val emitterVariables = LinkedHashMap<String, Double>()
        private val emitterCurveVariables = LinkedHashMap<String, Double>()
        private val emitterRandom1 = Math.random()
        private val emitterRandom2 = Math.random()
        private val emitterRandom3 = Math.random()
        private val emitterRandom4 = Math.random()

        var done = false
            private set

        private var ageTicks = 0
        private var accumulator = 0.0
        private var instantDone = false
        private var manualQueue = 0
        private var loopPhaseTicks = 0
        private var inSleep = false

        init {
            definition.emitterInitialization?.creationExpression?.let { expr ->
                Molang.evalDouble(expr, createContext(), emitterVariables)
            }
            refreshCurveVariables()
        }

        fun tick(level: ClientLevel) {
            if (done) return
            if (ageTicks++ >= maxTicks) {
                done = true
                return
            }

            refreshCurveVariables()

            if (!isActive()) return
            if (isExpiredByExpression()) {
                done = true
                return
            }
            if (!isWithinLifetimeWindow()) return

            val toEmit = computeEmitCount()
            if (toEmit <= 0) return

            repeat(toEmit) {
                val spawn = computeSpawn()
                val velocity = computeVelocity(spawn.direction)
                val minecraft = Minecraft.getInstance()
                val engine = minecraft.particleEngine
                val atlas = minecraft.atlasManager.getAtlasOrThrow(AtlasIds.PARTICLES)
                val spriteId = spriteIds[(Math.random() * spriteIds.size).toInt().coerceIn(0, spriteIds.lastIndex)]
                val sprite = atlas.getSprite(spriteId)
                val particle = BedrockBillboardParticle(
                    definition,
                    level,
                    spawn.pos.x,
                    spawn.pos.y,
                    spawn.pos.z,
                    velocity.x,
                    velocity.y,
                    velocity.z,
                    sprite,
                    emitterVariables,
                    ageTicks.toDouble() / 20.0,
                    emitterRandom1,
                    emitterRandom2,
                    emitterRandom3,
                    emitterRandom4,
                )
                particle.setEmitterOrigin(origin)
                engine.add(particle)
            }
        }

        private fun isActive(): Boolean {
            val expr = definition.emitterLifetimeExpression?.activationExpression ?: return true
            return (Molang.evalDouble(expr, createContext(), emitterVariables) ?: 1.0) != 0.0
        }

        private fun isExpiredByExpression(): Boolean {
            val expr = definition.emitterLifetimeExpression?.expirationExpression ?: return false
            return (Molang.evalDouble(expr, createContext(), emitterVariables) ?: 0.0) != 0.0
        }

        private fun isWithinLifetimeWindow(): Boolean {
            definition.emitterLifetimeOnce?.let { once ->
                val seconds = Molang.evalDouble(once.activeTime, createContext(), emitterVariables) ?: once.activeTime?.toDoubleOrNull() ?: 0.0
                val activeTicks = max(0, (seconds * 20.0).toInt())
                return ageTicks <= activeTicks
            }

            val looping = definition.emitterLifetimeLooping ?: return true
            val activeSeconds = Molang.evalDouble(looping.activeTime, createContext(), emitterVariables) ?: looping.activeTime?.toDoubleOrNull() ?: 0.0
            val sleepSeconds = Molang.evalDouble(looping.sleepTime, createContext(), emitterVariables) ?: looping.sleepTime?.toDoubleOrNull() ?: 0.0
            val activeTicks = max(1, (activeSeconds * 20.0).toInt())
            val sleepTicks = max(0, (sleepSeconds * 20.0).toInt())

            loopPhaseTicks++
            if (!inSleep && loopPhaseTicks >= activeTicks) {
                inSleep = true
                loopPhaseTicks = 0
                instantDone = false
            } else if (inSleep && loopPhaseTicks >= sleepTicks) {
                inSleep = false
                loopPhaseTicks = 0
                instantDone = false
            }
            return !inSleep
        }

        private fun computeEmitCount(): Int {
            val rate = definition.emitterRate
            val raw = when (rate) {
                is BedrockEmitterRate.Instant -> {
                    if (instantDone) return 0
                    instantDone = true
                    (Molang.evalDouble(rate.numParticles, createContext(), emitterVariables) ?: 1.0).toInt().coerceAtLeast(0)
                }
                is BedrockEmitterRate.Steady -> {
                    val perSecond = Molang.evalDouble(rate.spawnRate, createContext(), emitterVariables) ?: 0.0
                    accumulator += perSecond / 20.0
                    val count = accumulator.toInt()
                    accumulator -= count.toDouble()
                    count
                }
                is BedrockEmitterRate.Manual -> {
                    val count = manualQueue
                    manualQueue = 0
                    count
                }
                null -> 0
            }

            val maxParticles = when (rate) {
                is BedrockEmitterRate.Steady -> Molang.evalDouble(rate.maxParticles, createContext(), emitterVariables) ?: rate.maxParticles?.toDoubleOrNull()
                is BedrockEmitterRate.Manual -> Molang.evalDouble(rate.maxParticles, createContext(), emitterVariables) ?: rate.maxParticles?.toDoubleOrNull()
                else -> null
            }?.toInt()

            return if (maxParticles != null) min(raw, max(0, maxParticles)) else raw
        }

        fun emit(count: Int) {
            manualQueue += max(0, count)
        }

        private data class Spawn(val pos: Vec3, val direction: Vec3)

        private fun computeSpawn(): Spawn {
            return when (val shape = definition.emitterShape) {
                is BedrockEmitterShape.Sphere -> {
                    val radius = Molang.evalDouble(shape.radius, createContext(), emitterVariables) ?: 0.0
                    val offset = toVec(shape.offset)
                    val dir = randomUnit()
                    val distance = if (shape.surfaceOnly == true) radius else cbrt(Math.random()) * radius
                    val delta = dir.scale(distance)
                    Spawn(origin.add(offset).add(delta), resolveDirection(shape.direction, dir, delta))
                }
                is BedrockEmitterShape.Box -> {
                    val half = shape.halfDimensions ?: doubleArrayOf(0.0, 0.0, 0.0)
                    val offset = toVec(shape.offset)
                    val delta = Vec3(
                        (Math.random() * 2.0 - 1.0) * half.getOrElse(0) { 0.0 },
                        (Math.random() * 2.0 - 1.0) * half.getOrElse(1) { 0.0 },
                        (Math.random() * 2.0 - 1.0) * half.getOrElse(2) { 0.0 },
                    )
                    Spawn(origin.add(offset).add(delta), resolveDirection(shape.direction, randomUnit(), delta))
                }
                is BedrockEmitterShape.Disc -> {
                    val radius = Molang.evalDouble(shape.radius, createContext(), emitterVariables) ?: 0.0
                    val offset = toVec(shape.offset)
                    val normalRaw = shape.normal ?: doubleArrayOf(0.0, 1.0, 0.0)
                    val normal = normalizeSafe(
                        Vec3(
                            normalRaw.getOrElse(0) { 0.0 },
                            normalRaw.getOrElse(1) { 1.0 },
                            normalRaw.getOrElse(2) { 0.0 },
                        ),
                    )
                    val u = normalizeSafe(Vec3(normal.y, -normal.x, 0.0), Vec3(1.0, 0.0, 0.0))
                    val v = normalizeSafe(normal.cross(u), Vec3(0.0, 0.0, 1.0))
                    val theta = Math.random() * Math.PI * 2.0
                    val distance = if (shape.surfaceOnly == true) radius else sqrt(Math.random()) * radius
                    val delta = u.scale(cos(theta) * distance).add(v.scale(sin(theta) * distance))
                    Spawn(origin.add(offset).add(delta), resolveDirection(shape.direction, normal, delta))
                }
                is BedrockEmitterShape.Point -> {
                    val fallback = randomUnit()
                    Spawn(origin.add(toVec(shape.offset)), resolveDirection(shape.direction, fallback, fallback))
                }
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
                is String -> normalizeSafe(direction).scale((Molang.evalDouble(speed, createContext(), emitterVariables) ?: 0.0) / 20.0)
                else -> Vec3.ZERO
            }
        }

        private fun resolveDirection(field: Any?, fallback: Vec3, delta: Vec3): Vec3 {
            return when (field) {
                is String -> {
                    when (field.lowercase()) {
                        "outwards" -> normalizeSafe(delta, fallback)
                        "inwards" -> normalizeSafe(delta.scale(-1.0), fallback)
                        else -> normalizeSafe(fallback)
                    }
                }
                is DoubleArray -> normalizeSafe(
                    Vec3(
                        field.getOrElse(0) { 0.0 },
                        field.getOrElse(1) { 0.0 },
                        field.getOrElse(2) { 0.0 },
                    ),
                    fallback,
                )
                is Array<*> -> {
                    val ctx = createContext()
                    normalizeSafe(
                        Vec3(
                            evalFieldPart(field.getOrNull(0), ctx),
                            evalFieldPart(field.getOrNull(1), ctx),
                            evalFieldPart(field.getOrNull(2), ctx),
                        ),
                        fallback,
                    )
                }
                else -> normalizeSafe(fallback)
            }
        }

        private fun evalFieldPart(value: Any?, ctx: MolangContext): Double =
            when (value) {
                is Number -> value.toDouble()
                is String -> Molang.evalDouble(value, ctx, emitterVariables) ?: 0.0
                else -> 0.0
            }

        private fun randomUnit(): Vec3 {
            val x = Math.random() * 2.0 - 1.0
            val y = Math.random() * 2.0 - 1.0
            val z = Math.random() * 2.0 - 1.0
            return normalizeSafe(Vec3(x, y, z))
        }

        private fun normalizeSafe(vec: Vec3): Vec3 = normalizeSafe(vec, Vec3(0.0, 1.0, 0.0))

        private fun normalizeSafe(vec: Vec3, fallback: Vec3): Vec3 {
            val length = sqrt((vec.x * vec.x) + (vec.y * vec.y) + (vec.z * vec.z))
            return if (length <= 1.0E-9) fallback else Vec3(vec.x / length, vec.y / length, vec.z / length)
        }

        private fun toVec(values: DoubleArray?): Vec3 =
            if (values == null) Vec3.ZERO else Vec3(values.getOrElse(0) { 0.0 }, values.getOrElse(1) { 0.0 }, values.getOrElse(2) { 0.0 })

        private fun refreshCurveVariables() {
            emitterCurveVariables.clear()
            emitterCurveVariables.putAll(
                BedrockCurves.evaluateAll(
                    definition.curves,
                    createRawContext(),
                    emitterVariables,
                ),
            )
        }

        private fun createRawContext(): MolangContext =
            MolangContext(
                emitterAge = ageTicks.toDouble() / 20.0,
                emitterRandom1 = emitterRandom1,
                emitterRandom2 = emitterRandom2,
                emitterRandom3 = emitterRandom3,
                emitterRandom4 = emitterRandom4,
                variables = buildMap {
                    putAll(emitterVariables)
                    putAll(emitterCurveVariables)
                },
            )

        private fun createContext(): MolangContext =
            createRawContext()
    }
}
