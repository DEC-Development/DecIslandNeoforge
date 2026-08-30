package com.dec.decisland.datagen

import com.dec.decisland.block.BlockConfig
import com.dec.decisland.block.ModBlocks
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import java.util.function.Function

class ModBlockLootTablesProvider(registries: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(), FeatureFlags.DEFAULT_FLAGS, registries) {
    override fun generate() {
        ModBlocks.BLOCKS.getEntries().forEach { block ->
            val config: BlockConfig = BlockConfig.getConfig(block.value()) ?: return@forEach
            config.blockLootTableGenerator.accept(this)
        }
    }

    override fun getKnownBlocks(): Iterable<Block> =
        Iterable { ModBlocks.BLOCKS.getEntries().map(Holder<Block>::value).iterator() }

    override fun dropSelf(block: Block) {
        super.dropSelf(block)
    }

    fun addDropSelf(block: Block) {
        super.dropSelf(block)
    }

    fun addSingleItemDrop(
        block: Block,
        item: ItemLike,
    ) {
        super.add(block, createSingleItemTable(item))
    }

    fun addRangeDrop(
        block: Block,
        item: ItemLike,
        min: Float,
        max: Float,
    ) {
        super.add(block, createSingleItemTable(item, UniformGenerator.between(min, max)))
    }

    fun addSilkTouchRangeDrop(
        block: Block,
        item: ItemLike,
        min: Float,
        max: Float,
    ) {
        super.add(block, createSingleItemTableWithSilkTouch(block, item, UniformGenerator.between(min, max)))
    }

    override fun add(block: Block, factory: Function<Block, LootTable.Builder>) {
        super.add(block, factory)
    }

    override fun add(block: Block, builder: LootTable.Builder) {
        super.add(block, builder)
    }

    fun addCropDrop(
        block: Block,
        cropItem: Item,
        seedItem: Item,
        condition: LootItemCondition.Builder,
    ) {
        super.add(block, createCropDrops(block, cropItem, seedItem, condition))
    }
}
