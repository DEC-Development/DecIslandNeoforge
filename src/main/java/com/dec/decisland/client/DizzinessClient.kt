package com.dec.decisland.client

import com.dec.decisland.effect.ModEffects
import net.minecraft.client.Minecraft
import net.minecraft.util.Mth
import kotlin.math.pow

object DizzinessClient {
    private var lastProcessedTick = Int.MIN_VALUE
    private const val LEVEL_MULTIPLIER = 1.5f

    @JvmStatic
    fun tick(client: Minecraft) {
        val player = client.player ?: run {
            lastProcessedTick = Int.MIN_VALUE
            return
        }
        if (client.isPaused || player.isRemoved || !player.isAlive) {
            return
        }
        if (player.tickCount == lastProcessedTick) {
            return
        }
        lastProcessedTick = player.tickCount

        val effect = player.getEffect(ModEffects.DIZZINESS) ?: return
        val level = effect.amplifier + 1
        val blend = player.getEffectBlendFactor(ModEffects.DIZZINESS, 1.0f).coerceAtLeast(0.2f)
        val random = player.random
        val intensityScale = LEVEL_MULTIPLIER.pow(level - 1)

        val yawStep = (0.63f * intensityScale) * blend
        val pitchStep = (0.40f * intensityScale) * blend

        val yawDelta = randomSigned(random.nextFloat()) * yawStep
        val pitchDelta = randomSigned(random.nextFloat()) * pitchStep

        player.setYRot(player.yRot + yawDelta)
        player.setXRot(Mth.clamp(player.xRot + pitchDelta, -90.0f, 90.0f))

        val burstChance = (0.16f + (0.05f * intensityScale)).coerceAtMost(0.70f)
        if (random.nextFloat() < burstChance) {
            val burstYaw = randomSigned(random.nextFloat()) * yawStep * (1.2f + 0.4f * intensityScale)
            val burstPitch = randomSigned(random.nextFloat()) * pitchStep * (1.0f + 0.35f * intensityScale)
            player.setYRot(player.yRot + burstYaw)
            player.setXRot(Mth.clamp(player.xRot + burstPitch, -90.0f, 90.0f))
        }
    }

    private fun randomSigned(value: Float): Float = (value - 0.5f) * 2.0f
}
