package com.dec.decisland.datagen;

import com.dec.decisland.DecIsland;
import com.dec.decisland.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, DecIsland.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        ModBlocks.getBlockConfigs().forEach(config -> {
            if (config.tags == null) return;
            Block block = ModBlocks.getBlockByConfig(config).value();
            config.tags.forEach(block_tag ->{;
                tag(block_tag).add(block);
            });
        });
    }
}
