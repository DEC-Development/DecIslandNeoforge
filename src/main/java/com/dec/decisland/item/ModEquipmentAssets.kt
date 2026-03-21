package com.dec.decisland.item

import com.dec.decisland.DecIsland
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.equipment.EquipmentAsset
import net.minecraft.world.item.equipment.EquipmentAssets

object ModEquipmentAssets {
    @JvmField
    val FASHION: ResourceKey<EquipmentAsset> = createId("fashion")

    @JvmField
    val AMETHYST: ResourceKey<EquipmentAsset> = createId("amethyst")

    @JvmField
    val CRYING: ResourceKey<EquipmentAsset> = createId("crying")

    @JvmField
    val DIRT: ResourceKey<EquipmentAsset> = createId("dirt")

    @JvmField
    val EMERALD: ResourceKey<EquipmentAsset> = createId("emerald")

    @JvmField
    val EVERLASTING_WINTER: ResourceKey<EquipmentAsset> = createId("everlasting_winter")

    @JvmField
    val FROZEN: ResourceKey<EquipmentAsset> = createId("frozen")

    @JvmField
    val LAVA: ResourceKey<EquipmentAsset> = createId("lava")

    @JvmField
    val RUPERT: ResourceKey<EquipmentAsset> = createId("rupert")

    @JvmField
    val STEEL: ResourceKey<EquipmentAsset> = createId("steel")

    @JvmField
    val STONE: ResourceKey<EquipmentAsset> = createId("stone")

    @JvmField
    val WOOD: ResourceKey<EquipmentAsset> = createId("wood")

    private fun createId(name: String): ResourceKey<EquipmentAsset> =
        ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, name))
}
