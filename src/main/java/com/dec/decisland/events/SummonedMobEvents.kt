package com.dec.decisland.events

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.custom.SummonedMobSupport
import net.minecraft.world.entity.Mob
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.EntityTickEvent

@EventBusSubscriber(modid = DecIsland.MOD_ID)
object SummonedMobEvents {
    @SubscribeEvent
    @JvmStatic
    fun onEntityTick(event: EntityTickEvent.Post) {
        val mob = event.entity as? Mob ?: return
        SummonedMobSupport.tick(mob)
    }
}
