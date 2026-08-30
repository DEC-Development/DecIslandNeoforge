package com.dec.decisland.block.category

import com.dec.decisland.block.ModBlocks
import com.dec.decisland.block.custom.SimplePlantBlock
import com.dec.decisland.item.ModCreativeModeTabs
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredBlock
import java.util.function.Supplier

object SimplePlant {
    @JvmField
    val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_NATURE_TAB

    private val registeredPlants = mutableListOf<DeferredBlock<SimplePlantBlock>>()

    private fun registerSimplePlant(
        name: String,
        selectionWidth: Double,
        selectionHeight: Double,
        vararg placementPaths: String,
    ): DeferredBlock<SimplePlantBlock> =
        registerSimplePlant(
            name = name,
            selectionWidth = selectionWidth,
            selectionHeight = selectionHeight,
            lightLevel = 0,
            placementPaths = placementPaths,
        )

    private fun registerSimplePlant(
        name: String,
        selectionWidth: Double,
        selectionHeight: Double,
        lightLevel: Int,
        vararg placementPaths: String,
    ): DeferredBlock<SimplePlantBlock> =
        ModBlocks.registerSimplePlant(
            name = name,
            selectionWidth = selectionWidth,
            selectionHeight = selectionHeight,
            placement = ModBlocks.placementOfPaths(*placementPaths),
            creativeTab = creativeTab,
            lightLevel = lightLevel,
        ).also(registeredPlants::add)

    @JvmStatic
    fun allBlocks(): Array<SimplePlantBlock> = registeredPlants.map { it.get() }.toTypedArray()

