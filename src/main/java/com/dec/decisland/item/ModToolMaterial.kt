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
    val HARD_BAMBOO: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 273, 8.0f, 2.0f, 10, EMPTY_ITEM_TAG)

    @JvmField
    val HARD_LOLLIPOP: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 256, 8.0f, 2.0f, 20, EMPTY_ITEM_TAG)

    @JvmField
    val CACTUS: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 23, 6.0f, 3.0f, 15, EMPTY_ITEM_TAG)

    @JvmField
    val CORRUPTED: ToolMaterial = ToolMaterial(EMPTY_BLOCK_TAG, 34, 8.0f, 3.0f, 20, EMPTY_ITEM_TAG)
}
