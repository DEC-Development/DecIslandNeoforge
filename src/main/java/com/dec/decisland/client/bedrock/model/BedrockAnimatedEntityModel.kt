package com.dec.decisland.client.bedrock.model

import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import net.minecraft.client.renderer.entity.state.EntityRenderState
import kotlin.math.PI

class BedrockAnimatedEntityModel private constructor(
    private val bakedModel: BakedBedrockModel,
    private val animation: BedrockAnimationClip,
) : EntityModel<BedrockAnimatedEntityModel.State>(bakedModel.root) {
    constructor(geometry: BedrockGeometry, animation: BedrockAnimationClip) : this(
        bakeModel(geometry),
        animation,
    )

    override fun setupAnim(state: State) {
        bakedModel.resetPoses()

        bakedModel.animatedBones.forEach { (boneName, part) ->
            val sampledRotation = animation.sampleRotation(boneName, state.animationTimeSeconds)
            val sampledScale = animation.sampleScale(boneName, state.animationTimeSeconds)
            part.xRot += sampledRotation.x.toModelRadX()
            part.yRot += sampledRotation.y.toModelRadY()
            part.zRot += sampledRotation.z.toModelRadZ()
            part.xScale *= sampledScale.x
            part.yScale *= sampledScale.y
            part.zScale *= sampledScale.z
        }
    }

    class State : EntityRenderState() {
        var animationTimeSeconds: Float = 0.0f
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
        private fun bakeModel(geometry: BedrockGeometry): BakedBedrockModel {
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
                )
            }

            val bakedRoot = LayerDefinition.create(
                meshDefinition,
                geometry.textureWidth,
                geometry.textureHeight,
            ).bakeRoot()

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
        ) {
            val bonePose = PartPoseSnapshot(
                x = parentPivot.x - bone.pivot.x,
                y = bone.pivot.y - parentPivot.y,
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
                    x = cube.pivot.x - bone.pivot.x,
                    y = cube.pivot.y - bone.pivot.y,
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
                        cube.origin.y - cube.pivot.y,
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
