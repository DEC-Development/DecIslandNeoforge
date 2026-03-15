package com.dec.decisland.particles

import com.dec.decisland.DecIsland
import com.dec.decisland.particles.bedrock.BedrockEmitterLifetimeExpression
import com.dec.decisland.particles.bedrock.BedrockEmitterLifetimeLooping
import com.dec.decisland.particles.bedrock.BedrockEmitterLifetimeOnce
import com.dec.decisland.particles.bedrock.BedrockEmitterRate
import com.dec.decisland.particles.bedrock.BedrockEmitterShape
import com.dec.decisland.particles.bedrock.BedrockParticleAppearanceBillboard
import com.dec.decisland.particles.bedrock.BedrockParticleAppearanceLighting
import com.dec.decisland.particles.bedrock.BedrockParticleEffectDefinition
import com.dec.decisland.particles.bedrock.BedrockParticleFlipbook
import com.dec.decisland.particles.bedrock.BedrockParticleInitialSpeed
import com.dec.decisland.particles.bedrock.BedrockParticleInitialSpin
import com.dec.decisland.particles.bedrock.BedrockParticleLifetimeExpression
import com.dec.decisland.particles.bedrock.BedrockParticleMotionCollision
import com.dec.decisland.particles.bedrock.BedrockParticleMotionDynamic
import com.dec.decisland.particles.bedrock.BedrockParticleUv
import com.dec.decisland.particles.custom.AbsoluteZeroSmokeSeedParticle
import com.dec.decisland.particles.custom.AbsoluteZeroSmokeSingleParticle
import com.dec.decisland.particles.custom.BlizzardWakeParticle
import com.dec.decisland.particles.custom.KatanaParticle
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.io.InputStreamReader
import java.util.function.Supplier

