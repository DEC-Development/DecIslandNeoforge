package com.dec.decisland

import com.dec.decisland.block.category.SimplePlant
import com.dec.decisland.client.fog.VoidFogConfig
import com.dec.decisland.client.fog.VoidFogEvents
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.item.category.Crop
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
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
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SNOW_PORTAL.get(), RenderType.translucent())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.NIGHTMARE_BLOCK.get(), RenderType.translucent())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLOWER_GHOST_BLOCK.get(), RenderType.cutout())
            SimplePlant.allBlocks().forEach { block ->
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout())
            }
            Crop.allBlocks().forEach { block ->
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout())
            }
            ItemBlockRenderTypes.setRenderLayer(Crop.CORN_CROP.get(), RenderType.cutout())

            // 基岩版移植方块渲染层（alpha_test → cutout，blend → translucent）
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ASH_CAGE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FROZEN_CAGE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RED_LANTERN.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ICE_BOOKSHELF.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LURK_BLOCK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LURK_END_STONE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RADIATE_STONE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RADIATE_DIRT.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RADIATE_STONEBRICK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RED_PATTERNED_STONEBRICK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BROKEN_DIRT.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLESH_BLOCK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SCALE_BLOCK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SWEET_BERRIES_BLOCK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SNOW_BRICK_BLOCK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FUSE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EYE_OF_NATURE_LOG.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LACE_BLOCK_BLACK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LACE_BLOCK_LIGHT_RED.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.THIN_ROPE_BLOCK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRATE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FROZEN_CRATE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CAVE_CRATE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOLDEN_FENCE_BLOCK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOLDEN_BOOKSHELF.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOLDEN_BOOKSHELF_FRAME.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.NUKE_BLOCK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.END_ALTAR.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LIGHTNING_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RAINING_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SUNNY_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PURPUR_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENCHANTED_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLAIN_TOWER_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ASH_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ABYSSAL_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BAT_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEEP_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FOREST_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SOUL_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EVERLASTING_WINTER_SUMMONER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MONITOR.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MONITOR_ACTIVATED.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ITEM_PICKER.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MINING_MACHINE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MINE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MAGIC_LETTER_BOX.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLOWING_BLOCK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHRISTMAS_GIFT_BLOCK.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOLDEN_CHAIN.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LAMPSHADE.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.STONE_HEAP.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.STONE_ROAD.get(), RenderType.cutout())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SMALL_STONE_BLOCK_ENTITY.get(), RenderType.cutout())

            // blend → translucent
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GHOST_ICE.get(), RenderType.translucent())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DIRT_GHOST_BLOCK.get(), RenderType.translucent())
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ICE_ROPE_BLOCK.get(), RenderType.translucent())
        }
    }
}
