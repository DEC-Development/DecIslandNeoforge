package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.client.bedrock.model.BedrockAnimatedEntityModel
import com.dec.decisland.entity.custom.LeavesGolem
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.ResourceLocation

/**
 * 绿叶精华 Boss 渲染器：MobRenderer + 基岩版几何体模型。
 * 动画切换逻辑在 [LeavesGolemModel.setupAnim] 中按阶段处理。
 */
class LeavesGolemRenderer(context: EntityRendererProvider.Context) : MobRenderer<LeavesGolem, LeavesGolemModel>(
    context,
    LeavesGolemModel(),
    0.9f,
) {
    override fun render(
        entity: LeavesGolem,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
    ) {
        BedrockAnimatedEntityModel.withActiveBufferSource(bufferSource) {
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
        }
    }

    override fun getTextureLocation(entity: LeavesGolem): ResourceLocation = TEXTURE_LOCATION

    companion object {
        private val TEXTURE_LOCATION: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/leaves_golem.png")
    }
}
