package com.dec.decisland.effect

import com.dec.decisland.DecIsland
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModEffects {
    @JvmField
    val MOB_EFFECTS: DeferredRegister<MobEffect> =
        DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, DecIsland.MOD_ID)

    @JvmField
    val DIZZINESS: DeferredHolder<MobEffect, DizzinessEffect> =
        MOB_EFFECTS.register("dizziness", ::DizzinessEffect)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        MOB_EFFECTS.register(eventBus)
    }
}
