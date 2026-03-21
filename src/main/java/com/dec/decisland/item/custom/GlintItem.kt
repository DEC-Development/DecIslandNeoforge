package com.dec.decisland.item.custom

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class GlintItem(properties: Properties) : Item(properties) {
    override fun isFoil(stack: ItemStack): Boolean = true
}
