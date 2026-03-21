package com.dec.decisland.block

import com.dec.decisland.DecIsland
import com.dec.decisland.block.custom.FlowerGhostBlock
import com.dec.decisland.block.custom.NightmareBlock
import com.dec.decisland.block.custom.SnowPortalBlock
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import net.minecraft.core.Holder
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.PushReaction
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Function
import java.util.function.Supplier

object ModBlocks {
    @JvmField
    val BLOCKS: DeferredRegister.Blocks = DeferredRegister.createBlocks(DecIsland.MOD_ID)

    private val blockConfigs = mutableListOf<BlockConfig>()

    @JvmField
    val ASH: DeferredBlock<Block> = registerBlock(
        BlockConfig.Builder("ash", mapOf("en_us" to "Ash", "zh_cn" to "灰烬块"))
            .props { BlockBehaviour.Properties.of().strength(0.2f, 0.5f).sound(SoundType.SAND).noLootTable() }
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .blockLootTableGenerator {}
            .build(),
    )

    @JvmField
    val ANCIENT_ICE: DeferredBlock<Block> = registerBlock(
        BlockConfig.Builder("ancient_ice", mapOf("en_us" to "Ancient Ice", "zh_cn" to "远古冰块"))
            .props { BlockBehaviour.Properties.of().strength(5.0f, 30.0f).requiresCorrectToolForDrops().sound(SoundType.STONE) }
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .tags(listOf(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL))
            .build(),
    )

    @JvmField
    val SNOW_PORTAL: DeferredBlock<SnowPortalBlock> = registerBlock(
        "snow_portal",
        ::SnowPortalBlock,
        Supplier {
            BlockBehaviour.Properties.of()
                .noCollision()
                .strength(-1.0f)
                .lightLevel { 11 }
                .sound(SoundType.GLASS)
                .noLootTable()
                .pushReaction(PushReaction.BLOCK)
        },
        false,
    )

    @JvmField
    val NIGHTMARE_BLOCK: DeferredBlock<NightmareBlock> = registerBlock(
        BlockConfig.Builder("nightmare_block", mapOf("en_us" to "Nightmare Block", "zh_cn" to "梦魇方块"))
            .func(::NightmareBlock)
            .props {
                BlockBehaviour.Properties.of()
                    .strength(1.0f, 50.0f)
                    .lightLevel { 3 }
                    .noLootTable()
            }
            .shouldRegistryBlockItem(false)
            .blockLootTableGenerator {}
            .build(),
    )

    @JvmField
    val FLOWER_GHOST_BLOCK: DeferredBlock<FlowerGhostBlock> = registerBlock(
        BlockConfig.Builder("flower_ghost_block", mapOf("en_us" to "Flower Ghost Block", "zh_cn" to "花灵方块"))
            .func(::FlowerGhostBlock)
            .props {
                BlockBehaviour.Properties.of()
                    .strength(0.01f, 0.0f)
                    .noLootTable()
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
            }
            .shouldRegistryBlockItem(false)
            .blockLootTableGenerator {}
            .build(),
    )

    private fun <T : Block> registerBlock(
        name: String,
        func: Function<BlockBehaviour.Properties, out T>,
        props: Supplier<BlockBehaviour.Properties>,
        shouldRegistryBlockItem: Boolean,
    ): DeferredBlock<T> {
        val block = BLOCKS.registerBlock(name, func, props)
        if (shouldRegistryBlockItem) {
            ModItems.ITEMS.registerSimpleBlockItem(block)
        }
        return block
    }

    private fun <T : Block> registerBlock(
        name: String,
        func: Function<BlockBehaviour.Properties, out T>,
        shouldRegistryBlockItem: Boolean,
    ): DeferredBlock<T> {
        val block = BLOCKS.registerBlock(name, func)
        if (shouldRegistryBlockItem) {
            ModItems.ITEMS.registerSimpleBlockItem(block)
        }
        return block
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Block> registerBlock(config: BlockConfig): DeferredBlock<T> {
        val block = BLOCKS.registerBlock(config.name, config.func, config.props) as DeferredBlock<T>
        blockConfigs.add(config)
        if (config.shouldRegistryBlockItem) {
            ModItems.ITEMS.registerSimpleBlockItem(block)
        }
        return block
    }

    @JvmStatic
    fun register(eventBus: IEventBus) {
        BLOCKS.register(eventBus)
    }

    @JvmStatic
    fun getBlockByName(name: String): Holder<Block> =
        BLOCKS.getEntries()
            .firstOrNull { it.get().descriptionId == name }
            ?: throw NoSuchElementException(name)

    @JvmStatic
    fun getBlockByConfig(config: BlockConfig): Holder<Block> =
        getBlockByName("block.${DecIsland.MOD_ID}.${config.name}")

    @JvmStatic
    fun getBlockConfigs(): List<BlockConfig> = blockConfigs
}
