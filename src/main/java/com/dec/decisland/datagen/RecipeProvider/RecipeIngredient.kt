package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.core.HolderGetter
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.tags.TagKey

sealed class RecipeIngredient {
    abstract fun toIngredient(items: HolderGetter<Item>): Ingredient

    data class ItemValue(
        val item: ItemLike,
    ) : RecipeIngredient() {
        override fun toIngredient(items: HolderGetter<Item>): Ingredient = Ingredient.of(item)
    }

    data class TagValue(
        val tag: TagKey<Item>,
    ) : RecipeIngredient() {
        override fun toIngredient(items: HolderGetter<Item>): Ingredient = Ingredient.of(items.getOrThrow(tag))
    }

    data class CustomValue(
        val ingredient: Ingredient,
    ) : RecipeIngredient() {
        override fun toIngredient(items: HolderGetter<Item>): Ingredient = ingredient
    }

    companion object {
        @JvmStatic
        fun of(item: ItemLike): RecipeIngredient = ItemValue(item)

        @JvmStatic
        fun tag(tag: TagKey<Item>): RecipeIngredient = TagValue(tag)

        @JvmStatic
        fun custom(ingredient: Ingredient): RecipeIngredient = CustomValue(ingredient)
    }
}

data class RecipeIngredientEntry(
    @JvmField val ingredient: RecipeIngredient,
    @JvmField val count: Int = 1,
)
