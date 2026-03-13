package com.dec.decisland.datagen;

import com.dec.decisland.block.ModBlocks;
import com.dec.decisland.block.BlockConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Set;
import java.util.function.Function;

public class ModBlockLootTablesProvider extends BlockLootSubProvider {
    public ModBlockLootTablesProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected void generate() {
        ModBlocks.BLOCKS.getEntries().forEach(block -> {
            BlockConfig config = BlockConfig.getConfig(block.value());
            if (config == null) return;
            if (config.blockLootTableGenerator == null) return;
            config.blockLootTableGenerator.accept(this);
        });
//        dropSelf(ModBlocks.ANCIENT_ICE.get());
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }

    @Override
    public void dropSelf(Block block) {
        super.dropSelf(block);
    }

    @Override
    public void add(Block block, Function<Block, LootTable.Builder> factory) {
        super.add(block, factory);
    }

    @Override
    public void add(Block block, LootTable.Builder builder) {
        super.add(block, builder);
    }

    // 后面有需要继续加

}
