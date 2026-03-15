package com.dec.decisland.item.gun

import com.dec.decisland.entity.ModEntities
import net.minecraft.sounds.SoundEvents

class StarFlintlock(properties: Properties) : GunItem(
    properties,
    GunConfig(
        cooldownTicks = 30,
        baseDamage = 10.0f,
        knockback = 0.45,
        shots = listOf(
            GunShot(5.0f, 4.0f),
            GunShot(5.0f, 4.0f),
            GunShot(5.0f, 4.0f),
            GunShot(5.0f, 4.0f),
            GunShot(5.0f, 4.0f),
        ),
        bulletType = ModEntities.BULLET_BY_STAR_FLINTLOCK.get(),
        sneakMovementSpeedAddition = -0.06,
        shootSound = SoundEvents.GENERIC_EXPLODE.value(),
    ),
)
