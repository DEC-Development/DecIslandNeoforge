package com.dec.decisland

import com.dec.decisland.attachment.ModAttachments
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.category.Mask
import com.dec.decisland.item.category.Material
import com.dec.decisland.item.category.Weapon
import com.dec.decisland.particles.ModParticles
import com.dec.decisland.worldgen.ModWorldgen
import com.mojang.logging.LogUtils
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.level.block.Blocks
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.server.ServerStartingEvent
import org.slf4j.Logger

@Mod(DecIsland.MOD_ID)
class DecIsland(modEventBus: IEventBus, modContainer: ModContainer) {
    init {
        try {
            modEventBus.addListener(this::commonSetup)

            Material.load()
            Weapon.load()
            Mask.load()
            ModItems.register(modEventBus)

            ModBlocks.register(modEventBus)
            ModEntities.registry(modEventBus)
            ModCreativeModeTabs.register(modEventBus)
            ModParticles.register(modEventBus)
            ModAttachments.register(modEventBus)
            ModWorldgen.register(modEventBus)

            NeoForge.EVENT_BUS.register(this)
            modEventBus.addListener(this::addCreative)
            modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC)
        } catch (t: Throwable) {
            System.err.println("========== ERROR DURING MOD CONSTRUCTION ==========")
            t.printStackTrace()
            throw t
        }
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info("HELLO FROM COMMON SETUP")

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT))
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt())
        Config.ITEM_STRINGS.get().forEach { item -> LOGGER.info("ITEM >> {}", item) }
    }

    private fun addCreative(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.AMETHYST_AXE)
        }
    }

    @SubscribeEvent
    fun onServerStarting(event: ServerStartingEvent) {
        LOGGER.info("HELLO from server starting")
    }

    companion object {
        const val MOD_ID: String = "decisland"

        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }
}
