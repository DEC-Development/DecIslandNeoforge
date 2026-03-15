package com.dec.decisland.item.gun

import com.dec.decisland.entity.ModEntities
import net.minecraft.sounds.SoundEvents

class FlintlockPro(properties: Properties) : GunItem(
    properties,
    GunConfig(
        cooldownTicks = 80,
        baseDamage = 13.0f,
        knockback = 0.45,
        shots = listOf(
            GunShot(5.4f, 0.0f),
            GunShot(4.8f, 3.0f),
            GunShot(4.8f, 3.0f),
        ),
        bulletType = ModEntities.BULLET_BY_FLINTLOCK_PRO.get(),
        bulletBounces = 1,
        sneakMovementSpeedAddition = -0.06,
        shootSound = SoundEvents.GENERIC_EXPLODE.value(),
    ),
)
