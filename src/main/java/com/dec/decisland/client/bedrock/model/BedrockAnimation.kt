package com.dec.decisland.client.bedrock.model

import net.minecraft.util.Mth

data class BedrockKeyframe(
    val timeSeconds: Float,
    val value: BedrockVec3,
    val expressions: List<String?> = emptyList(),
)

data class BedrockBoneAnimation(
    val rotationFrames: List<BedrockKeyframe>,
    val scaleFrames: List<BedrockKeyframe>,
) {
    fun sampleRotation(timeSeconds: Float, evaluate: (String) -> Float? = { null }): BedrockVec3 {
        return sampleTrack(rotationFrames, timeSeconds, BedrockVec3.ZERO, evaluate)
    }

    fun sampleScale(timeSeconds: Float, evaluate: (String) -> Float? = { null }): BedrockVec3 {
        return sampleTrack(scaleFrames, timeSeconds, BedrockVec3(1.0f, 1.0f, 1.0f), evaluate)
    }

    private fun sampleTrack(
        frames: List<BedrockKeyframe>,
        timeSeconds: Float,
        defaultValue: BedrockVec3,
        evaluate: (String) -> Float?,
    ): BedrockVec3 {
        if (frames.isEmpty()) {
            return defaultValue
        }
        if (frames.size == 1) {
            return frames.first().resolve(evaluate)
        }

        if (timeSeconds <= frames.first().timeSeconds) {
            return frames.first().resolve(evaluate)
        }

        for (index in 0 until frames.lastIndex) {
            val start = frames[index]
            val end = frames[index + 1]
            if (timeSeconds <= end.timeSeconds) {
                val span = end.timeSeconds - start.timeSeconds
                if (span <= 1.0E-6f) {
                    return end.resolve(evaluate)
                }
                val delta = Mth.clamp((timeSeconds - start.timeSeconds) / span, 0.0f, 1.0f)
                return start.resolve(evaluate).lerp(end.resolve(evaluate), delta)
            }
        }

        return frames.last().resolve(evaluate)
    }

    private fun BedrockKeyframe.resolve(evaluate: (String) -> Float?): BedrockVec3 {
        fun component(index: Int, fallback: Float): Float =
            expressions.getOrNull(index)?.let(evaluate) ?: fallback

        return BedrockVec3(
            component(0, value.x),
            component(1, value.y),
            component(2, value.z),
        )
    }
}

data class BedrockAnimationClip(
    val name: String,
    val loop: Boolean,
    val lengthSeconds: Float,
    val boneAnimations: Map<String, BedrockBoneAnimation>,
) {
    fun sampleRotation(
        boneName: String,
        absoluteTimeSeconds: Float,
        evaluate: (String) -> Float? = { null },
    ): BedrockVec3 {
        val animation = boneAnimations[boneName] ?: return BedrockVec3.ZERO
        val sampledTime = wrapTime(absoluteTimeSeconds)
        return animation.sampleRotation(sampledTime, evaluate)
    }

    fun sampleScale(
        boneName: String,
        absoluteTimeSeconds: Float,
        evaluate: (String) -> Float? = { null },
    ): BedrockVec3 {
        val animation = boneAnimations[boneName] ?: return BedrockVec3(1.0f, 1.0f, 1.0f)
        val sampledTime = wrapTime(absoluteTimeSeconds)
        return animation.sampleScale(sampledTime, evaluate)
    }

    private fun wrapTime(timeSeconds: Float): Float {
        if (!loop || lengthSeconds <= 1.0E-6f) {
            return timeSeconds
        }

        val wrapped = timeSeconds % lengthSeconds
        return if (wrapped < 0.0f) wrapped + lengthSeconds else wrapped
    }
}
