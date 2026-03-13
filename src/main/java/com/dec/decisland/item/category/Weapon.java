package com.dec.decisland.item.category;

import com.dec.decisland.item.*;
import com.dec.decisland.item.custom.AbsoluteZero;
import com.dec.decisland.item.custom.BambooKatana;
import com.dec.decisland.item.custom.HardBambooKatana;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumables;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Map;

import static net.minecraft.world.item.Items.STICK;

public final class Weapon {

    public static final DeferredItem<Item> ABSOLUTE_ZERO = ModItems.registerItem(
            new ItemConfig.Builder("absolute_zero", Map.of(
                    "en_us", "Absolute Zero",
                    "zh_cn", "绝对零度"
            )).func(AbsoluteZero::new)
                    .props(
                            () -> new Item.Properties().sword(ModToolMaterial.ABSOLUTE_ZERO, 3.0F, -2.4F)
                                    .useCooldown(3.0F).stacksTo(1)
                    )
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> BAMBOO_KATANA = ModItems.registerItem(
            new ItemConfig.Builder("bamboo_katana", Map.of(
                    "en_us", "Bamboo Katana",
                    "zh_cn", "竹太刀"
            )).func(BambooKatana::new)
                    .props(
                            () -> new Item.Properties().sword(ModToolMaterial.BAMBOO, 2.0F, -2.4F)
                                    .useCooldown(2.5F).stacksTo(1)
                    )
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> CANDY_CANE = ModItems.registerItem(
            new ItemConfig.Builder("candy_cane", Map.of(
                    "en_us", "Candy Cane",
                    "zh_cn", "拐棍糖"
            )).props(() -> (new Item.Properties()).food(
                                            new FoodProperties(2, 0.2f, true),
                                            Consumables.defaultFood().build()
                                    ).sword(ModToolMaterial.CANDY_CANE, 2.0F, -2.0F)
                                    .stacksTo(1)
                    ).modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );
    public static final DeferredItem<Item> HARD_BAMBOO_KATANA = ModItems.registerItem(
            new ItemConfig.Builder("hard_bamboo_katana", Map.of(
                    "en_us", "Hard Bamboo Katana",
                    "zh_cn", "硬化竹太刀"
            )).func(HardBambooKatana::new)
                    .props(
                            () -> new Item.Properties().sword(ModToolMaterial.HARD_BAMBOO, 2.0F, -2.4F)
                                    .useCooldown(2.2F).stacksTo(1)
                    )
                    .customProp(new CustomItemProperties.Builder().burnTime(8).build())
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> HARD_LOLLIPOP = ModItems.registerItem(
            new ItemConfig.Builder("hard_lollipop", Map.of(
                    "en_us", "Hard Lollipop",
                    "zh_cn", "硬化棒棒糖"
            ))
                    .props(() -> (new Item.Properties()).food(
                            new FoodProperties(14, 0.8f, false),
                            Consumables.defaultFood().build()
                    ).usingConvertsTo(STICK).sword(ModToolMaterial.HARD_LOLLIPOP, 3, -3))
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> CACTUS_SWORD = ModItems.registerItem(
            new ItemConfig.Builder("cactus_sword", Map.of(
                    "en_us", "Cactus Sword",
                    "zh_cn", "仙人掌巨剑"
            ))
                    .props(() -> (new Item.Properties()).sword(ModToolMaterial.CACTUS, 2, -2.7F))
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );

    public static final DeferredItem<Item> CORRUPTED_SWORD = ModItems.registerItem(
            new ItemConfig.Builder("corrupted_sword", Map.of(
                    "en_us", "Corrupted Sword",
                    "zh_cn", "堕落之剑"
            ))
                    .props(() -> (new Item.Properties()).sword(ModToolMaterial.CORRUPTED, 2, -2.4F))
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    );


    public static void load() {

    }
}
