package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder
import net.minecraft.data.recipes.SmithingTrimRecipeBuilder
import net.minecraft.data.recipes.SpecialRecipeBuilder
import net.minecraft.data.recipes.TransmuteRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.core.registries.Registries

object RecipeDsl {
    fun save(
        context: RecipeContext,
        config: RecipeConfig,
    ) {
        when (config) {
            is ShapedRecipeConfig -> saveShaped(context, config)
            is ShapelessRecipeConfig -> saveShapeless(context, config)
            is CookingRecipeConfig -> saveCooking(context, config)
            is StonecuttingRecipeConfig -> saveStonecutting(context, config)
            is SmithingTransformRecipeConfig -> saveSmithingTransform(context, config)
            is SmithingTrimRecipeConfig -> saveSmithingTrim(context, config)
            is TransmuteRecipeConfig -> saveTransmute(context, config)
            is SpecialRecipeConfig -> saveSpecial(context, config)
        }
    }

    private fun saveShaped(
        context: RecipeContext,
        config: ShapedRecipeConfig,
    ) {
        val builder = ShapedRecipeBuilder.shaped(context.items, config.category, config.result, config.count)
        config.pattern.forEach(builder::pattern)
        config.keys.forEach { (key, ingredient) ->
            builder.define(key, ingredient.toIngredient(context.items))
        }
        applyCommon(builder, context, config)
    }

    private fun saveShapeless(
        context: RecipeContext,
        config: ShapelessRecipeConfig,
    ) {
        val builder = ShapelessRecipeBuilder.shapeless(context.items, config.category, config.result, config.count)
        config.ingredients.forEach { entry ->
            builder.requires(entry.ingredient.toIngredient(context.items), entry.count)
        }
        applyCommon(builder, context, config)
    }

    private fun saveCooking(
        context: RecipeContext,
        config: CookingRecipeConfig,
    ) {
        val ingredient = config.ingredient.toIngredient(context.items)
        val builder = when (config.type) {
            CookingRecipeConfig.Type.SMELTING ->
                SimpleCookingRecipeBuilder.smelting(ingredient, config.category, config.result, config.experience, config.cookingTime)
            CookingRecipeConfig.Type.BLASTING ->
                SimpleCookingRecipeBuilder.blasting(ingredient, config.category, config.result, config.experience, config.cookingTime)
            CookingRecipeConfig.Type.SMOKING ->
                SimpleCookingRecipeBuilder.smoking(ingredient, config.category, config.result, config.experience, config.cookingTime)
            CookingRecipeConfig.Type.CAMPFIRE ->
                SimpleCookingRecipeBuilder.campfireCooking(ingredient, config.category, config.result, config.experience, config.cookingTime)
        }
        applyCommon(builder, context, config)
    }

    private fun saveStonecutting(
        context: RecipeContext,
        config: StonecuttingRecipeConfig,
    ) {
        val builder = SingleItemRecipeBuilder.stonecutting(
            config.ingredient.toIngredient(context.items),
            config.category,
            config.result,
            config.count,
        )
        applyCommon(builder, context, config)
    }

    private fun saveSmithingTransform(
        context: RecipeContext,
        config: SmithingTransformRecipeConfig,
    ) {
        val builder = SmithingTransformRecipeBuilder.smithing(
            config.template.toIngredient(context.items),
            config.base.toIngredient(context.items),
            config.addition.toIngredient(context.items),
            config.category,
            config.result,
        )
        config.unlockCriteria.forEach { unlock ->
            builder.unlocks(unlock.name, unlock.build(context.items))
        }
        builder.save(context.output, recipeKey(config))
    }

    private fun saveSmithingTrim(
        context: RecipeContext,
        config: SmithingTrimRecipeConfig,
    ) {
        val builder = SmithingTrimRecipeBuilder.smithingTrim(
            config.template.toIngredient(context.items),
            config.base.toIngredient(context.items),
            config.addition.toIngredient(context.items),
            context.trimPatterns.getOrThrow(config.trimPattern),
            config.category,
        )
        config.unlockCriteria.forEach { unlock ->
            builder.unlocks(unlock.name, unlock.build(context.items))
        }
        builder.save(context.output, recipeKey(config))
    }

    private fun saveTransmute(
        context: RecipeContext,
        config: TransmuteRecipeConfig,
    ) {
        val builder = TransmuteRecipeBuilder.transmute(
            config.category,
            config.input.toIngredient(context.items),
            config.material.toIngredient(context.items),
            config.result,
        )
        applyCommon(builder, context, config)
    }

    private fun saveSpecial(
        context: RecipeContext,
        config: SpecialRecipeConfig,
    ) {
        SpecialRecipeBuilder.special(config.factory).save(context.output, recipeKey(config))
    }

    private fun applyCommon(
        builder: RecipeBuilder,
        context: RecipeContext,
        config: RecipeConfig,
    ) {
        if (config.group != null) {
            builder.group(config.group)
        }
        config.unlockCriteria.forEach { unlock ->
            builder.unlockedBy(unlock.name, unlock.build(context.items))
        }
        builder.save(context.output, recipeKey(config))
    }

    private fun recipeKey(config: RecipeConfig): ResourceKey<net.minecraft.world.item.crafting.Recipe<*>> =
        ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath("decisland", config.name))
}
