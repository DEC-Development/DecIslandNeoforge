package com.dec.decisland.item.gun

import com.dec.decisland.entity.ModEntities
import net.minecraft.sounds.SoundEvents

class GhostFlintlock(properties: Properties) : GunItem(
    properties,
    GunConfig(
        cooldownTicks = 60,
        baseDamage = 17.0f,
        knockback = 0.5,
        shots = listOf(GunShot(7.2f, 0.0f)),
        bulletType = ModEntities.BULLET_BY_GHOST_FLINTLOCK.get(),
        sneakMovementSpeedAddition = -0.06,
        shootSound = SoundEvents.GENERIC_EXPLODE.value(),
    ),
)
