package com.dec.decisland.item

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.FuelValues
import net.neoforged.neoforge.common.extensions.IItemExtension

class ModFuelItem(properties: Properties, private val burnTime: Int) : Item(properties), IItemExtension {
    override fun getBurnTime(itemStack: ItemStack, recipeType: RecipeType<*>?, fuelValues: FuelValues): Int = burnTime
}
