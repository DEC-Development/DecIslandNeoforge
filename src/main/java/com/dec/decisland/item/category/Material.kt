package com.dec.decisland.item.category

import com.dec.decisland.item.CustomItemProperties
import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.lang.Lang
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

object Material {
    private fun registerMaterial(
        name: String,
        props: Supplier<Item.Properties> = Supplier { Item.Properties() },
        customProp: CustomItemProperties = CustomItemProperties.Builder().build(),
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name, Lang.item.get(name))
                .props(props)
                .customProp(customProp)
                .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
                .build(),
        )

    @JvmField
    val BLUE_GEM_DEBRIS: DeferredItem<Item> = registerMaterial("blue_gem_debris")

    @JvmField
    val RED_GEM_DEBRIS: DeferredItem<Item> = registerMaterial("red_gem_debris")

    @JvmField
    val BAT_WING: DeferredItem<Item> = registerMaterial("bat_wing")

    @JvmField
    val GHOST_INGOT: DeferredItem<Item> = registerMaterial("ghost_ingot")

    @JvmField
    val ICE_INGOT: DeferredItem<Item> = registerMaterial("ice_ingot")

    @JvmField
    val LAVA_INGOT: DeferredItem<Item> = registerMaterial("lava_ingot")

    @JvmField
    val EVERLASTING_WINTER_INGOT: DeferredItem<Item> = registerMaterial("everlasting_winter_ingot")

    @JvmField
    val CORAL_INGOT: DeferredItem<Item> = registerMaterial("coral_ingot")

    @JvmField
    val GLASS_INGOT: DeferredItem<Item> = registerMaterial("glass_ingot")

    @JvmField
    val PURE_INGOT: DeferredItem<Item> = registerMaterial("pure_ingot")

    @JvmField
    val STEEL_INGOT: DeferredItem<Item> = registerMaterial("steel_ingot")

    @JvmField
    val ROUGH_STEEL: DeferredItem<Item> = registerMaterial("rough_steel")

    @JvmField
    val ALCHEMIC_STONE: DeferredItem<Item> = registerMaterial("alchemic_stone")

    @JvmField
    val COAL_NUGGET: DeferredItem<Item> = registerMaterial("coal_nugget")

    @JvmField
    val COMPLETE_BAT_WING: DeferredItem<Item> = registerMaterial("complete_bat_wing")

    @JvmField
    val DARK_DEBRIS: DeferredItem<Item> = registerMaterial("dark_debris")

    @JvmField
    val DIAMOND_NUGGET: DeferredItem<Item> = registerMaterial("diamond_nugget")

    @JvmField
    val EMERALD_NUGGET: DeferredItem<Item> = registerMaterial("emerald_nugget")

    @JvmField
    val EMPTY_TOTEM: DeferredItem<Item> = registerMaterial("empty_totem")

    @JvmField
    val ENDER_BONE: DeferredItem<Item> = registerMaterial("ender_bone")

    @JvmField
    val ENDER_BREATH: DeferredItem<Item> = registerMaterial("ender_breath")

    @JvmField
    val ENDER_CORE: DeferredItem<Item> = registerMaterial("ender_core")

    @JvmField
    val ENDER_STONE: DeferredItem<Item> = registerMaterial("ender_stone")

    @JvmField
    val ENDER_POWDER: DeferredItem<Item> = registerMaterial("ender_powder")

    @JvmField
    val ENDER_SUBSTANCE: DeferredItem<Item> = registerMaterial("ender_substance")

    @JvmField
    val ENRICHED_URANIUM: DeferredItem<Item> = registerMaterial("enriched_uranium")

    @JvmField
    val EYE_OF_NATURE: DeferredItem<Item> = registerMaterial("eye_of_nature")

    @JvmField
    val EVERLASTING_WINTER_STICK: DeferredItem<Item> = registerMaterial("everlasting_winter_stick")

    @JvmField
    val FLOWER_ESSENCE: DeferredItem<Item> = registerMaterial("flower_essence")

    @JvmField
    val FROZEN_POWER_DEBRIS: DeferredItem<Item> = registerMaterial("frozen_power_debris")

    @JvmField
    val GHOST_ESSENCE: DeferredItem<Item> = registerMaterial("ghost_essence")

    @JvmField
    val GOLD_YUANBAO: DeferredItem<Item> = registerMaterial("gold_yuanbao")

    @JvmField
    val HEALTH_NUGGET: DeferredItem<Item> = registerMaterial("health_nugget")

    @JvmField
    val HEALTH_STONE: DeferredItem<Item> = registerMaterial("health_stone")

    @JvmField
    val HEMP_ROPE: DeferredItem<Item> = registerMaterial("hemp_rope")

    @JvmField
    val HIGH_CONCENTRATION_URANIUM: DeferredItem<Item> = registerMaterial("high_concentration_uranium")

    @JvmField
    val ICE_BRICK: DeferredItem<Item> = registerMaterial("ice_brick")

    @JvmField
    val ICE_HEART: DeferredItem<Item> = registerMaterial("ice_heart")

    @JvmField
    val ICE_NUGGET: DeferredItem<Item> = registerMaterial("ice_nugget")

    @JvmField
    val ICE_ROD: DeferredItem<Item> = registerMaterial("ice_rod")

    @JvmField
    val IRON_KEY: DeferredItem<Item> = registerMaterial("iron_key")

    @JvmField
    val IRON_STICK: DeferredItem<Item> = registerMaterial("iron_stick")

    @JvmField
    val LAPIS_NUGGET: DeferredItem<Item> = registerMaterial("lapis_nugget")

    @JvmField
    val LAVA_ESSENCE: DeferredItem<Item> = registerMaterial("lava_essence")

    @JvmField
    val LAVA_NUGGET: DeferredItem<Item> = registerMaterial("lava_nugget")

    @JvmField
    val LIGHTNING_STONE: DeferredItem<Item> = registerMaterial("lightning_stone")

    @JvmField
    val MAGIC_ARTICLE: DeferredItem<Item> = registerMaterial("magic_article")

    @JvmField
    val MAGIC_POWDER: DeferredItem<Item> = registerMaterial("magic_powder")

    @JvmField
    val MAGIC_SURGE_CORE: DeferredItem<Item> = registerMaterial("magic_surge_core")

    @JvmField
    val MYSTERIOUS_KEY_DEBRIS: DeferredItem<Item> = registerMaterial("mysterious_key_debris")

    @JvmField
    val PINE_CONE: DeferredItem<Item> = registerMaterial("pine_cone")

    @JvmField
    val POISON_GLAND: DeferredItem<Item> = registerMaterial("poison_gland")

    @JvmField
    val PULSE_STONE: DeferredItem<Item> = registerMaterial("pulse_stone")

    @JvmField
    val RADIATE_CRYSTAL: DeferredItem<Item> = registerMaterial("radiate_crystal")

    @JvmField
    val SHADOW_CRYSTAL: DeferredItem<Item> = registerMaterial("shadow_crystal")

    @JvmField
    val SHADOW_FEATHER: DeferredItem<Item> = registerMaterial("shadow_feather")

    @JvmField
    val SHELL: DeferredItem<Item> = registerMaterial("shell")

    @JvmField
    val SLIVER_YUANBAO: DeferredItem<Item> = registerMaterial("sliver_yuanbao")

    @JvmField
    val SMALL_STONE: DeferredItem<Item> = registerMaterial("small_stone")

    @JvmField
    val SMALL_STONE_BLOCK: DeferredItem<Item> = registerMaterial("small_stone_block")

    @JvmField
    val SOUL: DeferredItem<Item> = registerMaterial("soul")

    @JvmField
    val SOUL_BLAZE_ROD: DeferredItem<Item> = registerMaterial("soul_blaze_rod")

    @JvmField
    val SPIRAL_SHELL: DeferredItem<Item> = registerMaterial("spiral_shell")

    @JvmField
    val STAR_DEBRIS: DeferredItem<Item> = registerMaterial("star_debris")

    @JvmField
    val STEEL_NUGGET: DeferredItem<Item> = registerMaterial("steel_nugget")

    @JvmField
    val STRAW_ROPE: DeferredItem<Item> = registerMaterial("straw_rope")

    @JvmField
    val STREAM_STONE: DeferredItem<Item> = registerMaterial("stream_stone")

    @JvmField
    val SUN_STONE: DeferredItem<Item> = registerMaterial("sun_stone")

    @JvmField
    val URANIUM: DeferredItem<Item> = registerMaterial("uranium")

    @JvmField
    val WASTE: DeferredItem<Item> = registerMaterial("waste")

    @JvmField
    val WINE_GLASS: DeferredItem<Item> = registerMaterial("wine_glass")

    @JvmField
    val WITHER_SUBSTANCE: DeferredItem<Item> = registerMaterial("wither_substance")

    @JvmField
    val OLD_BOOK: DeferredItem<Item> = registerMaterial(
        "old_book",
        props = Supplier { Item.Properties().stacksTo(1) },
        customProp = CustomItemProperties.Builder().burnTime(10).build(),
    )

    @JvmStatic
    fun load() {
    }
}
