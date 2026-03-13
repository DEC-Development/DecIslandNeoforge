package com.dec.decisland.client.model;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.dec.decisland.DecIsland;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

public class ClothesModel<T extends HumanoidRenderState> extends HumanoidModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "clothes_model"), "main");

	public ClothesModel(ModelPart root) {
        super(root);
	}

	@Override
	public void setupAnim(T state) {
		// 重置所有部分的变换
		this.head.xRot = 0;
		this.head.yRot = 0;
		this.head.zRot = 0;
		this.body.xRot = 0;
		this.body.yRot = 0;
		this.body.zRot = 0;
		this.rightArm.xRot = 0;
		this.rightArm.yRot = 0;
		this.rightArm.zRot = 0;
		this.leftArm.xRot = 0;
		this.leftArm.yRot = 0;
		this.leftArm.zRot = 0;
		this.rightLeg.xRot = 0;
		this.rightLeg.yRot = 0;
		this.rightLeg.zRot = 0;
		this.leftLeg.xRot = 0;
		this.leftLeg.yRot = 0;
		this.leftLeg.zRot = 0;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		// 添加head部分
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
			.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, 0.0F, 0.0F));

		// 添加hat部分作为head的子部分
		head.addOrReplaceChild("hat", CubeListBuilder.create()
			.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
			PartPose.offset(0.0F, 0.0F, 0.0F));

		// 添加body部分
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
			.texOffs(16, 20).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, 0.0F, 0.0F));

		// 添加手臂部分
		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
			.texOffs(40, 37).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
			PartPose.offset(-5.0F, 2.0F, 0.0F));
		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
			.texOffs(32, 53).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
			PartPose.offset(5.0F, 2.0F, 0.0F));

		// 添加腿部部分
		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
			.texOffs(0, 37).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
			PartPose.offset(-1.9F, 12.0F, 0.0F));
		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
			.texOffs(16, 53).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)),
			PartPose.offset(1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}