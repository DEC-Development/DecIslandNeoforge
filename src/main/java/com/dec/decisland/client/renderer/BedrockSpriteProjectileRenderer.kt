package com.dec.decisland.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

/** Billboard renderer for Bedrock entities using the vanilla egg/snowball runtime. */
class BedrockSpriteProjectileRenderer<T : Entity>(
    context: EntityRendererProvider.Context,
    private val texture: ResourceLocation,
    private val scale: Float = 0.35f,
) : EntityRenderer<T>(context) {
    init {
        shadowRadius = 0.0f
        shadowStrength = 0.0f
    }

    override fun getTextureLocation(entity: T): ResourceLocation = texture

    override fun render(
        entity: T,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
    ) {
        poseStack.pushPose()
        poseStack.mulPose(entityRenderDispatcher.camera.rotation())
        poseStack.scale(scale, scale, scale)
        val consumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture))
        val matrix = poseStack.last().pose()
        val overlay = OverlayTexture.NO_OVERLAY
        val half = 0.5f
        vertex(consumer, matrix, -half, -half, 0.0f, 0.0f, 1.0f, packedLight, overlay)
        vertex(consumer, matrix, half, -half, 0.0f, 1.0f, 1.0f, packedLight, overlay)
        vertex(consumer, matrix, half, half, 0.0f, 1.0f, 0.0f, packedLight, overlay)
        vertex(consumer, matrix, -half, half, 0.0f, 0.0f, 0.0f, packedLight, overlay)
        poseStack.popPose()
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
    }

    override fun getBlockLightLevel(entity: T, pos: BlockPos): Int = 15

    private fun vertex(
        consumer: VertexConsumer,
        matrix: org.joml.Matrix4f,
        x: Float,
        y: Float,
        z: Float,
        u: Float,
        v: Float,
        light: Int,
        overlay: Int,
    ) {
        consumer.addVertex(matrix, x, y, z)
            .setColor(255, 255, 255, 255)
            .setUv(u, v)
            .setOverlay(overlay)
            .setUv2(light and 0xFFFF, light ushr 16)
            .setNormal(0.0f, 0.0f, 1.0f)
    }
}
