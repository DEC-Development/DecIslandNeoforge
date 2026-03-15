package com.dec.decisland.client

import net.minecraft.client.Minecraft
import net.minecraft.util.Mth
import kotlin.math.abs

object RecoilClient {
    private var pitchOffset = 0.0f
    private var yawOffset = 0.0f
    private var lastPitchOffset = 0.0f
    private var lastYawOffset = 0.0f
    private var cameraBackOffset = 0.0f
    private var lastCameraBackOffset = 0.0f
    private var shakeStrength = 0.0f
    private var gunRaise = 0.0f
    private var lastGunRaise = 0.0f

    @JvmStatic
    fun kick(pitchUpDegrees: Float, yawDegrees: Float) {
        pitchOffset += pitchUpDegrees.coerceAtLeast(0.0f)
        yawOffset += yawDegrees
        cameraBackOffset += (pitchUpDegrees * 0.006f).coerceIn(0.02f, 0.09f)
        shakeStrength = maxOf(shakeStrength, (pitchUpDegrees * 0.18f).coerceIn(0.6f, 2.2f))
        gunRaise = maxOf(gunRaise, 1.0f)
    }

    @JvmStatic
    fun tick(client: Minecraft) {
        val player = client.player ?: return

        pitchOffset *= 0.82f
        yawOffset *= 0.82f
        cameraBackOffset *= 0.80f
        shakeStrength *= 0.86f
        gunRaise *= 0.78f

        val pitchDelta = pitchOffset - lastPitchOffset
        val yawDelta = yawOffset - lastYawOffset
        lastPitchOffset = pitchOffset
        lastYawOffset = yawOffset

        if (abs(pitchDelta) > 0.0001f || abs(yawDelta) > 0.0001f) {
            player.setXRot(Mth.clamp(player.xRot - pitchDelta, -90.0f, 90.0f))
            player.setYRot(player.yRot + yawDelta)
        }

        if (shakeStrength > 0.02f) {
            val random = player.random
            val jitterPitch = ((random.nextFloat() - 0.5f) * 2.0f) * (0.35f * shakeStrength)
            val jitterYaw = ((random.nextFloat() - 0.5f) * 2.0f) * (0.45f * shakeStrength)
            player.setXRot(Mth.clamp(player.xRot + jitterPitch, -90.0f, 90.0f))
            player.setYRot(player.yRot + jitterYaw)
        }
    }

    @JvmStatic
    fun getCameraBackOffset(partialTick: Float): Double {
        val current = cameraBackOffset.toDouble()
        val previous = lastCameraBackOffset.toDouble()
        lastCameraBackOffset = cameraBackOffset
        return Mth.lerp(partialTick.toDouble(), previous, current)
    }

    @JvmStatic
    fun getGunRaise(partialTick: Float): Float {
        val current = gunRaise
        val previous = lastGunRaise
        lastGunRaise = gunRaise
        return Mth.lerp(partialTick, previous, current)
    }
}
