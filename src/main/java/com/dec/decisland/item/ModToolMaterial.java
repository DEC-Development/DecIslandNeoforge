package com.dec.decisland.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public record ModToolMaterial(
        TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue, TagKey<Item> repairItems
) {
    public static final ToolMaterial ABSOLUTE_ZERO = new ToolMaterial(null, 2098, 8.0F, 7.0F, 10, null); // ItemTags.ABSOLUTE_ZERO_REPAIR_ITEMS
    public static final ToolMaterial CANDY_CANE = new ToolMaterial(null, 85, 8.0F, 2.0F, 25, null);
    public static final ToolMaterial BAMBOO = new ToolMaterial(null, 157, 8.0F, 2.0F, 10, null); // 后面写竹子
    public static final ToolMaterial HARD_BAMBOO = new ToolMaterial(null, 273, 8.0F, 2.0F, 10, null); // 后面写竹子
    public static final ToolMaterial HARD_LOLLIPOP = new ToolMaterial(null, 256, 8.0F, 2.0F, 20, null); // 后面写糖
    public static final ToolMaterial CACTUS = new ToolMaterial(null, 23, 6.0F, 3.0F, 15, null); // 后面写仙人掌
    public static final ToolMaterial CORRUPTED = new ToolMaterial(null, 34, 8.0F, 3.0F, 20, null);
}
