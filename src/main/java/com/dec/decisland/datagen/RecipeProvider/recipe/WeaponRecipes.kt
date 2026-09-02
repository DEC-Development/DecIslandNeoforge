package com.dec.decisland.datagen.RecipeProvider.recipe

import com.dec.decisland.datagen.RecipeProvider.RecipeContext
import com.dec.decisland.datagen.RecipeProvider.CookingRecipeConfig
import com.dec.decisland.datagen.RecipeProvider.RecipeDsl
import com.dec.decisland.datagen.RecipeProvider.RecipeIngredient
import com.dec.decisland.datagen.RecipeProvider.ShapedRecipeConfig
import com.dec.decisland.item.category.Material
import com.dec.decisland.item.category.Weapon
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object WeaponRecipes {
    private const val DAGGER_EXPERIENCE: Float = 0.1f
    private const val BATTLEAXE_EXPERIENCE: Float = 0.1f

    private fun daggerRecipe(
        name: String,
        result: ItemLike,
        material: RecipeIngredient,
        unlock: ItemLike,
    ): ShapedRecipeConfig = ShapedRecipeConfig.Builder(name)
        .category(RecipeCategory.COMBAT)
        .result(result)
        .pattern(
            "A",
            "X",
        )
        .define('A', material)
        .define('X', Items.STICK)
        .unlockedBy(unlock)
        .build()

    private fun daggerNuggetRecipes(
        name: String,
        dagger: ItemLike,
        nugget: ItemLike,
    ): List<CookingRecipeConfig> = listOf(
        CookingRecipeConfig.Builder("furnace_$name")
            .category(RecipeCategory.MISC)
            .type(CookingRecipeConfig.Type.SMELTING)
            .ingredient(dagger)
            .result(nugget)
            .experience(DAGGER_EXPERIENCE)
            .cookingTime(200)
            .unlockedBy(dagger)
            .build(),
        CookingRecipeConfig.Builder("blast_furnace_$name")
            .category(RecipeCategory.MISC)
            .type(CookingRecipeConfig.Type.BLASTING)
            .ingredient(dagger)
            .result(nugget)
            .experience(DAGGER_EXPERIENCE)
            .cookingTime(100)
            .unlockedBy(dagger)
            .build(),
    )

    private fun battleaxeRecycleRecipes(
        name: String,
        battleaxe: ItemLike,
        ingot: ItemLike,
    ): List<CookingRecipeConfig> = listOf(
        CookingRecipeConfig.Builder("furnace_$name")
            .category(RecipeCategory.MISC)
            .type(CookingRecipeConfig.Type.SMELTING)
            .ingredient(battleaxe)
            .result(ingot)
            .experience(BATTLEAXE_EXPERIENCE)
            .cookingTime(200)
            .unlockedBy(battleaxe)
            .build(),
        CookingRecipeConfig.Builder("blast_furnace_$name")
            .category(RecipeCategory.MISC)
            .type(CookingRecipeConfig.Type.BLASTING)
            .ingredient(battleaxe)
            .result(ingot)
            .experience(BATTLEAXE_EXPERIENCE)
            .cookingTime(100)
            .unlockedBy(battleaxe)
            .build(),
    )

    fun build(context: RecipeContext) {
        RecipeDsl.save(
            context,
            daggerRecipe("wooden_dagger", Weapon.WOODEN_DAGGER.get(), RecipeIngredient.tag(ItemTags.WOODEN_TOOL_MATERIALS), Items.STICK),
        )
        RecipeDsl.save(
            context,
            daggerRecipe("wooden_dagger_from_crimson_planks", Weapon.WOODEN_DAGGER.get(), RecipeIngredient.of(Items.CRIMSON_PLANKS), Items.CRIMSON_PLANKS),
        )
        RecipeDsl.save(
            context,
            daggerRecipe("wooden_dagger_from_warped_planks", Weapon.WOODEN_DAGGER.get(), RecipeIngredient.of(Items.WARPED_PLANKS), Items.WARPED_PLANKS),
        )
        RecipeDsl.save(context, daggerRecipe("stone_dagger", Weapon.STONE_DAGGER.get(), RecipeIngredient.of(Items.COBBLESTONE), Items.COBBLESTONE))
        RecipeDsl.save(context, daggerRecipe("copper_dagger", Weapon.COPPER_DAGGER.get(), RecipeIngredient.of(Items.COPPER_INGOT), Items.COPPER_INGOT))
        RecipeDsl.save(context, daggerRecipe("iron_dagger", Weapon.IRON_DAGGER.get(), RecipeIngredient.of(Items.IRON_INGOT), Items.IRON_INGOT))
        RecipeDsl.save(context, daggerRecipe("gold_dagger", Weapon.GOLDEN_DAGGER.get(), RecipeIngredient.of(Items.GOLD_INGOT), Items.GOLD_INGOT))
        RecipeDsl.save(context, daggerRecipe("diamond_dagger", Weapon.DIAMOND_DAGGER.get(), RecipeIngredient.of(Items.DIAMOND), Items.DIAMOND))
        RecipeDsl.save(context, daggerRecipe("netherite_dagger", Weapon.NETHERITE_DAGGER.get(), RecipeIngredient.of(Items.NETHERITE_INGOT), Items.NETHERITE_INGOT))
        RecipeDsl.save(context, daggerRecipe("emerald_dagger", Weapon.EMERALD_DAGGER.get(), RecipeIngredient.of(Items.EMERALD), Items.EMERALD))
        RecipeDsl.save(context, daggerRecipe("lapis_dagger", Weapon.LAPIS_DAGGER.get(), RecipeIngredient.of(Items.LAPIS_LAZULI), Items.LAPIS_LAZULI))
        RecipeDsl.save(context, daggerRecipe("amethyst_dagger", Weapon.AMETHYST_DAGGER.get(), RecipeIngredient.of(Items.AMETHYST_SHARD), Items.AMETHYST_SHARD))
        RecipeDsl.save(context, daggerRecipe("steel_dagger", Weapon.STEEL_DAGGER.get(), RecipeIngredient.of(Material.STEEL_INGOT.get()), Material.STEEL_INGOT.get()))
        RecipeDsl.save(context, daggerRecipe("ghost_dagger", Weapon.GHOST_DAGGER.get(), RecipeIngredient.of(Material.GHOST_INGOT.get()), Material.GHOST_INGOT.get()))
        RecipeDsl.save(context, daggerRecipe("wind_dagger", Weapon.WIND_DAGGER.get(), RecipeIngredient.of(Material.STREAM_STONE.get()), Material.STREAM_STONE.get()))
        RecipeDsl.save(context, daggerRecipe("leaves_dagger", Weapon.LEAVES_DAGGER.get(), RecipeIngredient.of(Material.EYE_OF_NATURE.get()), Material.EYE_OF_NATURE.get()))

        RecipeDsl.save(
            context,
            ShapedRecipeConfig.Builder("everlasting_winter_dagger")
                .category(RecipeCategory.COMBAT)
                .result(Weapon.EVERLASTING_WINTER_DAGGER.get())
                .pattern(
                    "X",
                    "A",
                    "B",
                )
                .define('X', Material.EVERLASTING_WINTER_INGOT.get())
                .define('A', Weapon.DIAMOND_DAGGER.get())
                .define('B', Material.EVERLASTING_WINTER_STICK.get())
                .unlockedBy(Material.EVERLASTING_WINTER_INGOT.get())
                .build(),
        )

        daggerNuggetRecipes("copper_dagger", Weapon.COPPER_DAGGER.get(), Items.COPPER_NUGGET).forEach { RecipeDsl.save(context, it) }
        daggerNuggetRecipes("diamond_dagger", Weapon.DIAMOND_DAGGER.get(), Material.DIAMOND_NUGGET.get()).forEach { RecipeDsl.save(context, it) }
        daggerNuggetRecipes("emerald_dagger", Weapon.EMERALD_DAGGER.get(), Material.EMERALD_NUGGET.get()).forEach { RecipeDsl.save(context, it) }
        daggerNuggetRecipes("golden_dagger", Weapon.GOLDEN_DAGGER.get(), Items.GOLD_NUGGET).forEach { RecipeDsl.save(context, it) }
        daggerNuggetRecipes("steel_dagger", Weapon.STEEL_DAGGER.get(), Material.STEEL_NUGGET.get()).forEach { RecipeDsl.save(context, it) }

        RecipeDsl.save(
            context,
            ShapedRecipeConfig.Builder("copper_battleaxe")
                .category(RecipeCategory.COMBAT)
                .result(Weapon.COPPER_BATTLEAXE.get())
                .pattern(
                    "XXX",
                    " #X",
                    " # ",
                )
                .define('X', Items.COPPER_INGOT)
                .define('#', Material.IRON_STICK.get())
                .unlockedBy(Items.COPPER_INGOT)
                .build(),
        )
        RecipeDsl.save(
            context,
            ShapedRecipeConfig.Builder("steel_battleaxe")
                .category(RecipeCategory.COMBAT)
                .result(Weapon.STEEL_BATTLEAXE.get())
                .pattern(
                    "BB",
                    "AB",
                    "X ",
                )
                .define('B', Material.STEEL_INGOT.get())
                .define('A', Items.IRON_AXE)
                .define('X', Material.IRON_STICK.get())
                .unlockedBy(Material.STEEL_INGOT.get())
                .build(),
        )
        RecipeDsl.save(
            context,
            ShapedRecipeConfig.Builder("thunder_rapier")
                .category(RecipeCategory.COMBAT)
                .result(Weapon.THUNDER_RAPIER.get())
                .pattern(
                    "DD#",
                    "B#D",
                    "XB#",
                )
                .define('D', Items.COPPER_BLOCK)
                .define('B', Items.LAPIS_LAZULI)
                .define('#', Material.LIGHTNING_STONE.get())
                .define('X', Material.IRON_STICK.get())
                .unlockedBy(Material.LIGHTNING_STONE.get())
                .build(),
        )
        RecipeDsl.save(
            context,
            ShapedRecipeConfig.Builder("ghost_sword")
                .category(RecipeCategory.COMBAT)
                .result(Weapon.GHOST_SWORD.get())
                .pattern(
                    "A",
                    "A",
                    "X",
                )
                .define('A', Material.GHOST_INGOT.get())
                .define('X', Material.ICE_ROD.get())
                .unlockedBy(Material.GHOST_INGOT.get())
                .build(),
        )
        RecipeDsl.save(
            context,
            ShapedRecipeConfig.Builder("growth")
                .category(RecipeCategory.COMBAT)
                .result(Weapon.GROWTH.get())
                .pattern(
                    "AXA",
                    "AXA",
                    "B#B",
                )
                .define('X', Material.PURE_INGOT.get())
                .define('A', Items.GLOW_BERRIES)
                .define('B', Items.VINE)
                .define('#', Material.IRON_STICK.get())
                .unlockedBy(Material.PURE_INGOT.get())
                .build(),
        )
        battleaxeRecycleRecipes("copper_battleaxe", Weapon.COPPER_BATTLEAXE.get(), Items.COPPER_INGOT).forEach { RecipeDsl.save(context, it) }
        battleaxeRecycleRecipes("steel_battleaxe", Weapon.STEEL_BATTLEAXE.get(), Material.STEEL_INGOT.get()).forEach { RecipeDsl.save(context, it) }
    }
}
