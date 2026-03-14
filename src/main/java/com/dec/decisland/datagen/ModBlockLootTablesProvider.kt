package com.dec.decisland.datagen

import com.dec.decisland.block.BlockConfig
import com.dec.decisland.block.ModBlocks
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
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

    override fun add(block: Block, factory: Function<Block, LootTable.Builder>) {
        super.add(block, factory)
    }

    override fun add(block: Block, builder: LootTable.Builder) {
        super.add(block, builder)
    }
}
