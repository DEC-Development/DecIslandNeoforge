package com.dec.decisland.client.renderer

import com.dec.decisland.client.bedrock.model.BedrockAnimationClip
import com.dec.decisland.client.bedrock.model.BedrockAnimatedEntityModel
import com.dec.decisland.client.bedrock.model.BedrockEntityAssets
import com.dec.decisland.entity.custom.LeavesGolem
import net.minecraft.resources.ResourceLocation

/**
 * 绿叶精华 Boss 模型：基岩版几何体 + 4 个动画剪辑（move/attack/roar/spawn）。
 *
 * setupAnim 按战斗阶段切换剪辑：
 * - SPAWN → spawn（破土动画，从阶段开始计时）
 * - ROAR  → roar（咆哮动画，从阶段开始计时）
 * - MELEE → 命中后 0.6 秒内播放 attack，移动时播放 move（按 limbSwing 驱动），
 *           静止时保持绑定姿态（不甩胳膊）
 */
class LeavesGolemModel : BedrockAnimatedEntityModel<LeavesGolem>(
    BedrockEntityAssets.geometry(GEOMETRY_LOCATION),
    BedrockEntityAssets.animation(ANIMATION_LOCATION, MOVE_CLIP),
) {
    private val moveClip = BedrockEntityAssets.animation(ANIMATION_LOCATION, MOVE_CLIP)
    private val attackClip = BedrockEntityAssets.animation(ANIMATION_LOCATION, ATTACK_CLIP)
    private val roarClip = BedrockEntityAssets.animation(ANIMATION_LOCATION, ROAR_CLIP)
    private val spawnClip = BedrockEntityAssets.animation(ANIMATION_LOCATION, SPAWN_CLIP)

    override fun setupAnim(
        entity: LeavesGolem,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
    ) {
        when (entity.phase) {
            LeavesGolem.Phase.SPAWN ->
                applyClip(spawnClip, (ageInTicks - entity.stateStartTick) / 20.0f)
            LeavesGolem.Phase.ROAR ->
                applyClip(roarClip, (ageInTicks - entity.stateStartTick) / 20.0f)
            LeavesGolem.Phase.MELEE -> {
                val sinceAttack = ageInTicks - entity.attackStartTick
                if (entity.attackStartTick > 0 && sinceAttack < ATTACK_WINDOW_TICKS) {
                    applyClip(attackClip, sinceAttack / 20.0f)
                } else if (limbSwingAmount > 0.01f) {
                    applyClip(moveClip, limbSwing * LIMB_SWING_SCALE)
                } else {
                    applyClip(idleClip, 0.0f)
                }
            }
        }
    }

    companion object {
        private const val MOVE_CLIP = "animation.leaves_golem.move"
        private const val ATTACK_CLIP = "animation.leaves_golem.attack"
        private const val ROAR_CLIP = "animation.leaves_golem.roar"
        private const val SPAWN_CLIP = "animation.leaves_golem.spawn"

        private const val ATTACK_WINDOW_TICKS = 12.0f   // 0.6 秒
        private const val LIMB_SWING_SCALE = 0.6662f    // 与原版四肢摆动频率一致

        /** 空剪辑 = 绑定姿态（站立不动） */
        private val idleClip = BedrockAnimationClip(
            name = "idle",
            loop = false,
            lengthSeconds = 0.0f,
            boneAnimations = emptyMap(),
        )

        private val GEOMETRY_LOCATION: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(
                com.dec.decisland.DecIsland.MOD_ID,
                "bedrock/models/entity/leaves_golem.geometry.json",
            )
        private val ANIMATION_LOCATION: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(
                com.dec.decisland.DecIsland.MOD_ID,
                "bedrock/animations/entity/leaves_golem.animation.json",
            )
    }
}