    @JvmField
    val BABY_BLUE_EYES_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "baby_blue_eyes_flower",
        selectionWidth = 9.0,
        selectionHeight = 16.0,
        "grass",
        "dirt",
    )

    @JvmField
    val BALLOON_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "balloon_flower",
        selectionWidth = 7.0,
        selectionHeight = 15.0,
        "stone",
        "snow",
        "grass",
        "dirt",
        "gravel",
    )

    @JvmField
    val BUTTER_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "butter_flower",
        selectionWidth = 9.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
    )

    @JvmField
    val ROSEMARY: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "rosemary",
        selectionWidth = 10.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
        "podzol",
    )

    @JvmField
    val THYME: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "thyme",
        selectionWidth = 10.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
        "podzol",
    )

    @JvmField
    val SNOW_LOTUS: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "snow_lotus",
        selectionWidth = 12.0,
        selectionHeight = 9.0,
        "stone",
        "snow",
        "grass",
        "dirt",
        "gravel",
    )

    @JvmField
    val WITHER_CONE: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "wither_cone",
        selectionWidth = 10.0,
        selectionHeight = 12.0,
        "grass",
        "dirt",
        "sand",
        "gravel",
    )

    @JvmField
    val BREEZE_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "breeze_flower",
        selectionWidth = 10.0,
        selectionHeight = 16.0,
        "grass",
        "dirt",
        "podzol",
    )

    @JvmField
    val BUTTERFLY_PEA: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "butterfly_pea",
        selectionWidth = 9.0,
        selectionHeight = 14.0,
        "grass",
        "dirt",
    )

    @JvmField
    val CLOUD_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "cloud_flower",
        selectionWidth = 10.0,
        selectionHeight = 16.0,
        "stone",
        "snow",
        "grass",
        "dirt",
        "gravel",
    )

    @JvmField
    val CRINUM: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "crinum",
        selectionWidth = 12.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
    )

    @JvmField
    val DANDELION: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "dandelion",
        selectionWidth = 10.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
    )

    @JvmField
    val DEEP_DANDELION: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "deep_dandelion",
        selectionWidth = 7.0,
        selectionHeight = 15.0,
        "ash",
        "soul_sand",
        "soul_soil",
    )

    @JvmField
    val DEPART_GRASS: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "depart_grass",
        selectionWidth = 10.0,
        selectionHeight = 14.0,
        "grass",
        "dirt",
        "podzol",
    )

    @JvmField
    val DESERT_FAN_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "desert_fan_flower",
        selectionWidth = 9.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
        "sand",
        "gravel",
    )

    @JvmField
    val FAN_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "fan_flower",
        selectionWidth = 9.0,
        selectionHeight = 16.0,
        "grass",
        "dirt",
        "clay",
        "moss_block",
    )

    @JvmField
    val FROZEN_MIRROR_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "frozen_mirror_flower",
        selectionWidth = 10.0,
        selectionHeight = 16.0,
        "grass",
        "dirt",
        "snow",
    )

    @JvmField
    val GOLD_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "gold_flower",
        selectionWidth = 10.0,
        selectionHeight = 14.0,
        "netherrack",
        "nether_wart_block",
        "crimson_nylium",
    )

    @JvmField
    val HAMMER_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "hammer_flower",
        selectionWidth = 10.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
        "podzol",
    )

    @JvmField
    val LAMP_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "lamp_flower",
        selectionWidth = 10.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
    )

    @JvmField
    val LEAVES_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "leaves_flower",
        selectionWidth = 12.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
        "clay",
        "moss_block",
    )

    @JvmField
    val LURK_BUD: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "lurk_bud",
        selectionWidth = 10.0,
        selectionHeight = 16.0,
        "lurk_block",
        "lurk_end_stone",
        "end_stone",
    )

    @JvmField
    val LURK_FRUIT: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "lurk_fruit",
        selectionWidth = 7.0,
        selectionHeight = 16.0,
        "lurk_block",
        "lurk_end_stone",
        "end_stone",
    )

    @JvmField
    val LURK_GRASS: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "lurk_grass",
        selectionWidth = 10.0,
        selectionHeight = 15.0,
        "end_stone",
        "lurk_end_stone",
    )

    @JvmField
    val LURK_MUSHROOM: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "lurk_mushroom",
        selectionWidth = 10.0,
        selectionHeight = 9.0,
        "end_stone",
        "lurk_end_stone",
    )

    @JvmField
    val LURK_ORCHID: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "lurk_orchid",
        selectionWidth = 7.0,
        selectionHeight = 15.0,
        "lurk_block",
        "lurk_end_stone",
        "end_stone",
    )

    @JvmField
    val LURK_SPRING: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "lurk_spring",
        selectionWidth = 12.0,
        selectionHeight = 15.0,
        "end_stone",
        "lurk_end_stone",
    )

    @JvmField
    val MAPLE_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "maple_flower",
        selectionWidth = 12.0,
        selectionHeight = 14.0,
        "grass",
        "dirt",
        "snow",
    )

    @JvmField
    val MOCK_STRAWBERRY: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "mock_strawberry",
        selectionWidth = 9.0,
        selectionHeight = 16.0,
        "grass",
        "dirt",
        "mud",
        "muddy_mangrove_roots",
    )

    @JvmField
    val MOUTH_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "mouth_flower",
        selectionWidth = 9.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
    )

    @JvmField
    val NETHER_DANDELION: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "nether_dandelion",
        selectionWidth = 10.0,
        selectionHeight = 15.0,
        "netherrack",
        "nether_wart_block",
        "crimson_nylium",
    )

    @JvmField
    val NETHER_STICK_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "nether_stick_flower",
        selectionWidth = 6.0,
        selectionHeight = 16.0,
        "netherrack",
        "nether_wart_block",
        "crimson_nylium",
    )

    @JvmField
    val PINK_DANDELION: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "pink_dandelion",
        selectionWidth = 10.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
    )

    @JvmField
    val PINK_FAN_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "pink_fan_flower",
        selectionWidth = 10.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
    )

    @JvmField
    val RED_SPIDER_LILY: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        "red_spider_lily",
        12.0,
        14.0,
        7,
        "netherrack",
        "crimson_nylium",
        "ash",
    )

    @JvmField
    val RUSSET_GRASS: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "russet_grass",
        selectionWidth = 9.0,
        selectionHeight = 16.0,
        "grass",
        "dirt",
        "sand",
        "gravel",
    )

    @JvmField
    val SNOW_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "snow_flower",
        selectionWidth = 10.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
        "snow",
    )

    @JvmField
    val SPARK_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "spark_flower",
        selectionWidth = 7.0,
        selectionHeight = 16.0,
        "grass",
        "dirt",
        "podzol",
    )

    @JvmField
    val SPELL_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "spell_flower",
        selectionWidth = 9.0,
        selectionHeight = 15.0,
        "grass",
        "dirt",
        "sand",
        "gravel",
    )

    @JvmField
    val STAR_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "star_flower",
        selectionWidth = 10.0,
        selectionHeight = 14.0,
        "stone",
        "snow",
        "grass",
        "dirt",
        "gravel",
    )

    @JvmField
    val STICK_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "stick_flower",
        selectionWidth = 7.0,
        selectionHeight = 16.0,
        "grass",
        "dirt",
    )

    @JvmField
    val YELLOW_BUTTERFLY_FLOWER: DeferredBlock<SimplePlantBlock> = registerSimplePlant(
        name = "yellow_butterfly_flower",
        selectionWidth = 10.0,
        selectionHeight = 13.0,
        "grass",
        "dirt",
        "sand",
        "gravel",
    )

    @JvmStatic
    fun load() {
    }
}
