package com.dec.decisland.datagen

import com.dec.decisland.DecIsland
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.effect.ModEffects
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.custom.ExperienceBookEmptyItem
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

abstract class ModLangProvider protected constructor(
    output: PackOutput,
    protected val locale: String,
) : LanguageProvider(output, DecIsland.MOD_ID, locale) {
    override fun addTranslations() {
        ModItems.getItemConfigs().forEach { config ->
            val item = ModItems.getItemByConfig(config)
            val translation = config.langMap[locale]
            if (item != null && translation != null) {
                add(item, translation)
            }
        }

        ModBlocks.getBlockConfigs().forEach { config ->
            val block = ModBlocks.getBlockByConfig(config).value()
            val translation = config.langMap[locale]
            if (translation != null) {
                add(block, translation)
            }
        }

        ModCreativeModeTabs.getTabConfigs().forEach { config ->
            val translation = config.langMap[locale] ?: return@forEach
            add("itemGroup.${config.name}", translation)
        }

        when (locale) {
            "en_us" -> add(ModEffects.DIZZINESS.get(), "Dizziness")
            "zh_cn" -> add(ModEffects.DIZZINESS.get(), "眩晕")
        }

        addCustomTranslations()
    }

    private fun addCustomTranslations() {
        when (locale) {
            "en_us" -> {
                add(ExperienceBookEmptyItem.FAIL_TRANSLATION_KEY, "Who cares about so little experience?")
                add("text.dec:guide_book_0.name", "§aGuide Book")
                add("text.dec:guide_book_1.name", "§aYou're weak, don't let them get close to you. Be careful at night, or the monsters will kill you easily. If fog gathers at night, don't go out for your own safety.")
                add("text.dec:guide_book_2.name", "§a")
                add("text.dec:guide_book_3.name", "§a")
            }
            "zh_cn" -> {
                add(ExperienceBookEmptyItem.FAIL_TRANSLATION_KEY, "如此干涸的经验谁会渴望呢？")
                add("text.dec:guide_book_0.name", "§a指导书")
                add("text.dec:guide_book_1.name", "§a你很虚弱，所以请不要让它们轻易地靠近你。在夜间需额外小心，否则怪物能轻易将你撕成碎片。如果雾气在夜间聚集，为了你的安全，请不要出门。")
                add("text.dec:guide_book_2.name", "§a")
                add("text.dec:guide_book_3.name", "§a")
            }
        }
    }
}
