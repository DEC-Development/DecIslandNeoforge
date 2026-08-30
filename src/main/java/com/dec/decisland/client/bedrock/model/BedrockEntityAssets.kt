package com.dec.decisland.client.bedrock.model

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap

object BedrockEntityAssets {
    private val geometryCache = ConcurrentHashMap<Identifier, BedrockGeometry>()
    private val animationCache = ConcurrentHashMap<Identifier, Map<String, BedrockAnimationClip>>()

    fun geometry(resourceId: Identifier): BedrockGeometry =
        geometryCache.computeIfAbsent(resourceId) { loadGeometry(it) }

    fun animation(resourceId: Identifier, animationName: String): BedrockAnimationClip =
        animationCache.computeIfAbsent(resourceId) { loadAnimations(it) }[animationName]
            ?: error("Missing Bedrock animation '$animationName' in $resourceId")

    private fun loadGeometry(resourceId: Identifier): BedrockGeometry {
        val root = parseJson(resourceId)
        val geometryObject = root.getAsJsonArrayOrNull("minecraft:geometry")
            ?.firstOrNull()
            ?.takeIf(JsonElement::isJsonObject)
            ?.asJsonObject

        if (geometryObject != null) {
            val description = geometryObject.getAsJsonObjectOrNull("description")
                ?: error("Bedrock geometry description missing in $resourceId")
            val bones = geometryObject.getAsJsonArrayOrNull("bones")
                ?.mapNotNull { element -> element.takeIf(JsonElement::isJsonObject)?.asJsonObject }
                ?.map(::parseBone)
                ?: emptyList()

            return BedrockGeometry(
                identifier = description.getAsJsonPrimitiveOrNull("identifier")?.asString ?: resourceId.toString(),
                textureWidth = description.getAsJsonPrimitiveOrNull("texture_width")?.asInt ?: 64,
                textureHeight = description.getAsJsonPrimitiveOrNull("texture_height")?.asInt ?: 64,
                bones = bones,
            )
        }

        val legacyEntry = root.entrySet()
            .firstOrNull { (name, value) -> name.startsWith("geometry.") && value.isJsonObject }
            ?: error("Invalid Bedrock geometry file: $resourceId")
        val legacyGeometryObject = legacyEntry.value.asJsonObject
        val bones = legacyGeometryObject.getAsJsonArrayOrNull("bones")
            ?.mapNotNull { element -> element.takeIf(JsonElement::isJsonObject)?.asJsonObject }
            ?.map(::parseBone)
            ?: emptyList()

        return BedrockGeometry(
            identifier = legacyEntry.key,
            textureWidth = legacyGeometryObject.getAsJsonPrimitiveOrNull("texturewidth")?.asInt ?: 64,
            textureHeight = legacyGeometryObject.getAsJsonPrimitiveOrNull("textureheight")?.asInt ?: 64,
            bones = bones,
        )
    }

    private fun loadAnimations(resourceId: Identifier): Map<String, BedrockAnimationClip> {
        val root = parseJson(resourceId)
        val animationsObject = root.getAsJsonObjectOrNull("animations")
            ?: error("Invalid Bedrock animation file: $resourceId")

        return buildMap {
            animationsObject.entrySet().forEach { (name, value) ->
                val animationObject = value.takeIf(JsonElement::isJsonObject)?.asJsonObject ?: return@forEach
                val bonesObject = animationObject.getAsJsonObjectOrNull("bones") ?: JsonObject()
                val boneAnimations = buildMap {
                    bonesObject.entrySet().forEach { (boneName, boneValue) ->
                        val boneObject = boneValue.takeIf(JsonElement::isJsonObject)?.asJsonObject ?: return@forEach
                        put(
                            boneName,
                            BedrockBoneAnimation(
                                rotationFrames = parseAnimationTrack(boneObject.get("rotation")),
                                scaleFrames = parseAnimationTrack(boneObject.get("scale")),
                            ),
                        )
                    }
                }

                put(
                    name,
                    BedrockAnimationClip(
                        name = name,
                        loop = when (val loop = animationObject.get("loop")) {
                            null -> false
                            else -> if (loop.isJsonPrimitive && loop.asJsonPrimitive.isBoolean) {
                                loop.asBoolean
                            } else {
                                loop.asString.equals("loop", ignoreCase = true)
                            }
                        },
                        lengthSeconds = animationObject.getAsJsonPrimitiveOrNull("animation_length")?.asFloat ?: 0.0f,
                        boneAnimations = boneAnimations,
                    ),
                )
            }
        }
    }

