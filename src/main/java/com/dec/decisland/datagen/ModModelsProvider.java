package com.dec.decisland.datagen;

import com.dec.decisland.DecIsland;
import com.dec.decisland.block.ModBlocks;
import com.dec.decisland.block.BlockConfig;
import com.dec.decisland.item.ItemConfig;
import com.dec.decisland.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class ModModelsProvider extends ModelProvider {
    public ModModelsProvider(PackOutput output) {
        super(output, DecIsland.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // 注册所有物品的模型
        ModItems.ITEMS.getEntries().forEach(item -> {
            Item currentItem = item.get();
            if (currentItem instanceof BlockItem) return;
            ItemConfig config = ItemConfig.getConfig(currentItem);
            itemModels.generateFlatItem(item.get(), config.modelTemplate);
        });

        // 注册所有方块的模型
        ModBlocks.BLOCKS.getEntries().forEach(block -> {
            Block currentBlock = block.get();
            BlockConfig config = BlockConfig.getConfig(currentBlock);
            config.blockModelGenerator.accept(blockModels);
        });
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return ModItems.ITEMS.getEntries().stream();
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream();
    }
}
