package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Recipe
import java.util.function.Function

class SpecialRecipeConfig private constructor(builder: Builder) : RecipeConfig(builder) {
    @JvmField
    val factory: Function<CraftingBookCategory, Recipe<*>> =
        builder.factory ?: error("Recipe '$name' is missing a special recipe factory")

    class Builder(
        name: String,
    ) : RecipeConfig.Builder<Builder>(name) {
        internal var factory: Function<CraftingBookCategory, Recipe<*>>? = null

        fun factory(factory: Function<CraftingBookCategory, Recipe<*>>): Builder = apply {
            this.factory = factory
        }

        fun build(): SpecialRecipeConfig = SpecialRecipeConfig(this)
    }
}
