package com.dec.decisland.block;

import com.dec.decisland.DecIsland;
import com.dec.decisland.item.ModCreativeModeTabs;
import com.dec.decisland.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(DecIsland.MOD_ID);
    private static final List<BlockConfig> BLOCK_CONFIGS = new ArrayList<>();

    public static final DeferredBlock<Block> ASH =
            registerBlock(new BlockConfig.Builder("ash", Map.of(
                    "en_us", "Ash",
                    "zh_cn", "灰烬块"
            )).props(() -> Block.Properties.of().strength(0.2F, 0.5F).sound(SoundType.SAND).noLootTable())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
                    .blockLootTableGenerator(null)
                    .build());

    public static final DeferredBlock<Block> ANCIENT_ICE =
            registerBlock(new BlockConfig.Builder("ancient_ice", Map.of(
                    "en_us", "Ancient Ice",
                    "zh_cn", "远古冰块"
            )).props(() -> Block.Properties.of().strength(5F, 30F).requiresCorrectToolForDrops().sound(SoundType.STONE))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
                    .tags(Arrays.asList(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL))
                    .build());

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> func,
                                                                    Supplier<BlockBehaviour.Properties> props, boolean shouldRegistryBlockItem) {
        DeferredBlock<T> block = BLOCKS.registerBlock(name, func, props);
        if (shouldRegistryBlockItem) ModItems.ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> func,
                                                                    boolean shouldRegistryBlockItem) {
        DeferredBlock<T> block = BLOCKS.registerBlock(name, func);
        if (shouldRegistryBlockItem) ModItems.ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(BlockConfig config) {
        DeferredBlock<T> block = (DeferredBlock<T>) BLOCKS.registerBlock(config.name, config.func, config.props);
        BLOCK_CONFIGS.add(config);
        if (config.shouldRegistryBlockItem) ModItems.ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    /**
     * 根据名称获取方块对象
     *
     * @param name 方块的名称，格式例如block.decisland:ancient_ice。可用.getId()获取
     * @return 匹配名称的方块对象
     * @throws IllegalArgumentException 如果没有找到匹配的方块
     */
    public static Holder<Block> getBlockByName(String name) {
//        System.out.println("getBlockByName: " + name);
//        ModBlocks.BLOCKS.getEntries().stream().forEach(block -> {
//            System.out.println(block.get().getDescriptionId());
//        });
        return ModBlocks.BLOCKS.getEntries().stream()
                // 获取第一个匹配项
                .filter(block -> block.get().getDescriptionId().equals(name))
                // 如果没有找到匹配项，则抛出异常
                .findFirst()
                .orElseThrow();
    }
    public static Holder<Block> getBlockByConfig(BlockConfig config) {
        return ModBlocks.getBlockByName("block." + DecIsland.MOD_ID + "." + config.name);
    }

    public static List<BlockConfig> getBlockConfigs() {
        return BLOCK_CONFIGS;
    }

}
