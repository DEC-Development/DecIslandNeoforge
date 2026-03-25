package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.equipment.trim.TrimPattern
import net.minecraft.world.level.ItemLike

class SmithingTrimRecipeConfig private constructor(builder: Builder) : RecipeConfig(builder) {
    @JvmField
    val template: RecipeIngredient = builder.template ?: error("Recipe '$name' is missing a template ingredient")

    @JvmField
    val base: RecipeIngredient = builder.base ?: error("Recipe '$name' is missing a base ingredient")

    @JvmField
    val addition: RecipeIngredient = builder.addition ?: error("Recipe '$name' is missing an addition ingredient")

    @JvmField
    val trimPattern: ResourceKey<TrimPattern> = builder.trimPattern ?: error("Recipe '$name' is missing a trim pattern")

    class Builder(
        name: String,
    ) : RecipeConfig.Builder<Builder>(name) {
        internal var template: RecipeIngredient? = null
        internal var base: RecipeIngredient? = null
        internal var addition: RecipeIngredient? = null
        internal var trimPattern: ResourceKey<TrimPattern>? = null

        fun template(item: ItemLike): Builder = template(RecipeIngredient.of(item))

        fun template(tag: TagKey<Item>): Builder = template(RecipeIngredient.tag(tag))

        fun template(ingredient: Ingredient): Builder = template(RecipeIngredient.custom(ingredient))

        fun template(ingredient: RecipeIngredient): Builder = apply {
            this.template = ingredient
        }

        fun base(item: ItemLike): Builder = base(RecipeIngredient.of(item))

        fun base(tag: TagKey<Item>): Builder = base(RecipeIngredient.tag(tag))

        fun base(ingredient: Ingredient): Builder = base(RecipeIngredient.custom(ingredient))

        fun base(ingredient: RecipeIngredient): Builder = apply {
            this.base = ingredient
        }

        fun addition(item: ItemLike): Builder = addition(RecipeIngredient.of(item))

        fun addition(tag: TagKey<Item>): Builder = addition(RecipeIngredient.tag(tag))

        fun addition(ingredient: Ingredient): Builder = addition(RecipeIngredient.custom(ingredient))

        fun addition(ingredient: RecipeIngredient): Builder = apply {
            this.addition = ingredient
        }

        fun trimPattern(trimPattern: ResourceKey<TrimPattern>): Builder = apply {
            this.trimPattern = trimPattern
        }

        fun build(): SmithingTrimRecipeConfig = SmithingTrimRecipeConfig(this)
    }
}
