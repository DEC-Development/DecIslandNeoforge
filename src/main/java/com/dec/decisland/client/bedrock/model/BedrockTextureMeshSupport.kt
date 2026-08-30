package com.dec.decisland.client.bedrock.model

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import org.joml.Vector3f
import java.awt.image.BufferedImage
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap
import javax.imageio.ImageIO
import kotlin.math.PI

object BedrockTextureMeshSupport {
    enum class TransformMode {
        DIRECT,
        HUMANOID_ARMOR,
        ATTACHABLE_ARMOR,
    }

    enum class MeshRenderMode {
        POLYGON,
    }

    private val childrenField = ModelPart::class.java.getDeclaredField("children").apply {
        isAccessible = true
    }

    private val textureMaskCache = ConcurrentHashMap<Identifier, TextureMask>()

    fun attachMeshesToNamedBones(
        root: ModelPart,
        geometry: BedrockGeometry,
        transformMode: TransformMode = TransformMode.DIRECT,
        textureId: Identifier? = null,
        meshRenderMode: MeshRenderMode = MeshRenderMode.POLYGON,
    ) {
        if (meshRenderMode != MeshRenderMode.POLYGON || geometry.bones.none { it.textureMeshes.isNotEmpty() }) {
            return
        }

        val textureMask = textureId?.let(::textureMask)
        val lookup = root.createPartLookup()
        geometry.bones.forEach { bone ->
            val parent = lookup.apply(bone.name) ?: return@forEach
            bone.textureMeshes.forEachIndexed { index, textureMesh ->
                val childName = "__texture_mesh_$index"
                if (!parent.hasChild(childName)) {
                    mutableChildren(parent)[childName] = createTextureMeshPart(
                        bone = bone,
                        textureMesh = textureMesh,
                        geometry = geometry,
                        transformMode = transformMode,
                        textureMask = textureMask,
                    )
                }
            }
        }
    }

    fun submitStoredSideMeshes(
        root: ModelPart,
        poseStack: PoseStack,
        collector: SubmitNodeCollector,
        renderType: RenderType,
        packedLight: Int,
        color: Int = -1,
        itemStack: ItemStack? = null,
    ) {
        // Texture meshes are now rendered entirely through polygon ModelParts.
    }

    @Suppress("UNCHECKED_CAST")
    private fun mutableChildren(part: ModelPart): MutableMap<String, ModelPart> =
        childrenField.get(part) as MutableMap<String, ModelPart>

    private fun createTextureMeshPart(
        bone: BedrockBone,
        textureMesh: BedrockTextureMesh,
        geometry: BedrockGeometry,
        transformMode: TransformMode,
        textureMask: TextureMask?,
    ): ModelPart {
        val part = ModelPart(
            createTextureMeshPolygons(textureMesh, geometry, textureMask),
            hashMapOf(),
        )

        val position = resolvePosition(bone, textureMesh, transformMode)
        val pose = PartPose(
            position.x,
            position.y,
            position.z,
            textureMesh.rotation.x.toModelRadX(),
            textureMesh.rotation.y.toModelRadY(),
            textureMesh.rotation.z.toModelRadZ(),
            1.0f,
            1.0f,
            1.0f,
        )
        part.setInitialPose(pose)
        part.loadPose(pose)
        return part
    }

