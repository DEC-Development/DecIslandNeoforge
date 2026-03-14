package com.dec.decisland

import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory

@Mod(value = DecIsland.MOD_ID, dist = [Dist.CLIENT])
class DecIslandClient(container: ModContainer) {
    init {
        container.registerExtensionPoint(
            IConfigScreenFactory::class.java,
            IConfigScreenFactory { mod, parent -> ConfigurationScreen(mod, parent) },
        )
    }
}

@EventBusSubscriber(modid = DecIsland.MOD_ID, value = [Dist.CLIENT])
object DecIslandClientEvents {
    @SubscribeEvent
    @JvmStatic
    fun onClientSetup(event: FMLClientSetupEvent) {
        DecIsland.LOGGER.info("HELLO FROM CLIENT SETUP")
        DecIsland.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().user.name)
    }
}
