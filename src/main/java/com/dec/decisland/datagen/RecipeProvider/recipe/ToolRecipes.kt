package com.dec.decisland.datagen.RecipeProvider.recipe

import com.dec.decisland.datagen.RecipeProvider.CookingRecipeConfig
import com.dec.decisland.datagen.RecipeProvider.RecipeContext
import com.dec.decisland.datagen.RecipeProvider.RecipeDsl
import com.dec.decisland.datagen.RecipeProvider.ShapedRecipeConfig
import com.dec.decisland.item.category.Material
import com.dec.decisland.item.category.Tool
import com.dec.decisland.item.category.Weapon
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object ToolRecipes {
    private const val TOOL_EXPERIENCE: Float = 0.1f

    private fun pickaxeRecipe(
        name: String,
        result: ItemLike,
        head: ItemLike,
        handle: ItemLike,
    ): ShapedRecipeConfig = ShapedRecipeConfig.Builder(name)
        .category(RecipeCategory.TOOLS)
        .result(result)
        .pattern(
            "XXX",
            " # ",
            " # ",
        )
        .define('X', head)
        .define('#', handle)
        .unlockedBy(head)
        .build()

    private fun axeRecipe(
        name: String,
        result: ItemLike,
        head: ItemLike,
        handle: ItemLike,
    ): ShapedRecipeConfig = ShapedRecipeConfig.Builder(name)
        .category(RecipeCategory.TOOLS)
        .result(result)
        .pattern(
            "XX",
            "X#",
            " #",
        )
        .define('X', head)
        .define('#', handle)
        .unlockedBy(head)
        .build()

    private fun diamondToolNuggetRecipes(
        name: String,
        tool: ItemLike,
    ): List<CookingRecipeConfig> = listOf(
        CookingRecipeConfig.Builder("furnace_$name")
            .category(RecipeCategory.MISC)
            .type(CookingRecipeConfig.Type.SMELTING)
            .ingredient(tool)
            .result(Material.DIAMOND_NUGGET.get())
            .experience(TOOL_EXPERIENCE)
            .cookingTime(200)
            .unlockedBy(tool)
            .build(),
        CookingRecipeConfig.Builder("blast_furnace_$name")
            .category(RecipeCategory.MISC)
            .type(CookingRecipeConfig.Type.BLASTING)
            .ingredient(tool)
            .result(Material.DIAMOND_NUGGET.get())
            .experience(TOOL_EXPERIENCE)
            .cookingTime(100)
            .unlockedBy(tool)
            .build(),
    )

    fun build(context: RecipeContext) {
        RecipeDsl.save(context, pickaxeRecipe("amethyst_pickaxe", Tool.AMETHYST_PICKAXE.get(), Items.AMETHYST_SHARD, Items.STICK))
        RecipeDsl.save(context, pickaxeRecipe("coral_pickaxe", Tool.CORAL_PICKAXE.get(), Material.CORAL_INGOT.get(), Weapon.SHARP_CORAL.get()))
        RecipeDsl.save(context, pickaxeRecipe("emerald_pickaxe", Tool.EMERALD_PICKAXE.get(), Items.EMERALD, Items.STICK))
        RecipeDsl.save(context, pickaxeRecipe("glass_pickaxe", Tool.GLASS_PICKAXE.get(), Material.GLASS_INGOT.get(), Items.STICK))
        RecipeDsl.save(context, pickaxeRecipe("slime_pickaxe", Tool.SLIME_PICKAXE.get(), Items.SLIME_BALL, Items.STICK))

        RecipeDsl.save(context, axeRecipe("amethyst_axe", Tool.AMETHYST_AXE.get(), Items.AMETHYST_SHARD, Items.STICK))
        RecipeDsl.save(context, axeRecipe("coral_axe", Tool.CORAL_AXE.get(), Material.CORAL_INGOT.get(), Weapon.SHARP_CORAL.get()))
        RecipeDsl.save(context, axeRecipe("emerald_axe", Tool.EMERALD_AXE.get(), Items.EMERALD, Items.STICK))
        RecipeDsl.save(context, axeRecipe("glass_axe", Tool.GLASS_AXE.get(), Material.GLASS_INGOT.get(), Items.STICK))
        RecipeDsl.save(context, axeRecipe("slime_axe", Tool.SLIME_AXE.get(), Items.SLIME_BALL, Items.STICK))

        diamondToolNuggetRecipes("diamond_pickaxe", Items.DIAMOND_PICKAXE).forEach { RecipeDsl.save(context, it) }
        diamondToolNuggetRecipes("diamond_axe", Items.DIAMOND_AXE).forEach { RecipeDsl.save(context, it) }
    }
}
