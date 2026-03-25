package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

class StonecuttingRecipeConfig private constructor(builder: Builder) : RecipeConfig(builder) {
    @JvmField
    val ingredient: RecipeIngredient = builder.ingredient ?: error("Recipe '$name' is missing an ingredient")

    @JvmField
    val result: ItemLike = builder.result ?: error("Recipe '$name' is missing a result")

    @JvmField
    val count: Int = builder.count

    class Builder(
        name: String,
    ) : RecipeConfig.Builder<Builder>(name) {
        internal var ingredient: RecipeIngredient? = null
        internal var result: ItemLike? = null
        internal var count: Int = 1

        fun ingredient(item: ItemLike): Builder = ingredient(RecipeIngredient.of(item))

        fun ingredient(tag: TagKey<Item>): Builder = ingredient(RecipeIngredient.tag(tag))

        fun ingredient(ingredient: Ingredient): Builder = ingredient(RecipeIngredient.custom(ingredient))

        fun ingredient(ingredient: RecipeIngredient): Builder = apply {
            this.ingredient = ingredient
        }

        fun result(result: ItemLike): Builder = apply {
            this.result = result
        }

        fun count(count: Int): Builder = apply {
            this.count = count
        }

        fun build(): StonecuttingRecipeConfig = StonecuttingRecipeConfig(this)
    }
}
