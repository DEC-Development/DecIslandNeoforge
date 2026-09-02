package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.client.bedrock.model.BedrockAnimatedEntityModel
import com.dec.decisland.client.bedrock.model.BedrockEntityAssets
import com.dec.decisland.entity.custom.PumpkinBombEntity
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier

class PumpkinBombRenderer(context: EntityRendererProvider.Context) :
    EntityRenderer<PumpkinBombEntity, BedrockAnimatedEntityModel.State>(context) {
    private val model = BedrockAnimatedEntityModel(
        BedrockEntityAssets.geometry(GEOMETRY_LOCATION),
        BedrockEntityAssets.animation(ANIMATION_LOCATION, EXPLODE_ANIMATION),
    )

    init {
        shadowRadius = 0.3f
    }

    override fun createRenderState(): BedrockAnimatedEntityModel.State = BedrockAnimatedEntityModel.State()

    override fun extractRenderState(
        entity: PumpkinBombEntity,
        reusedState: BedrockAnimatedEntityModel.State,
        partialTick: Float,
    ) {
        super.extractRenderState(entity, reusedState, partialTick)
        reusedState.animationTimeSeconds = (entity.tickCount + partialTick) / 20.0f
    }

    override fun submit(
        renderState: BedrockAnimatedEntityModel.State,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        cameraRenderState: CameraRenderState,
    ) {
        poseStack.pushPose()
        poseStack.scale(SCALE, SCALE, SCALE)
        submitNodeCollector.submitModel(
            model,
            renderState,
            poseStack,
            RenderTypes.entityCutout(TEXTURE_LOCATION),
            renderState.lightCoords,
            OverlayTexture.NO_OVERLAY,
            renderState.outlineColor,
            null,
        )
        poseStack.popPose()
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState)
    }

    override fun getBlockLightLevel(entity: PumpkinBombEntity, pos: BlockPos): Int = 15

    companion object {
        private const val SCALE: Float = 0.5f
        private const val EXPLODE_ANIMATION: String = "animation.pumpkin_bomb.explode"

        private val GEOMETRY_LOCATION: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/entity/block.geometry.json")
        private val ANIMATION_LOCATION: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/animations/entity/pumpkin_bomb.animation.json")
        private val TEXTURE_LOCATION: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/pumpkin_slime.png")
    }
}