package com.dec.decisland

import com.dec.decisland.block.category.SimplePlant
import com.dec.decisland.client.fog.VoidFogConfig
import com.dec.decisland.client.fog.VoidFogEvents
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.item.category.Crop
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.chunk.ChunkSectionLayer
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.common.NeoForge

@Mod(value = DecIsland.MOD_ID, dist = [Dist.CLIENT])
class DecIslandClient(container: ModContainer) {
    init {
        container.registerExtensionPoint(
            IConfigScreenFactory::class.java,
            IConfigScreenFactory { mod, parent -> ConfigurationScreen(mod, parent) },
        )
        NeoForge.EVENT_BUS.register(VoidFogEvents)
    }
}

@EventBusSubscriber(modid = DecIsland.MOD_ID, value = [Dist.CLIENT])
object DecIslandClientEvents {
    @SubscribeEvent
    @JvmStatic
    fun onClientSetup(event: FMLClientSetupEvent) {
        DecIsland.LOGGER.info("HELLO FROM CLIENT SETUP")
        DecIsland.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().user.name)
        event.enqueueWork {
            VoidFogConfig.load(Minecraft.getInstance().resourceManager)
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SNOW_PORTAL.get(), ChunkSectionLayer.TRANSLUCENT)
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.NIGHTMARE_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT)
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLOWER_GHOST_BLOCK.get(), ChunkSectionLayer.CUTOUT)
            SimplePlant.allBlocks().forEach { block ->
                ItemBlockRenderTypes.setRenderLayer(block, ChunkSectionLayer.CUTOUT)
            }
            Crop.allBlocks().forEach { block ->
                ItemBlockRenderTypes.setRenderLayer(block, ChunkSectionLayer.CUTOUT)
            }
            ItemBlockRenderTypes.setRenderLayer(Crop.CORN_CROP.get(), ChunkSectionLayer.CUTOUT)
        }
    }
}
