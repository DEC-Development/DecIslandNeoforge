package com.dec.decisland.item.gun

import com.dec.decisland.entity.ModEntities
import net.minecraft.sounds.SoundEvents

class ShortFlintlock(properties: Properties) : GunItem(
    properties,
    GunConfig(
        cooldownTicks = 40,
        baseDamage = 11.0f,
        knockback = 0.4,
        shots = listOf(
            GunShot(4.8f, 10.0f),
            GunShot(4.8f, 10.0f),
        ),
        bulletType = ModEntities.BULLET_BY_SHORT_FLINTLOCK.get(),
        movementSpeedAddition = 0.01,
        shootSound = SoundEvents.GENERIC_EXPLODE.value(),
    ),
)
