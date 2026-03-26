package com.dec.decisland.client.renderer

import com.dec.decisland.client.bedrock.model.BedrockAnimatedEntityModel
import com.dec.decisland.client.bedrock.model.BedrockEntityAssets
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity

open class BedrockProjectileRenderer<T : Entity>(
    context: EntityRendererProvider.Context,
    geometryLocation: Identifier,
    animationLocation: Identifier,
    animationName: String,
    private val textureLocation: Identifier,
    private val scale: Float,
) : EntityRenderer<T, BedrockAnimatedEntityModel.State>(context) {
    private val model = BedrockAnimatedEntityModel(
        BedrockEntityAssets.geometry(geometryLocation),
        BedrockEntityAssets.animation(animationLocation, animationName),
    )

    init {
        shadowRadius = 0.0f
        shadowStrength = 0.0f
    }

    override fun createRenderState(): BedrockAnimatedEntityModel.State = BedrockAnimatedEntityModel.State()

    override fun extractRenderState(
        entity: T,
        reusedState: BedrockAnimatedEntityModel.State,
        partialTick: Float,
    ) {
        super.extractRenderState(entity, reusedState, partialTick)
        reusedState.animationTimeSeconds = reusedState.ageInTicks / 20.0f
    }

    override fun submit(
        renderState: BedrockAnimatedEntityModel.State,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        cameraRenderState: CameraRenderState,
    ) {
        poseStack.pushPose()
        poseStack.scale(scale, scale, scale)
        val swirlOffset = (renderState.ageInTicks * 0.01f) % 1.0f
        submitNodeCollector.submitModel(
            model,
            renderState,
            poseStack,
            RenderTypes.energySwirl(textureLocation, swirlOffset, swirlOffset),
            renderState.lightCoords,
            OverlayTexture.NO_OVERLAY,
            renderState.outlineColor,
            null,
        )
        poseStack.popPose()
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState)
    }

    override fun getBlockLightLevel(entity: T, pos: BlockPos): Int = 15
}
