package com.dec.decisland.block

import com.dec.decisland.DecIsland
import com.dec.decisland.datagen.ModBlockLootTablesProvider
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.tags.TagKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

class BlockConfig private constructor(builder: Builder) {
    @JvmField
    val name: String = builder.name

    @JvmField
    val langMap: Map<String, String> = builder.langMap

    @JvmField
    val func: Function<BlockBehaviour.Properties, out Block> = builder.func

    @JvmField
    val props: Supplier<BlockBehaviour.Properties> = builder.props

    @JvmField
    val shouldRegistryBlockItem: Boolean = builder.shouldRegistryBlockItem

    @JvmField
    val creativeTab: Supplier<CreativeModeTab>? = builder.creativeTab

    @JvmField
    val blockModelGenerator: Consumer<BlockModelGenerators> = builder.blockModelGenerator

    @JvmField
    val blockLootTableGenerator: Consumer<ModBlockLootTablesProvider> = builder.blockLootTableGenerator

    @JvmField
    val tags: List<TagKey<Block>>? = builder.tags

    class Builder(
        @JvmField val name: String,
        @JvmField val langMap: Map<String, String> = emptyMap(),
    ) {
        @JvmField
        var func: Function<BlockBehaviour.Properties, out Block> = Function { properties -> Block(properties) }

        @JvmField
        var props: Supplier<BlockBehaviour.Properties> = Supplier { BlockBehaviour.Properties.of() }

        @JvmField
        var shouldRegistryBlockItem: Boolean = true

        @JvmField
        var creativeTab: Supplier<CreativeModeTab>? = null

        @JvmField
        var blockModelGenerator: Consumer<BlockModelGenerators> = Consumer { blockModels ->
            val block = ModBlocks.getBlockByName("block.${DecIsland.MOD_ID}.$name").value()
            blockModels.createTrivialCube(block)
        }

        @JvmField
        var blockLootTableGenerator: Consumer<ModBlockLootTablesProvider> = Consumer { blockLootProvider ->
            val block = ModBlocks.getBlockByName("block.${DecIsland.MOD_ID}.$name").value()
            blockLootProvider.addDropSelf(block)
        }

        @JvmField
        var tags: List<TagKey<Block>>? = null

        fun func(func: Function<BlockBehaviour.Properties, out Block>): Builder = apply {
            this.func = func
        }

        fun props(props: Supplier<BlockBehaviour.Properties>): Builder = apply {
            this.props = props
        }

        fun shouldRegistryBlockItem(shouldRegistryBlockItem: Boolean): Builder = apply {
            this.shouldRegistryBlockItem = shouldRegistryBlockItem
        }

        fun creativeTab(creativeTab: Supplier<CreativeModeTab>?): Builder = apply {
            this.creativeTab = creativeTab
        }

        fun blockModelGenerator(blockModelGenerator: Consumer<BlockModelGenerators>): Builder = apply {
            this.blockModelGenerator = blockModelGenerator
        }

        fun blockLootTableGenerator(blockLootTableGenerator: Consumer<ModBlockLootTablesProvider>): Builder = apply {
            this.blockLootTableGenerator = blockLootTableGenerator
        }

        fun tags(tags: List<TagKey<Block>>?): Builder = apply {
            this.tags = tags
        }

        fun build(): BlockConfig = BlockConfig(this)
    }

    companion object {
        @JvmStatic
        fun getConfig(name: String?): BlockConfig? =
            ModBlocks.getBlockConfigs()
                .firstOrNull { "block.${DecIsland.MOD_ID}.${it.name}" == name }

        @JvmStatic
        fun getConfig(block: Block): BlockConfig? = getConfig(block.descriptionId)
    }
}
