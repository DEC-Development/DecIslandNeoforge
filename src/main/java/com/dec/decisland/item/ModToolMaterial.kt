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
}
