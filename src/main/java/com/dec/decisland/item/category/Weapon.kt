package com.dec.decisland.item.category

import com.dec.decisland.DecIsland
import com.dec.decisland.item.CustomItemProperties
import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.ModToolMaterial
import com.dec.decisland.entity.projectile.dart.DartDefinition
import com.dec.decisland.entity.projectile.dart.ModDarts
import com.dec.decisland.item.custom.*
import com.dec.decisland.item.custom.dart.Dart
import com.dec.decisland.item.gun.EverlastingWinterFlintlock
import com.dec.decisland.item.gun.Flintlock
import com.dec.decisland.item.gun.FlintlockBullet
import com.dec.decisland.item.gun.FlintlockPro
import com.dec.decisland.item.gun.GhostFlintlock
import com.dec.decisland.item.gun.LavaFlintlock
import com.dec.decisland.item.gun.ShortFlintlock
import com.dec.decisland.item.gun.StarFlintlock
import com.dec.decisland.item.gun.StormFlintlock
import com.dec.decisland.tag.ModItemTags
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items.STICK
import net.minecraft.world.item.Items
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.item.component.Consumables
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Function
import java.util.function.Supplier

object Weapon {
    private val thrownWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.THROWN_WEAPON)
    private val dartWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.THROWN_WEAPON, ModItemTags.DART)
    private val sundriesThrownWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.THROWN_WEAPON, ModItemTags.SUNDRIES)
    private val gunWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.RANGE_WEAPON, ModItemTags.GUN)
    private val staffWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.MAGIC_WEAPON, ModItemTags.STAFF)
    private val magicBookWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.MAGIC_WEAPON, ModItemTags.MAGIC_BOOK)
    private val swordWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.MELEE_WEAPON, ModItemTags.SWORD)
    private val sickleWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.MELEE_WEAPON, ModItemTags.SICKLE)
    private val katanaWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.MELEE_WEAPON, ModItemTags.KATANA)
    private val daggerWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.MELEE_WEAPON, ModItemTags.DAGGER)
    private val battleaxeWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.MELEE_WEAPON, ModItemTags.AXE)
    private val rapierWeaponTags: List<TagKey<Item>> = listOf(ModItemTags.MELEE_WEAPON, ModItemTags.RAPIER)

    @JvmField
    val AMETHYST_DART: DeferredItem<Item> = registerDart(ModDarts.AMETHYST_DART)

    @JvmField
    val COPPER_DART: DeferredItem<Item> = registerDart(ModDarts.COPPER_DART)

    @JvmField
    val CORAL_DART: DeferredItem<Item> = registerDart(ModDarts.CORAL_DART)

    @JvmField
    val DIAMOND_DART: DeferredItem<Item> = registerDart(ModDarts.DIAMOND_DART)

    @JvmField
    val EMERALD_DART: DeferredItem<Item> = registerDart(ModDarts.EMERALD_DART)

    @JvmField
    val EVERLASTING_WINTER_DART: DeferredItem<Item> = registerDart(ModDarts.EVERLASTING_WINTER_DART)

    @JvmField
    val FROZEN_DART: DeferredItem<Item> = registerDart(ModDarts.FROZEN_DART)

    @JvmField
    val GOLD_DART: DeferredItem<Item> = registerDart(ModDarts.GOLD_DART)

    @JvmField
    val IRON_DART: DeferredItem<Item> = registerDart(ModDarts.IRON_DART)

    @JvmField
    val LAVA_DART: DeferredItem<Item> = registerDart(ModDarts.LAVA_DART)

    @JvmField
    val NETHERITE_DART: DeferredItem<Item> = registerDart(ModDarts.NETHERITE_DART)

    @JvmField
    val POISON_DART: DeferredItem<Item> = registerDart(ModDarts.POISON_DART)

    @JvmField
    val STEEL_DART: DeferredItem<Item> = registerDart(ModDarts.STEEL_DART)

    @JvmField
    val STONE_DART: DeferredItem<Item> = registerDart(ModDarts.STONE_DART)

    @JvmField
    val STREAM_DART: DeferredItem<Item> = registerDart(ModDarts.STREAM_DART)

    @JvmField
    val WOOD_DART: DeferredItem<Item> = registerDart(ModDarts.WOOD_DART)

    @JvmField
    val WOODEN_STAFF: DeferredItem<Item> = registerStaff(
        "wooden_staff",
        ::WoodenStaff,
        durability = 60,
        cooldown = 0.8f,
        enchantability = 15,
        repairTag = ItemTags.PLANKS,
        burnTime = 10,
    )

    @JvmField
    val AMETHYST_RAY_STAFF: DeferredItem<Item> = registerStaff(
        "amethyst_ray_staff",
        ::AmethystRayStaff,
        durability = 354,
        cooldown = 1.0f,
        enchantability = 20,
        repairItem = Supplier { Items.AMETHYST_SHARD },
    )

    @JvmField
    val BLIZZARD_STAFF: DeferredItem<Item> = registerStaff(
        "blizzard_staff",
        ::BlizzardStaff,
        durability = 855,
        cooldown = 0.7f,
        enchantability = 24,
        repairItem = Supplier { Material.ICE_INGOT.get() },
    )

    @JvmField
    val DEEP_STAFF: DeferredItem<Item> = registerStaff(
        "deep_staff",
        ::DeepStaff,
        durability = 1045,
        cooldown = 1.0f,
        enchantability = 17,
    )

    @JvmField
    val DIAMOND_STAFF: DeferredItem<Item> = registerStaff(
        "diamond_staff",
        ::DiamondStaff,
        durability = 1671,
        cooldown = 1.8f,
    )

    @JvmField
    val EMERALD_STAFF: DeferredItem<Item> = registerStaff(
        "emerald_staff",
        ::EmeraldStaff,
        durability = 326,
        cooldown = 0.5f,
        enchantability = 27,
        repairItem = Supplier { Items.EMERALD },
    )

    @JvmField
    val EVERLASTING_WINTER_STAFF: DeferredItem<Item> = registerStaff(
        "everlasting_winter_staff",
        ::EverlastingWinterStaff,
        durability = 1700,
        cooldown = 1.0f,
        enchantability = 27,
        repairItem = Supplier { Material.ICE_INGOT.get() },
    )

    @JvmField
    val FLARE_MAGIC_BOOK: DeferredItem<Item> = registerMagicBook(
        "flare_magic_book",
        ::FlareMagicBook,
        durability = 4096,
        enchantability = 20,
        repairItem = Supplier { Material.STAR_DEBRIS.get() },
        burnTime = 20,
    )

    @JvmField
    val FROZEN_RAY_STAFF: DeferredItem<Item> = registerStaff(
        "frozen_ray_staff",
        ::FrozenRayStaff,
        durability = 476,
        cooldown = 0.6f,
        enchantability = 15,
        repairItem = Supplier { Material.ICE_INGOT.get() },
    )

    @JvmField
    val FROZEN_STAFF: DeferredItem<Item> = registerStaff(
        "frozen_staff",
        ::FrozenStaff,
        durability = 436,
        repairItem = Supplier { Material.ICE_INGOT.get() },
    )

    @JvmField
    val GIANT_IVY: DeferredItem<Item> = registerStaff(
        "giant_ivy",
        ::GiantIvy,
        durability = 288,
        cooldown = 0.2f,
        enchantability = 25,
    )

    @JvmField
    val GOLDEN_STAFF: DeferredItem<Item> = registerStaff(
        "golden_staff",
        ::GoldenStaff,
        durability = 33,
        cooldown = 0.6f,
        enchantability = 35,
    )

    @JvmField
    val IRON_STAFF: DeferredItem<Item> = registerStaff(
        "iron_staff",
        ::IronStaff,
        durability = 251,
        cooldown = 1.25f,
        enchantability = 17,
        repairItem = Supplier { Items.IRON_INGOT },
    )

    @JvmField
    val JELLYFISH_STAFF: DeferredItem<Item> = registerStaff(
        "jellyfish_staff",
        ::JellyfishStaff,
        durability = 275,
        cooldown = 0.3f,
        enchantability = 32,
    )

    @JvmField
    val LAPIS_STAFF: DeferredItem<Item> = registerStaff(
        "lapis_staff",
        ::LapisStaff,
        durability = 121,
        cooldown = 0.5f,
        enchantability = 40,
        repairItem = Supplier { Items.LAPIS_LAZULI },
    )

    @JvmField
    val LAPIS_MAGIC_BOOK: DeferredItem<Item> = registerMagicBook(
        "lapis_magic_book",
        ::LapisMagicBook,
        durability = 156,
        cooldown = 0.05f,
        repairItem = Supplier { Items.LAPIS_LAZULI },
        burnTime = 10,
        tags = emptyList(),
    )

    @JvmField
    val SNOWBALL_MAGIC_BOOK: DeferredItem<Item> = registerMagicBook(
        "snowball_magic_book",
        ::SnowballMagicBook,
        durability = 436,
        cooldown = 0.5f,
        enchantability = 5,
        repairItem = Supplier { Material.ICE_INGOT.get() },
        burnTime = 10,
    )

    @JvmField
    val STORM_STAFF: DeferredItem<Item> = registerStaff(
        "storm_staff",
        ::StormStaff,
        durability = 434,
        cooldown = 0.8f,
        enchantability = 24,
        repairItem = Supplier { Material.STREAM_STONE.get() },
    )

    @JvmField
    val SOUL_STAFF: DeferredItem<Item> = registerStaff(
        "soul_staff",
        ::SoulStaff,
        durability = 189,
        cooldown = 0.9f,
        enchantability = 14,
    )

    @JvmField
    val THUNDER_STAFF: DeferredItem<Item> = registerStaff(
        "thunder_staff",
        ::ThunderStaff,
        durability = 34,
        cooldown = 1.5f,
        enchantability = 1,
    )

    @JvmField
    val WAVE_MAGIC_BOOK: DeferredItem<Item> = registerMagicBook(
        "wave_magic_book",
        ::WaveMagicBook,
        durability = 533,
        cooldown = 0.6f,
        enchantability = 18,
        repairItem = Supplier { Material.STREAM_STONE.get() },
        burnTime = 10,
    )

    @JvmField
    val FLINTLOCK_BULLET: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("flintlock_bullet")
            .func(::FlintlockBullet)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val STICKY_ASH: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("sticky_ash")
            .func(::StickyAsh)
            .props { Item.Properties().stacksTo(64).useCooldown(0.05f) }
            .tags(sundriesThrownWeaponTags)
            .customProp(CustomItemProperties.Builder().burnTime(20).build())
            .modelTemplate(ModelTemplates.FLAT_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val FROZEN_BALL: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("frozen_ball")
            .func(::FrozenBall)
            .tags(sundriesThrownWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val MIND_CONTROLLER: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("mind_controller")
            .func(::MindController)
            .tags(sundriesThrownWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val MUDDY_BALL: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("muddy_ball")
            .func(::MuddyBall)
            .props { Item.Properties().stacksTo(64).useCooldown(1.0f) }
            .tags(thrownWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val FIREFLY_BOTTLE: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("firefly_bottle")
            .func(::FireflyBottle)
            .props { Item.Properties().stacksTo(64) }
            .tags(thrownWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val SMOKE_BOMB: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("smoke_bomb")
            .func(::SmokeBomb)
            .props { Item.Properties().stacksTo(64).useCooldown(3.0f) }
            .tags(thrownWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val GAS_BOMB: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("gas_bomb")
            .func(::GasBomb)
            .props { Item.Properties().stacksTo(64).useCooldown(4.0f) }
            .tags(sundriesThrownWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val THROWABLE_BOMB: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("throwable_bomb")
            .func(::ThrowableBomb)
            .props { Item.Properties().stacksTo(64).useCooldown(1.2f) }
            .tags(sundriesThrownWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("flintlock")
            .func(::Flintlock)
            .props { Item.Properties().stacksTo(1).durability(230).enchantable(13) }
            .tags(gunWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val FLINTLOCK_PRO: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("flintlock_pro")
            .func(::FlintlockPro)
            .props { Item.Properties().stacksTo(1).durability(784).enchantable(14) }
            .tags(gunWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val SHORT_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("short_flintlock")
            .func(::ShortFlintlock)
            .props { Item.Properties().stacksTo(1).durability(120).enchantable(10) }
            .tags(gunWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val EVERLASTING_WINTER_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("everlasting_winter_flintlock")
            .func(::EverlastingWinterFlintlock)
            .props { Item.Properties().stacksTo(1).durability(1668).enchantable(24) }
            .tags(gunWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val GHOST_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("ghost_flintlock")
            .func(::GhostFlintlock)
            .props { Item.Properties().stacksTo(1).durability(3759).enchantable(18) }
            .tags(gunWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val LAVA_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("lava_flintlock")
            .func(::LavaFlintlock)
            .props { Item.Properties().stacksTo(1).durability(968).enchantable(11) }
            .tags(gunWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val STAR_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("star_flintlock")
            .func(::StarFlintlock)
            .props { Item.Properties().stacksTo(1).durability(3855).enchantable(17) }
            .tags(gunWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val STORM_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("storm_flintlock")
            .func(::StormFlintlock)
            .props { Item.Properties().stacksTo(1).durability(394).enchantable(26) }
            .tags(gunWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val ABSOLUTE_ZERO: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("absolute_zero")
            .func(::AbsoluteZero)
            .props { Item.Properties().sword(ModToolMaterial.ABSOLUTE_ZERO, 3.0f, -2.4f).useCooldown(3.0f).stacksTo(1) }
            .tags(swordWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val BAMBOO_KATANA: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("bamboo_katana")
            .func(::BambooKatana)
            .props { Item.Properties().sword(ModToolMaterial.BAMBOO, 2.0f, -2.4f).useCooldown(2.5f).stacksTo(1) }
            .tags(katanaWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val BLOOD_MARE: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("blood_mare")
            .func(::BloodMare)
            .props { Item.Properties().sword(ModToolMaterial.BLOOD_MARE, 2.0f, -2.4f).useCooldown(2.0f).stacksTo(1) }
            .tags(katanaWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val CANDY_CANE: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("candy_cane")
            .props {
                Item.Properties()
                    .food(FoodProperties(2, 0.2f, true), Consumables.defaultFood().build())
                    .sword(ModToolMaterial.CANDY_CANE, 2.0f, -2.0f)
                    .stacksTo(1)
            }
            .tags(swordWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val HARD_BAMBOO_KATANA: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("hard_bamboo_katana")
            .func(::HardBambooKatana)
            .props { Item.Properties().sword(ModToolMaterial.HARD_BAMBOO, 2.0f, -2.4f).useCooldown(2.2f).stacksTo(1) }
            .tags(katanaWeaponTags)
            .customProp(CustomItemProperties.Builder().burnTime(8).build())
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val ILLAGER_SWORD: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("illager_sword")
            .func(::IllagerSword)
            .props { Item.Properties().sword(ModToolMaterial.ILLAGER_SWORD, 2.0f, -2.4f).useCooldown(2.3f).stacksTo(1) }
            .tags(katanaWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val NIGHT_SWORD: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("night_sword")
            .func(::NightSword)
            .props { Item.Properties().sword(ModToolMaterial.NIGHT_SWORD, 7.0f, -2.4f).useCooldown(2.0f).stacksTo(1) }
            .tags(swordWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val NIGHTMARE: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("nightmare")
            .func(::Nightmare)
            .props { Item.Properties().stacksTo(1).durability(3442).useCooldown(0.5f).enchantable(27) }
            .tags(staffWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val THE_BLADE: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("the_blade")
            .func(::TheBlade)
            .props { Item.Properties().sword(ModToolMaterial.THE_BLADE, 2.0f, -2.4f).useCooldown(2.0f).stacksTo(1) }
            .tags(katanaWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val HARD_LOLLIPOP: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("hard_lollipop")
            .props {
                Item.Properties()
                    .food(FoodProperties(14, 0.8f, false), Consumables.defaultFood().build())
                    .usingConvertsTo(STICK)
                    .sword(ModToolMaterial.HARD_LOLLIPOP, 3.0f, -3.0f)
            }
            .tags(swordWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val GINGERBREAD_SWORD: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("gingerbread_sword")
            .props {
                Item.Properties()
                    .food(FoodProperties(5, 0.8f, true), Consumables.defaultFood().build())
                    .sword(ModToolMaterial.GINGERBREAD_SWORD, 2.0f, -2.4f)
                    .repairable(Food.GINGERBREAD_MAN.get())
            }
            .tags(swordWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val LOLLIPOP: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("lollipop")
            .props {
                Item.Properties()
                    .food(FoodProperties(8, 0.8f, false), Consumables.defaultFood().build())
                    .sword(ModToolMaterial.LOLLIPOP, 1.0f, -3.0f)
                    .repairable(ModItems.CANDY.get())
            }
            .tags(swordWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val LONG_BREAD: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("long_bread")
            .props {
                Item.Properties()
                    .food(FoodProperties(17, 0.8f, false), Consumables.defaultFood().build())
                    .sword(ModToolMaterial.LONG_BREAD, 0.0f, -2.8f)
                    .repairable(Items.BREAD)
            }
            .tags(swordWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val CACTUS_SWORD: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("cactus_sword")
            .props { Item.Properties().sword(ModToolMaterial.CACTUS, 2.0f, -2.7f) }
            .tags(swordWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val CORRUPTED_SWORD: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("corrupted_sword")
            .props { Item.Properties().sword(ModToolMaterial.CORRUPTED, 2.0f, -2.4f) }
            .tags(swordWeaponTags)
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val AMETHYST_SWORD: DeferredItem<Item> = registerSimpleSword(
        "amethyst_sword",
        "Amethyst Sword",
        "紫水晶剑",
        ModToolMaterial.AMETHYST_SWORD,
        repairItem = Supplier { Items.AMETHYST_SHARD },
    )

    @JvmField
    val BONE_SWORD: DeferredItem<Item> = registerSimpleSword(
        "bone_sword",
        "Bone Sword",
        "骨刃",
        ModToolMaterial.BONE_SWORD,
        repairItem = Supplier { Items.BONE },
    )

    @JvmField
    val CORAL_SWORD: DeferredItem<Item> = registerSimpleSword(
        "coral_sword",
        "Coral Sword",
        "珊瑚剑",
        ModToolMaterial.CORAL_SWORD,
        repairItem = Supplier { Material.CORAL_INGOT.get() },
    )

    @JvmField
    val EMERALD_SWORD: DeferredItem<Item> = registerSimpleSword(
        "emerald_sword",
        "Emerald Sword",
        "绿宝石剑",
        ModToolMaterial.EMERALD_SWORD,
        repairItem = Supplier { Items.EMERALD },
    )

    @JvmField
    val LAVA_SWORD: DeferredItem<Item> = registerSimpleSword(
        "lava_sword",
        "Lava Sword",
        "岩浆之剑",
        ModToolMaterial.LAVA_SWORD,
        repairItem = Supplier { Material.LAVA_INGOT.get() },
    )

    @JvmField
    val STEEL_SWORD: DeferredItem<Item> = registerSimpleSword(
        "steel_sword",
        "Steel Sword",
        "钢剑",
        ModToolMaterial.STEEL_SWORD,
        repairItem = Supplier { Material.STEEL_INGOT.get() },
    )

    @JvmField
    val TURTLE_SWORD: DeferredItem<Item> = registerSimpleSword(
        "turtle_sword",
        "Turtle Sword",
        "龟刃剑",
        ModToolMaterial.TURTLE_SWORD,
        repairItem = Supplier { Items.TURTLE_SCUTE },
    )

    @JvmField
    val SCIMITAR: DeferredItem<Item> = registerSimpleSword(
        "scimitar",
        "Scimitar",
        "弯刀",
        ModToolMaterial.SCIMITAR,
        attackSpeed = -2.2f,
        repairItem = Supplier { Items.IRON_INGOT },
    )

    @JvmField
    val CUDGEL: DeferredItem<Item> = registerSimpleSword(
        "cudgel",
        "Cudgel",
        "木棒",
        ModToolMaterial.CUDGEL,
        attackSpeed = -2.8f,
    )

    @JvmField
    val FANG_MACE: DeferredItem<Item> = registerSimpleSword(
        "fang_mace",
        "Fang Mace",
        "狼牙棒",
        ModToolMaterial.FANG_MACE,
        attackSpeed = -2.8f,
    )

    @JvmField
    val SHARP_CORAL: DeferredItem<Item> = registerSimpleSword(
        "sharp_coral",
        "Sharp Coral",
        "尖锐珊瑚",
        ModToolMaterial.SHARP_CORAL,
        attackSpeed = -2.1f,
        repairItem = Supplier { Material.CORAL_INGOT.get() },
    )

    @JvmField
    val BLIZZARD_SWORD: DeferredItem<Item> = registerSimpleSword(
        "blizzard_sword",
        "Blizzard Sword",
        "暴雪之剑",
        ModToolMaterial.BLIZZARD_SWORD,
        repairItem = Supplier { Material.ICE_INGOT.get() },
    )

    @JvmField
    val ICE_SWORD: DeferredItem<Item> = registerSimpleSword(
        "ice_sword",
        "Ice Sword",
        "冰棱",
        ModToolMaterial.ICE_SWORD,
        repairItem = Supplier { Material.ICE_INGOT.get() },
    )

    @JvmField
    val LAPIS_SWORD: DeferredItem<Item> = registerSimpleSword(
        "lapis_sword",
        "Lapis Sword",
        "青金石剑",
        ModToolMaterial.LAPIS_SWORD,
        repairItem = Supplier { Items.LAPIS_LAZULI },
    )

    @JvmField
    val BLOOD_SICKLE: DeferredItem<Item> = registerSickle(
        "blood_sickle",
        ModToolMaterial.BLOOD_SICKLE,
        config = SickleItem.SickleConfig.Builder("blood_sickle")
            .passiveSkill(
                SickleItem.PassiveSkillConfig.Builder()
                    .baseExtraDamage(1.0f)
                    .proc(
                        sickleProc(
                            9,
                            1.0f,
                            id("blood_sickle_particle"),
                            id("blood_spore_parasitic_particle"),
                            sickleEffect(MobEffects.SLOWNESS, 5, 0),
                        ),
                    )
                    .proc(
                        sickleProc(
                            8,
                            0.0f,
                            id("blood_sickle_particle"),
                            id("blood_spore_parasitic_particle"),
                            sickleEffect(MobEffects.WEAKNESS, 5, 0),
                        ),
                    )
                    .build(),
            )
            .movementSpeedAddition(-0.01)
            .build(),
        itemFactory = { properties, sickleConfig -> BloodSickleItem(properties, sickleConfig) },
        repairItem = Supplier { Material.HEALTH_STONE.get() },
    )

    @JvmField
    val COPPER_SICKLE: DeferredItem<Item> = registerSickle(
        "copper_sickle",
        ModToolMaterial.COPPER_SICKLE,
        config = SickleItem.SickleConfig.Builder("copper_sickle")
            .passiveSkill(
                SickleItem.PassiveSkillConfig.Builder()
                    .baseExtraDamage(1.0f)
                    .proc(sickleProc(9, 0.0f, id("copper_sickle_particle"), null, sickleEffect(MobEffects.SLOWNESS, 5, 0)))
                    .proc(sickleProc(8, 0.0f, id("copper_sickle_particle"), null, sickleEffect(MobEffects.WEAKNESS, 5, 0)))
                    .build(),
            )
            .movementSpeedAddition(-0.01)
            .build(),
        repairItem = Supplier { Items.COPPER_INGOT },
    )

    @JvmField
    val DIAMOND_SICKLE: DeferredItem<Item> = registerSickle(
        "diamond_sickle",
        ToolMaterial.DIAMOND,
        config = SickleItem.SickleConfig.Builder("diamond_sickle")
            .passiveSkill(
                SickleItem.PassiveSkillConfig.Builder()
                    .baseExtraDamage(1.0f)
                    .proc(sickleProc(7, 0.0f, id("diamond_sickle_particle"), null, sickleEffect(MobEffects.SLOWNESS, 8, 0)))
                    .proc(sickleProc(5, 0.0f, id("diamond_sickle_particle"), null, sickleEffect(MobEffects.WEAKNESS, 8, 1)))
                    .build(),
            )
            .movementSpeedAddition(-0.01)
            .build(),
        repairItem = Supplier { Items.DIAMOND },
    )

    @JvmField
    val EVERLASTING_WINTER_SICKLE: DeferredItem<Item> = registerSickle(
        "everlasting_winter_sickle",
        ModToolMaterial.EVERLASTING_WINTER_SICKLE,
        config = SickleItem.SickleConfig.Builder("everlasting_winter_sickle")
            .passiveSkill(
                SickleItem.PassiveSkillConfig.Builder()
                    .baseExtraDamage(1.0f)
                    .proc(
                        sickleProc(
                            6,
                            0.0f,
                            id("everlasting_winter_sickle_particle"),
                            id("frozen_attack_particle"),
                            sickleEffect(MobEffects.SLOWNESS, 2, 6),
                        ),
                    )
                    .proc(
                        sickleProc(
                            5,
                            0.0f,
                            id("everlasting_winter_sickle_particle"),
                            null,
                            sickleEffect(MobEffects.WEAKNESS, 5, 2),
                        ),
                    )
                    .build(),
            )
            .activeSkill(
                SickleItem.ActiveSkillConfig.Builder()
                    .manaCost(1.0f)
                    .minRadius(2.0)
                    .maxRadius(4.0)
                    .targetEffect(sickleEffect(MobEffects.SLOWNESS, 3, 1))
                    .casterParticleId(id("everlasting_winter_seep_particle"))
                    .targetParticleId(id("everlasting_winter_seep_particle"))
                    .particleDurationTicks(6)
                    .build(),
            )
            .movementSpeedAddition(-0.01)
            .build(),
        cooldown = 1.0f,
        repairItem = Supplier { Material.ICE_INGOT.get() },
    )

    @JvmField
    val GHOST_SICKLE: DeferredItem<Item> = registerSickle(
        "ghost_sickle",
        ModToolMaterial.GHOST_SICKLE,
        config = SickleItem.SickleConfig.Builder("ghost_sickle")
            .passiveSkill(
                SickleItem.PassiveSkillConfig.Builder()
                    .baseExtraDamage(1.0f)
                    .proc(
                        sickleProc(
                            9,
                            0.0f,
                            id("ghost_sickle_particle"),
                            null,
                            sickleEffect(MobEffects.SLOWNESS, 5, 1),
                        ),
                    )
                    .proc(
                        sickleProc(
                            6,
                            0.0f,
                            id("ghost_sickle_particle"),
                            id("ghost_spurt_particle"),
                            sickleEffect(MobEffects.INVISIBILITY, 3, 0),
                            sickleEffect(MobEffects.WEAKNESS, 3, 255),
                        ),
                    )
                    .build(),
            )
            .movementSpeedAddition(-0.01)
            .build(),
        repairItem = Supplier { Material.GHOST_INGOT.get() },
    )

    @JvmField
    val GOLD_SICKLE: DeferredItem<Item> = registerSickle(
        "gold_sickle",
        ToolMaterial.GOLD,
        config = SickleItem.SickleConfig.Builder("gold_sickle")
            .passiveSkill(
                SickleItem.PassiveSkillConfig.Builder()
                    .baseExtraDamage(1.0f)
                    .proc(sickleProc(5, 0.0f, id("gold_sickle_particle"), null, sickleEffect(MobEffects.SLOWNESS, 3, 2)))
                    .proc(sickleProc(4, 0.0f, id("gold_sickle_particle"), null, sickleEffect(MobEffects.WEAKNESS, 3, 2)))
                    .build(),
            )
            .movementSpeedAddition(-0.01)
            .build(),
        attackSpeed = -3.0f,
        repairItem = Supplier { Items.GOLD_INGOT },
    )

    @JvmField
    val IRON_SICKLE: DeferredItem<Item> = registerSickle(
        "iron_sickle",
        ToolMaterial.IRON,
        config = SickleItem.SickleConfig.Builder("iron_sickle")
            .passiveSkill(
                SickleItem.PassiveSkillConfig.Builder()
                    .baseExtraDamage(1.0f)
                    .proc(sickleProc(8, 0.0f, id("iron_sickle_particle"), null, sickleEffect(MobEffects.SLOWNESS, 6, 0)))
                    .proc(sickleProc(7, 0.0f, id("iron_sickle_particle"), null, sickleEffect(MobEffects.WEAKNESS, 6, 0)))
                    .build(),
            )
            .movementSpeedAddition(-0.01)
            .build(),
        repairItem = Supplier { Items.IRON_INGOT },
    )

    @JvmField
    val NETHERITE_SICKLE: DeferredItem<Item> = registerSickle(
        "netherite_sickle",
        ToolMaterial.NETHERITE,
        config = SickleItem.SickleConfig.Builder("netherite_sickle")
            .passiveSkill(
                SickleItem.PassiveSkillConfig.Builder()
                    .baseExtraDamage(1.0f)
                    .proc(sickleProc(7, 0.0f, id("netherite_sickle_particle"), null, sickleEffect(MobEffects.SLOWNESS, 9, 0)))
                    .proc(sickleProc(5, 0.0f, id("netherite_sickle_particle"), null, sickleEffect(MobEffects.WEAKNESS, 9, 1)))
                    .build(),
            )
            .movementSpeedAddition(-0.015)
            .build(),
        repairItem = Supplier { Items.NETHERITE_INGOT },
    )

    @JvmField
    val STEEL_SICKLE: DeferredItem<Item> = registerSickle(
        "steel_sickle",
        ModToolMaterial.STEEL_SICKLE,
        config = SickleItem.SickleConfig.Builder("steel_sickle")
            .passiveSkill(
                SickleItem.PassiveSkillConfig.Builder()
                    .baseExtraDamage(1.0f)
                    .proc(sickleProc(8, 0.0f, id("steel_sickle_particle"), null, sickleEffect(MobEffects.SLOWNESS, 6, 0)))
                    .proc(sickleProc(7, 0.0f, id("steel_sickle_particle"), null, sickleEffect(MobEffects.WEAKNESS, 7, 1)))
                    .build(),
            )
            .movementSpeedAddition(-0.015)
            .build(),
        repairItem = Supplier { Material.STEEL_INGOT.get() },
    )

    @JvmField
    val WOODEN_DAGGER: DeferredItem<Item> = registerDagger(
        "wooden_dagger",
        ToolMaterial.WOOD,
        config = DaggerItem.DaggerConfig.Builder("wooden_dagger", 12.0f)
            .movementSpeedAddition(0.01)
            .build(),
        attackDamage = 2.0f,
        cooldownSeconds = 3.0f,
        repairTag = ItemTags.PLANKS,
        burnTime = 10,
    )

    @JvmField
    val STONE_DAGGER: DeferredItem<Item> = registerDagger(
        "stone_dagger",
        ToolMaterial.STONE,
        config = DaggerItem.DaggerConfig.Builder("stone_dagger", 13.0f)
            .movementSpeedAddition(0.01)
            .build(),
        attackDamage = 2.0f,
        cooldownSeconds = 3.0f,
        repairTag = ItemTags.STONE_CRAFTING_MATERIALS,
    )

    @JvmField
    val COPPER_DAGGER: DeferredItem<Item> = registerDagger(
        "copper_dagger",
        ToolMaterial.COPPER,
        config = DaggerItem.DaggerConfig.Builder("copper_dagger", 15.0f)
            .movementSpeedAddition(0.01)
            .build(),
        attackDamage = 2.0f,
        cooldownSeconds = 4.0f,
        repairItem = Supplier { Items.COPPER_INGOT },
    )

    @JvmField
    val IRON_DAGGER: DeferredItem<Item> = registerDagger(
        "iron_dagger",
        ToolMaterial.IRON,
        config = DaggerItem.DaggerConfig.Builder("iron_dagger", 16.0f)
            .movementSpeedAddition(0.01)
            .build(),
        attackDamage = 2.0f,
        cooldownSeconds = 3.0f,
        repairItem = Supplier { Items.IRON_INGOT },
    )

    @JvmField
    val GOLDEN_DAGGER: DeferredItem<Item> = registerDagger(
        "golden_dagger",
        ToolMaterial.GOLD,
        config = DaggerItem.DaggerConfig.Builder("golden_dagger", 11.0f)
            .movementSpeedAddition(0.017)
            .build(),
        attackDamage = 3.0f,
        cooldownSeconds = 3.0f,
        repairItem = Supplier { Items.GOLD_INGOT },
    )

    @JvmField
    val DIAMOND_DAGGER: DeferredItem<Item> = registerDagger(
        "diamond_dagger",
        ToolMaterial.DIAMOND,
        config = DaggerItem.DaggerConfig.Builder("diamond_dagger", 18.0f)
            .movementSpeedAddition(0.01)
            .build(),
        attackDamage = 2.0f,
        cooldownSeconds = 3.0f,
        repairItem = Supplier { Items.DIAMOND },
    )

    @JvmField
    val NETHERITE_DAGGER: DeferredItem<Item> = registerDagger(
        "netherite_dagger",
        ToolMaterial.NETHERITE,
        config = DaggerItem.DaggerConfig.Builder("netherite_dagger", 22.0f)
            .movementSpeedAddition(0.01)
            .build(),
        attackDamage = 2.0f,
        cooldownSeconds = 3.0f,
        repairItem = Supplier { Items.NETHERITE_INGOT },
    )

    @JvmField
    val EMERALD_DAGGER: DeferredItem<Item> = registerDagger(
        "emerald_dagger",
        ModToolMaterial.EMERALD_SWORD,
        config = DaggerItem.DaggerConfig.Builder("emerald_dagger", 16.0f)
            .movementSpeedAddition(0.015)
            .build(),
        attackDamage = 1.0f,
        cooldownSeconds = 2.0f,
        repairItem = Supplier { Items.EMERALD },
    )

    @JvmField
    val LAPIS_DAGGER: DeferredItem<Item> = registerDagger(
        "lapis_dagger",
        ModToolMaterial.LAPIS_SWORD,
        config = DaggerItem.DaggerConfig.Builder("lapis_dagger", 13.0f)
            .movementSpeedAddition(0.019)
            .build(),
        attackDamage = 0.0f,
        cooldownSeconds = 3.0f,
        repairItem = Supplier { Items.LAPIS_LAZULI },
    )

    @JvmField
    val AMETHYST_DAGGER: DeferredItem<Item> = registerDagger(
        "amethyst_dagger",
        ModToolMaterial.AMETHYST_SWORD,
        config = DaggerItem.DaggerConfig.Builder("amethyst_dagger", 12.0f)
            .movementSpeedAddition(0.013)
            .build(),
        attackDamage = 0.0f,
        cooldownSeconds = 3.0f,
        repairItem = Supplier { Items.AMETHYST_SHARD },
    )

    @JvmField
    val STEEL_DAGGER: DeferredItem<Item> = registerDagger(
        "steel_dagger",
        ModToolMaterial.STEEL_SWORD,
        config = DaggerItem.DaggerConfig.Builder("steel_dagger", 18.0f)
            .movementSpeedAddition(0.01)
            .build(),
        attackDamage = 0.0f,
        cooldownSeconds = 3.0f,
        repairItem = Supplier { Material.STEEL_INGOT.get() },
    )

    @JvmField
    val DEAD_WOOD_DAGGER: DeferredItem<Item> = registerDagger(
        "dead_wood_dagger",
        ModToolMaterial.DEAD_WOOD_DAGGER,
        config = DaggerItem.DaggerConfig.Builder("dead_wood_dagger", 14.0f)
            .movementSpeedAddition(0.01)
            .targetEffect(sickleEffect(MobEffects.POISON, 14, 1))
            .targetEffect(sickleEffect(MobEffects.WITHER, 5, 0))
            .build(),
        attackDamage = 5.0f,
        cooldownSeconds = 3.0f,
    )

    @JvmField
    val LEAVES_DAGGER: DeferredItem<Item> = registerDagger(
        "leaves_dagger",
        ModToolMaterial.LEAVES_DAGGER,
        config = DaggerItem.DaggerConfig.Builder("leaves_dagger", 7.0f)
            .movementSpeedAddition(0.01)
            .build(),
        attackDamage = 5.0f,
        cooldownSeconds = 0.5f,
        repairTag = ItemTags.LEAVES,
    )

    @JvmField
    val GHOST_DAGGER: DeferredItem<Item> = registerDagger(
        "ghost_dagger",
        ModToolMaterial.GHOST_DAGGER,
        config = DaggerItem.DaggerConfig.Builder("ghost_dagger", 22.0f)
            .movementSpeedAddition(0.01)
            .targetEffect(sickleEffect(MobEffects.INVISIBILITY, 3, 0))
            .targetParticleId(id("ghost_sickle_particle"))
            .build(),
        attackDamage = 10.0f,
        cooldownSeconds = 3.0f,
        repairItem = Supplier { Material.GHOST_INGOT.get() },
    )

    @JvmField
    val WIND_DAGGER: DeferredItem<Item> = registerDagger(
        "wind_dagger",
        ModToolMaterial.WIND_DAGGER,
        config = DaggerItem.DaggerConfig.Builder("wind_dagger", 15.0f)
            .movementSpeedAddition(0.015)
            .build(),
        attackDamage = 4.0f,
        cooldownSeconds = 1.0f,
        repairItem = Supplier { Material.STREAM_STONE.get() },
    )

    @JvmField
    val EVERLASTING_WINTER_DAGGER: DeferredItem<Item> = registerDagger(
        "everlasting_winter_dagger",
        ModToolMaterial.EVERLASTING_WINTER_DAGGER,
        config = DaggerItem.DaggerConfig.Builder("everlasting_winter_dagger", 21.0f)
            .movementSpeedAddition(0.01)
            .aura(4.0, sickleEffect(MobEffects.SLOWNESS, 3, 1))
            .casterParticleId(id("everlasting_winter_seep_particle"))
            .targetEffect(sickleEffect(MobEffects.SLOWNESS, 3, 6))
            .targetParticleId(id("everlasting_winter_seep_particle"))
            .build(),
        attackDamage = 6.0f,
        cooldownSeconds = 4.0f,
        repairItem = Supplier { Material.ICE_INGOT.get() },
    )

    @JvmField
    val VOID_WHISPERING_DAGGER: DeferredItem<Item> = registerDagger(
        "void_whispering_dagger",
        ModToolMaterial.VOID_WHISPERING_DAGGER,
        config = DaggerItem.DaggerConfig.Builder("void_whispering_dagger", 15.0f)
            .movementSpeedAddition(0.01)
            .build(),
        attackDamage = 4.0f,
        cooldownSeconds = 2.0f,
        itemFactory = { properties, daggerConfig -> VoidWhisperingDaggerItem(properties, daggerConfig) },
        repairItem = Supplier { Material.WITHER_SUBSTANCE.get() },
    ).also(ModDarts.VOID_WHISPERING_DAGGER::bindItemSupplier)

    @JvmField
    val COPPER_BATTLEAXE: DeferredItem<Item> = registerBattleaxe(
        "copper_battleaxe",
        ToolMaterial.COPPER,
        config = BattleaxeItem.BattleaxeConfig.Builder(6.0f, 3.0f, id("copper_battleaxe_ring_particle"))
            .comboThreshold(4)
            .build(),
        attackDamage = 4.0f,
        cooldownSeconds = 8.0f,
        repairItem = Supplier { Items.COPPER_INGOT },
    )

    @JvmField
    val STEEL_BATTLEAXE: DeferredItem<Item> = registerBattleaxe(
        "steel_battleaxe",
        ModToolMaterial.STEEL_SWORD,
        config = BattleaxeItem.BattleaxeConfig.Builder(8.0f, 5.0f, id("steel_battleaxe_ring_particle"))
            .comboThreshold(3)
            .build(),
        attackDamage = 2.0f,
        cooldownSeconds = 7.0f,
        repairItem = Supplier { Material.STEEL_INGOT.get() },
    )

    @JvmField
    val STORM_BATTLEAXE: DeferredItem<Item> = registerBattleaxe(
        "storm_battleaxe",
        ModToolMaterial.STORM_BATTLEAXE,
        config = BattleaxeItem.BattleaxeConfig.Builder(10.0f, 4.0f, id("bubble_ring_particle"))
            .aoe(BattleaxeItem.GRID_OFFSETS, 2.0)
            .particleBursts(skill = 6, combo = 2)
            .comboThreshold(2)
            .build(),
        attackDamage = 4.0f,
        cooldownSeconds = 6.0f,
        repairItem = Supplier { Material.STREAM_STONE.get() },
    )

    @JvmField
    val RAPIER: DeferredItem<Item> = registerRapier(
        "rapier",
        ToolMaterial.IRON,
        config = RapierItem.RapierConfig.Builder(3.0f, 3.0f)
            .comboThreshold(4)
            .build(),
        attackDamage = 3.0f,
        cooldownSeconds = 3.3f,
        repairItem = Supplier { Items.IRON_INGOT },
    )

    @JvmField
    val DEAD_WOOD_RAPIER: DeferredItem<Item> = registerRapier(
        "dead_wood_rapier",
        ModToolMaterial.DEAD_WOOD_RAPIER,
        config = RapierItem.RapierConfig.Builder(3.2f, 3.0f)
            .pulse(skill = 4.0f, combo = 7.0f, magic = true)
            .trailParticle(id("dead_wood_wake_particle"), 17)
            .auraEffect(sickleEffect(MobEffects.WITHER, 2, 0))
            .auraEffect(sickleEffect(MobEffects.POISON, 3, 0))
            .comboAuraEffect(sickleEffect(MobEffects.WITHER, 4, 0))
            .comboAuraEffect(sickleEffect(MobEffects.POISON, 6, 0))
            .comboThreshold(5)
            .build(),
        attackDamage = 3.0f,
        cooldownSeconds = 4.0f,
    )

    @JvmField
    val THUNDER_RAPIER: DeferredItem<Item> = registerRapier(
        "thunder_rapier",
        ModToolMaterial.THUNDER_RAPIER,
        config = RapierItem.RapierConfig.Builder(3.4f, 3.0f)
            .pulse(skill = 7.0f, combo = 15.0f, magic = false)
            .trailParticle(id("thunder_wake_particle"), 20)
            .comboThreshold(4)
            .build(),
        attackDamage = 4.0f,
        cooldownSeconds = 4.4f,
        repairItem = Supplier { Material.LIGHTNING_STONE.get() },
    )

    @JvmStatic
    fun load() {
    }

    private fun registerSimpleSword(
        name: String,
        enUs: String,
        zhCn: String,
        material: ToolMaterial,
        attackDamage: Float = 2.0f,
        attackSpeed: Float = -2.4f,
        repairItem: Supplier<Item>? = null,
        tags: List<TagKey<Item>> = swordWeaponTags,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name, mapOf("en_us" to enUs, "zh_cn" to zhCn))
                .props {
                    var properties = Item.Properties().sword(material, attackDamage, attackSpeed).stacksTo(1)
                    if (repairItem != null) {
                        properties = properties.repairable(repairItem.get())
                    }
                    properties
                }
                .tags(tags)
                .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                .build(),
        )

    private fun registerSickle(
        name: String,
        material: ToolMaterial,
        config: SickleItem.SickleConfig,
        attackDamage: Float = 2.0f,
        attackSpeed: Float = -3.2f,
        cooldown: Float? = null,
        itemFactory: ((Item.Properties, SickleItem.SickleConfig) -> Item)? = null,
        repairItem: Supplier<Item>? = null,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .func { properties -> itemFactory?.invoke(properties, config) ?: SickleItem(properties, config) }
                .props {
                    var properties = Item.Properties().sword(material, attackDamage, attackSpeed).stacksTo(1)
                    if (cooldown != null) {
                        properties = properties.useCooldown(cooldown)
                    }
                    if (repairItem != null) {
                        properties = properties.repairable(repairItem.get())
                    }
                    properties
                }
                .tags(sickleWeaponTags)
                .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                .build(),
        )

    private const val DAGGER_ATTACK_SPEED: Float = -2.2f
    private const val BATTLEAXE_ATTACK_SPEED: Float = -3.4f
    private const val RAPIER_ATTACK_SPEED: Float = -1.8f

    private fun registerDagger(
        name: String,
        material: ToolMaterial,
        config: DaggerItem.DaggerConfig,
        attackDamage: Float,
        cooldownSeconds: Float,
        itemFactory: ((Item.Properties, DaggerItem.DaggerConfig) -> Item)? = null,
        repairItem: Supplier<Item>? = null,
        repairTag: TagKey<Item>? = null,
        burnTime: Int? = null,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .func { properties -> itemFactory?.invoke(properties, config) ?: DaggerItem(properties, config) }
                .props {
                    var properties = Item.Properties()
                        .sword(material, attackDamage, DAGGER_ATTACK_SPEED)
                        .stacksTo(1)
                        .useCooldown(cooldownSeconds)
                    when {
                        repairItem != null -> properties = properties.repairable(repairItem.get())
                        repairTag != null -> properties = properties.repairable(repairTag)
                    }
                    properties
                }
                .tags(daggerWeaponTags)
                .customProp(
                    CustomItemProperties.Builder().apply {
                        if (burnTime != null) {
                            burnTime(burnTime)
                        }
                    }.build(),
                )
                .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                .build(),
        )

    private fun registerBattleaxe(
        name: String,
        material: ToolMaterial,
        config: BattleaxeItem.BattleaxeConfig,
        attackDamage: Float,
        attackSpeed: Float = BATTLEAXE_ATTACK_SPEED,
        cooldownSeconds: Float,
        repairItem: Supplier<Item>,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .func { properties -> BattleaxeItem(properties, config) }
                .props {
                    Item.Properties()
                        .axe(material, attackDamage, attackSpeed)
                        .component(DataComponents.WEAPON, net.minecraft.world.item.component.Weapon(1))
                        .stacksTo(1)
                        .useCooldown(cooldownSeconds)
                        .repairable(repairItem.get())
                }
                .tags(battleaxeWeaponTags + ItemTags.AXES)
                .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                .build(),
        )

    private fun registerRapier(
        name: String,
        material: ToolMaterial,
        config: RapierItem.RapierConfig,
        attackDamage: Float,
        attackSpeed: Float = RAPIER_ATTACK_SPEED,
        cooldownSeconds: Float,
        repairItem: Supplier<Item>? = null,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .func { properties -> RapierItem(properties, config) }
                .props {
                    var properties = Item.Properties()
                        .sword(material, attackDamage, attackSpeed)
                        .stacksTo(1)
                        .useCooldown(cooldownSeconds)
                    if (repairItem != null) {
                        properties = properties.repairable(repairItem.get())
                    }
                    properties
                }
                .tags(rapierWeaponTags)
                .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                .build(),
        )

    private fun sickleProc(
        chanceDenominator: Int,
        extraDamage: Float = 0.0f,
        holderParticleId: Identifier? = null,
        targetParticleId: Identifier? = null,
        vararg effects: SickleItem.EffectConfig,
    ): SickleItem.PassiveProcConfig {
        val builder = SickleItem.PassiveProcConfig.Builder(chanceDenominator)
        if (extraDamage > 0.0f) {
            builder.extraDamage(extraDamage)
        }
        builder.holderParticleId(holderParticleId)
        builder.targetParticleId(targetParticleId)
        effects.forEach(builder::effect)
        return builder.build()
    }

    private fun sickleEffect(effect: Holder<MobEffect>, durationSeconds: Int, amplifier: Int): SickleItem.EffectConfig =
        SickleItem.EffectConfig.Builder(effect)
            .durationSeconds(durationSeconds)
            .amplifier(amplifier)
            .build()

    private fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, path)

    private fun registerStaff(
        name: String,
        func: Function<Item.Properties, out Item>,
        durability: Int,
        cooldown: Float? = null,
        enchantability: Int? = null,
        repairItem: Supplier<Item>? = null,
        repairTag: TagKey<Item>? = null,
        burnTime: Int? = null,
    ): DeferredItem<Item> =
        registerMagicWeapon(
            name = name,
            func = func,
            durability = durability,
            cooldown = cooldown,
            enchantability = enchantability,
            repairItem = repairItem,
            repairTag = repairTag,
            burnTime = burnTime,
            tags = staffWeaponTags,
            modelTemplate = ModelTemplates.FLAT_HANDHELD_ITEM,
        )

    private fun registerMagicBook(
        name: String,
        func: Function<Item.Properties, out Item>,
        durability: Int,
        cooldown: Float? = null,
        enchantability: Int? = null,
        repairItem: Supplier<Item>? = null,
        repairTag: TagKey<Item>? = null,
        burnTime: Int? = null,
        tags: List<TagKey<Item>> = magicBookWeaponTags,
    ): DeferredItem<Item> =
        registerMagicWeapon(
            name = name,
            func = func,
            durability = durability,
            cooldown = cooldown,
            enchantability = enchantability,
            repairItem = repairItem,
            repairTag = repairTag,
            burnTime = burnTime,
            tags = tags,
            modelTemplate = ModelTemplates.FLAT_ITEM,
        )

    private fun registerMagicWeapon(
        name: String,
        func: Function<Item.Properties, out Item>,
        durability: Int,
        cooldown: Float?,
        enchantability: Int?,
        repairItem: Supplier<Item>?,
        repairTag: TagKey<Item>?,
        burnTime: Int?,
        tags: List<TagKey<Item>>,
        modelTemplate: ModelTemplate,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .func(func)
                .props {
                    var properties = Item.Properties().stacksTo(1).durability(durability)
                    if (cooldown != null) {
                        properties = properties.useCooldown(cooldown)
                    }
                    if (enchantability != null) {
                        properties = properties.enchantable(enchantability)
                    }
                    when {
                        repairItem != null -> properties = properties.repairable(repairItem.get())
                        repairTag != null -> properties = properties.repairable(repairTag)
                    }
                    properties
                }
                .tags(tags)
                .customProp(
                    CustomItemProperties.Builder().apply {
                        if (burnTime != null) {
                            burnTime(burnTime)
                        }
                    }.build(),
                )
                .modelTemplate(modelTemplate)
                .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                .build(),
        )

    private fun registerDart(definition: DartDefinition): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(definition.path)
                .func(Dart.factory(definition))
                .props { Item.Properties().useCooldown(definition.itemSettings.cooldownTicks / 20.0f) }
                .tags(dartWeaponTags)
                .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
                .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
                .build(),
        ).also(definition::bindItemSupplier)
}
