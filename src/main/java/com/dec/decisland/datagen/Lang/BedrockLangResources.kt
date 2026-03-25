package com.dec.decisland.datagen.Lang

import com.dec.decisland.DecIsland
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object BedrockLangResources {
    private val rawTranslationsByLocale: Map<String, Map<String, String>> = mapOf(
        "en_us" to load("bedrock_lang/en_US.lang"),
        "zh_cn" to load("bedrock_lang/zh_CN.lang"),
    )
    private val javaTranslationsByLocale: Map<String, Map<String, String>> =
        rawTranslationsByLocale.mapValues { (_, translations) ->
            translations.entries.associate { (key, value) -> normalizeKey(key) to value }
        }

    @JvmStatic
    fun get(locale: String, key: String): String? {
        val normalizedLocale = locale.lowercase()
        val rawTranslations = rawTranslationsByLocale[normalizedLocale] ?: return null
        val javaTranslations = javaTranslationsByLocale[normalizedLocale].orEmpty()
        return rawTranslations[key] ?: javaTranslations[key] ?: javaTranslations[normalizeKey(key)]
    }

    @JvmStatic
    fun itemName(locale: String, name: String): String? = get(locale, javaItemKey(name))

    @JvmStatic
    fun blockName(locale: String, name: String): String? = get(locale, javaBlockKey(name))

    @JvmStatic
    fun itemNames(locale: String): Map<String, String> {
        val prefix = "item.${DecIsland.Companion.MOD_ID}."
        return javaTranslationsByLocale[locale.lowercase()]
            ?.mapNotNull { (key, value) ->
                if (!key.startsWith(prefix)) {
                    null
                } else {
                    key.removePrefix(prefix) to value
                }
            }
            ?.toMap(linkedMapOf())
            ?: emptyMap()
    }

    @JvmStatic
    fun javaTranslations(locale: String): Map<String, String> = javaTranslationsByLocale[locale.lowercase()] ?: emptyMap()

    @JvmStatic
    fun normalizeKey(key: String): String =
        when {
            key.startsWith(BEDROCK_ITEM_PREFIX) && key.endsWith(BEDROCK_NAME_SUFFIX) ->
                javaItemKey(key.removePrefix(BEDROCK_ITEM_PREFIX).removeSuffix(BEDROCK_NAME_SUFFIX))
            key.startsWith(BEDROCK_BLOCK_PREFIX) && key.endsWith(BEDROCK_NAME_SUFFIX) ->
                javaBlockKey(key.removePrefix(BEDROCK_BLOCK_PREFIX).removeSuffix(BEDROCK_NAME_SUFFIX))
            key.startsWith(BEDROCK_TEXT_PREFIX) && key.endsWith(BEDROCK_NAME_SUFFIX) ->
                "$JAVA_TEXT_PREFIX${key.removePrefix(BEDROCK_TEXT_PREFIX).removeSuffix(BEDROCK_NAME_SUFFIX)}"
            key.startsWith(BEDROCK_ENTITY_PREFIX) && key.endsWith(BEDROCK_NAME_SUFFIX) ->
                "$JAVA_ENTITY_PREFIX${key.removePrefix(BEDROCK_ENTITY_PREFIX).removeSuffix(BEDROCK_NAME_SUFFIX)}"
            else -> key
        }

    private fun load(path: String): Map<String, String> {
        val stream = BedrockLangResources::class.java.classLoader.getResourceAsStream(path) ?: return emptyMap()
        return BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8)).use { reader ->
            buildMap {
                reader.lineSequence()
                    .mapNotNull(::parseLine)
                    .forEach { (key, value) -> put(key, value) }
            }
        }
    }

    private fun parseLine(line: String): Pair<String, String>? {
        val trimmed = line.trim().removePrefix("\uFEFF")
        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
            return null
        }

        val separatorIndex = trimmed.indexOf('=')
        if (separatorIndex <= 0) {
            return null
        }

        val key = trimmed.substring(0, separatorIndex).trim()
        val value = trimmed.substring(separatorIndex + 1).trim()
        if (key.isEmpty()) {
            return null
        }
        return key to value
    }

    private fun javaItemKey(name: String): String = "item.${DecIsland.Companion.MOD_ID}.$name"

    private fun javaBlockKey(name: String): String = "block.${DecIsland.Companion.MOD_ID}.$name"

    private const val BEDROCK_ITEM_PREFIX: String = "item.dec:"
    private const val BEDROCK_BLOCK_PREFIX: String = "tile.dec:"
    private const val BEDROCK_TEXT_PREFIX: String = "text.dec:"
    private const val BEDROCK_ENTITY_PREFIX: String = "entity.dec:"
    private const val BEDROCK_NAME_SUFFIX: String = ".name"
    private const val JAVA_TEXT_PREFIX: String = "text.${DecIsland.Companion.MOD_ID}."
    private const val JAVA_ENTITY_PREFIX: String = "entity.${DecIsland.Companion.MOD_ID}."
}