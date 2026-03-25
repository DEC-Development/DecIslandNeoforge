package com.dec.decisland.datagen.RecipeProvider.recipe

import com.dec.decisland.datagen.RecipeProvider.RecipeContext
import com.dec.decisland.datagen.RecipeProvider.RecipeDsl
import com.dec.decisland.datagen.RecipeProvider.ShapelessRecipeConfig
import com.dec.decisland.item.category.Material
import net.minecraft.data.recipes.RecipeCategory

object MaterialRecipes {
    private val EVERLASTING_WINTER_STICK: ShapelessRecipeConfig =
        ShapelessRecipeConfig.Builder("everlasting_winter_stick")
            .category(RecipeCategory.MISC)
            .result(Material.EVERLASTING_WINTER_STICK.get())
            .require(Material.ICE_ROD.get())
            .require(Material.EVERLASTING_WINTER_INGOT.get())
            .unlockedBy(Material.EVERLASTING_WINTER_INGOT.get())
            .build()

    fun build(context: RecipeContext) {
        RecipeDsl.save(context, EVERLASTING_WINTER_STICK)
    }
}
