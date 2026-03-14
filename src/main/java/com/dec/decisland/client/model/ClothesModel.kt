package com.dec.decisland.client.model

import com.dec.decisland.DecIsland
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.resources.Identifier

class ClothesModel<T : HumanoidRenderState>(root: ModelPart) : HumanoidModel<T>(root) {
    override fun setupAnim(state: T) {
        head.xRot = 0.0f
        head.yRot = 0.0f
        head.zRot = 0.0f
        body.xRot = 0.0f
        body.yRot = 0.0f
        body.zRot = 0.0f
        rightArm.xRot = 0.0f
        rightArm.yRot = 0.0f
        rightArm.zRot = 0.0f
        leftArm.xRot = 0.0f
        leftArm.yRot = 0.0f
        leftArm.zRot = 0.0f
        rightLeg.xRot = 0.0f
        rightLeg.yRot = 0.0f
        rightLeg.zRot = 0.0f
        leftLeg.xRot = 0.0f
        leftLeg.yRot = 0.0f
        leftLeg.zRot = 0.0f
    }

    companion object {
        @JvmField
        val LAYER_LOCATION: ModelLayerLocation =
            ModelLayerLocation(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "clothes_model"), "main")

        @JvmStatic
        fun createBodyLayer(): LayerDefinition {
            val meshDefinition = MeshDefinition()
            val root = meshDefinition.root

            val head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                    .texOffs(0, 0)
                    .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f),
            )

            head.addOrReplaceChild(
                "hat",
                CubeListBuilder.create()
                    .texOffs(32, 0)
                    .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.5f)),
                PartPose.offset(0.0f, 0.0f, 0.0f),
            )

            root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                    .texOffs(16, 20)
                    .addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f),
            )

            root.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create()
                    .texOffs(40, 37)
                    .addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(-5.0f, 2.0f, 0.0f),
            )
            root.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                    .texOffs(32, 53)
                    .addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(5.0f, 2.0f, 0.0f),
            )

            root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                    .texOffs(0, 37)
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(-1.9f, 12.0f, 0.0f),
            )
            root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                    .texOffs(16, 53)
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(1.9f, 12.0f, 0.0f),
            )

            return LayerDefinition.create(meshDefinition, 64, 64)
        }
    }
}
