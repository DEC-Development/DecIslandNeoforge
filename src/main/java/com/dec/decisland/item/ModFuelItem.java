package com.dec.decisland.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import net.neoforged.neoforge.common.extensions.IItemExtension;

import javax.annotation.Nullable;

public class ModFuelItem extends Item implements IItemExtension {
    private int burnTime = 0;

    public ModFuelItem(Properties properties, int burnTime) {
        super(properties);
        this.burnTime = burnTime;

    }
    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
        // 直接返回构造函数中指定的燃料时间，忽略 recipeType 和 fuelValues
        return this.burnTime;
    }
}
