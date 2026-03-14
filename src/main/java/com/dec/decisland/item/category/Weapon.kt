package com.dec.decisland.item.category

import com.dec.decisland.item.CustomItemProperties
import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.ModToolMaterial
import com.dec.decisland.item.custom.AbsoluteZero
import com.dec.decisland.item.custom.BambooKatana
import com.dec.decisland.item.custom.HardBambooKatana
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items.STICK
import net.minecraft.world.item.component.Consumables
import net.neoforged.neoforge.registries.DeferredItem

object Weapon {
    @JvmField
    val ABSOLUTE_ZERO: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("absolute_zero", mapOf("en_us" to "Absolute Zero", "zh_cn" to "绝对零度"))
            .func(::AbsoluteZero)
            .props { Item.Properties().sword(ModToolMaterial.ABSOLUTE_ZERO, 3.0f, -2.4f).useCooldown(3.0f).stacksTo(1) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val BAMBOO_KATANA: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("bamboo_katana", mapOf("en_us" to "Bamboo Katana", "zh_cn" to "竹太刀"))
            .func(::BambooKatana)
            .props { Item.Properties().sword(ModToolMaterial.BAMBOO, 2.0f, -2.4f).useCooldown(2.5f).stacksTo(1) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val CANDY_CANE: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("candy_cane", mapOf("en_us" to "Candy Cane", "zh_cn" to "拐棍糖"))
            .props {
                Item.Properties()
                    .food(FoodProperties(2, 0.2f, true), Consumables.defaultFood().build())
                    .sword(ModToolMaterial.CANDY_CANE, 2.0f, -2.0f)
                    .stacksTo(1)
            }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val HARD_BAMBOO_KATANA: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("hard_bamboo_katana", mapOf("en_us" to "Hard Bamboo Katana", "zh_cn" to "硬化竹太刀"))
            .func(::HardBambooKatana)
            .props { Item.Properties().sword(ModToolMaterial.HARD_BAMBOO, 2.0f, -2.4f).useCooldown(2.2f).stacksTo(1) }
            .customProp(CustomItemProperties.Builder().burnTime(8).build())
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val HARD_LOLLIPOP: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("hard_lollipop", mapOf("en_us" to "Hard Lollipop", "zh_cn" to "硬化棒棒糖"))
            .props {
                Item.Properties()
                    .food(FoodProperties(14, 0.8f, false), Consumables.defaultFood().build())
                    .usingConvertsTo(STICK)
                    .sword(ModToolMaterial.HARD_LOLLIPOP, 3.0f, -3.0f)
            }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val CACTUS_SWORD: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("cactus_sword", mapOf("en_us" to "Cactus Sword", "zh_cn" to "仙人掌巨剑"))
            .props { Item.Properties().sword(ModToolMaterial.CACTUS, 2.0f, -2.7f) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val CORRUPTED_SWORD: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("corrupted_sword", mapOf("en_us" to "Corrupted Sword", "zh_cn" to "堕落之剑"))
            .props { Item.Properties().sword(ModToolMaterial.CORRUPTED, 2.0f, -2.4f) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmStatic
    fun load() {
    }
}
