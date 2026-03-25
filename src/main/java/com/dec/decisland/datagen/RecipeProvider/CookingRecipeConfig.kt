package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

class CookingRecipeConfig private constructor(builder: Builder) : RecipeConfig(builder) {
    enum class Type {
        SMELTING,
        BLASTING,
        SMOKING,
        CAMPFIRE,
    }

    @JvmField
    val type: Type = builder.type

    @JvmField
    val ingredient: RecipeIngredient = builder.ingredient ?: error("Recipe '$name' is missing an ingredient")

    @JvmField
    val result: ItemLike = builder.result ?: error("Recipe '$name' is missing a result")

    @JvmField
    val experience: Float = builder.experience

    @JvmField
    val cookingTime: Int = builder.cookingTime

    class Builder(
        name: String,
    ) : RecipeConfig.Builder<Builder>(name) {
        internal var type: Type = Type.SMELTING
        internal var ingredient: RecipeIngredient? = null
        internal var result: ItemLike? = null
        internal var experience: Float = 0.0f
        internal var cookingTime: Int = 200

        fun type(type: Type): Builder = apply {
            this.type = type
        }

        fun ingredient(item: ItemLike): Builder = ingredient(RecipeIngredient.of(item))

        fun ingredient(tag: TagKey<Item>): Builder = ingredient(RecipeIngredient.tag(tag))

        fun ingredient(ingredient: Ingredient): Builder = ingredient(RecipeIngredient.custom(ingredient))

        fun ingredient(ingredient: RecipeIngredient): Builder = apply {
            this.ingredient = ingredient
        }

        fun result(result: ItemLike): Builder = apply {
            this.result = result
        }

        fun experience(experience: Float): Builder = apply {
            this.experience = experience
        }

        fun cookingTime(cookingTime: Int): Builder = apply {
            this.cookingTime = cookingTime
        }

        fun build(): CookingRecipeConfig = CookingRecipeConfig(this)
    }
}
