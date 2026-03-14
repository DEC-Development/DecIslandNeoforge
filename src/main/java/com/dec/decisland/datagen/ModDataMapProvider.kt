package com.dec.decisland.datagen

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import java.util.concurrent.CompletableFuture

class ModDataMapProvider(
    packOutput: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : DataMapProvider(packOutput, lookupProvider) {
    override fun gather(provider: HolderLookup.Provider) {
        ModItems.ITEMS.getEntries().forEach { item ->
            val config: ItemConfig = ItemConfig.getConfig(item.get()) ?: return@forEach
            if (config.customProp.compostableChance > 0.0f) {
                builder(NeoForgeDataMaps.COMPOSTABLES)
                    .add(item.id, Compostable(config.customProp.compostableChance), true)
            }
            if (config.customProp.burnTime > 0) {
                builder(NeoForgeDataMaps.FURNACE_FUELS)
                    .add(item.id, FurnaceFuel(config.customProp.burnTime), true)
            }
        }
    }
}
