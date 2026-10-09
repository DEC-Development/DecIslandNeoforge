package com.dec.decisland.client.bedrock.model

import net.minecraft.world.entity.Entity

/**
 * 通用基岩版生物模型：按移动状态在 idle / walk 两个动画剪辑间切换。
 */
class SimpleBedrockMobModel<T : Entity>(
    geometry: BedrockGeometry,
    private val idleClip: BedrockAnimationClip,
    private val walkClip: BedrockAnimationClip,
) : BedrockAnimatedEntityModel<T>(geometry, idleClip) {

    override fun setupAnim(
        entity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
    ) {
        animation = if (limbSwingAmount > 0.03f) walkClip else idleClip
        applyAnimation(ageInTicks / 20.0f)
    }
}
