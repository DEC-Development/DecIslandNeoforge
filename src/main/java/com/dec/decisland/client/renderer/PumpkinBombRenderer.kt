package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.client.bedrock.model.BedrockAnimatedEntityModel
import com.dec.decisland.client.bedrock.model.BedrockEntityAssets
import com.dec.decisland.entity.custom.PumpkinBombEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation

class PumpkinBombRenderer(context: EntityRendererProvider.Context) : EntityRenderer<PumpkinBombEntity>(context) {
    private val model = BedrockAnimatedEntityModel<PumpkinBombEntity>(
        BedrockEntityAssets.geometry(GEOMETRY_LOCATION),
        BedrockEntityAssets.animation(ANIMATION_LOCATION, EXPLODE_ANIMATION),
    )

    init {
        shadowRadius = 0.3f
    }

    override fun getTextureLocation(entity: PumpkinBombEntity): ResourceLocation = TEXTURE_LOCATION

    override fun render(
        entity: PumpkinBombEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
    ) {
        poseStack.pushPose()
        // 模型烘焙锚点为脚底（y=24），直接渲染的实体按缩放比例补偿对齐
        poseStack.translate(0.0, -1.5 * SCALE, 0.0)
        poseStack.scale(SCALE, SCALE, SCALE)
        model.applyAnimation((entity.tickCount + partialTick) / 20.0f)
        val vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE_LOCATION))
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1)
        poseStack.popPose()
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
    }

    override fun getBlockLightLevel(entity: PumpkinBombEntity, pos: BlockPos): Int = 15

    companion object {
        private const val SCALE: Float = 0.5f
        private const val EXPLODE_ANIMATION: String = "animation.pumpkin_bomb.explode"

        private val GEOMETRY_LOCATION: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/entity/block.geometry.json")
        private val ANIMATION_LOCATION: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/animations/entity/pumpkin_bomb.animation.json")
        private val TEXTURE_LOCATION: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/pumpkin_slime.png")
    }
}
