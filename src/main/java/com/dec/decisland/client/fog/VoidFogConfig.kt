package com.dec.decisland.client.fog

import com.dec.decisland.DecIsland
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager
import java.io.InputStreamReader
import kotlin.math.max

object VoidFogConfig {
    private val gson = Gson()
    private val configId = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "fog/void.json")

    @Volatile
    private var loaded = false

    @Volatile
    private var config: FogConfig = FogConfig.defaults()

    fun load(resourceManager: ResourceManager?) {
        config = resourceManager?.let(::loadFromManager) ?: FogConfig.defaults()
        loaded = true
    }

    fun ensureLoaded(resourceManager: ResourceManager?) {
        if (loaded) {
            return
        }
        synchronized(this) {
            if (!loaded) {
                load(resourceManager)
            }
        }
    }

    fun specFor(biomeId: Identifier?): FogSpec {
        val current = config
        return biomeId?.let { current.byBiome[it] } ?: current.global
    }

    fun globalSpec(): FogSpec = config.global

    fun tintRgb(r: Float, g: Float, b: Float, biomeId: Identifier?): FloatArray {
        val current = config
        var rr = r
        var gg = g
        var bb = b

        val globalTint = current.global
        if (globalTint.color != null && globalTint.colorMix > 0f) {
            val (tr, tg, tb) = globalTint.color.toRgb()
            rr = lerp(globalTint.colorMix, rr, tr)
            gg = lerp(globalTint.colorMix, gg, tg)
            bb = lerp(globalTint.colorMix, bb, tb)
        }

        val biomeTint = biomeId?.let { current.byBiome[it] }
        if (biomeTint?.color != null && biomeTint.colorMix > 0f) {
            val (tr, tg, tb) = biomeTint.color.toRgb()
            rr = lerp(biomeTint.colorMix, rr, tr)
            gg = lerp(biomeTint.colorMix, gg, tg)
            bb = lerp(biomeTint.colorMix, bb, tb)
        }

        return floatArrayOf(rr, gg, bb)
    }

    private fun loadFromManager(manager: ResourceManager): FogConfig {
        val resource = manager.getResource(configId).orElse(null) ?: return FogConfig.defaults()
        return try {
            resource.open().use { input ->
                InputStreamReader(input).use { reader ->
                    val root = gson.fromJson(reader, JsonObject::class.java) ?: return FogConfig.defaults()
                    FogConfig.fromJson(root)
                }
            }
        } catch (t: Throwable) {
            DecIsland.LOGGER.warn("Failed to load void fog config from {}", configId, t)
            FogConfig.defaults()
        }
    }

    private fun Int.toRgb(): Triple<Float, Float, Float> {
        val r = ((this ushr 16) and 255) / 255f
        val g = ((this ushr 8) and 255) / 255f
        val b = (this and 255) / 255f
        return Triple(r, g, b)
    }

    private fun lerp(t: Float, a: Float, b: Float): Float = a + t * (b - a)

    data class FogSpec(
        val color: Int?,
        val colorMix: Float,
        val startMul: Float,
        val endMul: Float,
        val alphaMul: Float,
    ) {
        companion object {
            fun defaults(): FogSpec = FogSpec(
                color = null,
                colorMix = 0f,
                startMul = 1f,
                endMul = 1f,
                alphaMul = 1f,
            )

            fun fromJson(obj: JsonObject): FogSpec {
                val color = obj.get("color")?.let(::parseColor)
                val colorMix = obj.get("color_mix")?.asFloat ?: if (color != null) 0.35f else 0f
                val startMul = obj.get("start_mul")?.asFloat ?: 1f
                val endMul = obj.get("end_mul")?.asFloat ?: 1f
                val alphaMul = obj.get("alpha_mul")?.asFloat ?: 1f
                return FogSpec(color, colorMix, startMul, endMul, alphaMul)
            }

            private fun parseColor(element: JsonElement): Int? {
                if (!element.isJsonPrimitive) {
                    return null
                }
                val primitive = element.asJsonPrimitive
                if (primitive.isNumber) {
                    return primitive.asInt
                }
                val text = primitive.asString.trim()
                val hex = when {
                    text.startsWith("#") -> text.substring(1)
                    text.startsWith("0x") || text.startsWith("0X") -> text.substring(2)
                    else -> text
                }
                return hex.toIntOrNull(16)
            }
        }
    }

    data class FogConfig(
        val global: FogSpec,
        val byBiome: Map<Identifier, FogSpec>,
    ) {
        companion object {
            fun defaults(): FogConfig = FogConfig(
                global = FogSpec(
                    color = 0x8FB4FF,
                    colorMix = 0.22f,
                    startMul = 0.80f,
                    endMul = 0.62f,
                    alphaMul = 1.0f,
                ),
                byBiome = mapOf(
                    Identifier.fromNamespaceAndPath("minecraft", "ice_spikes") to FogSpec(0xB9F6FF, 0.45f, 0.78f, 0.58f, 1.0f),
                    Identifier.fromNamespaceAndPath("minecraft", "snowy_taiga") to FogSpec(0xA8C8FF, 0.28f, 0.84f, 0.66f, 1.0f),
                    Identifier.fromNamespaceAndPath("minecraft", "grove") to FogSpec(0x9FC3FF, 0.30f, 0.82f, 0.62f, 1.0f),
                    Identifier.fromNamespaceAndPath("minecraft", "forest") to FogSpec(0x93C8A0, 0.18f, 0.88f, 0.70f, 1.0f),
                    Identifier.fromNamespaceAndPath("minecraft", "birch_forest") to FogSpec(0xA6D7AE, 0.16f, 0.90f, 0.72f, 1.0f),
                    Identifier.fromNamespaceAndPath("minecraft", "river") to FogSpec(0x6AA5FF, 0.25f, 0.82f, 0.60f, 1.0f),
                    Identifier.fromNamespaceAndPath("minecraft", "frozen_river") to FogSpec(0xA3E6FF, 0.30f, 0.80f, 0.58f, 1.0f),
                ),
            )

            fun fromJson(root: JsonObject): FogConfig {
                val global = root.getAsJsonObject("global")?.let(FogSpec::fromJson) ?: FogSpec.defaults()
                val byBiomeObj = root.getAsJsonObject("by_biome") ?: JsonObject()
                val byBiome = buildMap {
                    byBiomeObj.entrySet().forEach { (key, value) ->
                        if (!value.isJsonObject) {
                            return@forEach
                        }
                        val id = Identifier.tryParse(key) ?: return@forEach
                        put(id, FogSpec.fromJson(value.asJsonObject))
                    }
                }
                return FogConfig(global, byBiome)
            }
        }
    }
}
