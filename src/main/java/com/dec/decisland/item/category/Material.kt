package com.dec.decisland.item.category

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem

object Material {
    @JvmField
    val BLUE_GEM_DEBRIS: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("blue_gem_debris", mapOf("en_us" to "Blue Gem Debris", "zh_cn" to "蓝宝石碎片"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val RED_GEM_DEBRIS: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("red_gem_debris", mapOf("en_us" to "Red Gem Debris", "zh_cn" to "红宝石碎片"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val BAT_WING: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("bat_wing", mapOf("en_us" to "Incomplete Bat Wing", "zh_cn" to "残缺蝙蝠翅"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val GHOST_INGOT: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("ghost_ingot", mapOf("en_us" to "Ghost Ingot", "zh_cn" to "幽魂锭"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val ICE_INGOT: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("ice_ingot", mapOf("en_us" to "Ice Ingot", "zh_cn" to "冰锭"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val LAVA_INGOT: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("lava_ingot", mapOf("en_us" to "Lava Ingot", "zh_cn" to "岩浆锭"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val EVERLASTING_WINTER_INGOT: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("everlasting_winter_ingot", mapOf("en_us" to "Everlasting Winter Ingot", "zh_cn" to "永冬锭"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val CORAL_INGOT: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("coral_ingot", mapOf("en_us" to "Coral Ingot", "zh_cn" to "珊瑚锭"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val GLASS_INGOT: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("glass_ingot", mapOf("en_us" to "Glass Ingot", "zh_cn" to "玻璃锭"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val PURE_INGOT: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("pure_ingot", mapOf("en_us" to "Pure Ingot", "zh_cn" to "纯洁锭"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val STEEL_INGOT: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("steel_ingot", mapOf("en_us" to "Steel Ingot", "zh_cn" to "钢锭"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmField
    val ROUGH_STEEL: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("rough_steel", mapOf("en_us" to "Rough Steel", "zh_cn" to "粗制钢料"))
            .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
            .build(),
    )

    @JvmStatic
    fun load() {
    }
}
