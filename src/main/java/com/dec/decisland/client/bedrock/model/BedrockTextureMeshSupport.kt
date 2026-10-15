package com.dec.decisland.client.bedrock.model

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.ItemStack
import org.joml.Vector3f
import java.awt.image.BufferedImage
import java.io.InputStream
import java.util.IdentityHashMap
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

    private val polygonsField = ModelPart.Cube::class.java.getDeclaredField("polygons").apply {
        isAccessible = true
    }

    // ModelPart.Vertex and ModelPart.Polygon are package-private in 1.21.1, so they
    // cannot be referenced directly from Kotlin. Reach them via reflection.
    private val vertexClass: Class<*> = Class.forName("net.minecraft.client.model.geom.ModelPart\$Vertex")
    private val vertexCtor = vertexClass
        .getDeclaredConstructor(
            java.lang.Float.TYPE,
            java.lang.Float.TYPE,
            java.lang.Float.TYPE,
            java.lang.Float.TYPE,
            java.lang.Float.TYPE,
        ).apply { isAccessible = true }

    private val polygonClass: Class<*> = Class.forName("net.minecraft.client.model.geom.ModelPart\$Polygon")
    private val polygonVerticesField = polygonClass.getDeclaredField("vertices").apply { isAccessible = true }
    private val polygonNormalField = polygonClass.getDeclaredField("normal").apply { isAccessible = true }

    private fun vertex(x: Float, y: Float, z: Float, u: Float, v: Float): Any =
        vertexCtor.newInstance(x, y, z, u, v) as Any

    private val textureMaskCache = ConcurrentHashMap<ResourceLocation, TextureMask>()

    /**
     * Texture meshes are geometry parts, but each mesh may use a different
     * material.  Keep them outside the normal ModelPart child tree so that a
     * model can submit every material with its own RenderType.
     */
    private data class StoredTextureMesh(
        val parentPath: List<ModelPart>,
        val part: ModelPart,
        val texture: ResourceLocation?,
        val usesDefaultBuffer: Boolean,
    )

    private val storedMeshesByRoot = IdentityHashMap<ModelPart, List<StoredTextureMesh>>()

    /**
     * Resolves the vanilla item model for a Bedrock texture mesh when the mesh
     * points at an item texture. Generated item models use the same rendering
     * path as DartRenderer, including the correct front/back/edge quads and
     * cutout handling. This avoids rebuilding item extrusion from the alpha
     * mask, which can make the mesh sides sample transparent texels.
     */
    fun itemStackForTextureMesh(
        geometry: BedrockGeometry,
        textureId: ResourceLocation,
        materialTextures: Map<String, ResourceLocation> = emptyMap(),
    ): ItemStack? {
        if (geometry.bones.none { it.textureMeshes.isNotEmpty() }) {
            return null
        }

        val resolvedTextures = geometry.bones
            .flatMap { it.textureMeshes }
            .map { mesh ->
                materialTextures[mesh.texture]
                    ?: materialTextures["default"]
                    ?: textureId
            }
            .distinct()
        if (resolvedTextures.size != 1) {
            return null
        }

        val itemTexture = resolvedTextures.single()
        val itemPath = itemTexture.path
            .removePrefix("textures/item/")
            .removeSuffix(".png")
        if (itemPath == itemTexture.path || itemPath.isEmpty()) {
            return null
        }

        val itemId = ResourceLocation.fromNamespaceAndPath(itemTexture.namespace, itemPath)
        val item = BuiltInRegistries.ITEM.get(itemId)
        return if (item == Items.AIR) null else ItemStack(item)
    }

    fun attachMeshesToNamedBones(
        root: ModelPart,
        geometry: BedrockGeometry,
        transformMode: TransformMode = TransformMode.DIRECT,
        textureId: ResourceLocation? = null,
        materialTextures: Map<String, ResourceLocation> = emptyMap(),
        meshRenderMode: MeshRenderMode = MeshRenderMode.POLYGON,
    ) {
        if (meshRenderMode != MeshRenderMode.POLYGON || geometry.bones.none { it.textureMeshes.isNotEmpty() }) {
            storedMeshesByRoot.remove(root)
            return
        }

        val paths = createPartPaths(root)
        val defaultTexture = materialTextures["default"] ?: textureId
        val storedMeshes = ArrayList<StoredTextureMesh>()
        geometry.bones.forEach { bone ->
            val parentPath = paths[bone.name] ?: return@forEach
            bone.textureMeshes.forEachIndexed { index, textureMesh ->
                val resolvedTexture = materialTextures[textureMesh.texture]
                    ?: materialTextures["default"]
                    ?: textureId
                storedMeshes += StoredTextureMesh(
                    parentPath = parentPath,
                    part = createTextureMeshPart(
                        bone = bone,
                        textureMesh = textureMesh,
                        geometry = geometry,
                        transformMode = transformMode,
                        textureMask = resolvedTexture?.let(::textureMask),
                    ),
                    texture = resolvedTexture,
                    usesDefaultBuffer = textureMesh.texture == "default" || resolvedTexture == defaultTexture,
                )
            }
        }
        storedMeshesByRoot[root] = storedMeshes
    }

    /**
     * Renders meshes whose material resolves to the model's normal buffer.
     * This is used by vanilla EntityModel/HumanoidModel call sites, which only
     * provide a VertexConsumer and therefore cannot select another texture.
     */
    fun renderDefaultMeshes(
        root: ModelPart,
        poseStack: PoseStack,
        buffer: com.mojang.blaze3d.vertex.VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        color: Int,
    ) {
        renderStoredMeshes(
            root = root,
            poseStack = poseStack,
            packedLight = packedLight,
            packedOverlay = packedOverlay,
            color = color,
            defaultBuffer = buffer,
            bufferSource = null,
            includeNonDefault = false,
        )
    }

    /**
     * Renders all stored meshes.  Named materials use an independent cutout
     * buffer while the default material uses the caller's already-selected
     * buffer (armor, emissive, energy swirl, etc.).
     */
    fun renderAllMeshes(
        root: ModelPart,
        poseStack: PoseStack,
        defaultBuffer: com.mojang.blaze3d.vertex.VertexConsumer,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
        color: Int,
    ) {
        renderStoredMeshes(
            root = root,
            poseStack = poseStack,
            packedLight = packedLight,
            packedOverlay = packedOverlay,
            color = color,
            defaultBuffer = defaultBuffer,
            bufferSource = bufferSource,
            includeNonDefault = true,
        )
    }

    fun submitStoredSideMeshes(
        root: ModelPart,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
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

    /**
     * Returns a root that is safe to pass to HumanoidModel.  A baked custom
     * layer is allowed to omit vanilla parts, but HumanoidModel eagerly reads
     * head, hat, and all limb parts from its constructor.  In the vanilla
     * humanoid layout, `hat` is a sibling of `head` at the root level (it is
     * not a child of `head`).  When a part is
     * missing, build a shallow safe root while retaining every existing
     * custom part (including body attachments and texture meshes).
     */
    fun ensureHumanoidRoot(root: ModelPart): ModelPart {
        val existingChildren = mutableChildren(root).toMutableMap()
        existingChildren.putIfAbsent("hat", emptyPart())

        listOf("body", "right_arm", "left_arm", "right_leg", "left_leg").forEach { name ->
            existingChildren.putIfAbsent(name, emptyPart())
        }

        val hasAllParts = root.hasChild("head") && root.hasChild("hat") &&
            listOf("body", "right_arm", "left_arm", "right_leg", "left_leg").all(root::hasChild)
        return if (hasAllParts) root else ModelPart(emptyList(), existingChildren)
    }

    /** Backward-compatible helper for existing callers that do not need the returned root. */
    fun ensureHumanoidParts(root: ModelPart): ModelPart = ensureHumanoidRoot(root)

    private fun emptyPart(): ModelPart = ModelPart(emptyList(), hashMapOf())

    private fun createPartPaths(root: ModelPart): Map<String, List<ModelPart>> {
        val pathsByName = HashMap<String, List<ModelPart>>()
        collectPartPaths(root, listOf(root), pathsByName)
        return pathsByName
    }

    private fun collectPartPaths(
        part: ModelPart,
        path: List<ModelPart>,
        pathsByName: MutableMap<String, List<ModelPart>>,
    ) {
        mutableChildren(part).forEach { (name, child) ->
            val childPath = path + child
            pathsByName[name] = childPath
            collectPartPaths(child, childPath, pathsByName)
        }
    }

    private fun renderStoredMeshes(
        root: ModelPart,
        poseStack: PoseStack,
        defaultBuffer: com.mojang.blaze3d.vertex.VertexConsumer,
        bufferSource: MultiBufferSource?,
        packedLight: Int,
        packedOverlay: Int,
        color: Int,
        includeNonDefault: Boolean,
    ) {
        storedMeshesByRoot[root].orEmpty().forEach { mesh ->
            if (!includeNonDefault && !mesh.usesDefaultBuffer) {
                return@forEach
            }
            if (mesh.parentPath.any { !it.visible }) {
                return@forEach
            }

            val meshBuffer = if (mesh.usesDefaultBuffer || bufferSource == null || mesh.texture == null) {
                defaultBuffer
            } else {
                bufferSource.getBuffer(RenderType.entityCutoutNoCull(mesh.texture))
            }

            poseStack.pushPose()
            mesh.parentPath.forEach { it.translateAndRotate(poseStack) }
            mesh.part.render(poseStack, meshBuffer, packedLight, packedOverlay, color)
            poseStack.popPose()
        }
    }

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
        val pose = PartPose.offsetAndRotation(
            position.x,
            position.y,
            position.z,
            textureMesh.rotation.x.toModelRadX(),
            textureMesh.rotation.y.toModelRadY(),
            textureMesh.rotation.z.toModelRadZ(),
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
            normalX = 0.0f,
            normalY = 1.0f,
            normalZ = 0.0f,
            vertices = arrayOf(
                vertex(xCoord(mask.width.toFloat()), yCoord(0.0f), zCoord(0.0f), 1.0f, 1.0f),
                vertex(xCoord(mask.width.toFloat()), yCoord(0.0f), zCoord(mask.height.toFloat()), 1.0f, 0.0f),
                vertex(xCoord(0.0f), yCoord(0.0f), zCoord(mask.height.toFloat()), 0.0f, 0.0f),
                vertex(xCoord(0.0f), yCoord(0.0f), zCoord(0.0f), 0.0f, 1.0f),
            ),
        )
        cubes += singlePolygonCube(
            normalX = 0.0f,
            normalY = -1.0f,
            normalZ = 0.0f,
            vertices = arrayOf(
                vertex(xCoord(mask.width.toFloat()), yCoord(-1.0f), zCoord(0.0f), 1.0f, 1.0f),
                vertex(xCoord(0.0f), yCoord(-1.0f), zCoord(0.0f), 0.0f, 1.0f),
                vertex(xCoord(0.0f), yCoord(-1.0f), zCoord(mask.height.toFloat()), 0.0f, 0.0f),
                vertex(xCoord(mask.width.toFloat()), yCoord(-1.0f), zCoord(mask.height.toFloat()), 1.0f, 0.0f),
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

        val baseVertices: Array<Any> = arrayOf(
            vertex(
                xCoord(sx),
                yCoord(0.0f),
                zCoord(sy),
                clampUv(uvEx / mask.width.toFloat()),
                clampUv(1.0f - (uvSy / mask.height.toFloat())),
            ),
            vertex(
                xCoord(sx),
                yCoord(-1.0f),
                zCoord(sy),
                clampUv(uvEx / mask.width.toFloat()),
                clampUv(1.0f - (uvEy / mask.height.toFloat())),
            ),
            vertex(
                xCoord(ex),
                yCoord(-1.0f),
                zCoord(ey),
                clampUv(uvSx / mask.width.toFloat()),
                clampUv(1.0f - (uvEy / mask.height.toFloat())),
            ),
            vertex(
                xCoord(ex),
                yCoord(0.0f),
                zCoord(ey),
                clampUv(uvSx / mask.width.toFloat()),
                clampUv(1.0f - (uvSy / mask.height.toFloat())),
            ),
        )

        val orderedVertices: Array<Any> = when {
            isVerticalBoundary && dir == 1 -> baseVertices
            isVerticalBoundary -> arrayOf(baseVertices[0], baseVertices[3], baseVertices[2], baseVertices[1])
            dir == -1 -> baseVertices
            else -> arrayOf(baseVertices[0], baseVertices[3], baseVertices[2], baseVertices[1])
        }

        return singlePolygonCube(normal.x, normal.y, normal.z, orderedVertices)
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

    @Suppress("UNCHECKED_CAST")
    private fun singlePolygonCube(
        normalX: Float,
        normalY: Float,
        normalZ: Float,
        vertices: Array<Any>,
    ): ModelPart.Cube {
        // Build a degenerate Cube whose NORTH polygon we overwrite via reflection,
        // since ModelPart.Polygon and ModelPart.Vertex are package-private in 1.21.1.
        // The NORTH face yields exactly one Polygon with 4 Vertex slots, matching our
        // caller-supplied vertices. We mutate the existing array in place because
        // Field.set rejects an Object[] on a Vertex[] field.
        val cube = ModelPart.Cube(
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
        )
        val polygons = polygonsField.get(cube) as Array<Any>
        val polygon = polygons[0]
        val existingVertices = polygonVerticesField.get(polygon) as Array<Any?>
        val copyCount = minOf(vertices.size, existingVertices.size)
        for (i in 0 until copyCount) {
            existingVertices[i] = vertices[i]
        }
        polygonNormalField.set(polygon, Vector3f(normalX, normalY, normalZ))
        return cube
    }

    private fun textureMask(textureId: ResourceLocation): TextureMask =
        textureMaskCache.computeIfAbsent(textureId) {
            openTexture(it)?.use(::readTextureMask) ?: TextureMask.full(64, 64)
        }

    private fun openTexture(textureId: ResourceLocation): InputStream? {
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
