package com.dec.decisland.item

import com.dec.decisland.tag.ModItemTags
import com.google.common.collect.Maps
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAsset

object ModArmorMaterials {
    private val EMPTY_REPAIR_TAG: TagKey<Item> =
        TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("decisland", "unused_repair"))

    private fun createArmorMaterial(
        durability: Int,
        boots: Int,
        leggings: Int,
        chestplate: Int,
        helmet: Int,
        enchantability: Int,
        equipSound: Holder<SoundEvent>,
        toughness: Float,
        knockbackResistance: Float,
        repairTag: TagKey<Item>,
        asset: ResourceKey<EquipmentAsset>,
    ): ArmorMaterial = ArmorMaterial(
        durability,
        makeDefense(boots, leggings, chestplate, helmet, chestplate),
        enchantability,
        equipSound,
        toughness,
        knockbackResistance,
        repairTag,
        asset,
    )

    @JvmField
    val FASHION: ArmorMaterial = createArmorMaterial(
        durability = 4,
        boots = 1,
        leggings = 1,
        chestplate = 1,
        helmet = 1,
        enchantability = 10,
        equipSound = SoundEvents.ARMOR_EQUIP_LEATHER,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = ModItemTags.REPAIRS_FASHION,
        asset = ModEquipmentAssets.FASHION,
    )

    @JvmField
    val AMETHYST: ArmorMaterial = createArmorMaterial(
        durability = 12,
        boots = 2,
        leggings = 2,
        chestplate = 5,
        helmet = 2,
        enchantability = 30,
        equipSound = SoundEvents.ARMOR_EQUIP_GOLD,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = ModItemTags.REPAIRS_AMETHYST_ARMOR,
        asset = ModEquipmentAssets.AMETHYST,
    )

    @JvmField
    val CRYING: ArmorMaterial = createArmorMaterial(
        durability = 58,
        boots = 4,
        leggings = 7,
        chestplate = 9,
        helmet = 4,
        enchantability = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_IRON,
        toughness = 1.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.CRYING,
    )

    @JvmField
    val DIRT: ArmorMaterial = createArmorMaterial(
        durability = 3,
        boots = 1,
        leggings = 1,
        chestplate = 2,
        helmet = 1,
        enchantability = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_LEATHER,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.DIRT,
    )

    @JvmField
    val EMERALD: ArmorMaterial = createArmorMaterial(
        durability = 14,
        boots = 2,
        leggings = 4,
        chestplate = 5,
        helmet = 2,
        enchantability = 30,
        equipSound = SoundEvents.ARMOR_EQUIP_DIAMOND,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.EMERALD,
    )

    @JvmField
    val EVERLASTING_WINTER: ArmorMaterial = createArmorMaterial(
        durability = 128,
        boots = 3,
        leggings = 6,
        chestplate = 8,
        helmet = 4,
        enchantability = 10,
        equipSound = SoundEvents.ARMOR_EQUIP_GOLD,
        toughness = 2.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.EVERLASTING_WINTER,
    )

    @JvmField
    val FROZEN: ArmorMaterial = createArmorMaterial(
        durability = 14,
        boots = 2,
        leggings = 4,
        chestplate = 6,
        helmet = 2,
        enchantability = 20,
        equipSound = SoundEvents.ARMOR_EQUIP_GOLD,
        toughness = 0.1f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.FROZEN,
    )

    @JvmField
    val LAVA: ArmorMaterial = createArmorMaterial(
        durability = 24,
        boots = 2,
        leggings = 5,
        chestplate = 6,
        helmet = 3,
        enchantability = 7,
        equipSound = SoundEvents.ARMOR_EQUIP_IRON,
        toughness = 0.5f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.LAVA,
    )

    @JvmField
    val RUPERT: ArmorMaterial = createArmorMaterial(
        durability = 24,
        boots = 1,
        leggings = 3,
        chestplate = 4,
        helmet = 2,
        enchantability = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_LEATHER,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.RUPERT,
    )

    @JvmField
    val STEEL: ArmorMaterial = createArmorMaterial(
        durability = 26,
        boots = 3,
        leggings = 5,
        chestplate = 7,
        helmet = 3,
        enchantability = 10,
        equipSound = SoundEvents.ARMOR_EQUIP_IRON,
        toughness = 1.0f,
        knockbackResistance = 0.1f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.STEEL,
    )

    @JvmField
    val STONE: ArmorMaterial = createArmorMaterial(
        durability = 4,
        boots = 1,
        leggings = 2,
        chestplate = 3,
        helmet = 1,
        enchantability = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_IRON,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.STONE,
    )

    @JvmField
    val WOOD: ArmorMaterial = createArmorMaterial(
        durability = 12,
        boots = 1,
        leggings = 2,
        chestplate = 3,
        helmet = 1,
        enchantability = 20,
        equipSound = SoundEvents.ARMOR_EQUIP_LEATHER,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.WOOD,
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
