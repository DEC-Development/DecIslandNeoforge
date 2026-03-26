package com.dec.decisland.item.custom

import com.dec.decisland.item.ModArmorMaterials
import net.minecraft.world.item.Item
import net.minecraft.world.item.equipment.ArmorType

class Mask(properties: Properties) : Item(properties.humanoidArmor(ModArmorMaterials.FASHION, ArmorType.HELMET))
