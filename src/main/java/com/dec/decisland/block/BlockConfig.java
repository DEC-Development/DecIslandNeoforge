package com.dec.decisland.block;

import com.dec.decisland.DecIsland;
import com.dec.decisland.datagen.ModBlockLootTablesProvider;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockConfig {
    public String name;
    public Map<String, String> langMap;
    public Function<BlockBehaviour.Properties, ? extends Block> func;
    public Supplier<BlockBehaviour.Properties> props;
    public boolean shouldRegistryBlockItem;
    public Supplier<CreativeModeTab> creativeTab;
    public Consumer<BlockModelGenerators> blockModelGenerator;
    public Consumer<ModBlockLootTablesProvider> blockLootTableGenerator;
    public List<TagKey<Block>> tags;

    private BlockConfig(Builder builder) {
        this.name = builder.name;
        this.langMap = builder.langMap;
        this.func = builder.func;
        this.props = builder.props;
        this.shouldRegistryBlockItem = builder.shouldRegistryBlockItem;
        this.creativeTab = builder.creativeTab;
        this.blockModelGenerator = builder.blockModelGenerator;
        this.blockLootTableGenerator = builder.blockLootTableGenerator;
        this.tags = builder.tags;
    }

    public static BlockConfig getConfig(String name) {
        return ModBlocks.getBlockConfigs().stream().filter(
                        config -> ("block." + DecIsland.MOD_ID + "." + config.name).equals(name)
                ).findFirst()
                .orElse(null);
    }

    public static BlockConfig getConfig(Block block) {
        return getConfig(block.getDescriptionId());
    }

    public static class Builder {
        public String name;
        public Map<String, String> langMap;
        public Function<BlockBehaviour.Properties, ? extends Block> func = Block::new;
        public Supplier<BlockBehaviour.Properties> props = () -> Block.Properties.of();
        public boolean shouldRegistryBlockItem = true;
        public Supplier<CreativeModeTab> creativeTab;
        public Consumer<BlockModelGenerators> blockModelGenerator = blockModels -> {
            // 通过name获取对应的方块Holder
            Holder<Block> blockHolder = ModBlocks.getBlockByName("block." + DecIsland.MOD_ID + "." + name);
            // 使用方块Holder创建模型
            blockModels.createTrivialCube(blockHolder.value());
        };
        public Consumer<ModBlockLootTablesProvider> blockLootTableGenerator = blockLootProvider -> {
            // 通过name获取对应的方块Holder
//            System.out.println("Try to find: " + "block." + DecIsland.MOD_ID + "." + name);
            Holder<Block> blockHolder = ModBlocks.getBlockByName("block." + DecIsland.MOD_ID + "." + name);
            // 使用方块Holder创建Loot表
            blockLootProvider.dropSelf(blockHolder.value());
        };
        public List<TagKey<Block>> tags;

        public Builder(String name, Map<String, String> langMap) {
            this.name = name;
            this.langMap = langMap;
        }

        public Builder func(Function<BlockBehaviour.Properties, ? extends Block> func) {
            this.func = func;
            return this;
        }

        public Builder props(Supplier<BlockBehaviour.Properties> props) {
            this.props = props;
            return this;
        }

        public Builder shouldRegistryBlockItem(boolean shouldRegistryBlockItem) {
            this.shouldRegistryBlockItem = shouldRegistryBlockItem;
            return this;
        }

        public Builder creativeTab(Supplier<CreativeModeTab> creativeTab) {
            this.creativeTab = creativeTab;
            return this;
        }

        public Builder blockModelGenerator(Consumer<BlockModelGenerators> blockModelGenerator) {
            this.blockModelGenerator = blockModelGenerator;
            return this;
        }

        public Builder blockLootTableGenerator(Consumer<ModBlockLootTablesProvider> blockLootTableGenerator) {
            this.blockLootTableGenerator = blockLootTableGenerator;
            return this;
        }

        public Builder tags(List<TagKey<Block>> tags) {
            this.tags = tags;
            return this;
        }

        public BlockConfig build() {
            return new BlockConfig(this);
        }
    }
}
