package com.dec.decisland.item.gun

import com.dec.decisland.entity.ModEntities
import net.minecraft.sounds.SoundEvents

class LavaFlintlock(properties: Properties) : GunItem(
    properties,
    GunConfig(
        cooldownTicks = 40,
        baseDamage = 10.0f,
        knockback = 0.45,
        shots = listOf(
            GunShot(4.8f, 6.0f),
            GunShot(4.8f, 6.0f),
            GunShot(4.8f, 6.0f),
            GunShot(4.8f, 6.0f),
        ),
        bulletType = ModEntities.BULLET_BY_LAVA_FLINTLOCK.get(),
        sneakMovementSpeedAddition = -0.06,
        shootSound = SoundEvents.GENERIC_EXPLODE.value(),
    ),
)
