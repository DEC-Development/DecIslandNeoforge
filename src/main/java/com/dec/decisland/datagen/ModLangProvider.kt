package com.dec.decisland.datagen

import com.dec.decisland.DecIsland
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.effect.ModEffects
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

abstract class ModLangProvider protected constructor(
    output: PackOutput,
    protected val locale: String,
) : LanguageProvider(output, DecIsland.MOD_ID, locale) {
    override fun addTranslations() {
        ModItems.getItemConfigs().forEach { config ->
            val item = ModItems.getItemByConfig(config)
            val translation = config.langMap?.get(locale)
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
            val translation = config.langMap?.get(locale) ?: return@forEach
            add("itemGroup.${config.name}", translation)
        }

        when (locale) {
            "en_us" -> add(ModEffects.DIZZINESS.get(), "Dizziness")
            "zh_cn" -> add(ModEffects.DIZZINESS.get(), "眩晕")
        }
    }
}
