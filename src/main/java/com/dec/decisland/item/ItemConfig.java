package com.dec.decisland.item;

import com.dec.decisland.DecIsland;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemConfig {
    public String name;
    public Map<String, String> langMap;
    public Function<Item.Properties, ? extends Item> func;
    public Supplier<Item.Properties> props;
    public Supplier<CreativeModeTab> creativeTab;
    public ModelTemplate modelTemplate;
    public CustomItemProperties customProp;
    public List<TagKey<Item>> tags;

    private ItemConfig(Builder builder) {
        this.name = builder.name;
        this.langMap = builder.langMap;
        this.func = builder.func;
        this.props = builder.props;
        this.creativeTab = builder.creativeTab;
        this.modelTemplate = builder.modelTemplate;
        this.customProp = builder.customProp;
        this.tags = builder.tags;
    }

    /**
     * 根据给定的名称获取物品配置
     *
     * @param name 要查找的物品名称。格式为item.decisland.xxx
     * @return 匹配的ItemConfig对象，如果没有找到则返回null
     */
    public static ItemConfig getConfig(String name) {
        // 调试代码：打印正在查找的物品名称
//        System.out.println("Try to find: " + name);
        // 调试代码：打印所有可用的物品配置名称
//        System.out.println("Search: ");
//        ModItems.getItemConfigs().stream().forEach(config -> {
//            System.out.println("item." + DecIsland.MOD_ID + "." + config.name);
//        });
        // 使用Stream API过滤物品配置列表，查找匹配的名称
        // 名称格式为："item." + MOD_ID + "." + config.name
        // 如果找到匹配项，返回第一个；否则返回null
        return ModItems.getItemConfigs().stream()
                .filter(config -> ("item." + DecIsland.MOD_ID + "." + config.name).equals(name))
                .findFirst()
                .orElse(null);
    }

    public static ItemConfig getConfig(Item item) {
        return getConfig(item.getDescriptionId());
    }

    public static class Builder {

        private String name;
        private Map<String, String> langMap;
        private Function<Item.Properties, ? extends Item> func = Item::new;
        private Supplier<Item.Properties> props = Item.Properties::new;
        private Supplier<CreativeModeTab> creativeTab;
        private ModelTemplate modelTemplate = ModelTemplates.FLAT_ITEM;
        private CustomItemProperties customProp = new CustomItemProperties.Builder().build();
        private List<TagKey<Item>> tags = List.of();

        public Builder(String name, Map<String, String> langMap) {
            this.name = name;
            this.langMap = langMap;
        }

        public Builder func(Function<Item.Properties, ? extends Item> func) {
            this.func = func;
            return this;
        }

        public Builder props(Supplier<Item.Properties> props) {
            this.props = props;
            return this;
        }

        public Builder creativeTab(Supplier<CreativeModeTab> creativeTab) {
            this.creativeTab = creativeTab;
            return this;
        }

        public Builder modelTemplate(ModelTemplate modelTemplate) {
            this.modelTemplate = modelTemplate;
            return this;
        }

        public Builder customProp(CustomItemProperties customItemProps) {
            this.customProp = customItemProps;
            return this;
        }

        public Builder tags(List<TagKey<Item>> tags) {
            this.tags = tags;
            return this;
        }

        public ItemConfig build() {
            return new ItemConfig(this);
        }
    }
}