    private fun createTextureMeshPolygons(
        textureMesh: BedrockTextureMesh,
        geometry: BedrockGeometry,
        textureMask: TextureMask?,
    ): List<ModelPart.Cube> {
        val mask = textureMask ?: TextureMask.full(geometry.textureWidth, geometry.textureHeight)
        val localPivot = BedrockVec3(
            textureMesh.localPivot.x,
            textureMesh.localPivot.y,
            -textureMesh.localPivot.z,
        )

        fun xCoord(pixelX: Float): Float =
            (-pixelX * geometry.textureWidth.toFloat() / mask.width.toFloat()) * textureMesh.scale.x + localPivot.x

        fun yCoord(depth: Float): Float = depth * textureMesh.scale.y + localPivot.y

        fun zCoord(pixelY: Float): Float =
            (pixelY * geometry.textureHeight.toFloat() / mask.height.toFloat()) * textureMesh.scale.z + localPivot.z

        val cubes = ArrayList<ModelPart.Cube>()

        cubes += singlePolygonCube(
            polygon(
                normalX = 0.0f,
                normalY = 1.0f,
                normalZ = 0.0f,
                vertices = arrayOf(
                    ModelPart.Vertex(xCoord(mask.width.toFloat()), yCoord(0.0f), zCoord(0.0f), 1.0f, 1.0f),
                    ModelPart.Vertex(xCoord(mask.width.toFloat()), yCoord(0.0f), zCoord(mask.height.toFloat()), 1.0f, 0.0f),
                    ModelPart.Vertex(xCoord(0.0f), yCoord(0.0f), zCoord(mask.height.toFloat()), 0.0f, 0.0f),
                    ModelPart.Vertex(xCoord(0.0f), yCoord(0.0f), zCoord(0.0f), 0.0f, 1.0f),
                ),
            ),
        )
        cubes += singlePolygonCube(
            polygon(
                normalX = 0.0f,
                normalY = -1.0f,
                normalZ = 0.0f,
                vertices = arrayOf(
                    ModelPart.Vertex(xCoord(mask.width.toFloat()), yCoord(-1.0f), zCoord(0.0f), 1.0f, 1.0f),
                    ModelPart.Vertex(xCoord(0.0f), yCoord(-1.0f), zCoord(0.0f), 0.0f, 1.0f),
                    ModelPart.Vertex(xCoord(0.0f), yCoord(-1.0f), zCoord(mask.height.toFloat()), 0.0f, 0.0f),
                    ModelPart.Vertex(xCoord(mask.width.toFloat()), yCoord(-1.0f), zCoord(mask.height.toFloat()), 1.0f, 0.0f),
                ),
            ),
        )

        for (y in 0 until mask.height) {
            for (x in 0..mask.width) {
                val leftOpaque = if (x == 0) false else mask.isOpaque(x - 1, y)
                val rightOpaque = if (x == mask.width) false else mask.isOpaque(x, y)
                if (leftOpaque != rightOpaque) {
                    cubes += createTransitionCube(
                        sx = x.toFloat(),
                        sy = y.toFloat(),
                        ex = x.toFloat(),
                        ey = y.toFloat() + 1.0f,
                        dir = if (leftOpaque) 1 else -1,
                        mask = mask,
                        xCoord = ::xCoord,
                        yCoord = ::yCoord,
                        zCoord = ::zCoord,
                    )
                }
            }
        }

        for (x in 0 until mask.width) {
            for (y in 0..mask.height) {
                val topOpaque = if (y == 0) false else mask.isOpaque(x, y - 1)
                val bottomOpaque = if (y == mask.height) false else mask.isOpaque(x, y)
                if (topOpaque != bottomOpaque) {
                    cubes += createTransitionCube(
                        sx = x.toFloat(),
                        sy = y.toFloat(),
                        ex = x.toFloat() + 1.0f,
                        ey = y.toFloat(),
                        dir = if (topOpaque) -1 else 1,
                        mask = mask,
                        xCoord = ::xCoord,
                        yCoord = ::yCoord,
                        zCoord = ::zCoord,
                    )
                }
            }
        }

        return cubes
    }

    private fun createTransitionCube(
        sx: Float,
        sy: Float,
        ex: Float,
        ey: Float,
        dir: Int,
        mask: TextureMask,
        xCoord: (Float) -> Float,
        yCoord: (Float) -> Float,
        zCoord: (Float) -> Float,
    ): ModelPart.Cube {
        val isVerticalBoundary = sx == ex
        var uvSx = sx
        var uvSy = sy
        var uvEx = ex
        var uvEy = ey

        val normal = if (isVerticalBoundary) {
            uvSx += 0.1f * -dir
            uvEx += 0.4f * -dir
            uvSy += 0.1f
            uvEy -= 0.1f
            Vector3f(-dir.toFloat(), 0.0f, 0.0f)
        } else {
            uvSy += 0.1f * dir
            uvEy += 0.4f * dir
            uvSx += 0.1f
            uvEx -= 0.1f
            Vector3f(0.0f, 0.0f, -dir.toFloat())
        }

        val baseVertices = arrayOf(
            ModelPart.Vertex(
                xCoord(sx),
                yCoord(0.0f),
                zCoord(sy),
                clampUv(uvEx / mask.width.toFloat()),
                clampUv(1.0f - (uvSy / mask.height.toFloat())),
            ),
            ModelPart.Vertex(
                xCoord(sx),
                yCoord(-1.0f),
                zCoord(sy),
                clampUv(uvEx / mask.width.toFloat()),
                clampUv(1.0f - (uvEy / mask.height.toFloat())),
            ),
            ModelPart.Vertex(
                xCoord(ex),
                yCoord(-1.0f),
                zCoord(ey),
                clampUv(uvSx / mask.width.toFloat()),
                clampUv(1.0f - (uvEy / mask.height.toFloat())),
            ),
            ModelPart.Vertex(
                xCoord(ex),
                yCoord(0.0f),
                zCoord(ey),
                clampUv(uvSx / mask.width.toFloat()),
                clampUv(1.0f - (uvSy / mask.height.toFloat())),
            ),
        )

        val orderedVertices = when {
            isVerticalBoundary && dir == 1 -> baseVertices
            isVerticalBoundary -> arrayOf(baseVertices[0], baseVertices[3], baseVertices[2], baseVertices[1])
            dir == -1 -> baseVertices
            else -> arrayOf(baseVertices[0], baseVertices[3], baseVertices[2], baseVertices[1])
        }

        return singlePolygonCube(ModelPart.Polygon(orderedVertices, normal))
    }

