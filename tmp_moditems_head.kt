package com.dec.decisland.item

import com.dec.decisland.DecIsland
import com.dec.decisland.item.custom.*
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items.*
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.component.Consumables
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect
import net.minecraft.world.item.equipment.ArmorType
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {
    @JvmField
    val ITEMS: DeferredRegister.Items =
        DeferredRegister.createItems(DecIsland.MOD_ID)
    private val ITEM_CONFIGS = mutableListOf<ItemConfig>()
    //    public static final DeferredItem<Item> A_BOWL_OF_RICE =
//            ITEMS.registerSimpleItem("a_bowl_of_rice", props -> props.food(
//                    FoodProperties(10, 10, true),
//                    Consumables.defaultFood().build()
//            ))
    @JvmField
    val A_BOWL_OF_RICE: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("a_bowl_of_rice", mapOf("en_us" to "A Bowl of Rice", "zh_cn" to "米饭")).props { (Item.Properties()).food(
                    FoodProperties(10, 0.6F, false),
                    Consumables.defaultFood().build()
            ).usingConvertsTo(BOWL)}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )
    @JvmField
    val A_PIECE_OF_SALMON: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("a_piece_of_salmon", mapOf("en_us" to "a Piece of Salmon", "zh_cn" to "生鱼片")).props { (Item.Properties()).food(
                    FoodProperties(1, 0.4F, false),
                    Consumables.defaultFood().build()
            )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val APPLE_PIE: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("apple_pie", mapOf("en_us" to "Apple Pie", "zh_cn" to "苹果派")).props { (Item.Properties()).food(
                    FoodProperties(12, 0.6F, false),
                    Consumables.defaultFood().onConsume(ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 2))).build()
            )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val APPLE_JUICE: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("apple_juice", mapOf("en_us" to "Apple Juice", "zh_cn" to "苹果汁")).props { (Item.Properties()).food(
                    FoodProperties(2, 0.0f, false),
                    Consumables.defaultDrink().onConsume(ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 1))).build()
            )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val AMETHYST_AXE: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("amethyst_axe", mapOf("en_us" to "Amethyst Axe", "zh_cn" to "紫水晶斧")).func { p -> AxeItem(ToolMaterial.DIAMOND, 0.0F, -3.2F, p)}
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM).build()
    )

    @JvmField
    val ASH_PUFFERFISH: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("ash_pufferfish", mapOf("en_us" to "Ash Pufferfish", "zh_cn" to "灰烬豚")).func(::AshPufferfish)
                    .props { Item.Properties().stacksTo(16).useCooldown(3.0f)}
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB).build()
    )

    @JvmField
    val AUTUMN_LEAVES: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("autumn_leaves", mapOf("en_us" to "Autumn Leaves", "zh_cn" to "秋叶")).customProp(CustomItemProperties.Builder().compostableChance(0.3f).build())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB).build()
    )

    @JvmField
    val BIZARRE_CHILLI: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("bizarre_chilli", mapOf("en_us" to "Bizarre Chilli", "zh_cn" to "奇异椒")).creativeTab(ModCreativeModeTabs.DECISLAND_CROPS_TAB).build()
    )

    @JvmField
    val BLOOD_WORM: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("blood_worm", mapOf("en_us" to "Blood Worm", "zh_cn" to "血蠕虫")).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val BRACKEN: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("bracken", mapOf("en_us" to "Bracken", "zh_cn" to "蕨菜")).props { (Item.Properties()).food(
                            FoodProperties(1, 0.1F, false),
                            Consumables.defaultFood().build()
                    )}.customProp(CustomItemProperties.Builder().compostableChance(0.3f).build())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val BLUE_JELLYFISH: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("blue_jellyfish", mapOf("en_us" to "Blue Jellyfish", "zh_cn" to "蓝水母")).props { (Item.Properties()).food(
                    FoodProperties(2, 0.2f, true),
                    Consumables.defaultFood()
                            .onConsume(ApplyStatusEffectsConsumeEffect(
                                    MobEffectInstance(MobEffects.POISON, 5 * 20, 2), 0.7f))
                            .onConsume(ApplyStatusEffectsConsumeEffect(
                                    MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 0), 0.5f))
                            .build()
            )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )


    @JvmField
    val MOON_JELLYFISH: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("moon_jellyfish", mapOf("en_us" to "Moon Jellyfish", "zh_cn" to "海月水母")).props { (Item.Properties()).food(
                    FoodProperties(2, 0.2f, true),
                    Consumables.defaultFood()
                            .onConsume(ApplyStatusEffectsConsumeEffect(
                                    MobEffectInstance(MobEffects.POISON, 5 * 20, 2), 0.7f))
                            .onConsume(ApplyStatusEffectsConsumeEffect(
                                    MobEffectInstance(MobEffects.INSTANT_DAMAGE, 1, 0), 0.75f))
                            .build()
            )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val PINK_JELLYFISH: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("pink_jellyfish", mapOf("en_us" to "Pink Jellyfish", "zh_cn" to "粉水母")).props { (Item.Properties()).food(
                    FoodProperties(2, 0.2f, true),
                    Consumables.defaultFood()
                            .onConsume(ApplyStatusEffectsConsumeEffect(
                                    MobEffectInstance(MobEffects.POISON, 5 * 20, 3), 0.7f))
                            .build()
            )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val YELLOW_JELLYFISH: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("yellow_jellyfish", mapOf("en_us" to "Yellow Jellyfish", "zh_cn" to "黄水母")).props { (Item.Properties()).food(
                    FoodProperties(2, 0.2f, true),
                    Consumables.defaultFood()
                            .onConsume(ApplyStatusEffectsConsumeEffect(
                                    MobEffectInstance(MobEffects.POISON, 5 * 20, 2), 0.7f))
                            .build()
            )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val CANDY: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("candy", mapOf("en_us" to "Candy", "zh_cn" to "糖果")).props { (Item.Properties()).food(
                    FoodProperties(1, 0.4f, true),
                    Consumables.defaultFood().build()
            )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val CHOCOLATE_COOKIE: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("chocolate_cookie", mapOf("en_us" to "Chocolate Cookie", "zh_cn" to "巧克力曲奇")).props { (Item.Properties()).food(
                    FoodProperties(3, 0.6f, false),
                    Consumables.defaultFood().build()
            )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val CHOCOLATES: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("chocolates", mapOf("en_us" to "Chocolates", "zh_cn" to "巧克力")).props { (Item.Properties()).food(
                    FoodProperties(7, 0.6f, false),
                    Consumables.defaultFood().onConsume(ApplyStatusEffectsConsumeEffect(
                            MobEffectInstance(MobEffects.SPEED, 20 * 20, 0), 0.73f)).build()
            ).usingConvertsTo(PAPER)}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val COAL_FISH: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("coal_fish", mapOf("en_us" to "Coal Fish", "zh_cn" to "煤炭鱼")).props { (Item.Properties()).food(
                    FoodProperties(3, 0.4f, false),
                    Consumables.defaultFood().build()
            )}.customProp(
                    CustomItemProperties.Builder().burnTime(8).build()
            ).creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val COB: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("cob", mapOf("en_us" to "Cob", "zh_cn" to "玉米棒")).customProp(CustomItemProperties.Builder().compostableChance(0.3f).build())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_CROPS_TAB).build()
    )


    @JvmField
    val AMETHYST_HELMET: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("amethyst_helmet", mapOf("en_us" to "Amethyst Helmet", "zh_cn" to "紫水晶头盔"))
                    .props { (Item.Properties().humanoidArmor(ModArmorMaterials.AMETHYST, ArmorType.HELMET)
                    )}
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    )

    @JvmField
    val AMETHYST_CHESTPLATE: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("amethyst_chestplate", mapOf("en_us" to "Amethyst Chestplate", "zh_cn" to "紫水晶胸甲"))
                    .props { (Item.Properties().humanoidArmor(ModArmorMaterials.AMETHYST, ArmorType.CHESTPLATE)
                    )}
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    )

    @JvmField
    val AMETHYST_LEGGINGS: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("amethyst_leggings", mapOf("en_us" to "Amethyst Leggings", "zh_cn" to "紫水晶护腿"))
                    .props { (Item.Properties().humanoidArmor(ModArmorMaterials.AMETHYST, ArmorType.LEGGINGS)
                    )}
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    )

    @JvmField
    val AMETHYST_BOOTS: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("amethyst_boots", mapOf("en_us" to "Amethyst Boots", "zh_cn" to "紫水晶靴子"))
                    .props { (Item.Properties().humanoidArmor(ModArmorMaterials.AMETHYST, ArmorType.BOOTS)
                    )}
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    )

    @JvmField
    val FROZEN_HELMET: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("frozen_helmet", mapOf("en_us" to "Frozen Helmet", "zh_cn" to "霜冻头盔"))
                    .props { (Item.Properties().humanoidArmor(ModArmorMaterials.FROZEN, ArmorType.HELMET)
                    )}
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    )

    @JvmField
    val FROZEN_CHESTPLATE: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("frozen_chestplate", mapOf("en_us" to "Frozen Chestplate", "zh_cn" to "霜冻甲"))
                    .props { (Item.Properties().humanoidArmor(ModArmorMaterials.FROZEN, ArmorType.CHESTPLATE)
                    )}
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    )

    @JvmField
    val FROZEN_LEGGINGS: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("frozen_leggings", mapOf("en_us" to "Frozen Leggings", "zh_cn" to "霜冻护腿"))
                    .props { (Item.Properties().humanoidArmor(ModArmorMaterials.FROZEN, ArmorType.LEGGINGS)
                    )}
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    )

    @JvmField
    val FROZEN_BOOTS: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("frozen_boots", mapOf("en_us" to "Frozen Boots", "zh_cn" to "霜冻靴"))
                    .props { (Item.Properties().humanoidArmor(ModArmorMaterials.FROZEN, ArmorType.BOOTS)
                    )}
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    )

    @JvmField
    val FRIED_EGG: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("fried_egg", mapOf("en_us" to "Fried Egg", "zh_cn" to "煎蛋")).props { (Item.Properties()).food(
                    FoodProperties(5, 0.4f, false),
                    Consumables.defaultFood().build()
            )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val HEART_EGG: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("heart_egg", mapOf("en_us" to "Heart Egg", "zh_cn" to "生命蛋")).func(::HeartEgg)
                    .props { (Item.Properties()).food(
                            FoodProperties(4, 1.0f, true),
                            Consumables.defaultFood().build()
                    )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val SPURT_EGG: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("spurt_egg", mapOf("en_us" to "Spurt Egg", "zh_cn" to "迸发蛋")).func(::SpurtEgg)
                    .props { (Item.Properties()).food(
                            FoodProperties(4, 1.0f, true),
                            Consumables.defaultFood().build()
                    )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val ENDER_EGG: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("ender_egg", mapOf("en_us" to "Ender Egg", "zh_cn" to "末影蛋")).func(::EnderEgg)
                    .props { (Item.Properties()).food(
                            FoodProperties(4, 1.0f, true),
                            Consumables.defaultFood().build()
                    )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val ENERGY_RAY_STAFF: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("energy_ray_staff", mapOf("en_us" to "Energy Ray Staff", "zh_cn" to "能量射线杖")).func(::EnergyRayStaff)
                    .props { Item.Properties().useCooldown(0.7F).stacksTo(1) }
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    )

    @JvmField
    val BLUE_GEM: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("blue_gem", mapOf("en_us" to "Blue Gem", "zh_cn" to "蓝宝石")).func(::BlueGem)
                    .props { (Item.Properties()).food(
                            FoodProperties(0, 0.0f, true),
                            Consumables.defaultFood().build()
                    )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val RED_GEM: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("red_gem", mapOf("en_us" to "Red Gem", "zh_cn" to "红宝石")).func(::RedGem)
                    .props { (Item.Properties()).food(
                            FoodProperties(0, 0.0f, true),
                            Consumables.defaultFood().build()
                    )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )

    @JvmField
    val MAGIC_CRYSTAL: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("magic_crystal", mapOf("en_us" to "Magic Crystal", "zh_cn" to "魔法水晶")).func(::MagicCrystal)
                    .props { (Item.Properties()).food(
                            FoodProperties(0, 0.0f, true),
                            Consumables.defaultFood().build()
                    )}.creativeTab(ModCreativeModeTabs.DECISLAND_FOODS_TAB).build()
    )


    @JvmStatic
    fun registerItem(config: ItemConfig): DeferredItem<Item> {
//        DecIsland.LOGGER.info("Registering item: {}", config.name)
        ITEM_CONFIGS.add(config)
        return ITEMS.registerItem(config.name, config.func, config.props)
    }

    @JvmStatic
    fun register(eventBus: IEventBus) {
        ITEMS.register(eventBus)
    }

    @JvmStatic
    fun getItemConfigs(): List<ItemConfig> {
        return ITEM_CONFIGS
    }

    @JvmStatic
    fun getItemByConfig(config: ItemConfig): Item? {
        return ITEMS.getEntries()
            .firstOrNull { it.id.path == config.name }
            ?.value()
    }
}
