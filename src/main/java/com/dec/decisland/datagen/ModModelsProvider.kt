package com.dec.decisland.datagen

import com.dec.decisland.DecIsland
import com.dec.decisland.block.BlockConfig
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.core.Holder
import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.stream.Stream

class ModModelsProvider(output: PackOutput) : ModelProvider(output, DecIsland.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        ModItems.ITEMS.getEntries().forEach { item ->
            val currentItem: Item = item.get()
            if (currentItem is BlockItem) return@forEach
            val config: ItemConfig = ItemConfig.getConfig(currentItem) ?: return@forEach
            itemModels.generateFlatItem(currentItem, config.modelTemplate)
        }

        ModBlocks.BLOCKS.getEntries().forEach { block ->
            val currentBlock: Block = block.get()
            val config: BlockConfig = BlockConfig.getConfig(currentBlock) ?: return@forEach
            config.blockModelGenerator.accept(blockModels)
        }
    }

    override fun getKnownItems(): Stream<out Holder<Item>> = ModItems.ITEMS.getEntries().stream()

    override fun getKnownBlocks(): Stream<out Holder<Block>> = ModBlocks.BLOCKS.getEntries().stream()
}
