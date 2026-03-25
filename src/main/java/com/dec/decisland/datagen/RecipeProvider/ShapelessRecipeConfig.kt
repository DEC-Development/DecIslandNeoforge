package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

class ShapelessRecipeConfig private constructor(builder: Builder) : RecipeConfig(builder) {
    @JvmField
    val result: ItemLike = builder.result ?: error("Recipe '$name' is missing a result")

    @JvmField
    val count: Int = builder.count

    @JvmField
    val ingredients: List<RecipeIngredientEntry> = builder.ingredients.toList()

    class Builder(
        name: String,
    ) : RecipeConfig.Builder<Builder>(name) {
        internal var result: ItemLike? = null
        internal var count: Int = 1
        internal val ingredients: MutableList<RecipeIngredientEntry> = mutableListOf()

        fun result(result: ItemLike): Builder = apply {
            this.result = result
        }

        fun count(count: Int): Builder = apply {
            this.count = count
        }

        fun require(item: ItemLike, count: Int = 1): Builder = require(RecipeIngredient.of(item), count)

        fun require(tag: TagKey<Item>, count: Int = 1): Builder = require(RecipeIngredient.tag(tag), count)

        fun require(ingredient: Ingredient, count: Int = 1): Builder = require(RecipeIngredient.custom(ingredient), count)

        fun require(ingredient: RecipeIngredient, count: Int = 1): Builder = apply {
            this.ingredients.add(RecipeIngredientEntry(ingredient, count))
        }

        fun build(): ShapelessRecipeConfig = ShapelessRecipeConfig(this)
    }
}
