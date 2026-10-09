package com.dec.decisland.client.renderer

import com.dec.decisland.client.bedrock.model.BedrockAnimatedEntityModel
import com.dec.decisland.client.bedrock.model.BedrockEntityAssets
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

open class BedrockProjectileRenderer<T : Entity>(
    context: EntityRendererProvider.Context,
    geometryLocation: ResourceLocation,
    animationLocation: ResourceLocation,
    animationName: String,
    private val textureLocation: ResourceLocation,
    private val scale: Float,
) : EntityRenderer<T>(context) {
    private val model = BedrockAnimatedEntityModel<T>(
        BedrockEntityAssets.geometry(geometryLocation),
        BedrockEntityAssets.animation(animationLocation, animationName),
    )

    init {
        shadowRadius = 0.0f
        shadowStrength = 0.0f
    }

    override fun getTextureLocation(entity: T): ResourceLocation = textureLocation

    override fun render(
        entity: T,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
    ) {
        poseStack.pushPose()
        // 模型烘焙锚点为脚底（y=24），直接渲染的实体按缩放比例补偿对齐
        poseStack.translate(0.0, -1.5 * scale, 0.0)
        poseStack.scale(scale, scale, scale)
        model.applyAnimation((entity.tickCount + partialTick) / 20.0f)
        val swirlOffset = ((entity.tickCount + partialTick) * 0.01f) % 1.0f
        val vertexConsumer = bufferSource.getBuffer(RenderType.energySwirl(textureLocation, swirlOffset, swirlOffset))
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1)
        poseStack.popPose()
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
    }

    override fun getBlockLightLevel(entity: T, pos: BlockPos): Int = 15
}
