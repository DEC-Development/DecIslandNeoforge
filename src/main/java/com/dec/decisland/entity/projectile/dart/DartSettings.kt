package com.dec.decisland.entity.projectile.dart

import net.minecraft.world.effect.MobEffectInstance

data class DartSettings(
    val baseDamage: Float,
    val gravity: Double = 0.022,
    val bedrockInertia: Double = 1.23,
    val waterInertia: Float = 0.8f,
    val applyKnockback: Boolean = true,
    val knockbackStrength: Double = 0.35,
    val igniteSecondsOnHit: Float = 0.0f,
    val effectOnHit: (() -> MobEffectInstance)? = null,
    val spinDegreesPerSpeed: Float = 45.0f,
    val randomTiltDegrees: Float = 10.0f,
    val entityWidth: Float = 0.25f,
    val entityHeight: Float = 0.25f,
    val clientTrackingRange: Int = 64,
    val updateInterval: Int = 1,
)
