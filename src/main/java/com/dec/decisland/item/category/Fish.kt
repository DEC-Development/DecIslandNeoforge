package com.dec.decisland.item.category

import com.dec.decisland.item.CustomItemProperties
import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.custom.AshPufferfish
import com.dec.decisland.item.custom.EnderFish
import com.dec.decisland.item.custom.SwordFish
import com.dec.decisland.tag.ModItemTags
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.component.Consumables
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

object Fish {
    private fun registerFish(
        name: String,
        enUs: String,
        zhCn: String,
        props: Supplier<Item.Properties>,
        tab: Supplier<net.minecraft.world.item.CreativeModeTab> = ModCreativeModeTabs.DECISLAND_FOODS_TAB,
        modelTemplate: net.minecraft.client.data.models.model.ModelTemplate = ModelTemplates.FLAT_ITEM,
        customProp: CustomItemProperties = CustomItemProperties.Builder().build(),
        func: java.util.function.Function<Item.Properties, out Item> = java.util.function.Function(::Item),
        tags: List<TagKey<Item>> = emptyList(),
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name, mapOf("en_us" to enUs, "zh_cn" to zhCn))
                .func(func)
                .props(props)
                .customProp(customProp)
                .tags(tags)
                .modelTemplate(modelTemplate)
                .creativeTab(tab)
                .build(),
        )

    private fun foodProps(
        nutrition: Int,
        saturation: Float,
        alwaysEat: Boolean = false,
        useDurationSeconds: Float = 1.6f,
        convertTo: Item? = null,
        consumeEffects: List<ApplyStatusEffectsConsumeEffect> = emptyList(),
        configureProps: (Item.Properties.() -> Item.Properties)? = null,
    ): Supplier<Item.Properties> = Supplier {
        val consumable = Consumables.defaultFood()
        if (useDurationSeconds != 1.6f) {
            consumable.consumeSeconds(useDurationSeconds)
        }
        consumeEffects.forEach(consumable::onConsume)

        var props = Item.Properties().food(
            FoodProperties(nutrition, saturation, alwaysEat),
            consumable.build(),
        )
        if (convertTo != null) {
            props = props.usingConvertsTo(convertTo)
        }
        if (configureProps != null) {
            props = configureProps.invoke(props)
        }
        props
    }

    @JvmField
    val ASH_PUFFERFISH: DeferredItem<Item> = registerFish(
        "ash_pufferfish",
        "Inflamed Ash Pufferfish",
        "红肿灰烬豚",
        props = Supplier { Item.Properties().stacksTo(16).useCooldown(3.0f) },
        tab = ModCreativeModeTabs.DECISLAND_MISC_TAB,
        func = java.util.function.Function(::AshPufferfish),
        tags = listOf(ModItemTags.THROWN_WEAPON),
    )

    @JvmField
    val BLUE_JELLYFISH: DeferredItem<Item> = registerFish(
        "blue_jellyfish",
        "Blue Jellyfish",
        "蓝水母",
        foodProps(
            2,
            0.2f,
            alwaysEat = true,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.POISON, 5 * 20, 2), 0.7f),
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 0), 0.5f),
            ),
        ),
    )

    @JvmField
    val MOON_JELLYFISH: DeferredItem<Item> = registerFish(
        "moon_jellyfish",
        "Moon Jellyfish",
        "海月水母",
        foodProps(
            2,
            0.2f,
            alwaysEat = true,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.POISON, 5 * 20, 2), 0.7f),
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 0), 0.75f),
            ),
        ),
    )

    @JvmField
    val PINK_JELLYFISH: DeferredItem<Item> = registerFish(
        "pink_jellyfish",
        "Pink Jellyfish",
        "粉水母",
        foodProps(
            2,
            0.2f,
            alwaysEat = true,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.POISON, 5 * 20, 3), 0.7f),
            ),
        ),
    )

    @JvmField
    val YELLOW_JELLYFISH: DeferredItem<Item> = registerFish(
        "yellow_jellyfish",
        "Yellow Jellyfish",
        "黄水母",
        foodProps(
            2,
            0.2f,
            alwaysEat = true,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.POISON, 5 * 20, 2), 0.7f),
            ),
        ),
    )

    @JvmField
    val COAL_FISH: DeferredItem<Item> = registerFish(
        "coal_fish",
        "Coal Fish",
        "煤炭鱼",
        foodProps(3, 0.4f),
        customProp = CustomItemProperties.Builder().burnTime(8).build(),
    )

    @JvmField
    val PERCH: DeferredItem<Item> = registerFish(
        "perch",
        "Perch",
        "鲈鱼",
        foodProps(3, 0.6f),
    )

    @JvmField
    val PERCH_COOKED: DeferredItem<Item> = registerFish(
        "perch_cooked",
        "Cooked Perch",
        "烤鲈鱼",
        foodProps(6, 0.8f),
    )

    @JvmField
    val TROPICAL_FISH: DeferredItem<Item> = registerFish(
        "tropical_fish",
        "Tropical Fish",
        "热带鱼",
        foodProps(2, 0.6f),
    )

    @JvmField
    val TROPICAL_FISH_COOKED: DeferredItem<Item> = registerFish(
        "tropical_fish_cooked",
        "Cooked Tropical Fish",
        "烤熟的热带鱼",
        foodProps(4, 0.6f),
    )

    @JvmField
    val SARDINE: DeferredItem<Item> = registerFish(
        "sardine",
        "Sardine",
        "沙丁鱼",
        foodProps(2, 0.6f),
    )

    @JvmField
    val SNAILFISH: DeferredItem<Item> = registerFish(
        "snailfish",
        "Snailfish",
        "狮子鱼",
        foodProps(
            3,
            0.6f,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.POISON, 20 * 20, 1), 0.8f),
            ),
        ),
    )

    @JvmField
    val LAMPREY: DeferredItem<Item> = registerFish(
        "lamprey",
        "Lamprey",
        "七鳃鳗",
        foodProps(
            3,
            0.6f,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 1), 0.7f),
            ),
        ),
    )

    @JvmField
    val MUDDY_FISH: DeferredItem<Item> = registerFish(
        "muddy_fish",
        "Muddy Fish",
        "泥鱼",
        foodProps(
            4,
            0.6f,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.SLOWNESS, 10 * 20, 0), 0.5f),
            ),
        ),
    )

    @JvmField
    val DIAMOND_FISH: DeferredItem<Item> = registerFish(
        "diamond_fish",
        "Diamond Fish",
        "钻石鱼",
        foodProps(8, 0.6f, useDurationSeconds = 8.0f),
    )

    @JvmField
    val EMERALD_FISH: DeferredItem<Item> = registerFish(
        "emerald_fish",
        "Emerald Fish",
        "绿宝石鱼",
        foodProps(6, 0.6f, useDurationSeconds = 4.0f),
    )

    @JvmField
    val ENDER_FISH: DeferredItem<Item> = registerFish(
        "ender_fish",
        "Ender Fish",
        "末影鱼",
        foodProps(3, 0.5f, alwaysEat = true, configureProps = { useCooldown(5.0f) }),
        func = java.util.function.Function(::EnderFish),
    )

    @JvmField
    val FROZEN_FISH: DeferredItem<Item> = registerFish(
        "frozen_fish",
        "Frozen Fish",
        "冰霜鱼",
        foodProps(
            4,
            0.5f,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.SLOWNESS, 30 * 20, 1)),
            ),
        ),
    )

    @JvmField
    val GOLD_FISH: DeferredItem<Item> = registerFish(
        "gold_fish",
        "Gold Fish",
        "金矿鱼",
        foodProps(5, 0.6f),
    )

    @JvmField
    val IRON_FISH: DeferredItem<Item> = registerFish(
        "iron_fish",
        "Iron Fish",
        "铁矿鱼",
        foodProps(4, 0.6f, useDurationSeconds = 4.0f),
    )

    @JvmField
    val POISON_FISH: DeferredItem<Item> = registerFish(
        "poison_fish",
        "Poison Fish",
        "剧毒鱼",
        foodProps(
            3,
            0.6f,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.POISON, 5 * 20, 3), 0.9f),
            ),
        ),
    )

    @JvmField
    val SKELETON_FISH: DeferredItem<Item> = registerFish(
        "skeleton_fish",
        "Skeleton Fish",
        "骷髅鱼",
        foodProps(2, 0.4f, convertTo = Items.BONE),
    )

    @JvmField
    val ZOMBIE_FISH: DeferredItem<Item> = registerFish(
        "zombie_fish",
        "Zombie Fish",
        "僵尸鱼",
        foodProps(
            2,
            0.6f,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.POISON, 10 * 20, 0), 0.3f),
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.HUNGER, 30 * 20, 0), 0.25f),
            ),
        ),
    )

    @JvmField
    val CRAB_LEG: DeferredItem<Item> = registerFish(
        "crab_leg",
        "Crab Leg",
        "螃蟹腿",
        foodProps(3, 0.5f),
    )

    @JvmField
    val CRAB_LEG_COOKED: DeferredItem<Item> = registerFish(
        "crab_leg_cooked",
        "Cooked Crab Leg",
        "烤熟的螃蟹腿",
        foodProps(5, 0.5f),
    )

    @JvmField
    val SEA_URCHIN: DeferredItem<Item> = registerFish(
        "sea_urchin",
        "Sea Urchin",
        "海胆",
        foodProps(
            3,
            0.5f,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.POISON, 5 * 20, 1)),
            ),
        ),
    )

    @JvmField
    val NAUTILUS_ALIVE: DeferredItem<Item> = registerFish(
        "nautilus_alive",
        "Nautilus Alive",
        "活的鹦鹉螺",
        foodProps(2, 0.4f, convertTo = Items.NAUTILUS_SHELL),
    )

    @JvmField
    val SWORD_FISH: DeferredItem<Item> = registerFish(
        "sword_fish",
        "Sword Fish",
        "剑鱼",
        props = foodProps(
            6,
            0.8f,
            alwaysEat = true,
            useDurationSeconds = 3.2f,
            consumeEffects = listOf(
                ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.STRENGTH, 150 * 20, 1)),
            ),
        ).let { supplier ->
            Supplier {
                supplier.get()
                    .sword(ToolMaterial.IRON, 3.0f, -2.4f)
                    .durability(256)
                    .stacksTo(1)
            }
        },
        tab = ModCreativeModeTabs.DECISLAND_WEAPONS_TAB,
        modelTemplate = ModelTemplates.FLAT_HANDHELD_ITEM,
        func = java.util.function.Function(::SwordFish),
        tags = listOf(ModItemTags.MELEE_WEAPON, ModItemTags.SWORD),
    )

    @JvmStatic
    fun load() {
    }
}

