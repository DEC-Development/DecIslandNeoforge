package com.dec.decisland.item.category

import com.dec.decisland.item.CustomItemProperties
import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

object Material {
    private fun registerMaterial(
        name: String,
        enUs: String,
        zhCn: String,
        props: Supplier<Item.Properties> = Supplier { Item.Properties() },
        customProp: CustomItemProperties = CustomItemProperties.Builder().build(),
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name, mapOf("en_us" to enUs, "zh_cn" to zhCn))
                .props(props)
                .customProp(customProp)
                .creativeTab(ModCreativeModeTabs.DECISLAND_MATERIALS_TAB)
                .build(),
        )

    @JvmField
    val BLUE_GEM_DEBRIS: DeferredItem<Item> = registerMaterial("blue_gem_debris", "Blue Gem Debris", "蓝宝石碎片")

    @JvmField
    val RED_GEM_DEBRIS: DeferredItem<Item> = registerMaterial("red_gem_debris", "Red Gem Debris", "红宝石碎片")

    @JvmField
    val BAT_WING: DeferredItem<Item> = registerMaterial("bat_wing", "Incomplete Bat Wing", "残缺蝙蝠翅")

    @JvmField
    val GHOST_INGOT: DeferredItem<Item> = registerMaterial("ghost_ingot", "Ghost Ingot", "幽魂锭")

    @JvmField
    val ICE_INGOT: DeferredItem<Item> = registerMaterial("ice_ingot", "Ice Ingot", "冰锭")

    @JvmField
    val LAVA_INGOT: DeferredItem<Item> = registerMaterial("lava_ingot", "Lava Ingot", "岩浆锭")

    @JvmField
    val EVERLASTING_WINTER_INGOT: DeferredItem<Item> =
        registerMaterial("everlasting_winter_ingot", "Everlasting Winter Ingot", "永冬锭")

    @JvmField
    val CORAL_INGOT: DeferredItem<Item> = registerMaterial("coral_ingot", "Coral Ingot", "珊瑚锭")

    @JvmField
    val GLASS_INGOT: DeferredItem<Item> = registerMaterial("glass_ingot", "Glass Ingot", "玻璃锭")

    @JvmField
    val PURE_INGOT: DeferredItem<Item> = registerMaterial("pure_ingot", "Pure Ingot", "纯洁之锭")

    @JvmField
    val STEEL_INGOT: DeferredItem<Item> = registerMaterial("steel_ingot", "Steel Ingot", "钢锭")

    @JvmField
    val ROUGH_STEEL: DeferredItem<Item> = registerMaterial("rough_steel", "Rough Steel", "粗制钢料")

    @JvmField
    val ALCHEMIC_STONE: DeferredItem<Item> = registerMaterial("alchemic_stone", "Alchemic Stone", "炼金石")

    @JvmField
    val ASH_KEY: DeferredItem<Item> = registerMaterial("ash_key", "Ash Key", "灰烬钥匙")

    @JvmField
    val BAT_BAIT: DeferredItem<Item> = registerMaterial("bat_bait", "Bat Bait", "蝙蝠诱饵")

    @JvmField
    val COAL_NUGGET: DeferredItem<Item> = registerMaterial("coal_nugget", "Coal Nugget", "煤炭粒")

    @JvmField
    val COMPLETE_BAT_WING: DeferredItem<Item> = registerMaterial("complete_bat_wing", "Complete Bat Wing", "完整蝙蝠翅")

    @JvmField
    val DARK_DEBRIS: DeferredItem<Item> = registerMaterial("dark_debris", "Dark Debris", "暗夜碎片")

    @JvmField
    val DARK_PEARL: DeferredItem<Item> = registerMaterial("dark_pearl", "Dark Pearl", "暗黑珍珠")

    @JvmField
    val DARK_STONE: DeferredItem<Item> = registerMaterial("dark_stone", "Dark Stone", "黑暗石")

    @JvmField
    val DIAMOND_NUGGET: DeferredItem<Item> = registerMaterial("diamond_nugget", "Diamond Nugget", "钻石粒")

    @JvmField
    val EMERALD_NUGGET: DeferredItem<Item> = registerMaterial("emerald_nugget", "Emerald Nugget", "绿宝石粒")

    @JvmField
    val EMPTY_TOTEM: DeferredItem<Item> = registerMaterial("empty_totem", "Empty Totem", "空图腾")

    @JvmField
    val ENDER_BONE: DeferredItem<Item> = registerMaterial("ender_bone", "Ender Bone", "末影骨")

    @JvmField
    val ENDER_BREATH: DeferredItem<Item> = registerMaterial("ender_breath", "Ender Breath", "末影之息")

    @JvmField
    val ENDER_CORE: DeferredItem<Item> = registerMaterial("ender_core", "Ender Core", "末影核心")

    @JvmField
    val ENDER_STONE: DeferredItem<Item> = registerMaterial("ender_stone", "Ender Stone", "末影石")

    @JvmField
    val ENDER_POWDER: DeferredItem<Item> = registerMaterial("ender_powder", "Ender Powder", "末影粉末")

    @JvmField
    val ENDER_SUBSTANCE: DeferredItem<Item> = registerMaterial("ender_substance", "Ender Substance", "潜影物质")

    @JvmField
    val ENRICHED_URANIUM: DeferredItem<Item> = registerMaterial("enriched_uranium", "Enriched Uranium", "浓缩铀")

    @JvmField
    val ENTITY_SOUL: DeferredItem<Item> = registerMaterial("entity_soul", "Entity Soul", "实体灵魂")

    @JvmField
    val EYE_OF_NATURE: DeferredItem<Item> = registerMaterial("eye_of_nature", "Eye Of Nature", "自然之眼")

    @JvmField
    val EVERLASTING_WINTER_STICK: DeferredItem<Item> =
        registerMaterial("everlasting_winter_stick", "Everlasting Winter Stick", "永冬棍")

    @JvmField
    val FLOWER_ESSENCE: DeferredItem<Item> = registerMaterial("flower_essence", "Flower Essence", "花之精华")

    @JvmField
    val FROZEN_POWER_DEBRIS: DeferredItem<Item> =
        registerMaterial("frozen_power_debris", "Frozen Power Debris", "冰霜能量碎片")

    @JvmField
    val GHOST_ESSENCE: DeferredItem<Item> = registerMaterial("ghost_essence", "Ghost Essence", "幽魂精华")

    @JvmField
    val GOLD_YUANBAO: DeferredItem<Item> = registerMaterial("gold_yuanbao", "Gold Yuanbao", "金元宝")

    @JvmField
    val HEALTH_NUGGET: DeferredItem<Item> = registerMaterial("health_nugget", "Health Nugget", "生命粒")

    @JvmField
    val HEALTH_STONE: DeferredItem<Item> = registerMaterial("health_stone", "Health Stone", "生命石")

    @JvmField
    val HEMP_ROPE: DeferredItem<Item> = registerMaterial("hemp_rope", "Hemp Rope", "麻绳")

    @JvmField
    val HIGH_CONCENTRATION_URANIUM: DeferredItem<Item> =
        registerMaterial("high_concentration_uranium", "High Concentration Uranium", "高纯度铀")

    @JvmField
    val ICE_BRICK: DeferredItem<Item> = registerMaterial("ice_brick", "Ice Brick", "冰砖")

    @JvmField
    val ICE_HEART: DeferredItem<Item> = registerMaterial("ice_heart", "Heart Of Ice", "冰冻之心")

    @JvmField
    val ICE_NUGGET: DeferredItem<Item> = registerMaterial("ice_nugget", "Ice Nugget", "冰粒")

    @JvmField
    val ICE_ROD: DeferredItem<Item> = registerMaterial("ice_rod", "Ice Rod", "寒霜棍")

    @JvmField
    val IRON_KEY: DeferredItem<Item> = registerMaterial("iron_key", "Iron Key", "铁钥匙")

    @JvmField
    val IRON_STICK: DeferredItem<Item> = registerMaterial("iron_stick", "Iron Stick", "铁棍")

    @JvmField
    val LAPIS_NUGGET: DeferredItem<Item> = registerMaterial("lapis_nugget", "Lapis Nugget", "青金石粒")

    @JvmField
    val LAVA_ESSENCE: DeferredItem<Item> = registerMaterial("lava_essence", "Lava Essence", "岩浆精华")

    @JvmField
    val LAVA_NUGGET: DeferredItem<Item> = registerMaterial("lava_nugget", "Lava Nugget", "炙热粒")

    @JvmField
    val LIGHTNING_STONE: DeferredItem<Item> = registerMaterial("lightning_stone", "Lightning Stone", "雷电石")

    @JvmField
    val MAGIC_CHEST_DEBRIS: DeferredItem<Item> =
        registerMaterial("magic_chest_debris", "Magic Chest Debris", "魔法箱子碎片")

    @JvmField
    val MAGIC_ARTICLE: DeferredItem<Item> = registerMaterial("magic_article", "Magic Article", "魔法充能条")

    @JvmField
    val MAGIC_POWDER: DeferredItem<Item> = registerMaterial("magic_powder", "Magic Powder", "魔法粉末")

    @JvmField
    val MAGIC_SURGE_CORE: DeferredItem<Item> = registerMaterial("magic_surge_core", "Magic Surge Core", "魔法涌动核心")

    @JvmField
    val MYSTERIOUS_KEY: DeferredItem<Item> = registerMaterial("mysterious_key", "Mysterious Key", "神秘钥匙")

    @JvmField
    val MYSTERIOUS_KEY_DEBRIS: DeferredItem<Item> =
        registerMaterial("mysterious_key_debris", "Mysterious Key Debris", "神秘钥匙碎片")

    @JvmField
    val NATURE_ESSENCE: DeferredItem<Item> = registerMaterial("nature_essence", "Nature Essence", "自然精华")

    @JvmField
    val PINE_CONE: DeferredItem<Item> = registerMaterial("pine_cone", "Pine Cone", "松果")

    @JvmField
    val POISON_GLAND: DeferredItem<Item> = registerMaterial("poison_gland", "Poison Gland", "剧毒腺体")

    @JvmField
    val PULSE_STONE: DeferredItem<Item> = registerMaterial("pulse_stone", "Pulse Stone", "脉冲石")

    @JvmField
    val RADIATE_CRYSTAL: DeferredItem<Item> = registerMaterial("radiate_crystal", "Radiate Crystal", "辐射水晶")

    @JvmField
    val SHADOW_CRYSTAL: DeferredItem<Item> = registerMaterial("shadow_crystal", "Shadow Crystal", "暗影水晶")

    @JvmField
    val SHADOW_FEATHER: DeferredItem<Item> = registerMaterial("shadow_feather", "Shadow Feather", "暗影羽")

    @JvmField
    val SHELL: DeferredItem<Item> = registerMaterial("shell", "Shell", "贝壳")

    @JvmField
    val SLIVER_YUANBAO: DeferredItem<Item> = registerMaterial("sliver_yuanbao", "Sliver Yuanbao", "银元宝")

    @JvmField
    val SMALL_STONE: DeferredItem<Item> = registerMaterial("small_stone", "Small Stone", "小石子")

    @JvmField
    val SMALL_STONE_BLOCK: DeferredItem<Item> = registerMaterial("small_stone_block", "Small Stone Block", "小石块")

    @JvmField
    val SOUL: DeferredItem<Item> = registerMaterial("soul", "Soul", "灵魂")

    @JvmField
    val SOUL_BLAZE_ROD: DeferredItem<Item> = registerMaterial("soul_blaze_rod", "Soul Blaze Rod", "灵魂烈焰棒")

    @JvmField
    val SOUL_KEY: DeferredItem<Item> = registerMaterial("soul_key", "Soul Key", "灵魂钥匙")

    @JvmField
    val SPIRAL_SHELL: DeferredItem<Item> = registerMaterial("spiral_shell", "Spiral Shell", "螺蛳")

    @JvmField
    val STAR_DEBRIS: DeferredItem<Item> = registerMaterial("star_debris", "Star Debris", "星体碎片")

    @JvmField
    val STEEL_NUGGET: DeferredItem<Item> = registerMaterial("steel_nugget", "Steel Nugget", "钢粒")

    @JvmField
    val STRAW_ROPE: DeferredItem<Item> = registerMaterial("straw_rope", "Straw Rope", "草绳")

    @JvmField
    val STREAM_STONE: DeferredItem<Item> = registerMaterial("stream_stone", "Stream Stone", "溪流石")

    @JvmField
    val SUN_STONE: DeferredItem<Item> = registerMaterial("sun_stone", "Sun Stone", "日光石")

    @JvmField
    val URANIUM: DeferredItem<Item> = registerMaterial("uranium", "Uranium", "铀")

    @JvmField
    val WASTE: DeferredItem<Item> = registerMaterial("waste", "Waste", "废料")

    @JvmField
    val WINE_GLASS: DeferredItem<Item> = registerMaterial("wine_glass", "Wine Glass", "酒杯")

    @JvmField
    val WITHER_SUBSTANCE: DeferredItem<Item> = registerMaterial("wither_substance", "Wither Substance", "凋零物质")

    @JvmField
    val OLD_BOOK: DeferredItem<Item> = registerMaterial(
        "old_book",
        "Ancient Book",
        "古老的书",
        props = Supplier { Item.Properties().stacksTo(1) },
        customProp = CustomItemProperties.Builder().burnTime(10).build(),
    )

    @JvmStatic
    fun load() {
    }
}
