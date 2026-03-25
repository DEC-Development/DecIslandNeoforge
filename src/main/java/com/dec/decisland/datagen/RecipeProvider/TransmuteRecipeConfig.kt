package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

class TransmuteRecipeConfig private constructor(builder: Builder) : RecipeConfig(builder) {
    @JvmField
    val input: RecipeIngredient = builder.input ?: error("Recipe '$name' is missing an input ingredient")

    @JvmField
    val material: RecipeIngredient = builder.material ?: error("Recipe '$name' is missing a material ingredient")

    @JvmField
    val result: Item = builder.result ?: error("Recipe '$name' is missing a result item")

    class Builder(
        name: String,
    ) : RecipeConfig.Builder<Builder>(name) {
        internal var input: RecipeIngredient? = null
        internal var material: RecipeIngredient? = null
        internal var result: Item? = null

        fun input(item: ItemLike): Builder = input(RecipeIngredient.of(item))

        fun input(tag: TagKey<Item>): Builder = input(RecipeIngredient.tag(tag))

        fun input(ingredient: Ingredient): Builder = input(RecipeIngredient.custom(ingredient))

        fun input(ingredient: RecipeIngredient): Builder = apply {
            this.input = ingredient
        }

        fun material(item: ItemLike): Builder = material(RecipeIngredient.of(item))

        fun material(tag: TagKey<Item>): Builder = material(RecipeIngredient.tag(tag))

        fun material(ingredient: Ingredient): Builder = material(RecipeIngredient.custom(ingredient))

        fun material(ingredient: RecipeIngredient): Builder = apply {
            this.material = ingredient
        }

        fun result(result: Item): Builder = apply {
            this.result = result
        }

        fun build(): TransmuteRecipeConfig = TransmuteRecipeConfig(this)
    }
}
