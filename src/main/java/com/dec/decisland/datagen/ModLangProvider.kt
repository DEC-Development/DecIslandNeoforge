package com.dec.decisland.datagen

import com.dec.decisland.DecIsland
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.effect.ModEffects
import com.dec.decisland.entity.GeneratedMobs
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.datagen.Lang.BedrockLangResources
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

abstract class ModLangProvider protected constructor(
    output: PackOutput,
    protected val locale: String,
) : LanguageProvider(output, DecIsland.MOD_ID, locale) {
    override fun addTranslations() {
        val translations = linkedMapOf<String, String>()

        // Layer 2: Bedrock entries are imported first and act as the global fallback.
        translations.putAll(BedrockLangResources.javaTranslations(locale))

        // Layer 1: explicit Java-side definitions override Bedrock when both are present.
        ModItems.getItemConfigs().forEach { config ->
            val translation = config.langMap[locale] ?: BedrockLangResources.itemName(locale, config.name)
            if (translation != null) {
                translations["item.${DecIsland.MOD_ID}.${config.name}"] = translation
            }
        }

        ModBlocks.getBlockConfigs().forEach { config ->
            val translation = config.langMap[locale] ?: BedrockLangResources.blockName(locale, config.name)
            if (translation != null) {
                translations["block.${DecIsland.MOD_ID}.${config.name}"] = translation
            }
        }

        ModCreativeModeTabs.getTabConfigs().forEach { config ->
            val translation = config.langMap[locale] ?: return@forEach
            translations["itemGroup.${config.name}"] = translation
        }

        // 生成生物刷怪蛋：基岩版 spawn_egg 条目优先，缺失时用实体名生成（生成 哥布林法师 / Spawn Goblin Wizard）
        GeneratedMobs.SPAWN_EGGS.entries.forEach { entry ->
            val entityName = entry.id.path.removeSuffix("_spawn_egg")
            val eggName = BedrockLangResources.spawnEggName(locale, entityName)
                ?: translations["entity.${DecIsland.MOD_ID}.$entityName"]?.let { base ->
                    if (locale == "zh_cn") "生成$base" else "Spawn $base"
                }
            if (eggName != null) {
                translations["item.${DecIsland.MOD_ID}.${entry.id.path}"] = eggName
            }
        }

        when (locale) {
            "en_us" -> translations[ModEffects.DIZZINESS.get().descriptionId] = "Dizziness"
            "zh_cn" -> translations[ModEffects.DIZZINESS.get().descriptionId] = "眩晕"
        }

        translations.forEach(::add)
    }
}
