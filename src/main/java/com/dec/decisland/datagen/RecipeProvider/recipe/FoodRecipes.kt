package com.dec.decisland.datagen.RecipeProvider.recipe

import com.dec.decisland.datagen.RecipeProvider.RecipeContext
import com.dec.decisland.datagen.RecipeProvider.RecipeDsl
import com.dec.decisland.datagen.RecipeProvider.ShapedRecipeConfig
import com.dec.decisland.item.category.Food
import com.dec.decisland.item.category.Material
import com.dec.decisland.item.ModItems
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.item.Items

object FoodRecipes {
    private val A_BOWL_OF_RICE: ShapedRecipeConfig = ShapedRecipeConfig.Builder("a_bowl_of_rice")
        .category(RecipeCategory.FOOD)
        .result(ModItems.A_BOWL_OF_RICE.get())
        .pattern(
            "###",
            " X ",
        )
        .define('#', ModItems.RICE.get())
        .define('X', Items.BOWL)
        .unlockedBy(ModItems.RICE.get())
        .build()

    private val RICE_WINE: ShapedRecipeConfig = ShapedRecipeConfig.Builder("rice_wine")
        .category(RecipeCategory.FOOD)
        .result(Food.RICE_WINE.get())
        .pattern(
            "X ",
            "# ",
        )
        .define('X', ModItems.RICE.get())
        .define('#', Material.WINE_GLASS.get())
        .unlockedBy(ModItems.RICE.get())
        .build()

    fun build(context: RecipeContext) {
        RecipeDsl.save(context, A_BOWL_OF_RICE)
        RecipeDsl.save(context, RICE_WINE)
    }
}
