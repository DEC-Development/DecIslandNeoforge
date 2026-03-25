package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.world.level.ItemLike
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

class ShapedRecipeConfig private constructor(builder: Builder) : RecipeConfig(builder) {
    @JvmField
    val result: ItemLike = builder.result ?: error("Recipe '$name' is missing a result")

    @JvmField
    val count: Int = builder.count

    @JvmField
    val pattern: List<String> = builder.pattern

    @JvmField
    val keys: Map<Char, RecipeIngredient> = builder.keys.toMap()

    class Builder(
        name: String,
    ) : RecipeConfig.Builder<Builder>(name) {
        internal var result: ItemLike? = null
        internal var count: Int = 1
        internal var pattern: List<String> = emptyList()
        internal val keys: LinkedHashMap<Char, RecipeIngredient> = linkedMapOf()

        fun result(result: ItemLike): Builder = apply {
            this.result = result
        }

        fun count(count: Int): Builder = apply {
            this.count = count
        }

        fun pattern(vararg pattern: String): Builder = apply {
            this.pattern = pattern.toList()
        }

        fun define(key: Char, item: ItemLike): Builder = define(key, RecipeIngredient.of(item))

        fun define(key: Char, tag: TagKey<Item>): Builder = define(key, RecipeIngredient.tag(tag))

        fun define(key: Char, ingredient: Ingredient): Builder = define(key, RecipeIngredient.custom(ingredient))

        fun define(key: Char, ingredient: RecipeIngredient): Builder = apply {
            this.keys[key] = ingredient
        }

        fun build(): ShapedRecipeConfig = ShapedRecipeConfig(this)
    }
}
