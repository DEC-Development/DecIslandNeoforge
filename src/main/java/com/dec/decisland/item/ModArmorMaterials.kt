package com.dec.decisland.item

import com.dec.decisland.tag.ModItemTags
import com.google.common.collect.Maps
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType

object ModArmorMaterials {
    private val EMPTY_REPAIR_TAG: TagKey<Item> =
        TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("decisland", "unused_repair"))

    @JvmField
    val FASHION: ArmorMaterial = ArmorMaterial(
        4,
        makeDefense(1, 1, 1, 1, 0),
        10,
        SoundEvents.ARMOR_EQUIP_LEATHER,
        0.0f,
        0.0f,
        ModItemTags.REPAIRS_FASHION,
        ModEquipmentAssets.FASHION,
    )

    @JvmField
    val AMETHYST: ArmorMaterial = ArmorMaterial(
        12,
        makeDefense(2, 2, 5, 2, 5),
        30,
        SoundEvents.ARMOR_EQUIP_GOLD,
        0.0f,
        0.0f,
        ModItemTags.REPAIRS_AMETHYST_ARMOR,
        ModEquipmentAssets.AMETHYST,
    )

    @JvmField
    val FROZEN: ArmorMaterial = ArmorMaterial(
        14,
        makeDefense(2, 4, 6, 2, 5),
        20,
        SoundEvents.ARMOR_EQUIP_GOLD,
        0.1f,
        0.0f,
        EMPTY_REPAIR_TAG,
        ModEquipmentAssets.FROZEN,
    )

    private fun makeDefense(
        boots: Int,
        leggings: Int,
        chestplate: Int,
        helmet: Int,
        body: Int,
    ): Map<ArmorType, Int> = Maps.newEnumMap(
        mapOf(
            ArmorType.BOOTS to boots,
            ArmorType.LEGGINGS to leggings,
            ArmorType.CHESTPLATE to chestplate,
            ArmorType.HELMET to helmet,
            ArmorType.BODY to body,
        ),
    )
}
