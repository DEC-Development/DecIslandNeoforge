package com.dec.decisland.client.bedrock.model

import com.dec.decisland.particles.bedrock.Molang
import com.dec.decisland.particles.bedrock.MolangContext
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import kotlin.math.PI

open class BedrockAnimatedEntityModel<T : Entity> private constructor(
    private val bakedModel: BakedBedrockModel,
    var animation: BedrockAnimationClip,
    private var animations: List<BedrockAnimationClip>,
) : EntityModel<T>() {
    constructor(
        geometry: BedrockGeometry,
        animation: BedrockAnimationClip,
        defaultTexture: ResourceLocation? = null,
        materialTextures: Map<String, ResourceLocation> = emptyMap(),
    ) : this(
        bakeModel(geometry, defaultTexture, materialTextures),
        animation,
        listOf(animation),
    )

    constructor(
        geometry: BedrockGeometry,
        animations: List<BedrockAnimationClip>,
        defaultTexture: ResourceLocation? = null,
        materialTextures: Map<String, ResourceLocation> = emptyMap(),
    ) : this(
        bakeModel(geometry, defaultTexture, materialTextures),
        // A Bedrock client entity is allowed to have geometry without an
        // animation.  Keep the model API usable for those static entities by
        // retaining a no-op clip while leaving the animation list empty.
        animations.firstOrNull() ?: BedrockAnimationClip(
            name = "__static__",
            loop = true,
            lengthSeconds = 0.0f,
            boneAnimations = emptyMap(),
        ),
        animations,
    )

    override fun setupAnim(
        entity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
    ) {
        applyAnimation(ageInTicks / 20.0f)
    }

    fun applyClip(clip: BedrockAnimationClip, timeSeconds: Float) {
        animation = clip
        animations = listOf(clip)
        applyAnimation(timeSeconds)
    }

    fun applyClips(
        clips: List<BedrockAnimationClip>,
        timeSeconds: Float,
        variables: Map<String, Double> = emptyMap(),
    ) {
        if (clips.isEmpty()) {
            return
        }
        animation = clips.first()
        animations = clips
        applyAnimation(timeSeconds, variables)
    }

    override fun renderToBuffer(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        color: Int,
    ) {
        bakedModel.root.render(poseStack, buffer, packedLight, packedOverlay, color)
        val activeSource = activeBufferSource.get()
        if (activeSource == null) {
            BedrockTextureMeshSupport.renderDefaultMeshes(
                bakedModel.root,
                poseStack,
                buffer,
                packedLight,
                packedOverlay,
                color,
            )
        } else {
            BedrockTextureMeshSupport.renderAllMeshes(
                bakedModel.root,
                poseStack,
                buffer,
                activeSource,
                packedLight,
                packedOverlay,
                color,
            )
        }
    }

    /**
     * Renders the model and submits named texture meshes with their own
     * material buffers.  Vanilla EntityModel callers use renderToBuffer;
     * custom renderers that have a MultiBufferSource should use this overload
     * to render every material.
     */
    fun renderWithMaterials(
        poseStack: PoseStack,
        defaultBuffer: VertexConsumer,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
        color: Int,
    ) {
        bakedModel.root.render(poseStack, defaultBuffer, packedLight, packedOverlay, color)
        BedrockTextureMeshSupport.renderAllMeshes(
            bakedModel.root,
            poseStack,
            defaultBuffer,
            bufferSource,
            packedLight,
            packedOverlay,
            color,
        )
    }

    fun applyAnimation(timeSeconds: Float, variables: Map<String, Double> = emptyMap()) {
        bakedModel.resetPoses()
        val context = MolangContext(variables = variables)
        val evaluate: (String) -> Float? = { expression ->
            Molang.evalDouble(expression, context)?.toFloat()
        }

        bakedModel.animatedBones.forEach { (boneName, part) ->
            var sampledRotation = BedrockVec3.ZERO
            var sampledScale = BedrockVec3(1.0f, 1.0f, 1.0f)
            animations.forEach { clip ->
                val rotation = clip.sampleRotation(boneName, timeSeconds, evaluate)
                val scale = clip.sampleScale(boneName, timeSeconds, evaluate)
                sampledRotation = BedrockVec3(
                    sampledRotation.x + rotation.x,
                    sampledRotation.y + rotation.y,
                    sampledRotation.z + rotation.z,
                )
                sampledScale = BedrockVec3(
                    sampledScale.x * scale.x,
                    sampledScale.y * scale.y,
                    sampledScale.z * scale.z,
                )
            }
            part.xRot += sampledRotation.x.toModelRadX()
            part.yRot += sampledRotation.y.toModelRadY()
            part.zRot += sampledRotation.z.toModelRadZ()
            part.xScale *= sampledScale.x
            part.yScale *= sampledScale.y
            part.zScale *= sampledScale.z
        }
    }

    private data class PartPoseSnapshot(
        val x: Float,
        val y: Float,
        val z: Float,
        val xRot: Float,
        val yRot: Float,
        val zRot: Float,
        val xScale: Float,
        val yScale: Float,
        val zScale: Float,
    ) {
        fun applyTo(part: ModelPart) {
            part.x = x
            part.y = y
            part.z = z
            part.xRot = xRot
            part.yRot = yRot
            part.zRot = zRot
            part.xScale = xScale
            part.yScale = yScale
            part.zScale = zScale
        }
    }

    private data class NamedPartNode(
        val name: String,
        val pose: PartPoseSnapshot,
        val animatedBoneName: String?,
        val children: MutableList<NamedPartNode> = mutableListOf(),
    )

    private data class BakedBedrockModel(
        val root: ModelPart,
        val animatedBones: Map<String, ModelPart>,
        val snapshots: List<Pair<ModelPart, PartPoseSnapshot>>,
    ) {
        fun resetPoses() {
            snapshots.forEach { (part, snapshot) -> snapshot.applyTo(part) }
        }
    }

    companion object {
        private val activeBufferSource = ThreadLocal<MultiBufferSource?>()

        fun <R> withActiveBufferSource(bufferSource: MultiBufferSource, action: () -> R): R {
            val previous = activeBufferSource.get()
            activeBufferSource.set(bufferSource)
            return try {
                action()
            } finally {
                if (previous == null) {
                    activeBufferSource.remove()
                } else {
                    activeBufferSource.set(previous)
                }
            }
        }

        private fun bakeModel(
            geometry: BedrockGeometry,
            defaultTexture: ResourceLocation?,
            materialTextures: Map<String, ResourceLocation>,
        ): BakedBedrockModel {
            val meshDefinition = MeshDefinition()
            val rootDefinition = meshDefinition.root
            val rootNode = NamedPartNode(
                name = "__root__",
                pose = PartPoseSnapshot(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f),
                animatedBoneName = null,
            )

            geometry.rootBones.forEach { bone ->
                addBonePart(
                    rootDefinition,
                    rootNode,
                    geometry,
                    bone,
                    parentPivot = BedrockVec3.ZERO,
                    isRootBone = true,
                )
            }

            val bakedRoot = LayerDefinition.create(
                meshDefinition,
                geometry.textureWidth,
                geometry.textureHeight,
            ).bakeRoot()
            BedrockTextureMeshSupport.attachMeshesToNamedBones(
                root = bakedRoot,
                geometry = geometry,
                textureId = defaultTexture,
                materialTextures = materialTextures,
            )

            val animatedBones = LinkedHashMap<String, ModelPart>()
            val snapshots = ArrayList<Pair<ModelPart, PartPoseSnapshot>>()
            resolveParts(bakedRoot, rootNode, animatedBones, snapshots)
            return BakedBedrockModel(
                root = bakedRoot,
                animatedBones = animatedBones,
                snapshots = snapshots,
            )
        }

        private fun addBonePart(
            parentDefinition: PartDefinition,
            parentNode: NamedPartNode,
            geometry: BedrockGeometry,
            bone: BedrockBone,
            parentPivot: BedrockVec3,
            isRootBone: Boolean = false,
        ) {
            // 基岩几何 Y 轴向上、原点在脚底；Java 模型 Y 轴向下、脚底在 y=24：
            // 根骨骼锚定 24 − pivot.y，子骨骼用 父pivot − 子pivot；X 镜像、Z 不镜像
            val boneYOffset = if (isRootBone) 24f - bone.pivot.y else parentPivot.y - bone.pivot.y
            val bonePose = PartPoseSnapshot(
                x = parentPivot.x - bone.pivot.x,
                y = boneYOffset,
                z = bone.pivot.z - parentPivot.z,
                xRot = bone.rotation.x.toModelRadX(),
                yRot = bone.rotation.y.toModelRadY(),
                zRot = bone.rotation.z.toModelRadZ(),
                xScale = 1.0f,
                yScale = 1.0f,
                zScale = 1.0f,
            )

            val boneDefinition = parentDefinition.addOrReplaceChild(
                bone.name,
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(
                    bonePose.x,
                    bonePose.y,
                    bonePose.z,
                    bonePose.xRot,
                    bonePose.yRot,
                    bonePose.zRot,
                ),
            )
            val boneNode = NamedPartNode(
                name = bone.name,
                pose = bonePose,
                animatedBoneName = bone.name,
            )
            parentNode.children.add(boneNode)

            bone.cubes.forEachIndexed { index, cube ->
                val cubeName = "${bone.name}__cube_$index"
                val cubePose = PartPoseSnapshot(
                    x = bone.pivot.x - cube.pivot.x,
                    y = bone.pivot.y - cube.pivot.y,
                    z = cube.pivot.z - bone.pivot.z,
                    xRot = cube.rotation.x.toModelRadX(),
                    yRot = cube.rotation.y.toModelRadY(),
                    zRot = cube.rotation.z.toModelRadZ(),
                    xScale = 1.0f,
                    yScale = 1.0f,
                    zScale = 1.0f,
                )
                val cubeDefinition = CubeListBuilder.create().apply {
                    val uv = cube.uv
                    if (uv != null) {
                        texOffs(uv.first, uv.second)
                    }
                    mirror(cube.mirror)
                    addBox(
                        cube.pivot.x - cube.origin.x - cube.size.x,
                        cube.pivot.y - cube.origin.y - cube.size.y,
                        cube.origin.z - cube.pivot.z,
                        cube.size.x,
                        cube.size.y,
                        cube.size.z,
                        CubeDeformation(cube.inflate),
                    )
                }

                boneDefinition.addOrReplaceChild(
                    cubeName,
                    cubeDefinition,
                    PartPose.offsetAndRotation(
                        cubePose.x,
                        cubePose.y,
                        cubePose.z,
                        cubePose.xRot,
                        cubePose.yRot,
                        cubePose.zRot,
                    ),
                )
                boneNode.children.add(
                    NamedPartNode(
                        name = cubeName,
                        pose = cubePose,
                        animatedBoneName = null,
                    ),
                )
            }

            geometry.childBonesByParent[bone.name].orEmpty().forEach { childBone ->
                addBonePart(
                    parentDefinition = boneDefinition,
                    parentNode = boneNode,
                    geometry = geometry,
                    bone = childBone,
                    parentPivot = bone.pivot,
                )
            }
        }

        private fun resolveParts(
            rootPart: ModelPart,
            rootNode: NamedPartNode,
            animatedBones: MutableMap<String, ModelPart>,
            snapshots: MutableList<Pair<ModelPart, PartPoseSnapshot>>,
        ) {
            rootNode.children.forEach { childNode ->
                resolveChildPart(rootPart, childNode, animatedBones, snapshots)
            }
        }

        private fun resolveChildPart(
            parentPart: ModelPart,
            node: NamedPartNode,
            animatedBones: MutableMap<String, ModelPart>,
            snapshots: MutableList<Pair<ModelPart, PartPoseSnapshot>>,
        ) {
            val part = parentPart.getChild(node.name)
            snapshots += part to node.pose
            node.animatedBoneName?.let { animatedBones[it] = part }
            node.children.forEach { child -> resolveChildPart(part, child, animatedBones, snapshots) }
        }

        private fun Float.toRadians(): Float = (this * PI.toFloat()) / 180.0f

        private fun Float.toModelRadX(): Float = -toRadians()

        private fun Float.toModelRadY(): Float = -toRadians()

        private fun Float.toModelRadZ(): Float = toRadians()
    }
}
