package com.dec.decisland.item.category

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.custom.GlintItem
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Function
import java.util.function.Supplier

object SummonItem {
    @JvmField
    val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_MISC_TAB

    private fun registerSummonItem(
        name: String,
        enUs: String,
        zhCn: String,
        func: Function<Item.Properties, out Item> = Function(::Item),
        props: Supplier<Item.Properties> = Supplier { Item.Properties() },
        modelTemplate: ModelTemplate = ModelTemplates.FLAT_ITEM,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name, mapOf("en_us" to enUs, "zh_cn" to zhCn))
                .func(func)
                .props(props)
                .modelTemplate(modelTemplate)
                .creativeTab(creativeTab)
                .build(),
        )

    @JvmField
    val ASH_KEY: DeferredItem<Item> =
        registerSummonItem("ash_key", "Ash Key", "灰烬钥匙")

    @JvmField
    val BAT_BAIT: DeferredItem<Item> =
        registerSummonItem("bat_bait", "Bat Bait", "蝙蝠诱饵")

    @JvmField
    val BOOK_OF_DEEP: DeferredItem<Item> =
        registerSummonItem("book_of_deep", "Book of Deep", "深渊之书", Function(::GlintItem))

    @JvmField
    val CHALLENGE_OF_ASH: DeferredItem<Item> =
        registerSummonItem("challenge_of_ash", "Challenge Of Ash", "灰烬挑战书", Function(::GlintItem))

    @JvmField
    val DARK_PEARL: DeferredItem<Item> =
        registerSummonItem("dark_pearl", "Dark Pearl", "暗黑珍珠", Function(::GlintItem))

    @JvmField
    val DARK_STONE: DeferredItem<Item> =
        registerSummonItem("dark_stone", "Dark Stone", "黑暗石")

    @JvmField
    val ENTITY_SOUL: DeferredItem<Item> =
        registerSummonItem("entity_soul", "Entity Soul", "实体灵魂", Function(::GlintItem))

    @JvmField
    val FROZEN_POWER: DeferredItem<Item> =
        registerSummonItem(
            "frozen_power",
            "Frozen Power",
            "冰霜能量",
            Function(::GlintItem),
            modelTemplate = ModelTemplates.FLAT_HANDHELD_ITEM,
        )

    @JvmField
    val MAGIC_CHEST_DEBRIS: DeferredItem<Item> =
        registerSummonItem("magic_chest_debris", "Magic Chest Debris", "魔法箱子碎片")

    @JvmField
    val MYSTERIOUS_KEY: DeferredItem<Item> =
        registerSummonItem("mysterious_key", "Mysterious Key", "神秘钥匙", Function(::GlintItem))

    @JvmField
    val NATURE_ESSENCE: DeferredItem<Item> =
        registerSummonItem("nature_essence", "Nature Essence", "自然精华")

    @JvmField
    val SOUL_KEY: DeferredItem<Item> =
        registerSummonItem("soul_key", "Soul Key", "灵魂钥匙")

    @JvmStatic
    fun load() {
    }
}
