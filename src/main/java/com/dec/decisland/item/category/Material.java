package com.dec.decisland.item.category;

import com.dec.decisland.item.ItemConfig;
import com.dec.decisland.item.ModCreativeModeTabs;
import com.dec.decisland.item.ModItems;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Map;

public final class Material {

    public static final DeferredItem<Item> BLUE_GEM_DEBRIS = ModItems.registerItem(
            new ItemConfig.Builder("blue_gem_debris", Map.of(
                    "en_us", "Blue Gem Debris",
                    "zh_cn", "蓝宝石碎片"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );

    public static final DeferredItem<Item> RED_GEM_DEBRIS = ModItems.registerItem(
            new ItemConfig.Builder("red_gem_debris", Map.of(
                    "en_us", "Red Gem Debris",
                    "zh_cn", "红宝石碎片"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );

    public static final DeferredItem<Item> BAT_WING = ModItems.registerItem(
            new ItemConfig.Builder("bat_wing", Map.of(
                    "en_us", "Incomplete Bat Wing",
                    "zh_cn", "残缺蝙蝠翅"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );

    public static final DeferredItem<Item> GHOST_INGOT = ModItems.registerItem(
            new ItemConfig.Builder("ghost_ingot", Map.of(
                    "en_us", "Ghost Ingot",
                    "zh_cn", "幽魂锭"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );

    public static final DeferredItem<Item> ICE_INGOT = ModItems.registerItem(
            new ItemConfig.Builder("ice_ingot", Map.of(
                    "en_us", "Ice Ingot",
                    "zh_cn", "冰锭"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );

    public static final DeferredItem<Item> LAVA_INGOT = ModItems.registerItem(
            new ItemConfig.Builder("lava_ingot", Map.of(
                    "en_us", "Lava Ingot",
                    "zh_cn", "岩浆锭"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );

    public static final DeferredItem<Item> EVERLASTING_WINTER_INGOT = ModItems.registerItem(
            new ItemConfig.Builder("everlasting_winter_ingot", Map.of(
                    "en_us", "Everlasting Winter Ingot",
                    "zh_cn", "永冬锭"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );
    public static final DeferredItem<Item> CORAL_INGOT = ModItems.registerItem(
            new ItemConfig.Builder("coral_ingot", Map.of(
                    "en_us", "Coral Ingot",
                    "zh_cn", "珊瑚锭"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );
    public static final DeferredItem<Item> GLASS_INGOT = ModItems.registerItem(
            new ItemConfig.Builder("glass_ingot", Map.of(
                    "en_us", "Glass Ingot",
                    "zh_cn", "玻璃锭"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );
    public static final DeferredItem<Item> PURE_INGOT = ModItems.registerItem(
            new ItemConfig.Builder("pure_ingot", Map.of(
                    "en_us", "Pure Ingot",
                    "zh_cn", "纯洁锭"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );
    public static final DeferredItem<Item> STEEL_INGOT = ModItems.registerItem(
            new ItemConfig.Builder("steel_ingot", Map.of(
                    "en_us", "Steel Ingot",
                    "zh_cn", "钢锭"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );
    public static final DeferredItem<Item> ROUGH_STEEL = ModItems.registerItem(
            new ItemConfig.Builder("rough_steel", Map.of(
                    "en_us", "Rough Steel",
                    "zh_cn", "粗制钢料"
            )).creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB).build()
    );
    public static void load() {

    }
}
