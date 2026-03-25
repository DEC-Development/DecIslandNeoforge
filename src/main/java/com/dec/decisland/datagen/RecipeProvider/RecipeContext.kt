package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.equipment.trim.TrimPattern

class RecipeContext(
    @JvmField val lookupProvider: HolderLookup.Provider,
    @JvmField val output: RecipeOutput,
) {
    @JvmField
    val items: HolderGetter<Item> = lookupProvider.lookupOrThrow(Registries.ITEM)

    @JvmField
    val trimPatterns: HolderGetter<TrimPattern> = lookupProvider.lookupOrThrow(Registries.TRIM_PATTERN)
}
