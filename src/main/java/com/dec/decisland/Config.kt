package com.dec.decisland

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.common.ModConfigSpec

object Config {
    private val builder = ModConfigSpec.Builder()

    @JvmField
    val LOG_DIRT_BLOCK: ModConfigSpec.BooleanValue = builder
        .comment("Whether to log the dirt block on common setup")
        .define("logDirtBlock", true)

    @JvmField
    val MAGIC_NUMBER: ModConfigSpec.IntValue = builder
        .comment("A magic number")
        .defineInRange("magicNumber", 42, 0, Int.MAX_VALUE)

    @JvmField
    val MAGIC_NUMBER_INTRODUCTION: ModConfigSpec.ConfigValue<String> = builder
        .comment("What you want the introduction message to be for the magic number")
        .define("magicNumberIntroduction", "The magic number is... ")

    @JvmField
    val ITEM_STRINGS: ModConfigSpec.ConfigValue<List<String>> = builder
        .comment("A list of items to log on common setup.")
        .defineListAllowEmpty("items", listOf("minecraft:iron_ingot"), { "" }, ::validateItemName)

    @JvmField
    val SPEC: ModConfigSpec = builder.build()

    private fun validateItemName(obj: Any): Boolean =
        obj is String && BuiltInRegistries.ITEM.containsKey(Identifier.parse(obj))
}