    private fun parseBone(obj: JsonObject): BedrockBone = BedrockBone(
        name = obj.getAsJsonPrimitiveOrNull("name")?.asString ?: error("Bedrock bone missing name"),
        parent = obj.getAsJsonPrimitiveOrNull("parent")?.asString,
        pivot = parseVec3(obj.get("pivot")),
        rotation = parseVec3(obj.get("rotation")),
        cubes = obj.getAsJsonArrayOrNull("cubes")
            ?.mapNotNull { it.takeIf(JsonElement::isJsonObject)?.asJsonObject }
            ?.map { cubeObject ->
                BedrockCube(
                    origin = parseVec3(cubeObject.get("origin")),
                    size = parseVec3(cubeObject.get("size")),
                    pivot = parseVec3(cubeObject.get("pivot"), parseVec3(obj.get("pivot"))),
                    rotation = parseVec3(cubeObject.get("rotation")),
                    uv = parseUv(cubeObject.get("uv")),
                    inflate = cubeObject.getAsJsonPrimitiveOrNull("inflate")?.asFloat ?: 0.0f,
                    mirror = cubeObject.getAsJsonPrimitiveOrNull("mirror")?.asBoolean ?: false,
                )
            }
            ?: emptyList(),
        textureMeshes = obj.getAsJsonArrayOrNull("texture_meshes")
            ?.mapNotNull { it.takeIf(JsonElement::isJsonObject)?.asJsonObject }
            ?.map { meshObject ->
                BedrockTextureMesh(
                    texture = meshObject.getAsJsonPrimitiveOrNull("texture")?.asString ?: "default",
                    position = parseVec3(meshObject.get("position")),
                    rotation = parseVec3(meshObject.get("rotation")),
                    localPivot = parseVec3(meshObject.get("local_pivot")),
                    scale = parseVec3(meshObject.get("scale"), BedrockVec3(1.0f, 1.0f, 1.0f)),
                )
            }
            ?: emptyList(),
        locators = obj.getAsJsonObjectOrNull("locators")
            ?.entrySet()
            ?.mapNotNull { (name, value) ->
                parseVec3OrNull(value)?.let { name to BedrockLocator(name, it) }
            }
            ?.toMap()
            ?: emptyMap(),
    )

    private fun parseAnimationTrack(element: JsonElement?): List<BedrockKeyframe> {
        if (element == null || element.isJsonNull) {
            return emptyList()
        }
        if (element.isJsonArray) {
            return listOf(BedrockKeyframe(0.0f, parseVec3(element)))
        }
        if (!element.isJsonObject) {
            return emptyList()
        }

        return element.asJsonObject.entrySet()
            .mapNotNull { (timeKey, value) ->
                val time = timeKey.toFloatOrNull() ?: return@mapNotNull null
                val vector = parseAnimationVector(value) ?: return@mapNotNull null
                BedrockKeyframe(time, vector)
            }
            .sortedBy(BedrockKeyframe::timeSeconds)
    }

    private fun parseAnimationVector(element: JsonElement): BedrockVec3? {
        if (element.isJsonArray) {
            return parseVec3(element)
        }
        if (!element.isJsonObject) {
            return null
        }

        val obj = element.asJsonObject
        return parseVec3OrNull(obj.get("post"))
            ?: parseVec3OrNull(obj.get("vector"))
            ?: parseVec3OrNull(obj.get("pre"))
    }

    private fun parseUv(element: JsonElement?): Pair<Int, Int>? {
        val array = element?.takeIf(JsonElement::isJsonArray)?.asJsonArray ?: return null
        if (array.size() < 2) {
            return null
        }
        return array[0].asInt to array[1].asInt
    }

    private fun parseVec3(element: JsonElement?, fallback: BedrockVec3 = BedrockVec3.ZERO): BedrockVec3 =
        parseVec3OrNull(element) ?: fallback

    private fun parseVec3OrNull(element: JsonElement?): BedrockVec3? {
        val array = element?.takeIf(JsonElement::isJsonArray)?.asJsonArray ?: return null
        if (array.size() < 3) {
            return null
        }

        return BedrockVec3(
            array[0].asFloat,
            array[1].asFloat,
            array[2].asFloat,
        )
    }

    private fun parseJson(resourceId: Identifier): JsonObject {
        openResource(resourceId).use { stream ->
            if (stream == null) {
                error("Missing Bedrock asset: $resourceId")
            }

            return InputStreamReader(stream).use { reader ->
                JsonParser.parseReader(reader).asJsonObject
            }
        }
    }

    private fun openResource(resourceId: Identifier): InputStream? {
        val resourceManager = runCatching { Minecraft.getInstance().resourceManager }.getOrNull()
        val resource = resourceManager?.getResource(resourceId)?.orElse(null)
        if (resource != null) {
            return resource.open()
        }

        val classLoader = Thread.currentThread().contextClassLoader ?: javaClass.classLoader
        val fallbackPath = "assets/${resourceId.namespace}/${resourceId.path}"
        return classLoader.getResourceAsStream(fallbackPath)
    }

    private fun JsonObject.getAsJsonArrayOrNull(name: String): JsonArray? =
        get(name)?.takeIf(JsonElement::isJsonArray)?.asJsonArray

    private fun JsonObject.getAsJsonObjectOrNull(name: String): JsonObject? =
        get(name)?.takeIf(JsonElement::isJsonObject)?.asJsonObject

    private fun JsonObject.getAsJsonPrimitiveOrNull(name: String) =
        get(name)?.takeIf(JsonElement::isJsonPrimitive)?.asJsonPrimitive

    private fun JsonArray.firstOrNull(): JsonElement? = if (size() > 0) get(0) else null
}
