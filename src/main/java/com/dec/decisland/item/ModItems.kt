package com.dec.decisland.item

import com.dec.decisland.DecIsland
import com.dec.decisland.item.custom.*
import com.dec.decisland.tag.ModItemTags
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
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object ModItems {
    @JvmField
    val ITEMS: DeferredRegister.Items =
        DeferredRegister.createItems(DecIsland.MOD_ID)
    private val ITEM_CONFIGS = mutableListOf<ItemConfig>()
    private val ITEM_CONFIGS_BY_NAME = linkedMapOf<String, ItemConfig>()
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
                    .tags(listOf(ModItemTags.MELEE_WEAPON, ModItemTags.AXE))
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM).build()
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
    val COB: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("cob", mapOf("en_us" to "Cob", "zh_cn" to "玉米棒")).customProp(CustomItemProperties.Builder().compostableChance(0.3f).build())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_CROPS_TAB).build()
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
                    .tags(listOf(ModItemTags.MAGIC_WEAPON, ModItemTags.STAFF))
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB).build()
    )

    @JvmField
    val BLUE_GEM: DeferredItem<Item> = registerBoostConsumable(
            "blue_gem",
            mapOf("en_us" to "Blue Gem", "zh_cn" to "蓝宝石"),
            ::BlueGem,
            cooldown = 0.3f,
    )

    @JvmField
    val RED_GEM: DeferredItem<Item> = registerBoostConsumable(
            "red_gem",
            mapOf("en_us" to "Red Gem", "zh_cn" to "红宝石"),
            ::RedGem,
            cooldown = 0.3f,
    )

    @JvmField
    val MAGIC_CRYSTAL: DeferredItem<Item> = registerBoostConsumable(
            "magic_crystal",
            mapOf("en_us" to "Magic Crystal", "zh_cn" to "魔法水晶"),
            ::MagicCrystal,
            cooldown = 0.3f,
    )

    @JvmField
    val CRYSTAL_NUCLEUS: DeferredItem<Item> = registerManaRestoreItem(
            "crystal_nucleus",
            mapOf("en_us" to "Crystal Nucleus", "zh_cn" to "魔法晶核"),
            ::CrystalNucleus,
            cooldown = 3.0f,
    )

    @JvmField
    val DIAMOND_CRYSTAL_NUCLEUS: DeferredItem<Item> = registerManaRestoreItem(
            "diamond_crystal_nucleus",
            mapOf("en_us" to "Diamond Crystal Nucleus", "zh_cn" to "钻石晶核"),
            ::DiamondCrystalNucleus,
            cooldown = 3.0f,
    )

    @JvmField
    val ENDER_CRYSTAL_NUCLEUS: DeferredItem<Item> = registerManaRestoreItem(
            "ender_crystal_nucleus",
            mapOf("en_us" to "Ender Crystal Nucleus", "zh_cn" to "末影晶核"),
            ::EnderCrystalNucleus,
            cooldown = 3.0f,
    )

    @JvmField
    val INFINITE_CRYSTAL_NUCLEUS_1: DeferredItem<Item> = registerManaRestoreItem(
            "infinite_crystal_nucleus_1",
            mapOf("en_us" to "Infinite Crystal Nucleus I", "zh_cn" to "无限晶核I"),
            ::InfiniteCrystalNucleus1,
            cooldown = 0.2f,
            durability = 942,
            repairItem = LAPIS_LAZULI,
    )

    @JvmField
    val INFINITE_CRYSTAL_NUCLEUS_2: DeferredItem<Item> = registerManaRestoreItem(
            "infinite_crystal_nucleus_2",
            mapOf("en_us" to "Infinite Crystal Nucleus II", "zh_cn" to "无限晶核II"),
            ::InfiniteCrystalNucleus2,
            cooldown = 0.15f,
            durability = 1245,
            repairItem = LAPIS_LAZULI,
    )

    @JvmField
    val INFINITE_CRYSTAL_NUCLEUS_3: DeferredItem<Item> = registerManaRestoreItem(
            "infinite_crystal_nucleus_3",
            mapOf("en_us" to "Infinite Crystal Nucleus III", "zh_cn" to "无限晶核III"),
            ::InfiniteCrystalNucleus3,
            cooldown = 0.1f,
            durability = 1672,
            repairItem = LAPIS_LAZULI,
    )

    @JvmField
    val FINAL_CRYSTAL_NUCLEUS: DeferredItem<Item> = registerManaRestoreItem(
            "final_crystal_nucleus",
            mapOf("en_us" to "Final Crystal Nucleus", "zh_cn" to "终极晶核"),
            ::FinalCrystalNucleus,
            cooldown = 0.05f,
            durability = 2340,
            repairItem = LAPIS_LAZULI,
    )

    @JvmField
    val EXPERIENCE_BOOK: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("experience_book", mapOf("en_us" to "Experience Book", "zh_cn" to "经验书"))
                    .func(::ExperienceBookItem)
                    .props { Item.Properties().stacksTo(64).useCooldown(0.5f) }
                    .customProp(CustomItemProperties.Builder().burnTime(20).build())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB)
                    .build()
    )

    @JvmField
    val EXPERIENCE_BOOK_EMPTY: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("experience_book_empty", mapOf("en_us" to "Empty Experience Book", "zh_cn" to "空的经验书"))
                    .func(::ExperienceBookEmptyItem)
                    .props { Item.Properties().stacksTo(64).useCooldown(0.5f) }
                    .customProp(CustomItemProperties.Builder().burnTime(10).build())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB)
                    .build()
    )

    @JvmField
    val EXPERIENCE_BOOK_PRO_EMPTY: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("experience_book_pro_empty", mapOf("en_us" to "Empty Experience Book Pro", "zh_cn" to "空的汲取之书"))
                    .func(::ExperienceBookProEmptyItem)
                    .props { Item.Properties().stacksTo(64).useCooldown(0.5f) }
                    .customProp(CustomItemProperties.Builder().burnTime(10).build())
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB)
                    .build()
    )

    @JvmField
    val GUIDE_BOOK: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("guide_book", mapOf("en_us" to "Guide Book", "zh_cn" to "指引之书"))
                    .func(::GuideBookItem)
                    .props { Item.Properties().stacksTo(64).useCooldown(0.5f) }
                    .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB)
                    .build()
    )

    @JvmField
    val HUNTER_BOOK: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("hunter_book", mapOf("en_us" to "Book of Hunter", "zh_cn" to "猎人之书"))
                    .props { Item.Properties().stacksTo(1) }
                    .modelTemplate(ModelTemplates.FLAT_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB)
                    .build()
    )

    @JvmField
    val TIME_COMPASS: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("time_compass", mapOf("en_us" to "Time Compass", "zh_cn" to "时间指针"))
                    .func(::TimeCompassItem)
                    .props { Item.Properties().stacksTo(1) }
                    .modelTemplate(ModelTemplates.FLAT_ITEM)
                    .creativeTab(ModCreativeModeTabs.DECISLAND_ACCESSORIES_TAB)
                    .build()
    )

    @JvmField
    val LAPIS_BULLET_RENDER: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("lapis_bullet_render", mapOf("en_us" to "Lapis Bullet Render", "zh_cn" to "青金石弹渲染"))
                    .modelTemplate(ModelTemplates.FLAT_ITEM)
                    .build()
    )

    @JvmField
    val SOUL_WAKE_BULLET_RENDER: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("soul_wake_bullet_render", mapOf("en_us" to "Soul Wake Bullet Render", "zh_cn" to "灵魂尾迹渲染"))
                    .modelTemplate(ModelTemplates.FLAT_ITEM)
                    .build()
    )

    @JvmField
    val JELLYFISH_STAFF_PROJECTILE_RENDER: DeferredItem<Item> = registerItem(
            ItemConfig.Builder("jellyfish_staff_projectile_render", mapOf("en_us" to "Jellyfish Staff Projectile Render", "zh_cn" to "水母法杖射弹渲染"))
                    .modelTemplate(ModelTemplates.FLAT_ITEM)
                    .build()
    )


    @JvmStatic
    fun registerItem(config: ItemConfig): DeferredItem<Item> {
//        DecIsland.LOGGER.info("Registering item: {}", config.name)
        ITEM_CONFIGS.add(config)
        ITEM_CONFIGS_BY_NAME[config.name] = config
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
    fun getItemConfigByName(name: String): ItemConfig? {
        return ITEM_CONFIGS_BY_NAME[name]
    }

    @JvmStatic
    fun getItemByConfig(config: ItemConfig): Item? {
        return ITEMS.getEntries()
            .firstOrNull { it.id.path == config.name }
            ?.value()
    }

    private fun registerBoostConsumable(
            name: String,
            lang: Map<String, String>,
            func: (Item.Properties) -> Item,
            cooldown: Float,
    ): DeferredItem<Item> = registerItem(
            ItemConfig.Builder(name, lang)
                    .func(func)
                    .props {
                        Item.Properties()
                                .food(
                                        FoodProperties(0, 0.0f, true),
                                        Consumables.defaultFood().build()
                                )
                                .useCooldown(cooldown)
                    }
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB)
                    .build()
    )

    private fun registerManaRestoreItem(
            name: String,
            lang: Map<String, String>,
            func: (Item.Properties) -> Item,
            cooldown: Float,
            durability: Int? = null,
            repairItem: Item? = null,
    ): DeferredItem<Item> = registerItem(
            ItemConfig.Builder(name, lang)
                    .func(func)
                    .props {
                        var properties = Item.Properties().useCooldown(cooldown)
                        if (durability != null) {
                            properties = properties.stacksTo(1).durability(durability)
                            if (repairItem != null) {
                                properties = properties.repairable(repairItem)
                            }
                        }
                        properties
                    }
                    .creativeTab(ModCreativeModeTabs.DECISLAND_MISC_TAB)
                    .build()
    )
}
