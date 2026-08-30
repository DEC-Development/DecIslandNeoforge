package com.dec.decisland.client.model

import com.dec.decisland.DecIsland
import com.dec.decisland.client.bedrock.model.BedrockBone
import com.dec.decisland.client.bedrock.model.BedrockCube
import com.dec.decisland.client.bedrock.model.BedrockEntityAssets
import com.dec.decisland.client.bedrock.model.BedrockTextureMeshSupport
import com.dec.decisland.item.category.Fashion
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.model.geom.builders.PartDefinition
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import kotlin.math.PI

class FashionArmorModel<T : HumanoidRenderState>(root: ModelPart) : HumanoidModel<T>(root) {
    fun submitTextureMeshSides(
        poseStack: PoseStack,
        collector: SubmitNodeCollector,
        packedLight: Int,
        itemStack: ItemStack,
    ) {
        // Giant bat wings use polygon texture_mesh rendering now; the old generated-item side pass must stay disabled.
    }

    companion object {
        @JvmField
        val CLOTHES_LAYER_LOCATION: ModelLayerLocation =
            layerLocation("fashion_clothes")

        @JvmField
        val CLOTHES_WITH_HOOD_LAYER_LOCATION: ModelLayerLocation =
            layerLocation("fashion_clothes_with_hood")

        @JvmField
        val HAT_LAYER_LOCATION: ModelLayerLocation =
            layerLocation("fashion_hat")

        @JvmField
        val WITCH_HAT_LAYER_LOCATION: ModelLayerLocation =
            layerLocation("fashion_witch_hat")

        @JvmField
        val CHRISTMAS_CAP_LAYER_LOCATION: ModelLayerLocation =
            layerLocation("fashion_christmas_cap")

        @JvmField
        val WINGS_FROM_DEEP_LAYER_LOCATION: ModelLayerLocation =
            layerLocation("fashion_wings_from_deep")

        @JvmField
        val GIANT_BAT_WINGS_LAYER_LOCATION: ModelLayerLocation =
            layerLocation("fashion_giant_bat_wings")

        private val CLOTHES_GEOMETRY: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/armor/fashion_clothes.geometry.json")

        private val CLOTHES_WITH_HOOD_GEOMETRY: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/armor/fashion_clothes_with_hood.geometry.json")

        private val HAT_GEOMETRY: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/armor/hat.geometry.json")

        private val WITCH_HAT_GEOMETRY: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/armor/witch_hat.geometry.json")

        private val CHRISTMAS_CAP_GEOMETRY: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/armor/christmas_cap.geometry.json")

        private val WINGS_FROM_DEEP_GEOMETRY: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/armor/wings_from_deep.geometry.json")

        private val GIANT_BAT_WINGS_GEOMETRY: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/armor/giant_bat_wings.geometry.json")

        @JvmStatic
        fun createClothesBodyLayer(): LayerDefinition =
            createClothesBodyLayer(CLOTHES_GEOMETRY)

        @JvmStatic
        fun createClothesWithHoodBodyLayer(): LayerDefinition =
            createClothesBodyLayer(CLOTHES_WITH_HOOD_GEOMETRY)

        @JvmStatic
        fun createHatBodyLayer(): LayerDefinition =
            createHeadwearBodyLayer(HAT_GEOMETRY)

        @JvmStatic
        fun createWitchHatBodyLayer(): LayerDefinition =
            createHeadwearBodyLayer(WITCH_HAT_GEOMETRY)

        @JvmStatic
        fun createChristmasCapBodyLayer(): LayerDefinition =
            createHeadwearBodyLayer(CHRISTMAS_CAP_GEOMETRY)

        @JvmStatic
        fun createWingsFromDeepBodyLayer(): LayerDefinition =
            createBodyAttachmentLayer(WINGS_FROM_DEEP_GEOMETRY, "body", listOf("right", "left"))

        @JvmStatic
        fun createGiantBatWingsBodyLayer(): LayerDefinition =
            createAttachableBodyLayer(GIANT_BAT_WINGS_GEOMETRY, "body", listOf("right", "left"))

        @JvmStatic
        fun attachTextureMeshes(root: ModelPart, modelKind: Fashion.ModelKind) {
            val (geometryId, textureId) = when (modelKind) {
                Fashion.ModelKind.WINGS_FROM_DEEP -> WINGS_FROM_DEEP_GEOMETRY to
                    Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/armor/fashion/wings_from_deep.png")
                Fashion.ModelKind.GIANT_BAT_WINGS -> GIANT_BAT_WINGS_GEOMETRY to
                    Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/armor/fashion/giant_bat_wings.png")
                else -> return
            }

            BedrockTextureMeshSupport.attachMeshesToNamedBones(
                root,
                BedrockEntityAssets.geometry(geometryId),
                when (modelKind) {
                    Fashion.ModelKind.GIANT_BAT_WINGS -> BedrockTextureMeshSupport.TransformMode.ATTACHABLE_ARMOR
                    else -> BedrockTextureMeshSupport.TransformMode.HUMANOID_ARMOR
                },
                textureId,
                BedrockTextureMeshSupport.MeshRenderMode.POLYGON,
            )
        }

        private fun createClothesBodyLayer(geometryId: Identifier): LayerDefinition {
            val geometry = BedrockEntityAssets.geometry(geometryId)
            val meshDefinition = MeshDefinition()
            val root = meshDefinition.root

            val head = root.addOrReplaceChild(
                "head",
                geometry.bonesByName["head"]?.let(::cubeListFromBone) ?: CubeListBuilder.create(),
                geometry.bonesByName["head"]?.let(::poseFromAbsoluteBone) ?: PartPose.offset(0.0f, 0.0f, 0.0f),
            )
            head.addOrReplaceChild(
                "hat",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 0.0f, 0.0f),
            )
            root.addOrReplaceChild(
                "body",
                geometry.bonesByName["bodyArmor"]?.let(::cubeListFromBone) ?: CubeListBuilder.create(),
                geometry.bonesByName["bodyArmor"]?.let(::poseFromAbsoluteBone) ?: PartPose.offset(0.0f, 0.0f, 0.0f),
            )
            root.addOrReplaceChild(
                "right_arm",
                geometry.bonesByName["rightArm"]?.let(::cubeListFromBone) ?: CubeListBuilder.create(),
                geometry.bonesByName["rightArm"]?.let(::poseFromAbsoluteBone) ?: PartPose.offset(-5.0f, 2.0f, 0.0f),
            )
            root.addOrReplaceChild(
                "left_arm",
                geometry.bonesByName["leftArm"]?.let(::cubeListFromBone) ?: CubeListBuilder.create(),
                geometry.bonesByName["leftArm"]?.let(::poseFromAbsoluteBone) ?: PartPose.offset(5.0f, 2.0f, 0.0f),
            )
            root.addOrReplaceChild(
                "right_leg",
                geometry.bonesByName["rightLeg"]?.let(::cubeListFromBone) ?: CubeListBuilder.create(),
                geometry.bonesByName["rightLeg"]?.let(::poseFromAbsoluteBone) ?: PartPose.offset(-1.9f, 12.0f, 0.0f),
            )
            root.addOrReplaceChild(
                "left_leg",
                geometry.bonesByName["leftLeg"]?.let(::cubeListFromBone) ?: CubeListBuilder.create(),
                geometry.bonesByName["leftLeg"]?.let(::poseFromAbsoluteBone) ?: PartPose.offset(1.9f, 12.0f, 0.0f),
            )

            return LayerDefinition.create(meshDefinition, geometry.textureWidth, geometry.textureHeight)
        }

