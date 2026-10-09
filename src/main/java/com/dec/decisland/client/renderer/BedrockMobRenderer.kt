package com.dec.decisland.client.renderer

import com.dec.decisland.client.bedrock.model.BedrockEntityAssets
import com.dec.decisland.client.bedrock.model.SimpleBedrockMobModel
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Mob

/**
 * 通用基岩版生物渲染器：烘焙 geometry + idle/walk 动画 + 自定义贴图，参数化缩放。
 */
open class BedrockMobRenderer<T : Mob>(
    context: EntityRendererProvider.Context,
    geometryLocation: ResourceLocation,
    animationLocation: ResourceLocation,
    idleName: String,
    walkName: String,
    private val textureLocation: ResourceLocation,
    private val scale: Float = 1.0f,
) : MobRenderer<T, SimpleBedrockMobModel<T>>(
    context,
    SimpleBedrockMobModel(
        BedrockEntityAssets.geometry(geometryLocation),
        BedrockEntityAssets.animation(animationLocation, idleName),
        BedrockEntityAssets.animation(animationLocation, walkName),
    ),
    0.45f * scale,
) {
    override fun getTextureLocation(entity: T): ResourceLocation = textureLocation

    override fun scale(entity: T, poseStack: PoseStack, partialTick: Float) {
        poseStack.scale(scale, scale, scale)
    }
}
