package com.dec.decisland.client.renderer

import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.state.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class FixedItemProjectileRenderer<T : Entity>(
    context: EntityRendererProvider.Context,
    private val stackSupplier: () -> ItemStack,
    private val scale: Float = 1.0f,
) : EntityRenderer<T, FixedItemProjectileRenderer.FixedItemRenderState>(context) {
    private val itemModelResolver: ItemModelResolver = context.itemModelResolver

    init {
        shadowRadius = 0.0f
        shadowStrength = 0.0f
    }

    override fun createRenderState(): FixedItemRenderState = FixedItemRenderState()

    override fun extractRenderState(entity: T, reusedState: FixedItemRenderState, partialTick: Float) {
        super.extractRenderState(entity, reusedState, partialTick)
        itemModelResolver.updateForNonLiving(
            reusedState.item,
            stackSupplier(),
            ItemDisplayContext.FIXED,
            entity,
        )
    }

    override fun submit(
        renderState: FixedItemRenderState,
        poseStack: com.mojang.blaze3d.vertex.PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        cameraRenderState: CameraRenderState,
    ) {
        poseStack.pushPose()
        poseStack.scale(scale, scale, scale)
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

    class FixedItemRenderState : ThrownItemRenderState()
}
