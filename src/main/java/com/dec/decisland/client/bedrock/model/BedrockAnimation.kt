package com.dec.decisland.client.bedrock.model

import net.minecraft.util.Mth

data class BedrockKeyframe(
    val timeSeconds: Float,
    val value: BedrockVec3,
)

data class BedrockBoneAnimation(
    val rotationFrames: List<BedrockKeyframe>,
    val scaleFrames: List<BedrockKeyframe>,
) {
    fun sampleRotation(timeSeconds: Float): BedrockVec3 {
        return sampleTrack(rotationFrames, timeSeconds, BedrockVec3.ZERO)
    }

    fun sampleScale(timeSeconds: Float): BedrockVec3 {
        return sampleTrack(scaleFrames, timeSeconds, BedrockVec3(1.0f, 1.0f, 1.0f))
    }

    private fun sampleTrack(
        frames: List<BedrockKeyframe>,
        timeSeconds: Float,
        defaultValue: BedrockVec3,
    ): BedrockVec3 {
        if (frames.isEmpty()) {
            return defaultValue
        }
        if (frames.size == 1) {
            return frames.first().value
        }

        if (timeSeconds <= frames.first().timeSeconds) {
            return frames.first().value
        }

        for (index in 0 until frames.lastIndex) {
            val start = frames[index]
            val end = frames[index + 1]
            if (timeSeconds <= end.timeSeconds) {
                val span = end.timeSeconds - start.timeSeconds
                if (span <= 1.0E-6f) {
                    return end.value
                }
                val delta = Mth.clamp((timeSeconds - start.timeSeconds) / span, 0.0f, 1.0f)
                return start.value.lerp(end.value, delta)
            }
        }

        return frames.last().value
    }
}

data class BedrockAnimationClip(
    val name: String,
    val loop: Boolean,
    val lengthSeconds: Float,
    val boneAnimations: Map<String, BedrockBoneAnimation>,
) {
    fun sampleRotation(boneName: String, absoluteTimeSeconds: Float): BedrockVec3 {
        val animation = boneAnimations[boneName] ?: return BedrockVec3.ZERO
        val sampledTime = wrapTime(absoluteTimeSeconds)
        return animation.sampleRotation(sampledTime)
    }

    fun sampleScale(boneName: String, absoluteTimeSeconds: Float): BedrockVec3 {
        val animation = boneAnimations[boneName] ?: return BedrockVec3(1.0f, 1.0f, 1.0f)
        val sampledTime = wrapTime(absoluteTimeSeconds)
        return animation.sampleScale(sampledTime)
    }

    private fun wrapTime(timeSeconds: Float): Float {
        if (!loop || lengthSeconds <= 1.0E-6f) {
            return timeSeconds
        }

        val wrapped = timeSeconds % lengthSeconds
        return if (wrapped < 0.0f) wrapped + lengthSeconds else wrapped
    }
}
