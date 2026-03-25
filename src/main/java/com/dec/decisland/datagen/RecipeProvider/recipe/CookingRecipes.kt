package com.dec.decisland.datagen.RecipeProvider.recipe

import com.dec.decisland.datagen.RecipeProvider.CookingRecipeConfig
import com.dec.decisland.datagen.RecipeProvider.RecipeContext
import com.dec.decisland.datagen.RecipeProvider.RecipeDsl
import com.dec.decisland.item.category.Fish
import net.minecraft.data.recipes.RecipeCategory

object CookingRecipes {
    private const val DEFAULT_FOOD_EXPERIENCE = 0.35f

    private val PERCH_SMELTING: CookingRecipeConfig = CookingRecipeConfig.Builder("perch_cooked_from_smelting")
        .category(RecipeCategory.FOOD)
        .type(CookingRecipeConfig.Type.SMELTING)
        .ingredient(Fish.PERCH.get())
        .result(Fish.PERCH_COOKED.get())
        .experience(DEFAULT_FOOD_EXPERIENCE)
        .cookingTime(200)
        .unlockedBy(Fish.PERCH.get())
        .build()

    private val PERCH_SMOKING: CookingRecipeConfig = CookingRecipeConfig.Builder("perch_cooked_from_smoking")
        .category(RecipeCategory.FOOD)
        .type(CookingRecipeConfig.Type.SMOKING)
        .ingredient(Fish.PERCH.get())
        .result(Fish.PERCH_COOKED.get())
        .experience(DEFAULT_FOOD_EXPERIENCE)
        .cookingTime(100)
        .unlockedBy(Fish.PERCH.get())
        .build()

    private val PERCH_CAMPFIRE: CookingRecipeConfig = CookingRecipeConfig.Builder("perch_cooked_from_campfire_cooking")
        .category(RecipeCategory.FOOD)
        .type(CookingRecipeConfig.Type.CAMPFIRE)
        .ingredient(Fish.PERCH.get())
        .result(Fish.PERCH_COOKED.get())
        .experience(DEFAULT_FOOD_EXPERIENCE)
        .cookingTime(600)
        .unlockedBy(Fish.PERCH.get())
        .build()

    private val CRAB_LEG_SMELTING: CookingRecipeConfig = CookingRecipeConfig.Builder("crab_leg_cooked_from_smelting")
        .category(RecipeCategory.FOOD)
        .type(CookingRecipeConfig.Type.SMELTING)
        .ingredient(Fish.CRAB_LEG.get())
        .result(Fish.CRAB_LEG_COOKED.get())
        .experience(DEFAULT_FOOD_EXPERIENCE)
        .cookingTime(200)
        .unlockedBy(Fish.CRAB_LEG.get())
        .build()

    private val CRAB_LEG_BLASTING: CookingRecipeConfig = CookingRecipeConfig.Builder("crab_leg_cooked_from_blasting")
        .category(RecipeCategory.FOOD)
        .type(CookingRecipeConfig.Type.BLASTING)
        .ingredient(Fish.CRAB_LEG.get())
        .result(Fish.CRAB_LEG_COOKED.get())
        .experience(DEFAULT_FOOD_EXPERIENCE)
        .cookingTime(100)
        .unlockedBy(Fish.CRAB_LEG.get())
        .build()

    private val CRAB_LEG_SMOKING: CookingRecipeConfig = CookingRecipeConfig.Builder("crab_leg_cooked_from_smoking")
        .category(RecipeCategory.FOOD)
        .type(CookingRecipeConfig.Type.SMOKING)
        .ingredient(Fish.CRAB_LEG.get())
        .result(Fish.CRAB_LEG_COOKED.get())
        .experience(DEFAULT_FOOD_EXPERIENCE)
        .cookingTime(100)
        .unlockedBy(Fish.CRAB_LEG.get())
        .build()

    private val CRAB_LEG_CAMPFIRE: CookingRecipeConfig = CookingRecipeConfig.Builder("crab_leg_cooked_from_campfire_cooking")
        .category(RecipeCategory.FOOD)
        .type(CookingRecipeConfig.Type.CAMPFIRE)
        .ingredient(Fish.CRAB_LEG.get())
        .result(Fish.CRAB_LEG_COOKED.get())
        .experience(DEFAULT_FOOD_EXPERIENCE)
        .cookingTime(600)
        .unlockedBy(Fish.CRAB_LEG.get())
        .build()

    fun build(context: RecipeContext) {
        RecipeDsl.save(context, PERCH_SMELTING)
        RecipeDsl.save(context, PERCH_SMOKING)
        RecipeDsl.save(context, PERCH_CAMPFIRE)
        RecipeDsl.save(context, CRAB_LEG_SMELTING)
        RecipeDsl.save(context, CRAB_LEG_BLASTING)
        RecipeDsl.save(context, CRAB_LEG_SMOKING)
        RecipeDsl.save(context, CRAB_LEG_CAMPFIRE)
    }
}
