package com.dec.decisland.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class CreativeTabConfig {
    public String name;
    public Map<String, String> langMap;
    public Supplier<Item> iconItem;
    private CreativeTabConfig(Builder builder) {
        this.name = builder.name;
        this.langMap = builder.langMap;
        this.iconItem = builder.iconItem;
    }
    public static class Builder {
        private String name;
        private Map<String, String> langMap;
        private Supplier<Item> iconItem;
        public Builder(String name, Map<String, String> langMap) {
            this.name = name;
            this.langMap = langMap;
        }
        public Builder iconItem(Supplier <Item> iconItem) {
            this.iconItem = iconItem;
            return this;
        }
        public CreativeTabConfig build() {
            return new CreativeTabConfig(this);
        }
    }
}
