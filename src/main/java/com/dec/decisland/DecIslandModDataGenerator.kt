package com.dec.decisland

import com.dec.decisland.datagen.ModBlockLootTablesProvider
import com.dec.decisland.datagen.ModBlockTagsProvider
import com.dec.decisland.datagen.ModDataMapProvider
import com.dec.decisland.datagen.ModItemTagsProvider
import com.dec.decisland.datagen.ModLangProvider
import com.dec.decisland.datagen.ModModelsProvider
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = DecIsland.MOD_ID)
object DecIslandModDataGenerator {
    @SubscribeEvent
    @JvmStatic
    fun gatherData(event: GatherDataEvent.Client) {
        event.createProvider(::ModModelsProvider)
        event.createProvider(::ModDataMapProvider)

        arrayOf("en_us", "zh_cn").forEach { locale ->
            event.createProvider { output ->
                object : ModLangProvider(output, locale) {}
            }
        }

        event.createProvider { output, lookupProvider ->
            LootTableProvider(
                output,
                setOf(),
                listOf(LootTableProvider.SubProviderEntry(::ModBlockLootTablesProvider, LootContextParamSets.BLOCK)),
                lookupProvider,
            )
        }

        event.createProvider(::ModBlockTagsProvider)
        event.createProvider(::ModItemTagsProvider)
    }
}
