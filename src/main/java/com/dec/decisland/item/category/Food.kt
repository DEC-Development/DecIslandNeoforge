package com.dec.decisland.item.category

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.custom.GlintItem
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.Consumables
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

object Food {
    private fun registerConsumable(
        name: String,
        enUs: String,
        zhCn: String,
        nutrition: Int,
        saturation: Float,
        alwaysEat: Boolean = false,
        drink: Boolean = false,
        glint: Boolean = false,
        stackSize: Int = 64,
        convertTo: Supplier<Item>? = null,
        cooldown: Float? = null,
        effects: List<ApplyStatusEffectsConsumeEffect> = emptyList(),
    ): DeferredItem<Item> {
        val builder = ItemConfig.Builder(name, mapOf("en_us" to enUs, "zh_cn" to zhCn))
        if (glint) {
            builder.func(::GlintItem)
        }

        return ModItems.registerItem(
            builder
                .props {
                    var consumable = if (drink) Consumables.defaultDrink() else Consumables.defaultFood()
                    effects.forEach { effect ->
                        consumable = consumable.onConsume(effect)
                    }

                    var properties = Item.Properties()
                        .stacksTo(stackSize)
                        .food(FoodProperties(nutrition, saturation, alwaysEat), consumable.build())

                    if (convertTo != null) {
                        properties = properties.usingConvertsTo(convertTo.get())
                    }
                    if (cooldown != null) {
                        properties = properties.useCooldown(cooldown)
                    }

                    properties
                }
                .modelTemplate(ModelTemplates.FLAT_ITEM)
                .creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB)
                .build(),
        )
    }

    @JvmField
    val DIAMOND_APPLE: DeferredItem<Item> = registerConsumable(
        "diamond_apple",
        "Diamond Apple",
        "钻石苹果",
        nutrition = 4,
        saturation = 1.0f,
        alwaysEat = true,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 2)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.ABSORPTION, 180 * 20, 2)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.RESISTANCE, 120 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.FIRE_RESISTANCE, 120 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.STRENGTH, 180 * 20, 0)),
        ),
    )

    @JvmField
    val DIAMOND_APPLE_ENCHANTED: DeferredItem<Item> = registerConsumable(
        "diamond_apple_enchanted",
        "Enchanted Diamond Apple",
        "附魔钻石苹果",
        nutrition = 6,
        saturation = 1.0f,
        alwaysEat = true,
        glint = true,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 2)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.ABSORPTION, 180 * 20, 2)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.RESISTANCE, 120 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.FIRE_RESISTANCE, 120 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.STRENGTH, 180 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.NIGHT_VISION, 180 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.HASTE, 180 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.JUMP_BOOST, 180 * 20, 1)),
        ),
    )

    @JvmField
    val EMERALD_APPLE: DeferredItem<Item> = registerConsumable(
        "emerald_apple",
        "Emerald Apple",
        "绿宝石苹果",
        nutrition = 4,
        saturation = 1.0f,
        alwaysEat = true,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 180 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.HASTE, 180 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.SPEED, 180 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.NIGHT_VISION, 180 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.STRENGTH, 180 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.RESISTANCE, 180 * 20, 0)),
        ),
    )

    @JvmField
    val EMERALD_APPLE_ENCHANTED: DeferredItem<Item> = registerConsumable(
        "emerald_apple_enchanted",
        "Enchanted Emerald Apple",
        "附魔绿宝石苹果",
        nutrition = 4,
        saturation = 1.0f,
        alwaysEat = true,
        glint = true,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 180 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.HASTE, 180 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.SPEED, 180 * 20, 2)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.NIGHT_VISION, 180 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.STRENGTH, 180 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.RESISTANCE, 180 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.JUMP_BOOST, 180 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.SATURATION, 60 * 20, 0)),
        ),
    )

    @JvmField
    val IRON_APPLE: DeferredItem<Item> = registerConsumable(
        "iron_apple",
        "Iron Apple",
        "铁苹果",
        nutrition = 4,
        saturation = 1.0f,
        alwaysEat = true,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.ABSORPTION, 120 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.RESISTANCE, 120 * 20, 0)),
        ),
    )

    @JvmField
    val IRON_APPLE_ENCHANTED: DeferredItem<Item> = registerConsumable(
        "iron_apple_enchanted",
        "Enchanted Iron Apple",
        "附魔铁苹果",
        nutrition = 4,
        saturation = 1.0f,
        alwaysEat = true,
        glint = true,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.RESISTANCE, 120 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.ABSORPTION, 120 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.REGENERATION, 60 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.HEALTH_BOOST, 150 * 20, 1)),
        ),
    )

    @JvmField
    val LAPIS_APPLE: DeferredItem<Item> = registerConsumable(
        "lapis_apple",
        "Lapis Apple",
        "青金石苹果",
        nutrition = 4,
        saturation = 1.0f,
        alwaysEat = true,
        cooldown = 30.0f,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.RESISTANCE, 4 * 20, 6)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.SPEED, 20 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 0)),
        ),
    )

    @JvmField
    val REDSTONE_APPLE: DeferredItem<Item> = registerConsumable(
        "redstone_apple",
        "Redstone Apple",
        "红石苹果",
        nutrition = 4,
        saturation = 1.0f,
        alwaysEat = true,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.ABSORPTION, 120 * 20, 0)),
        ),
    )

    @JvmField
    val REDSTONE_APPLE_ENCHANTED: DeferredItem<Item> = registerConsumable(
        "redstone_apple_enchanted",
        "Enchanted Redstone Apple",
        "附魔红石苹果",
        nutrition = 4,
        saturation = 1.0f,
        alwaysEat = true,
        glint = true,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.ABSORPTION, 180 * 20, 2)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.NIGHT_VISION, 120 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.REGENERATION, 180 * 20, 0)),
        ),
    )

    @JvmField
    val URANIUM_APPLE: DeferredItem<Item> = registerConsumable(
        "uranium_apple",
        "Uranium Apple",
        "铀苹果",
        nutrition = 4,
        saturation = 1.0f,
        alwaysEat = true,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.HUNGER, 10 * 20, 1)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.POISON, 10 * 20, 2)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.STRENGTH, 10 * 20, 7)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.NAUSEA, 10 * 20, 0)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.SLOWNESS, 10 * 20, 2)),
        ),
    )

    @JvmField
    val HASTE_POTION: DeferredItem<Item> = registerConsumable(
        "haste_potion",
        "Haste Potion",
        "挖掘药水",
        nutrition = 0,
        saturation = 1.0f,
        alwaysEat = true,
        drink = true,
        glint = true,
        stackSize = 1,
        convertTo = Supplier { Items.GLASS_BOTTLE },
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.HASTE, 300 * 20, 0)),
        ),
    )

    @JvmField
    val RESISTANCE_POTION: DeferredItem<Item> = registerConsumable(
        "resistance_potion",
        "Resistance Potion",
        "抗性药水",
        nutrition = 0,
        saturation = 1.0f,
        alwaysEat = true,
        drink = true,
        glint = true,
        stackSize = 1,
        convertTo = Supplier { Items.GLASS_BOTTLE },
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.RESISTANCE, 300 * 20, 0)),
        ),
    )

    @JvmField
    val RICE_WINE: DeferredItem<Item> = registerConsumable(
        "rice_wine",
        "Rice Wine",
        "米酒",
        nutrition = 2,
        saturation = 0.4f,
        alwaysEat = true,
        drink = true,
        convertTo = Supplier { Material.WINE_GLASS.get() },
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.NAUSEA, 60 * 20, 0), 0.3f),
        ),
    )

    @JvmField
    val ICE_CREAM: DeferredItem<Item> = registerConsumable(
        "ice_cream",
        "Ice Cream",
        "冰淇淋",
        nutrition = 4,
        saturation = 0.6f,
    )

    @JvmField
    val MAGIC_ICE_CREAM: DeferredItem<Item> = registerConsumable(
        "magic_ice_cream",
        "Magic Ice Cream",
        "魔法冰淇淋",
        nutrition = 3,
        saturation = 0.5f,
        alwaysEat = true,
        cooldown = 60.0f,
        effects = listOf(
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.RESISTANCE, 4 * 20, 4)),
            ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.SLOWNESS, 4 * 20, 4)),
        ),
    )

    @JvmStatic
    fun load() {
    }
}