        private fun createHeadwearBodyLayer(geometryId: Identifier): LayerDefinition {
            val geometry = BedrockEntityAssets.geometry(geometryId)
            val meshDefinition = MeshDefinition()
            val root = meshDefinition.root

            val head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 0.0f, 0.0f),
            )
            head.addOrReplaceChild(
                "hat",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 0.0f, 0.0f),
            )
            geometry.rootBones.forEach { bone ->
                addHeadwearBone(
                    parent = head,
                    bone = bone,
                    geometry = geometry,
                    isRootBone = true,
                    parentPivot = null,
                )
            }

            root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))
            root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0f, 2.0f, 0.0f))
            root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0f, 2.0f, 0.0f))
            root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9f, 12.0f, 0.0f))
            root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9f, 12.0f, 0.0f))

            return LayerDefinition.create(meshDefinition, geometry.textureWidth, geometry.textureHeight)
        }

        private fun createBodyAttachmentLayer(
            geometryId: Identifier,
            bodyBoneName: String,
            extraBodyBoneNames: List<String>,
        ): LayerDefinition {
            val geometry = BedrockEntityAssets.geometry(geometryId)
            val meshDefinition = MeshDefinition()
            val root = meshDefinition.root

            val head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))
            head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val body = geometry.bonesByName[bodyBoneName]
            val bodyPart = root.addOrReplaceChild(
                "body",
                body?.let(::cubeListFromBone) ?: CubeListBuilder.create(),
                body?.let(::poseFromAbsoluteBone) ?: PartPose.offset(0.0f, 0.0f, 0.0f),
            )
            extraBodyBoneNames.forEach { name ->
                geometry.bonesByName[name]?.let { bone ->
                    bodyPart.addOrReplaceChild(
                        bone.name,
                        cubeListFromBone(bone),
                        poseRelativeToParent(body!!, bone),
                    )
                }
            }

            root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0f, 2.0f, 0.0f))
            root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0f, 2.0f, 0.0f))
            root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9f, 12.0f, 0.0f))
            root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9f, 12.0f, 0.0f))

            return LayerDefinition.create(meshDefinition, geometry.textureWidth, geometry.textureHeight)
        }

        private fun createEntityStyleBodyAttachmentLayer(
            geometryId: Identifier,
            bodyBoneName: String,
            extraBodyBoneNames: List<String>,
        ): LayerDefinition {
            val geometry = BedrockEntityAssets.geometry(geometryId)
            val meshDefinition = MeshDefinition()
            val root = meshDefinition.root

            val head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))
            head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val body = geometry.bonesByName[bodyBoneName]
            val bodyPart = root.addOrReplaceChild(
                "body",
                body?.let(::cubeListFromBone) ?: CubeListBuilder.create(),
                body?.let(::poseFromEntityRootBone) ?: PartPose.offset(0.0f, 0.0f, 0.0f),
            )
            extraBodyBoneNames.forEach { name ->
                geometry.bonesByName[name]?.let { bone ->
                    bodyPart.addOrReplaceChild(
                        bone.name,
                        cubeListFromBone(bone),
                        poseEntityRelativeToParent(body!!, bone),
                    )
                }
            }

            root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0f, 2.0f, 0.0f))
            root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0f, 2.0f, 0.0f))
            root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9f, 12.0f, 0.0f))
            root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9f, 12.0f, 0.0f))

            return LayerDefinition.create(meshDefinition, geometry.textureWidth, geometry.textureHeight)
        }

        private fun createAttachableBodyLayer(
            geometryId: Identifier,
            bodyBoneName: String,
            extraBodyBoneNames: List<String>,
        ): LayerDefinition {
            val geometry = BedrockEntityAssets.geometry(geometryId)
            val meshDefinition = MeshDefinition()
            val root = meshDefinition.root

            val head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))
            head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val body = geometry.bonesByName[bodyBoneName]
            val bodyPart = root.addOrReplaceChild(
                "body",
                body?.let(::cubeListFromBone) ?: CubeListBuilder.create(),
                PartPose.offset(0.0f, 0.0f, 0.0f),
            )
            extraBodyBoneNames.forEach { name ->
                geometry.bonesByName[name]?.let { bone ->
                    bodyPart.addOrReplaceChild(
                        bone.name,
                        cubeListFromBone(bone),
                        poseRelativeToParent(body!!, bone),
                    )
                }
            }

            root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0f, 2.0f, 0.0f))
            root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0f, 2.0f, 0.0f))
            root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9f, 12.0f, 0.0f))
            root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9f, 12.0f, 0.0f))

            return LayerDefinition.create(meshDefinition, geometry.textureWidth, geometry.textureHeight)
        }

        private fun addHeadwearBone(
            parent: PartDefinition,
            bone: BedrockBone,
            geometry: com.dec.decisland.client.bedrock.model.BedrockGeometry,
            isRootBone: Boolean,
            parentPivot: com.dec.decisland.client.bedrock.model.BedrockVec3?,
        ) {
            val pose = if (isRootBone) {
                PartPose.offsetAndRotation(
                    0.0f,
                    0.0f,
                    0.0f,
                    bone.rotation.x.toModelRadX(),
                    bone.rotation.y.toModelRadY(),
                    bone.rotation.z.toModelRadZ(),
                )
            } else {
                poseRelativeToParent(parentPivot ?: error("Missing parent pivot"), bone)
            }

            val current = parent.addOrReplaceChild(
                bone.name,
                cubeListFromBone(bone),
                pose,
            )

            geometry.childBonesByParent[bone.name].orEmpty().forEach { child ->
                addHeadwearBone(current, child, geometry, isRootBone = false, parentPivot = bone.pivot)
            }
        }

        private fun cubeListFromBone(bone: BedrockBone): CubeListBuilder {
            val builder = CubeListBuilder.create()
            bone.cubes.forEach { cube ->
                appendCube(builder, bone, cube)
            }
            return builder
        }

        private fun appendCube(builder: CubeListBuilder, bone: BedrockBone, cube: BedrockCube) {
            cube.uv?.let { builder.texOffs(it.first, it.second) }
            builder.mirror(cube.mirror)
            builder.addBox(
                cube.origin.x - bone.pivot.x,
                -(cube.origin.y - bone.pivot.y) - cube.size.y,
                cube.origin.z - bone.pivot.z,
                cube.size.x,
                cube.size.y,
                cube.size.z,
                CubeDeformation(cube.inflate),
            )
        }

        private fun poseFromAbsoluteBone(bone: BedrockBone): PartPose = PartPose.offsetAndRotation(
            bone.pivot.x,
            24.0f - bone.pivot.y,
            bone.pivot.z,
            bone.rotation.x.toModelRadX(),
            bone.rotation.y.toModelRadY(),
            bone.rotation.z.toModelRadZ(),
        )

        private fun poseFromEntityRootBone(bone: BedrockBone): PartPose = PartPose.offsetAndRotation(
            -bone.pivot.x,
            bone.pivot.y,
            bone.pivot.z,
            bone.rotation.x.toModelRadX(),
            bone.rotation.y.toModelRadY(),
            bone.rotation.z.toModelRadZ(),
        )

        private fun poseRelativeToParent(parentBone: BedrockBone, childBone: BedrockBone): PartPose =
            poseRelativeToParent(parentBone.pivot, childBone)

        private fun poseRelativeToParent(
            parentPivot: com.dec.decisland.client.bedrock.model.BedrockVec3,
            childBone: BedrockBone,
        ): PartPose = PartPose.offsetAndRotation(
            childBone.pivot.x - parentPivot.x,
            parentPivot.y - childBone.pivot.y,
            childBone.pivot.z - parentPivot.z,
            childBone.rotation.x.toModelRadX(),
            childBone.rotation.y.toModelRadY(),
            childBone.rotation.z.toModelRadZ(),
        )

        private fun poseEntityRelativeToParent(parentBone: BedrockBone, childBone: BedrockBone): PartPose =
            PartPose.offsetAndRotation(
                parentBone.pivot.x - childBone.pivot.x,
                childBone.pivot.y - parentBone.pivot.y,
                childBone.pivot.z - parentBone.pivot.z,
                childBone.rotation.x.toModelRadX(),
                childBone.rotation.y.toModelRadY(),
                childBone.rotation.z.toModelRadZ(),
            )

        private fun layerLocation(name: String): ModelLayerLocation =
            ModelLayerLocation(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, name), "main")

        private fun Float.toRadians(): Float = (this * PI.toFloat()) / 180.0f

        private fun Float.toModelRadX(): Float = -toRadians()

        private fun Float.toModelRadY(): Float = -toRadians()

        private fun Float.toModelRadZ(): Float = toRadians()
    }
}
