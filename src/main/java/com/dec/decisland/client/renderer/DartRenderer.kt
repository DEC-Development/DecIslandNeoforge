package com.dec.decisland.client.renderer

import com.dec.decisland.client.renderer.state.DartRenderState
import com.dec.decisland.entity.projectile.dart.DartEntity
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.phys.Vec3

class DartRenderer(context: EntityRendererProvider.Context) :
    EntityRenderer<DartEntity, DartRenderState>(context) {
    private val itemModelResolver: ItemModelResolver = context.itemModelResolver

    init {
        shadowRadius = 0.0f
        shadowStrength = 0.0f
    }

    override fun createRenderState(): DartRenderState = DartRenderState()

    override fun extractRenderState(entity: DartEntity, reusedState: DartRenderState, partialTick: Float) {
        super.extractRenderState(entity, reusedState, partialTick)
        reusedState.xRot = Mth.rotLerp(partialTick, entity.xRotO, entity.xRot)
        reusedState.yRot = Mth.rotLerp(partialTick, entity.yRotO, entity.yRot)
        reusedState.spin = Mth.rotLerp(partialTick, entity.spinRotationO, entity.spinRotation)
        reusedState.randomTilt = entity.randomTilt
        itemModelResolver.updateForNonLiving(reusedState.item, entity.item, ItemDisplayContext.FIXED, entity)
    }

    override fun submit(
        renderState: DartRenderState,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        cameraRenderState: CameraRenderState,
    ) {
        poseStack.pushPose()
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0f))
        poseStack.mulPose(Axis.XP.rotationDegrees(renderState.randomTilt))
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot - renderState.spin))
        renderState.item.submit(
            poseStack,
            submitNodeCollector,
            renderState.lightCoords,
            OverlayTexture.NO_OVERLAY,
            renderState.outlineColor,
        )
        poseStack.popPose()
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState)
    }

    override fun getRenderOffset(renderState: DartRenderState): Vec3 = Vec3.ZERO
}
