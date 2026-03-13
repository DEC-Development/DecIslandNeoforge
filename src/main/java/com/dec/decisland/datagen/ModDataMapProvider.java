package com.dec.decisland.datagen;

import com.dec.decisland.item.ItemConfig;
import com.dec.decisland.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {

    /**
     * Create a new provider.
     *
     * @param packOutput     the output location
     * @param lookupProvider a {@linkplain CompletableFuture} supplying the registries
     */
    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    public void gather(HolderLookup.Provider provider) {
        ModItems.ITEMS.getEntries().forEach(item -> {
            ItemConfig config = ItemConfig.getConfig(item.get());
            if (config == null) return;
            if (config.customProp.compostableChance > 0) {
                this.builder(NeoForgeDataMaps.COMPOSTABLES)
                        .add(item.getId(), new Compostable(config.customProp.compostableChance), true);
            }
            if (config.customProp.burnTime > 0) {
                this.builder(NeoForgeDataMaps.FURNACE_FUELS)
                        .add(item.getId(), new FurnaceFuel(config.customProp.burnTime), true);
            }
        });
    }
}