    private fun clampUv(value: Float): Float = value.coerceIn(0.0f, 1.0f)

    private fun resolvePosition(
        bone: BedrockBone,
        textureMesh: BedrockTextureMesh,
        transformMode: TransformMode,
    ): BedrockVec3 = when (transformMode) {
        TransformMode.DIRECT -> textureMesh.position
        TransformMode.HUMANOID_ARMOR,
        TransformMode.ATTACHABLE_ARMOR,
        -> BedrockVec3(
            bone.pivot.x - textureMesh.position.x,
            -textureMesh.position.y,
            textureMesh.position.z - bone.pivot.z,
        )
    }

    private fun polygon(
        normalX: Float,
        normalY: Float,
        normalZ: Float,
        vertices: Array<ModelPart.Vertex>,
    ): ModelPart.Polygon = ModelPart.Polygon(vertices, Vector3f(normalX, normalY, normalZ))

    private fun singlePolygonCube(polygon: ModelPart.Polygon): ModelPart.Cube =
        ModelPart.Cube(
            0,
            0,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            false,
            1.0f,
            1.0f,
            setOf(net.minecraft.core.Direction.NORTH),
        ).also { cube ->
            cube.polygons[0] = polygon
        }

    private fun textureMask(textureId: Identifier): TextureMask =
        textureMaskCache.computeIfAbsent(textureId) {
            openTexture(it)?.use(::readTextureMask) ?: TextureMask.full(64, 64)
        }

    private fun openTexture(textureId: Identifier): InputStream? {
        val resourceManager = runCatching { Minecraft.getInstance().resourceManager }.getOrNull()
        val resource = resourceManager?.getResource(textureId)?.orElse(null)
        if (resource != null) {
            return resource.open()
        }

        val classLoader = Thread.currentThread().contextClassLoader ?: javaClass.classLoader
        return classLoader.getResourceAsStream("assets/${textureId.namespace}/${textureId.path}")
    }

    private fun readTextureMask(stream: InputStream): TextureMask {
        val image = ImageIO.read(stream) ?: return TextureMask.full(64, 64)
        return TextureMask.fromImage(image)
    }

    private data class TextureMask(
        val width: Int,
        val height: Int,
        private val alpha: BooleanArray,
    ) {
        fun isOpaque(x: Int, y: Int): Boolean = alpha[y * width + x]

        companion object {
            fun fromImage(image: BufferedImage): TextureMask {
                val alpha = BooleanArray(image.width * image.height)
                for (y in 0 until image.height) {
                    for (x in 0 until image.width) {
                        alpha[y * image.width + x] = ((image.getRGB(x, y) ushr 24) and 0xFF) > 140
                    }
                }
                return TextureMask(image.width, image.height, alpha)
            }

            fun full(width: Int, height: Int): TextureMask =
                TextureMask(width, height, BooleanArray(width * height) { true })
        }
    }

    private fun Float.toRadians(): Float = (this * PI.toFloat()) / 180.0f

    private fun Float.toModelRadX(): Float = -toRadians()

    private fun Float.toModelRadY(): Float = -toRadians()

    private fun Float.toModelRadZ(): Float = toRadians()
}
