package com.dec.decisland.datagen

import com.dec.decisland.datagen.RecipeProvider.RecipeContext
import com.dec.decisland.datagen.RecipeProvider.recipe.CookingRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.FishRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.FoodRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.MaterialRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.SummonItemRecipes
import com.dec.decisland.datagen.RecipeProvider.recipe.WeaponRecipes
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : RecipeProvider.Runner(output, lookupProvider) {
    override fun createRecipeProvider(
        lookupProvider: HolderLookup.Provider,
        recipeOutput: RecipeOutput,
    ): RecipeProvider = DecIslandRecipes(lookupProvider, recipeOutput)

    override fun getName(): String = "DecIsland Recipes"

    private class DecIslandRecipes(
        lookupProvider: HolderLookup.Provider,
        private val recipeOutput: RecipeOutput,
    ) : RecipeProvider(lookupProvider, recipeOutput) {
        override fun buildRecipes() {
            val context = RecipeContext(registries, recipeOutput)
            MaterialRecipes.build(context)
            FoodRecipes.build(context)
            FishRecipes.build(context)
            WeaponRecipes.build(context)
            SummonItemRecipes.build(context)
            CookingRecipes.build(context)
        }
    }
}
