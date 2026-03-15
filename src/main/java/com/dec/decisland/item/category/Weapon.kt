package com.dec.decisland.item.category

import com.dec.decisland.item.CustomItemProperties
import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.ModToolMaterial
import com.dec.decisland.lang.Lang
import com.dec.decisland.item.custom.AbsoluteZero
import com.dec.decisland.item.custom.BambooKatana
import com.dec.decisland.item.custom.HardBambooKatana
import com.dec.decisland.item.gun.EverlastingWinterFlintlock
import com.dec.decisland.item.gun.Flintlock
import com.dec.decisland.item.gun.FlintlockBullet
import com.dec.decisland.item.gun.FlintlockPro
import com.dec.decisland.item.gun.GhostFlintlock
import com.dec.decisland.item.gun.LavaFlintlock
import com.dec.decisland.item.gun.ShortFlintlock
import com.dec.decisland.item.gun.StarFlintlock
import com.dec.decisland.item.gun.StormFlintlock
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items.STICK
import net.minecraft.world.item.component.Consumables
import net.neoforged.neoforge.registries.DeferredItem

object Weapon {
    @JvmField
    val FLINTLOCK_BULLET: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("flintlock_bullet", Lang.item.get("flintlock_bullet"))
            .func(::FlintlockBullet)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("flintlock", Lang.item.get("flintlock"))
            .func(::Flintlock)
            .props { Item.Properties().stacksTo(1).durability(230).enchantable(13) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val FLINTLOCK_PRO: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("flintlock_pro", Lang.item.get("flintlock_pro"))
            .func(::FlintlockPro)
            .props { Item.Properties().stacksTo(1).durability(784).enchantable(14) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val SHORT_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("short_flintlock", Lang.item.get("short_flintlock"))
            .func(::ShortFlintlock)
            .props { Item.Properties().stacksTo(1).durability(120).enchantable(10) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val EVERLASTING_WINTER_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("everlasting_winter_flintlock", Lang.item.get("everlasting_winter_flintlock"))
            .func(::EverlastingWinterFlintlock)
            .props { Item.Properties().stacksTo(1).durability(1668).enchantable(24) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val GHOST_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("ghost_flintlock", Lang.item.get("ghost_flintlock"))
            .func(::GhostFlintlock)
            .props { Item.Properties().stacksTo(1).durability(3759).enchantable(18) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val LAVA_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("lava_flintlock", Lang.item.get("lava_flintlock"))
            .func(::LavaFlintlock)
            .props { Item.Properties().stacksTo(1).durability(968).enchantable(11) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val STAR_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("star_flintlock", Lang.item.get("star_flintlock"))
            .func(::StarFlintlock)
            .props { Item.Properties().stacksTo(1).durability(3855).enchantable(17) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val STORM_FLINTLOCK: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("storm_flintlock", Lang.item.get("storm_flintlock"))
            .func(::StormFlintlock)
            .props { Item.Properties().stacksTo(1).durability(394).enchantable(26) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val ABSOLUTE_ZERO: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("absolute_zero", Lang.item.get("absolute_zero"))
            .func(::AbsoluteZero)
            .props { Item.Properties().sword(ModToolMaterial.ABSOLUTE_ZERO, 3.0f, -2.4f).useCooldown(3.0f).stacksTo(1) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val BAMBOO_KATANA: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("bamboo_katana", Lang.item.get("bamboo_katana"))
            .func(::BambooKatana)
            .props { Item.Properties().sword(ModToolMaterial.BAMBOO, 2.0f, -2.4f).useCooldown(2.5f).stacksTo(1) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val CANDY_CANE: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("candy_cane", Lang.item.get("candy_cane"))
            .props {
                Item.Properties()
                    .food(FoodProperties(2, 0.2f, true), Consumables.defaultFood().build())
                    .sword(ModToolMaterial.CANDY_CANE, 2.0f, -2.0f)
                    .stacksTo(1)
            }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val HARD_BAMBOO_KATANA: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("hard_bamboo_katana", Lang.item.get("hard_bamboo_katana"))
            .func(::HardBambooKatana)
            .props { Item.Properties().sword(ModToolMaterial.HARD_BAMBOO, 2.0f, -2.4f).useCooldown(2.2f).stacksTo(1) }
            .customProp(CustomItemProperties.Builder().burnTime(8).build())
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val HARD_LOLLIPOP: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("hard_lollipop", Lang.item.get("hard_lollipop"))
            .props {
                Item.Properties()
                    .food(FoodProperties(14, 0.8f, false), Consumables.defaultFood().build())
                    .usingConvertsTo(STICK)
                    .sword(ModToolMaterial.HARD_LOLLIPOP, 3.0f, -3.0f)
            }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val CACTUS_SWORD: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("cactus_sword", Lang.item.get("cactus_sword"))
            .props { Item.Properties().sword(ModToolMaterial.CACTUS, 2.0f, -2.7f) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmField
    val CORRUPTED_SWORD: DeferredItem<Item> = ModItems.registerItem(
        ItemConfig.Builder("corrupted_sword", Lang.item.get("corrupted_sword"))
            .props { Item.Properties().sword(ModToolMaterial.CORRUPTED, 2.0f, -2.4f) }
            .modelTemplate(ModelTemplates.FLAT_HANDHELD_ITEM)
            .creativeTab(ModCreativeModeTabs.DECISLAND_WEAPONS_TAB)
            .build(),
    )

    @JvmStatic
    fun load() {
    }
}
