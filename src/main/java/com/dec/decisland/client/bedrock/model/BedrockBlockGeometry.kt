package com.dec.decisland.client.bedrock.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.mojang.math.Transformation
import net.minecraft.client.renderer.block.model.BlockElement
import net.minecraft.client.renderer.block.model.BlockElementFace
import net.minecraft.client.renderer.block.model.BlockElementRotation
import net.minecraft.client.renderer.block.model.BlockFaceUV
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.ItemOverrides
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.client.resources.model.Material
import net.minecraft.client.resources.model.ModelBaker
import net.minecraft.client.resources.model.ModelState
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.IModelBuilder
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry
import net.neoforged.neoforge.client.model.geometry.UnbakedGeometryHelper
import org.joml.Vector3f
import org.joml.Matrix4f
import kotlin.math.PI
import java.util.function.Function

/** Bakes the same Bedrock geometry JSON used by entity models into a block model. */
class BedrockBlockGeometry(
    private val geometryLocation: ResourceLocation,
    private val textureName: String,
) : IUnbakedGeometry<BedrockBlockGeometry> {
    override fun bake(
        context: IGeometryBakingContext,
        baker: ModelBaker,
        spriteGetter: Function<Material, TextureAtlasSprite>,
        modelState: ModelState,
        overrides: ItemOverrides,
    ): BakedModel {
        val geometry = BedrockEntityAssets.geometry(geometryLocation)
        val material = if (textureName.startsWith("#")) {
            context.getMaterial(textureName)
        } else {
            Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.parse(textureName))
        }
        val sprite = spriteGetter.apply(material)
        val builder = IModelBuilder.of(
            context.useAmbientOcclusion(),
            context.isGui3d(),
            true,
            context.getTransforms(),
            overrides,
            sprite,
            context.getRenderTypeHint()?.let(context::getRenderType)
                ?: net.neoforged.neoforge.client.RenderTypeGroup.EMPTY,
        )

        geometry.bones.flatMap { bone -> bone.cubes.map { cube -> cube to bone } }
            .forEach { (cube, bone) ->
                bakeCubeQuads(cube, bone, geometry, sprite, modelState)
                    .forEach(builder::addUnculledFace)
            }
        return builder.build()
    }

    /**
     * Bake every Bedrock cube through one coordinate pipeline.  The template
     * is deliberately unrotated; all cube and bone transforms are applied to
     * its vertices in Bedrock space, exactly like GeckoLib's PoseStack path.
     * This also supports combined/multi-axis rotations which
     * BlockElementRotation cannot represent.
     */
    private fun bakeCubeQuads(
        cube: BedrockCube,
        bone: BedrockBone,
        geometry: BedrockGeometry,
        sprite: TextureAtlasSprite,
        modelState: ModelState,
    ): List<BakedQuad> {
        val templateCube = cube.copy(rotation = BedrockVec3.ZERO, hasRotation = false)
        val element = toElement(templateCube, bone, geometry)
        return Direction.values().flatMap { direction ->
            val face = element.faces[direction] ?: return@flatMap emptyList()
            val template = UnbakedGeometryHelper.bakeElementFace(element, face, sprite, direction, modelState)
            val data = template.vertices.copyOf()
            for (vertex in 0 until 4) {
                val base = vertex * 8
                val point = Vector3f(
                    java.lang.Float.intBitsToFloat(data[base]) * 16.0f,
                    java.lang.Float.intBitsToFloat(data[base + 1]) * 16.0f,
                    java.lang.Float.intBitsToFloat(data[base + 2]) * 16.0f,
                )
                transformJavaVertex(point, cube, bone)
                data[base] = java.lang.Float.floatToRawIntBits(point.x / 16.0f)
                data[base + 1] = java.lang.Float.floatToRawIntBits(point.y / 16.0f)
                data[base + 2] = java.lang.Float.floatToRawIntBits(point.z / 16.0f)
            }
            val front = BakedQuad(data, template.tintIndex, direction, sprite, true, true)
            listOf(front, reverseWinding(front))
        }
    }

    private fun reverseWinding(quad: BakedQuad): BakedQuad {
        val source = quad.vertices
        val reversed = IntArray(source.size)
        for (vertex in 0 until 4) {
            java.lang.System.arraycopy(source, (3 - vertex) * 8, reversed, vertex * 8, 8)
        }
        return BakedQuad(reversed, quad.tintIndex, quad.direction.getOpposite(), quad.sprite, true, true)
    }

    private fun toElement(cube: BedrockCube, bone: BedrockBone, geometry: BedrockGeometry): BlockElement {
        // Bedrock block geometry uses the same block-space Y convention as Java:
        // y=0 is the bottom of the block. The entity renderer has a different
        // 24-y conversion, but it must not be used here.
        val from = Vector3f(
            blockX(cube.origin.x, cube.size.x),
            cube.origin.y,
            cube.origin.z + 8.0f,
        )
        val to = Vector3f(
            from.x + cube.size.x,
            from.y + cube.size.y,
            from.z + cube.size.z,
        )
        val faces = linkedMapOf<Direction, BlockElementFace>()
        Direction.values().forEach { direction ->
            val uv = cube.faceUvs[direction.bedrockName]
            val faceUv = if (uv != null) {
                // Java block-model UVs are expressed in a 0..16 model space,
                // while Bedrock stores pixel coordinates in the geometry's
                // actual texture dimensions (the lantern is 22x22).
                val u1 = uv.u * 16.0f / geometry.textureWidth
                val v1 = uv.v * 16.0f / geometry.textureHeight
                val u2 = (uv.u + uv.width) * 16.0f / geometry.textureWidth
                val v2 = (uv.v + uv.height) * 16.0f / geometry.textureHeight
                BlockFaceUV(floatArrayOf(u1, v1, u2, v2), 0)
            } else {
                BlockFaceUV(null, 0)
            }
            faces[direction] = BlockElementFace(null, -1, "texture", faceUv)
        }

        return BlockElement(from, to, faces, null, true)
    }

    /**
     * Bake a Bedrock zero-thickness cube through Minecraft's FaceBakery.
     *
     * These parts are intentional planes (chains and lantern struts), so they
     * must not be inflated to a paper-thin cube.  Using FaceBakery here is
     * important: it keeps the BLOCK vertex format, UV interpolation, shading,
     * and element rotation identical to ordinary block-model faces.
     */
    private fun zeroThicknessQuads(
        cube: BedrockCube,
        bone: BedrockBone,
        geometry: BedrockGeometry,
        sprite: TextureAtlasSprite,
        modelState: ModelState,
    ): List<BakedQuad> {
        val axis = when {
            cube.size.x == 0.0f -> Direction.Axis.X
            cube.size.y == 0.0f -> Direction.Axis.Y
            else -> Direction.Axis.Z
        }
        val directions = when (axis) {
            Direction.Axis.X -> arrayOf(Direction.EAST, Direction.WEST)
            Direction.Axis.Y -> arrayOf(Direction.UP, Direction.DOWN)
            Direction.Axis.Z -> arrayOf(Direction.SOUTH, Direction.NORTH)
        }
        // FaceBakery intentionally rejects some degenerate elements.  Bake a
        // microscopic template only to obtain Minecraft's correct BLOCK
        // vertex layout, UVs, lightmap and normal; replace its positions below
        // with the exact zero-thickness Bedrock plane.
        val templateSize = when (axis) {
            Direction.Axis.X -> cube.copy(size = cube.size.copy(x = 0.001f))
            Direction.Axis.Y -> cube.copy(size = cube.size.copy(y = 0.001f))
            Direction.Axis.Z -> cube.copy(size = cube.size.copy(z = 0.001f))
        }
        val element = toElement(templateSize, bone, geometry)
        return directions.flatMap { direction ->
            val face = element.faces[direction] ?: return@flatMap emptyList()
            val template = UnbakedGeometryHelper.bakeElementFace(element, face, sprite, direction, modelState)
            val data = template.vertices.copyOf()
            // FaceBakery and Bedrock use different corner orders, especially
            // after a bone has been rotated around its pivot. Match corners
            // in transformed model space instead of assuming that vertex 0
            // in one system is vertex 0 in the other (the old assumption was
            // the source of the chain's X/Y displacement).
            val exactPoints = exactPlanePoints(cube, bone, geometry)
            val used = BooleanArray(exactPoints.size)
            for (index in 0 until 4) {
                val base = index * 8
                val source = Vector3f(
                    java.lang.Float.intBitsToFloat(data[base]) * 16.0f,
                    java.lang.Float.intBitsToFloat(data[base + 1]) * 16.0f,
                    java.lang.Float.intBitsToFloat(data[base + 2]) * 16.0f,
                )
                var best = -1
                var bestDistance = Float.POSITIVE_INFINITY
                exactPoints.forEachIndexed { pointIndex, point ->
                    if (used[pointIndex]) return@forEachIndexed
                    val dx = source.x - point.x
                    val dy = source.y - point.y
                    val dz = source.z - point.z
                    val distance = dx * dx + dy * dy + dz * dz
                    if (distance < bestDistance) {
                        bestDistance = distance
                        best = pointIndex
                    }
                }
                if (best < 0) best = index
                used[best] = true
                val point = exactPoints[best]
                data[base] = java.lang.Float.floatToRawIntBits(point.x / 16.0f)
                data[base + 1] = java.lang.Float.floatToRawIntBits(point.y / 16.0f)
                data[base + 2] = java.lang.Float.floatToRawIntBits(point.z / 16.0f)
            }
            val front = BakedQuad(data, template.tintIndex, direction, sprite, true, true)
            // A zero-thickness Bedrock polygon has no geometric back face.
            // GeckoLib emits it as a two-sided polygon; represent that here
            // with a second quad whose vertex order is explicitly reversed.
            val backData = IntArray(data.size)
            for (vertex in 0 until 4) {
                java.lang.System.arraycopy(data, (3 - vertex) * 8, backData, vertex * 8, 8)
            }
            val back = BakedQuad(backData, template.tintIndex, direction, sprite, true, true)
            listOf(front, back)
        }
    }

    private fun exactPlanePoints(
        cube: BedrockCube,
        bone: BedrockBone,
        geometry: BedrockGeometry,
    ): List<Vector3f> {
        val x = blockX(cube.origin.x, cube.size.x)
        val y = cube.origin.y
        val z = cube.origin.z + 8.0f
        val points = when {
            cube.size.x == 0.0f -> listOf(
                Vector3f(x, y, z), Vector3f(x, y, z + cube.size.z),
                Vector3f(x, y + cube.size.y, z + cube.size.z), Vector3f(x, y + cube.size.y, z),
            )
            cube.size.y == 0.0f -> listOf(
                Vector3f(x, y, z), Vector3f(x + cube.size.x, y, z),
                Vector3f(x + cube.size.x, y, z + cube.size.z), Vector3f(x, y, z + cube.size.z),
            )
            else -> listOf(
                Vector3f(x, y, z), Vector3f(x, y + cube.size.y, z),
                Vector3f(x + cube.size.x, y + cube.size.y, z), Vector3f(x + cube.size.x, y, z),
            )
        }.toMutableList()
        // GeckoLib renders cube-local transforms first, then lets the bone
        // PoseStack transform the complete cube. An omitted cube pivot or
        // rotation is zero; it never inherits the bone pivot.
        points.forEach { transformJavaVertex(it, cube, bone) }
        return points
    }

    private fun transformJavaVertex(point: Vector3f, cube: BedrockCube, bone: BedrockBone) {
        // Convert the already mirrored block-space point back to Bedrock
        // coordinates, apply GeckoLib's local-then-bone order, then convert
        // it back.  Keeping rotations in one coordinate system is essential:
        // applying Bedrock angles directly after the X mirror changes their
        // handedness and produces the large lampshade displacement.
        val bedrockPoint = Vector3f(8.0f - point.x, point.y, point.z - 8.0f)
        if (cube.hasRotation) {
            rotateBedrockAround(bedrockPoint, cube.rotation, cube.pivot)
        }
        rotateBedrockAround(bedrockPoint, bone.rotation, bone.pivot)
        point.set(8.0f - bedrockPoint.x, bedrockPoint.y, bedrockPoint.z + 8.0f)
    }

    private fun rotateBedrockAround(point: Vector3f, rotation: BedrockVec3, pivot: BedrockVec3) {
        if (rotation == BedrockVec3.ZERO) return
        // The point is in the original Bedrock coordinate system here. The
        // X-mirrored GeckoLib/vanilla angles (-X, -Y, +Z) must therefore be
        // conjugated by the mirror first, yielding the original Bedrock
        // angles (+X, +Y, +Z). Applying the mirrored signs here rotates a
        // Y=90 cube to the opposite side of the block.
        val rx = rotation.x * PI.toFloat() / 180.0f
        val ry = rotation.y * PI.toFloat() / 180.0f
        val rz = rotation.z * PI.toFloat() / 180.0f
        point.sub(pivot.x, pivot.y, pivot.z)
        point.rotateX(rx).rotateY(ry).rotateZ(rz)
        point.add(pivot.x, pivot.y, pivot.z)
    }

    /** No per-model translation is applied: Bedrock JSON coordinates are authoritative. */
    /**
     * GeckoLib's Bedrock loader mirrors the X axis for every geometry. This
     * is a coordinate-system conversion, not a per-model correction.
     */
    private fun blockX(originX: Float, sizeX: Float): Float =
        8.0f - originX - sizeX

    private val Direction.bedrockName: String
        get() = when (this) {
            Direction.DOWN -> "down"
            Direction.UP -> "up"
            Direction.NORTH -> "north"
            Direction.SOUTH -> "south"
            Direction.WEST -> "west"
            Direction.EAST -> "east"
        }

    companion object {
        fun read(json: JsonObject, context: JsonDeserializationContext): BedrockBlockGeometry {
            val geometry = ResourceLocation.parse(json.get("geometry").asString)
            val texture = json.get("texture")?.asString ?: "#texture"
            return BedrockBlockGeometry(geometry, texture)
        }
    }
}
