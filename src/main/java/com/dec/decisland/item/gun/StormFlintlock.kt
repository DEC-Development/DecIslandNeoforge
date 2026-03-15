package com.dec.decisland.item.gun

import com.dec.decisland.entity.ModEntities
import net.minecraft.sounds.SoundEvents

class StormFlintlock(properties: Properties) : GunItem(
    properties,
    GunConfig(
        cooldownTicks = 60,
        baseDamage = 13.0f,
        knockback = 0.45,
        shots = listOf(
            GunShot(4.2f, 0.0f),
            GunShot(3.0f, 3.0f),
            GunShot(3.0f, 3.0f),
        ),
        bulletType = ModEntities.BULLET_BY_STORM_FLINTLOCK.get(),
        sneakMovementSpeedAddition = -0.06,
        shootSound = SoundEvents.GENERIC_EXPLODE.value(),
    ),
)
