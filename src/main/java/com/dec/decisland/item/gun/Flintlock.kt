package com.dec.decisland.item.gun

import com.dec.decisland.entity.ModEntities
import net.minecraft.sounds.SoundEvents

class Flintlock(properties: Properties) : GunItem(
    properties,
    GunConfig(
        cooldownTicks = 80,
        baseDamage = 11.0f,
        knockback = 0.45,
        shots = listOf(
            GunShot(5.0f, 0.1f),
            GunShot(4.3f, 3.0f),
            GunShot(4.3f, 3.0f),
        ),
        bulletType = ModEntities.BULLET_BY_FLINTLOCK.get(),
        shootSound = SoundEvents.GENERIC_EXPLODE.value(),
    ),
)
