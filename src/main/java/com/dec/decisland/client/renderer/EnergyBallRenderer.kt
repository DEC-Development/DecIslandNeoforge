package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.projectile.EnergyBall
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.model.`object`.projectile.ShulkerBulletModel
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.state.ShulkerBulletRenderState
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.util.Mth

class EnergyBallRenderer(context: EntityRendererProvider.Context) :
    EntityRenderer<EnergyBall, ShulkerBulletRenderState>(context) {
    private val model: ShulkerBulletModel = ShulkerBulletModel(context.bakeLayer(ModelLayers.SHULKER_BULLET))

    init {
        shadowRadius = 0.0f
        shadowStrength = 0.0f
    }

    override fun createRenderState(): ShulkerBulletRenderState = ShulkerBulletRenderState()

    override fun extractRenderState(
        entity: EnergyBall,
        reusedState: ShulkerBulletRenderState,
        partialTick: Float,
    ) {
        super.extractRenderState(entity, reusedState, partialTick)
        reusedState.yRot = entity.getYRot(partialTick)
        reusedState.xRot = entity.getXRot(partialTick)
    }

    override fun submit(
        renderState: ShulkerBulletRenderState,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        cameraRenderState: CameraRenderState,
    ) {
        poseStack.pushPose()
        val age = renderState.ageInTicks.toDouble()
        poseStack.translate(0.0, 0.15, 0.0)
        poseStack.mulPose(Axis.YP.rotationDegrees((Mth.sin(age * 0.1) * 180.0).toFloat()))
        poseStack.mulPose(Axis.XP.rotationDegrees((Mth.cos(age * 0.1) * 180.0).toFloat()))
        poseStack.mulPose(Axis.ZP.rotationDegrees((Mth.sin(age * 0.15) * 360.0).toFloat()))
        poseStack.scale(-0.4f, -0.4f, 0.4f)
        submitNodeCollector.submitModel(
            model,
            renderState,
            poseStack,
            model.renderType(TEXTURE_LOCATION),
            renderState.lightCoords,
            OverlayTexture.NO_OVERLAY,
            renderState.outlineColor,
            null,
        )
        poseStack.scale(1.5f, 1.5f, 1.5f)
        submitNodeCollector.order(1).submitModel(
            model,
            renderState,
            poseStack,
            TRANSLUCENT_RENDER_TYPE,
            renderState.lightCoords,
            OverlayTexture.NO_OVERLAY,
            654311423,
            null,
            renderState.outlineColor,
            null,
        )
        poseStack.popPose()
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState)
    }

    override fun getBlockLightLevel(entity: EnergyBall, pos: BlockPos): Int = 15

    companion object {
        private val TEXTURE_LOCATION: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/energy_ball.png")
        private val TRANSLUCENT_RENDER_TYPE = RenderTypes.entityTranslucent(TEXTURE_LOCATION)
    }
}
