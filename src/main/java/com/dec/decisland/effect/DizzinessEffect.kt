package com.dec.decisland.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory

class DizzinessEffect : MobEffect(MobEffectCategory.HARMFUL, 0x8F9AA3) {
    init {
        setBlendDuration(10, 10, 10)
    }
}
