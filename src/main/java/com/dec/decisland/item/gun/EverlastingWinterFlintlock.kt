package com.dec.decisland.item.gun

import com.dec.decisland.entity.ModEntities
import net.minecraft.sounds.SoundEvents

class EverlastingWinterFlintlock(properties: Properties) : GunItem(
    properties,
    GunConfig(
        cooldownTicks = 60,
        baseDamage = 14.0f,
        knockback = 0.45,
        shots = listOf(
            GunShot(6.0f, 0.0f),
            GunShot(5.0f, 0.5f),
            GunShot(5.0f, 0.5f),
        ),
        bulletType = ModEntities.BULLET_BY_EVERLASTING_WINTER_FLINTLOCK.get(),
        sneakMovementSpeedAddition = -0.06,
        shootSound = SoundEvents.GENERIC_EXPLODE.value(),
    ),
)
