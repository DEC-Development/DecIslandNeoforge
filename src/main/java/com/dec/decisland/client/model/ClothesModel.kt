package com.dec.decisland.client.model

import com.dec.decisland.DecIsland
import com.dec.decisland.client.bedrock.model.BedrockBone
import com.dec.decisland.client.bedrock.model.BedrockCube
import com.dec.decisland.client.bedrock.model.BedrockEntityAssets
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.resources.Identifier
import kotlin.math.PI

class ClothesModel<T : HumanoidRenderState>(root: ModelPart) : HumanoidModel<T>(root) {
    override fun setupAnim(state: T) {
        super.setupAnim(state)
    }

    companion object {
        @JvmField
        val LAYER_LOCATION: ModelLayerLocation =
            ModelLayerLocation(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "clothes_model"), "main")

        private val GEOMETRY_LOCATION: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/armor/clothes.geometry.json")

        @JvmStatic
        fun createBodyLayer(): LayerDefinition {
            val geometry = BedrockEntityAssets.geometry(GEOMETRY_LOCATION)
            val meshDefinition = MeshDefinition()
            val root = meshDefinition.root

            val head = root.addOrReplaceChild(
                "head",
                cubeListFromBone(geometry.bonesByName.getValue("armorHead")),
                poseFromBone(geometry.bonesByName.getValue("armorHead")),
            )
            head.addOrReplaceChild(
                "hat",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 0.0f, 0.0f),
            )

            root.addOrReplaceChild(
                "body",
                cubeListFromBone(geometry.bonesByName.getValue("armorBody")),
                poseFromBone(geometry.bonesByName.getValue("armorBody")),
            )
            root.addOrReplaceChild(
                "right_arm",
                cubeListFromBone(geometry.bonesByName.getValue("armorRightArm")),
                poseFromBone(geometry.bonesByName.getValue("armorRightArm")),
            )
            root.addOrReplaceChild(
                "left_arm",
                cubeListFromBone(geometry.bonesByName.getValue("armorLeftArm")),
                poseFromBone(geometry.bonesByName.getValue("armorLeftArm")),
            )
            root.addOrReplaceChild(
                "right_leg",
                cubeListFromBone(geometry.bonesByName.getValue("armorRightLeg")),
                poseFromBone(geometry.bonesByName.getValue("armorRightLeg")),
            )
            root.addOrReplaceChild(
                "left_leg",
                cubeListFromBone(geometry.bonesByName.getValue("armorLeftLeg")),
                poseFromBone(geometry.bonesByName.getValue("armorLeftLeg")),
            )

            return LayerDefinition.create(meshDefinition, geometry.textureWidth, geometry.textureHeight)
        }

        private fun cubeListFromBone(bone: BedrockBone): CubeListBuilder {
            val builder = CubeListBuilder.create()
            bone.cubes.forEach { cube ->
                appendCube(builder, bone, cube)
            }
            return builder
        }

        private fun appendCube(builder: CubeListBuilder, bone: BedrockBone, cube: BedrockCube) {
            val uv = cube.uv
            if (uv != null) {
                builder.texOffs(uv.first, uv.second)
            }
            builder.mirror(cube.mirror)
            builder.addBox(
                cube.origin.x - bone.pivot.x,
                -(cube.origin.y - bone.pivot.y) - cube.size.y,
                cube.origin.z - bone.pivot.z,
                cube.size.x,
                cube.size.y,
                cube.size.z,
                net.minecraft.client.model.geom.builders.CubeDeformation(cube.inflate),
            )
        }

        private fun poseFromBone(bone: BedrockBone): PartPose = PartPose.offsetAndRotation(
            bone.pivot.x,
            24.0f - bone.pivot.y,
            bone.pivot.z,
            bone.rotation.x.toModelRadX(),
            bone.rotation.y.toModelRadY(),
            bone.rotation.z.toModelRadZ(),
        )

        private fun Float.toRadians(): Float = (this * PI.toFloat()) / 180.0f

        private fun Float.toModelRadX(): Float = -toRadians()

        private fun Float.toModelRadY(): Float = -toRadians()

        private fun Float.toModelRadZ(): Float = toRadians()
    }
}
