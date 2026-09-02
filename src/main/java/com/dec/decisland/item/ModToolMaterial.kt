package com.dec.decisland.item

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.level.block.Block

object ModToolMaterial {
    private val EMPTY_BLOCK_TAG: TagKey<Block> =
        TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("decisland", "unused_tool_blocks"))

    private val EMPTY_ITEM_TAG: TagKey<Item> =
        TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("decisland", "unused_tool_repairs"))

    @JvmField
    val ABSOLUTE_ZERO: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 2098, 8.0f, 7.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val CANDY_CANE: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 85, 8.0f, 2.0f, 25, EMPTY_ITEM_TAG)

    @JvmField
    val BAMBOO: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 157, 8.0f, 2.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val BLOOD_MARE: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 376, 8.0f, 4.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val HARD_BAMBOO: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 273, 8.0f, 2.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val HARD_LOLLIPOP: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 256, 8.0f, 2.0f, 20, EMPTY_ITEM_TAG)

    @JvmField
    val GINGERBREAD_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 123, 8.0f, 2.0f, 30, EMPTY_ITEM_TAG)

    @JvmField
    val LOLLIPOP: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 64, 8.0f, 2.0f, 5, EMPTY_ITEM_TAG)

    @JvmField
    val LONG_BREAD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 64, 8.0f, 2.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val CACTUS: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 23, 6.0f, 3.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val CORRUPTED: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 34, 8.0f, 3.0f, 20, EMPTY_ITEM_TAG)

    @JvmField
    val ILLAGER_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 513, 8.0f, 3.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val NIGHT_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 4320, 8.0f, 6.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val THE_BLADE: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 312, 8.0f, 3.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val AMETHYST_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 66, 8.0f, 3.0f, 20, EMPTY_ITEM_TAG)

    @JvmField
    val BONE_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 128, 8.0f, 3.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val CORAL_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 256, 8.0f, 4.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val EMERALD_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 326, 8.0f, 4.0f, 20, EMPTY_ITEM_TAG)

    @JvmField
    val LAVA_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 1024, 8.0f, 7.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val STEEL_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 851, 8.0f, 5.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val TURTLE_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 712, 8.0f, 6.0f, 2, EMPTY_ITEM_TAG)

    @JvmField
    val SCIMITAR: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 231, 8.0f, 4.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val CUDGEL: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 85, 8.0f, 3.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val FANG_MACE: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 94, 8.0f, 5.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val SHARP_CORAL: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 64, 8.0f, 2.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val BLIZZARD_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 965, 8.0f, 9.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val ICE_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 436, 8.0f, 7.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val LAPIS_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 125, 8.0f, 3.0f, 30, EMPTY_ITEM_TAG)

    @JvmField
    val BLOOD_SICKLE: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 302, 8.0f, 4.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val COPPER_SICKLE: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 251, 8.0f, 4.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val EVERLASTING_WINTER_SICKLE: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 1645, 8.0f, 7.0f, 5, EMPTY_ITEM_TAG)

    @JvmField
    val GHOST_SICKLE: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 2048, 8.0f, 11.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val STEEL_SICKLE: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 723, 8.0f, 5.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val DEAD_WOOD_DAGGER: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 281, 8.0f, 0.0f, 4, EMPTY_ITEM_TAG)

    @JvmField
    val LEAVES_DAGGER: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 341, 8.0f, 0.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val GHOST_DAGGER: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 2048, 8.0f, 0.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val WIND_DAGGER: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 128, 8.0f, 0.0f, 20, EMPTY_ITEM_TAG)

    @JvmField
    val EVERLASTING_WINTER_DAGGER: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 1645, 8.0f, 0.0f, 5, EMPTY_ITEM_TAG)

    @JvmField
    val VOID_WHISPERING_DAGGER: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 562, 8.0f, 0.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val STORM_BATTLEAXE: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 487, 8.0f, 2.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val DEAD_WOOD_RAPIER: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 322, 8.0f, 0.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val THUNDER_RAPIER: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 316, 8.0f, 0.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val DECREPIT_ATLANTIS: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 100, 8.0f, 5.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val SWORD_OF_GUARD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 231, 8.0f, 2.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val SWORD_OF_HALLOWEEN: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 513, 8.0f, 1.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val GHOST_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 4095, 8.0f, 5.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val DUST_DESTROYER: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 2045, 8.0f, 4.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val GROWTH_SWORD: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 502, 8.0f, 2.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val AMETHYST_TOOL: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, AMETHYST_SWORD.durability(), 7.0f, 0.0f, 20, EMPTY_ITEM_TAG)

    @JvmField
    val CORAL_TOOL: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, CORAL_SWORD.durability(), 5.0f, 0.0f, 16, EMPTY_ITEM_TAG)

    @JvmField
    val EMERALD_TOOL: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, EMERALD_SWORD.durability(), 7.0f, 0.0f, 21, EMPTY_ITEM_TAG)

    @JvmField
    val GLASS_TOOL: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 64, 10.0f, 0.0f, 2, EMPTY_ITEM_TAG)

    @JvmField
    val SLIME_TOOL: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 537, 3.0f, 0.0f, 50, EMPTY_ITEM_TAG)
}