object ModParticles {
    @JvmField
    val PARTICLE_TYPES: DeferredRegister<ParticleType<*>> =
        DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, DecIsland.MOD_ID)

    private const val BEDROCK_MANIFEST = "assets/decisland/particles/bedrock_manifest.txt"

    private val particleConfigs = mutableListOf<ParticleConfig>()
    private val bedrockDefinitionById = LinkedHashMap<Identifier, BedrockParticleEffectDefinition>()
    private val bedrockSpriteById = LinkedHashMap<Identifier, Identifier>()

    @Volatile
    private var bedrockLoaded = false

    @JvmField
    val BLIZZARD_WAKE_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("blizzard_wake_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, BlizzardWakeParticle::Provider) }
            .build(),
    )

    @JvmField
    val ABSOLUTE_ZERO_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("absolute_zero_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, KatanaParticle::Provider) }
            .build(),
    )

    @JvmField
    val BAMBOO_KATANA_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("bamboo_katana_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, KatanaParticle::Provider) }
            .build(),
    )

    @JvmField
    val HARD_BAMBOO_KATANA_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("hard_bamboo_katana_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, KatanaParticle::Provider) }
            .build(),
    )

    @JvmField
    val ABSOLUTE_ZERO_SMOKE_SINGLE_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("absolute_zero_smoke_single_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, AbsoluteZeroSmokeSingleParticle::Provider) }
            .build(),
    )

    @JvmField
    val ABSOLUTE_ZERO_SMOKE_SMALL_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("absolute_zero_smoke_small_particle")
            .factoryProvider { event, particleType ->
                event.registerSpecial(particleType, AbsoluteZeroSmokeSeedParticle.Provider(10, 3.0, 0.1))
            }
            .build(),
    )

    @JvmField
    val ABSOLUTE_ZERO_SMOKE_BIG_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("absolute_zero_smoke_big_particle")
            .factoryProvider { event, particleType ->
                event.registerSpecial(particleType, AbsoluteZeroSmokeSeedParticle.Provider(30, 5.0, 0.1))
            }
            .build(),
    )

    @JvmStatic
    fun registerParticle(config: ParticleConfig): Supplier<SimpleParticleType> {
        particleConfigs.add(config)
        val holder: DeferredHolder<ParticleType<*>, ParticleType<*>> = PARTICLE_TYPES.register(config.name, config.sup)
        config.registeredType = Supplier { holder.get() as SimpleParticleType }
        return config.registeredType!!
    }

    @JvmStatic
    fun register(eventBus: IEventBus) {
        PARTICLE_TYPES.register(eventBus)
    }

    @JvmStatic
    fun getParticleConfigs(): List<ParticleConfig> = particleConfigs

    @JvmStatic
    fun resolveBedrockDefinition(id: Identifier): BedrockParticleEffectDefinition? {
        ensureBedrockLoaded()
        return bedrockDefinitionById[normalizeBedrockId(id)]
    }

    @JvmStatic
    fun resolveBedrockSpriteId(id: Identifier): Identifier? {
        ensureBedrockLoaded()
        return bedrockSpriteById[normalizeBedrockId(id)]
    }

    private fun ensureBedrockLoaded() {
        if (bedrockLoaded) return
        synchronized(this) {
            if (bedrockLoaded) return
            loadBedrockDefinitions()
            bedrockLoaded = true
        }
    }

    private fun loadBedrockDefinitions() {
        val loader = Thread.currentThread().contextClassLoader ?: javaClass.classLoader
        val manifestStream = loader.getResourceAsStream(BEDROCK_MANIFEST) ?: return
        manifestStream.bufferedReader().useLines { lines ->
            lines.map(String::trim)
                .filter { it.isNotEmpty() && !it.startsWith("#") }
                .forEach { relativePath ->
                    val resourcePath = "assets/decisland/particles/$relativePath"
                    val input = loader.getResourceAsStream(resourcePath) ?: return@forEach
                    input.use { stream ->
                        parseBedrockResource(stream)?.let { parsed ->
                            bedrockDefinitionById[parsed.first] = parsed.second
                            parsed.third?.let { bedrockSpriteById[parsed.first] = it }
                        }
                    }
                }
        }
    }

    private fun parseBedrockResource(stream: java.io.InputStream): Triple<Identifier, BedrockParticleEffectDefinition, Identifier?>? {
        val root = InputStreamReader(stream).use { JsonParser.parseReader(it) }
        val effect = root.asJsonObjectOrNull()?.getAsJsonObjectOrNull("particle_effect") ?: return null
        val description = effect.getAsJsonObjectOrNull("description") ?: return null
        val rawIdentifier = description.getAsJsonPrimitiveOrNull("identifier")?.asString ?: return null
        val id = normalizeBedrockId(Identifier.tryParse(rawIdentifier) ?: return null)
        val texturePath = description.getAsJsonObjectOrNull("basic_render_parameters")
            ?.getAsJsonPrimitiveOrNull("texture")
            ?.asString
        val spriteId = texturePath?.let(::texturePathToSpriteId)
        val components = effect.getAsJsonObjectOrNull("components") ?: JsonObject()

        val definition = BedrockParticleEffectDefinition(
            emitterLifetimeExpression = components.getAsJsonObjectOrNull("minecraft:emitter_lifetime_expression")?.let { obj ->
                BedrockEmitterLifetimeExpression(
                    activationExpression = obj.getAsJsonPrimitiveOrNull("activation_expression")?.asString,
                    expirationExpression = obj.getAsJsonPrimitiveOrNull("expiration_expression")?.asString,
                )
            },
            emitterLifetimeOnce = components.getAsJsonObjectOrNull("minecraft:emitter_lifetime_once")?.let { obj ->
                BedrockEmitterLifetimeOnce(obj.getAsJsonPrimitiveOrNull("active_time")?.asString)
            },
            emitterLifetimeLooping = components.getAsJsonObjectOrNull("minecraft:emitter_lifetime_looping")?.let { obj ->
                BedrockEmitterLifetimeLooping(
                    activeTime = obj.getAsJsonPrimitiveOrNull("active_time")?.asString,
                    sleepTime = obj.getAsJsonPrimitiveOrNull("sleep_time")?.asString,
                )
            },
            emitterRate = components.getAsJsonObjectOrNull("minecraft:emitter_rate_instant")?.let { obj ->
                BedrockEmitterRate.Instant(obj.getAsJsonPrimitiveOrNull("num_particles")?.asString)
            } ?: components.getAsJsonObjectOrNull("minecraft:emitter_rate_steady")?.let { obj ->
                BedrockEmitterRate.Steady(
                    spawnRate = obj.getAsJsonPrimitiveOrNull("spawn_rate")?.asString,
                    maxParticles = obj.getAsJsonPrimitiveOrNull("max_particles")?.asString,
                )
            },
            emitterShape = components.getAsJsonObjectOrNull("minecraft:emitter_shape_sphere")?.let { obj ->
                BedrockEmitterShape.Sphere(
                    radius = obj.getAsJsonPrimitiveOrNull("radius")?.asString,
                    offset = obj.getAsJsonArrayOrNull("offset")?.toDoubleArray3(),
                    surfaceOnly = obj.getAsJsonPrimitiveOrNull("surface_only")?.asBoolean,
                    direction = obj.get("direction")?.let(::parseScalarOrVecOrString),
                )
            } ?: components.getAsJsonObjectOrNull("minecraft:emitter_shape_point")?.let { obj ->
                BedrockEmitterShape.Point(
                    offset = obj.getAsJsonArrayOrNull("offset")?.toDoubleArray3(),
                    direction = obj.get("direction")?.let(::parseScalarOrVecOrString),
                )
            },
            particleLifetimeExpression = components.getAsJsonObjectOrNull("minecraft:particle_lifetime_expression")?.let { obj ->
                BedrockParticleLifetimeExpression(obj.getAsJsonPrimitiveOrNull("max_lifetime")?.asString)
            },
            particleInitialSpeed = components.get("minecraft:particle_initial_speed")?.let { BedrockParticleInitialSpeed(parseScalarOrVecOrString(it)) },
            particleInitialSpin = components.getAsJsonObjectOrNull("minecraft:particle_initial_spin")?.let { obj ->
                BedrockParticleInitialSpin(
                    rotation = obj.getAsJsonPrimitiveOrNull("rotation")?.asString,
                    rotationRate = obj.getAsJsonPrimitiveOrNull("rotation_rate")?.asString,
                )
            },
            particleMotionDynamic = components.getAsJsonObjectOrNull("minecraft:particle_motion_dynamic")?.let { obj ->
                BedrockParticleMotionDynamic(
                    linearAcceleration = obj.getAsJsonArrayOrNull("linear_acceleration")?.toDoubleArray3(),
                    linearDragCoefficient = obj.getAsJsonPrimitiveOrNull("linear_drag_coefficient")?.asString,
                )
            },
            particleMotionCollision = components.getAsJsonObjectOrNull("minecraft:particle_motion_collision")?.let { obj ->
                BedrockParticleMotionCollision(obj.getAsJsonPrimitiveOrNull("collision_radius")?.asString)
            },
            particleAppearanceBillboard = components.getAsJsonObjectOrNull("minecraft:particle_appearance_billboard")?.let { obj ->
                val uvObject = obj.getAsJsonObjectOrNull("uv")
                val flipbookObject = uvObject?.getAsJsonObjectOrNull("flipbook")
                BedrockParticleAppearanceBillboard(
                    size = obj.get("size")?.let(::parseScalarOrVecOrString),
                    facingCameraMode = obj.getAsJsonPrimitiveOrNull("facing_camera_mode")?.asString,
                    uv = uvObject?.let { uv ->
                        BedrockParticleUv(
                            textureWidth = uv.getAsJsonPrimitiveOrNull("texture_width")?.asInt,
                            textureHeight = uv.getAsJsonPrimitiveOrNull("texture_height")?.asInt,
                            uv = uv.getAsJsonArrayOrNull("uv")?.toIntArray2(),
                            uvSize = uv.getAsJsonArrayOrNull("uv_size")?.toIntArray2(),
                            flipbook = flipbookObject?.let { flip ->
                                BedrockParticleFlipbook(
                                    baseUV = flip.getAsJsonArrayOrNull("base_UV")?.toIntArray2(),
                                    sizeUV = flip.getAsJsonArrayOrNull("size_UV")?.toIntArray2(),
                                    stepUV = flip.getAsJsonArrayOrNull("step_UV")?.toIntArray2(),
                                    framesPerSecond = flip.getAsJsonPrimitiveOrNull("frames_per_second")?.asDouble,
                                    maxFrame = flip.getAsJsonPrimitiveOrNull("max_frame")?.asInt,
                                    stretchToLifetime = flip.getAsJsonPrimitiveOrNull("stretch_to_lifetime")?.asBoolean,
                                    loop = flip.getAsJsonPrimitiveOrNull("loop")?.asBoolean,
                                )
                            },
                        )
                    },
                )
            },
            particleAppearanceLighting = if (components.getAsJsonObjectOrNull("minecraft:particle_appearance_lighting") != null) {
                BedrockParticleAppearanceLighting(true)
            } else {
                null
            },
        )

        return Triple(id, definition, spriteId)
    }

    private fun normalizeBedrockId(id: Identifier): Identifier =
        when (id.namespace) {
            "dec", "the-poetry-of-winter" -> Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, id.path)
            else -> id
        }

    private fun texturePathToSpriteId(raw: String): Identifier? {
        val normalized = raw.replace('\\', '/').removeSuffix(".png")
        val path = if (normalized.startsWith("textures/")) normalized else "textures/$normalized"
        return when {
            path.startsWith("textures/particle/") ->
                Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, path.removePrefix("textures/particle/"))
            path.startsWith("textures/wb_par/") ->
                Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "wb_par/${path.removePrefix("textures/wb_par/")}")
            path.startsWith("textures/EPIC/") ->
                Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "epic/${path.removePrefix("textures/EPIC/")}")
            path.startsWith("textures/epic/") ->
                Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "epic/${path.removePrefix("textures/epic/")}")
            else -> null
        }
    }

    private fun parseScalarOrVecOrString(element: com.google.gson.JsonElement): Any? =
        when {
            element.isJsonPrimitive && element.asJsonPrimitive.isNumber -> element.asDouble
            element.isJsonPrimitive && element.asJsonPrimitive.isString -> element.asString
            element.isJsonArray -> {
                val array = element.asJsonArray
                if ((0 until array.size()).all { array[it].isJsonPrimitive && array[it].asJsonPrimitive.isNumber }) {
                    DoubleArray(array.size()) { index -> array[index].asDouble }
                } else if ((0 until array.size()).all { array[it].isJsonPrimitive && array[it].asJsonPrimitive.isString }) {
                    Array(array.size()) { index -> array[index].asString }
                } else {
                    null
                }
            }
            else -> null
        }

    private fun JsonArray.toDoubleArray3(): DoubleArray = doubleArrayOf(
        getOrNullNumber(0),
        getOrNullNumber(1),
        getOrNullNumber(2),
    )

    private fun JsonArray.toIntArray2(): IntArray = intArrayOf(
        getOrNullInt(0),
        getOrNullInt(1),
    )

    private fun JsonArray.getOrNullNumber(index: Int): Double =
        if (index in 0 until size() && this[index].isJsonPrimitive && this[index].asJsonPrimitive.isNumber) this[index].asDouble else 0.0

    private fun JsonArray.getOrNullInt(index: Int): Int =
        if (index in 0 until size() && this[index].isJsonPrimitive && this[index].asJsonPrimitive.isNumber) this[index].asInt else 0

    private fun com.google.gson.JsonElement.asJsonObjectOrNull(): JsonObject? = if (isJsonObject) asJsonObject else null

    private fun JsonObject.getAsJsonObjectOrNull(name: String): JsonObject? = get(name)?.takeIf { it.isJsonObject }?.asJsonObject

    private fun JsonObject.getAsJsonArrayOrNull(name: String): JsonArray? = get(name)?.takeIf { it.isJsonArray }?.asJsonArray

    private fun JsonObject.getAsJsonPrimitiveOrNull(name: String): com.google.gson.JsonPrimitive? =
        get(name)?.takeIf { it.isJsonPrimitive }?.asJsonPrimitive
}
