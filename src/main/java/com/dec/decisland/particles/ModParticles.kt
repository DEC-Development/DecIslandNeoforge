package com.dec.decisland.particles

import com.dec.decisland.DecIsland
import com.dec.decisland.particles.bedrock.BedrockEmitterLifetimeExpression
import com.dec.decisland.particles.bedrock.BedrockEmitterLifetimeLooping
import com.dec.decisland.particles.bedrock.BedrockEmitterLifetimeOnce
import com.dec.decisland.particles.bedrock.BedrockEmitterInitialization
import com.dec.decisland.particles.bedrock.BedrockEmitterLocalSpace
import com.dec.decisland.particles.bedrock.BedrockEmitterRate
import com.dec.decisland.particles.bedrock.BedrockEmitterShape
import com.dec.decisland.particles.bedrock.BedrockBezierNode
import com.dec.decisland.particles.bedrock.BedrockCurve
import com.dec.decisland.particles.bedrock.BedrockParticleAppearanceBillboard
import com.dec.decisland.particles.bedrock.BedrockParticleAppearanceLighting
import com.dec.decisland.particles.bedrock.BedrockParticleAppearanceTinting
import com.dec.decisland.particles.bedrock.BedrockParticleCollisionEvent
import com.dec.decisland.particles.bedrock.BedrockParticleEffectDefinition
import com.dec.decisland.particles.bedrock.BedrockParticleEventDefinition
import com.dec.decisland.particles.bedrock.BedrockParticleEventEffect
import com.dec.decisland.particles.bedrock.BedrockParticleFlipbook
import com.dec.decisland.particles.bedrock.BedrockParticleInitialSpeed
import com.dec.decisland.particles.bedrock.BedrockParticleInitialSpin
import com.dec.decisland.particles.bedrock.BedrockParticleInitialization
import com.dec.decisland.particles.bedrock.BedrockParticleLifetimeExpression
import com.dec.decisland.particles.bedrock.BedrockParticleMotionCollision
import com.dec.decisland.particles.bedrock.BedrockParticleMotionDynamic
import com.dec.decisland.particles.bedrock.BedrockParticleUv
import com.dec.decisland.particles.bedrock.BedrockTintColor
import com.dec.decisland.particles.bedrock.ColorStop
import com.dec.decisland.particles.custom.AbsoluteZeroSmokeSeedParticle
import com.dec.decisland.particles.custom.AbsoluteZeroSmokeSingleParticle
import com.dec.decisland.particles.custom.BlizzardWakeParticle
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
    private val bedrockSpritesById = LinkedHashMap<Identifier, List<Identifier>>()

    @Volatile
    private var bedrockLoaded = false

    @JvmField
    val BLIZZARD_WAKE_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("blizzard_wake_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, BlizzardWakeParticle::Provider) }
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
        return bedrockSpritesById[normalizeBedrockId(id)]?.firstOrNull()
    }

    @JvmStatic
    fun resolveBedrockSpriteIds(id: Identifier): List<Identifier> {
        ensureBedrockLoaded()
        return bedrockSpritesById[normalizeBedrockId(id)] ?: emptyList()
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
                            parsed.third?.let { bedrockSpritesById[parsed.first] = it }
                        }
                    }
                }
        }
    }

    private fun parseBedrockResource(stream: java.io.InputStream): Triple<Identifier, BedrockParticleEffectDefinition, List<Identifier>?>? {
        val root = InputStreamReader(stream).use { JsonParser.parseReader(it) }
        val effect = root.asJsonObjectOrNull()?.getAsJsonObjectOrNull("particle_effect") ?: return null
        val description = effect.getAsJsonObjectOrNull("description") ?: return null
        val rawIdentifier = description.getAsJsonPrimitiveOrNull("identifier")?.asString ?: return null
        val id = normalizeBedrockId(Identifier.tryParse(rawIdentifier) ?: return null)
        val texturePath = description.getAsJsonObjectOrNull("basic_render_parameters")
            ?.getAsJsonPrimitiveOrNull("texture")
            ?.asString
        val renderMaterial = description.getAsJsonObjectOrNull("basic_render_parameters")
            ?.getAsJsonPrimitiveOrNull("material")
            ?.asString
        val spriteIds = texturePath?.let(::texturePathToSpriteIds)
        val components = effect.getAsJsonObjectOrNull("components") ?: JsonObject()
        val curves = parseCurves(effect.getAsJsonObjectOrNull("curves"))
        val events = effect.getAsJsonObjectOrNull("events")
            ?.entrySet()
            ?.mapNotNull { (name, value) ->
                val obj = value.takeIf { it.isJsonObject }?.asJsonObject ?: return@mapNotNull null
                val particleEffectObj = obj.getAsJsonObjectOrNull("particle_effect")
                name to BedrockParticleEventDefinition(
                    particleEffect = particleEffectObj?.let {
                        BedrockParticleEventEffect(
                            effect = it.getAsJsonPrimitiveOrNull("effect")?.asString,
                            type = it.getAsJsonPrimitiveOrNull("type")?.asString,
                        )
                    },
                )
            }
            ?.toMap()
            ?: emptyMap()

        val definition = BedrockParticleEffectDefinition(
            renderMaterial = renderMaterial,
            emitterInitialization = components.getAsJsonObjectOrNull("minecraft:emitter_initialization")?.let { obj ->
                BedrockEmitterInitialization(
                    creationExpression = obj.getAsJsonPrimitiveOrNull("creation_expression")?.asString,
                )
            },
            emitterLocalSpace = components.getAsJsonObjectOrNull("minecraft:emitter_local_space")?.let { obj ->
                BedrockEmitterLocalSpace(
                    position = obj.getAsJsonPrimitiveOrNull("position")?.asBoolean,
                    rotation = obj.getAsJsonPrimitiveOrNull("rotation")?.asBoolean,
                    velocity = obj.getAsJsonPrimitiveOrNull("velocity")?.asBoolean,
                )
            },
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
            emitterRate = components.getAsJsonObjectOrNull("minecraft:emitter_rate_steady")?.let { obj ->
                BedrockEmitterRate.Steady(
                    spawnRate = obj.getAsJsonPrimitiveOrNull("spawn_rate")?.asString,
                    maxParticles = obj.getAsJsonPrimitiveOrNull("max_particles")?.asString,
                )
            } ?: components.getAsJsonObjectOrNull("minecraft:emitter_rate_instant")?.let { obj ->
                BedrockEmitterRate.Instant(obj.getAsJsonPrimitiveOrNull("num_particles")?.asString)
            } ?: components.getAsJsonObjectOrNull("minecraft:emitter_rate_manual")?.let { obj ->
                BedrockEmitterRate.Manual(
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
            } ?: components.getAsJsonObjectOrNull("minecraft:emitter_shape_box")?.let { obj ->
                BedrockEmitterShape.Box(
                    halfDimensions = obj.getAsJsonArrayOrNull("half_dimensions")?.toDoubleArray3(),
                    offset = obj.getAsJsonArrayOrNull("offset")?.toDoubleArray3(),
                    surfaceOnly = obj.getAsJsonPrimitiveOrNull("surface_only")?.asBoolean,
                    direction = obj.get("direction")?.let(::parseScalarOrVecOrString),
                )
            } ?: components.getAsJsonObjectOrNull("minecraft:emitter_shape_disc")?.let { obj ->
                BedrockEmitterShape.Disc(
                    radius = obj.getAsJsonPrimitiveOrNull("radius")?.asString,
                    normal = obj.getAsJsonArrayOrNull("normal")?.toDoubleArray3(),
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
            particleInitialization = components.getAsJsonObjectOrNull("minecraft:particle_initialization")?.let { obj ->
                BedrockParticleInitialization(
                    creationExpression = obj.getAsJsonPrimitiveOrNull("creation_expression")?.asString,
                    perUpdateExpression = obj.getAsJsonPrimitiveOrNull("per_update_expression")?.asString,
                    perRenderExpression = obj.getAsJsonPrimitiveOrNull("per_render_expression")?.asString,
                )
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
                BedrockParticleMotionCollision(
                    collisionRadius = obj.getAsJsonPrimitiveOrNull("collision_radius")?.asString,
                    expireOnContact = obj.getAsJsonPrimitiveOrNull("expire_on_contact")?.asBoolean,
                    events = obj.getAsJsonArrayOrNull("events")
                        ?.mapNotNull { eventElement ->
                            val eventObj = eventElement.takeIf { it.isJsonObject }?.asJsonObject ?: return@mapNotNull null
                            val eventName = eventObj.getAsJsonPrimitiveOrNull("event")?.asString ?: return@mapNotNull null
                            BedrockParticleCollisionEvent(
                                event = eventName,
                                minSpeed = eventObj.getAsJsonPrimitiveOrNull("min_speed")?.asString,
                            )
                        }
                        ?: emptyList(),
                )
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
            particleAppearanceTinting = components.getAsJsonObjectOrNull("minecraft:particle_appearance_tinting")?.let { obj ->
                BedrockParticleAppearanceTinting(
                    color = obj.get("color")?.let(::parseTintColor),
                    alpha = obj.get("alpha")?.let(::parseScalarOrVecOrString),
                )
            },
            particleAppearanceLighting = if (components.getAsJsonObjectOrNull("minecraft:particle_appearance_lighting") != null) {
                BedrockParticleAppearanceLighting(true)
            } else {
                null
            },
            events = events,
            curves = curves,
        )

        return Triple(id, definition, spriteIds)
    }

    private fun normalizeBedrockId(id: Identifier): Identifier =
        when (id.namespace) {
            "dec", "the-poetry-of-winter" -> Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, id.path)
            else -> id
        }

    private fun texturePathToSpriteIds(raw: String): List<Identifier>? {
        val normalized = raw.replace('\\', '/').removeSuffix(".png")
        val hasExplicitNamespace = normalized.contains(':')
        val namespace: String
        val rawPath: String
        if (hasExplicitNamespace) {
            val parts = normalized.split(':', limit = 2)
            namespace = parts[0]
            rawPath = parts[1]
        } else {
            namespace = DecIsland.MOD_ID
            rawPath = normalized
        }
        val path = if (rawPath.startsWith("textures/")) rawPath else "textures/$rawPath"
        return when {
            path == "textures/particle/sga_*" && namespace == "minecraft" ->
                ('a'..'z').map { char -> Identifier.fromNamespaceAndPath(namespace, "sga_$char") }
            path.startsWith("textures/particle/") ->
                listOf(Identifier.fromNamespaceAndPath(namespace, path.removePrefix("textures/particle/")))
            path.startsWith("textures/wb_par/") ->
                listOf(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "wb_par/${path.removePrefix("textures/wb_par/")}"))
            path.startsWith("textures/EPIC/") ->
                listOf(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "epic/${path.removePrefix("textures/EPIC/")}"))
            path.startsWith("textures/epic/") ->
                listOf(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "epic/${path.removePrefix("textures/epic/")}"))
            else -> null
        }
    }

    private fun parseScalarOrVecOrString(element: com.google.gson.JsonElement): Any? =
        when {
            element.isJsonPrimitive && element.asJsonPrimitive.isNumber -> element.asDouble
            element.isJsonPrimitive && element.asJsonPrimitive.isString -> element.asString
            element.isJsonArray -> parseJsonArray(element.asJsonArray)
            else -> null
        }

    private fun parseTintColor(element: com.google.gson.JsonElement): BedrockTintColor? {
        if (element.isJsonArray) {
            val parsed = parseJsonArray(element.asJsonArray)
            if (parsed is DoubleArray && parsed.size >= 3) {
                return BedrockTintColor.ConstantRgb(parsed.copyOfRange(0, 3))
            }
            return null
        }

        if (element.isJsonPrimitive && element.asJsonPrimitive.isString) {
            return parseHexColor(element.asString)?.let(BedrockTintColor::ConstantHex)
        }

        if (!element.isJsonObject) {
            return null
        }

        val obj = element.asJsonObject
        val interpolant = obj.getAsJsonPrimitiveOrNull("interpolant")?.asString ?: return null
        val gradient = obj.getAsJsonObjectOrNull("gradient") ?: return null
        val stops = ArrayList<ColorStop>(gradient.entrySet().size)
        for ((key, valueElement) in gradient.entrySet()) {
            val t = key.toDoubleOrNull() ?: continue
            val argb = if (valueElement.isJsonPrimitive && valueElement.asJsonPrimitive.isString) {
                parseHexColor(valueElement.asString)
            } else {
                null
            } ?: continue
            stops += ColorStop(t, argb)
        }

        if (stops.isEmpty()) {
            return null
        }
        stops.sortBy(ColorStop::t)
        return BedrockTintColor.Gradient(interpolant, stops)
    }

    private fun parseHexColor(value: String): Int? {
        val hex = value.trim().removePrefix("#")
        if (hex.length != 6 && hex.length != 8) {
            return null
        }
        return try {
            val parsed = hex.toLong(16).toInt()
            if (hex.length == 6) {
                (0xFF shl 24) or parsed
            } else {
                parsed
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun parseJsonArray(array: JsonArray): Any? {
        if (array.size() == 0) {
            return emptyList<Any>()
        }

        var allNumbers = true
        var allStrings = true
        for (index in 0 until array.size()) {
            val element = array[index]
            if (!element.isJsonPrimitive) {
                allNumbers = false
                allStrings = false
                break
            }
            val primitive = element.asJsonPrimitive
            allNumbers = allNumbers && primitive.isNumber
            allStrings = allStrings && primitive.isString
        }

        return when {
            allNumbers -> DoubleArray(array.size()) { index -> array[index].asDouble }
            allStrings -> Array(array.size()) { index -> array[index].asString }
            else -> {
                val out = ArrayList<Any?>(array.size())
                for (index in 0 until array.size()) {
                    val element = array[index]
                    out += when {
                        element.isJsonPrimitive && element.asJsonPrimitive.isNumber -> element.asDouble
                        element.isJsonPrimitive && element.asJsonPrimitive.isString -> element.asString
                        else -> null
                    }
                }
                out
            }
        }
    }

    private fun parseCurves(curvesObject: JsonObject?): Map<String, BedrockCurve> {
        if (curvesObject == null) {
            return emptyMap()
        }

        val curves = LinkedHashMap<String, BedrockCurve>()
        for ((rawName, element) in curvesObject.entrySet()) {
            val obj = element.takeIf { it.isJsonObject }?.asJsonObject ?: continue
            val type = obj.getAsJsonPrimitiveOrNull("type")?.asString?.lowercase() ?: continue
            val input = obj.get("input")?.let { parseScalarOrVecOrString(it) as? String }
            val horizontalRange = obj.get("horizontal_range")?.let { parseScalarOrVecOrString(it)?.toString() }

            val curve = when (type) {
                "linear" -> {
                    val nodes = obj.getAsJsonArrayOrNull("nodes")
                        ?.mapNotNull { node -> node.takeIf { it.isJsonPrimitive && it.asJsonPrimitive.isNumber }?.asDouble }
                        ?: emptyList()
                    BedrockCurve.Linear(
                        input = input,
                        horizontalRange = horizontalRange,
                        nodes = nodes,
                    )
                }
                "catmull_rom" -> {
                    val nodes = obj.getAsJsonArrayOrNull("nodes")
                        ?.mapNotNull { node -> node.takeIf { it.isJsonPrimitive && it.asJsonPrimitive.isNumber }?.asDouble }
                        ?: emptyList()
                    BedrockCurve.CatmullRom(
                        input = input,
                        horizontalRange = horizontalRange,
                        nodes = nodes,
                    )
                }
                "bezier_chain" -> {
                    val nodes = obj.getAsJsonObjectOrNull("nodes")
                        ?.entrySet()
                        ?.mapNotNull { (key, value) ->
                            val position = key.toDoubleOrNull() ?: return@mapNotNull null
                            val nodeObj = value.takeIf { it.isJsonObject }?.asJsonObject ?: return@mapNotNull null
                            BedrockBezierNode(
                                position = position,
                                value = nodeObj.getAsJsonPrimitiveOrNull("value")?.asDouble ?: return@mapNotNull null,
                                slope = nodeObj.getAsJsonPrimitiveOrNull("slope")?.asDouble ?: 0.0,
                            )
                        }
                        ?.sortedBy(BedrockBezierNode::position)
                        ?: emptyList()
                    BedrockCurve.BezierChain(
                        input = input,
                        horizontalRange = horizontalRange,
                        nodes = nodes,
                    )
                }
                else -> null
            } ?: continue

            curves[rawName] = curve
        }
        return curves
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
