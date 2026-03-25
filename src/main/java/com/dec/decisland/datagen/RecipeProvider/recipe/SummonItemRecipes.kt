package com.dec.decisland.datagen.RecipeProvider.recipe

import com.dec.decisland.datagen.RecipeProvider.RecipeContext
import com.dec.decisland.datagen.RecipeProvider.RecipeDsl
import com.dec.decisland.datagen.RecipeProvider.ShapelessRecipeConfig
import com.dec.decisland.item.category.Material
import com.dec.decisland.item.category.SummonItem
import net.minecraft.data.recipes.RecipeCategory

object SummonItemRecipes {
    private val FROZEN_POWER: ShapelessRecipeConfig =
        ShapelessRecipeConfig.Builder("frozen_power")
            .category(RecipeCategory.MISC)
            .result(SummonItem.FROZEN_POWER.get())
            .require(Material.FROZEN_POWER_DEBRIS.get(), 7)
            .unlockedBy(Material.FROZEN_POWER_DEBRIS.get())
            .build()

    fun build(context: RecipeContext) {
        RecipeDsl.save(context, FROZEN_POWER)
    }
}
