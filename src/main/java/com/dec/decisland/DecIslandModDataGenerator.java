package com.dec.decisland;

import com.dec.decisland.datagen.*;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = DecIsland.MOD_ID)
public class DecIslandModDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(ModModelsProvider::new);
        event.createProvider(ModDataMapProvider::new);

        String[] locales = {"en_us", "zh_cn"};
        for (String locale : locales) {
            event.createProvider(output -> new ModLangProvider(output, locale) {
            });
        }

        event.createProvider(((output, lookupProvider) ->
                new LootTableProvider(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(
                        ModBlockLootTablesProvider::new, LootContextParamSets.BLOCK
                )), lookupProvider)));

        event.createProvider((ModBlockTagsProvider::new));
        event.createProvider((ModItemTagsProvider::new));
    }
}
