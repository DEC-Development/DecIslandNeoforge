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
    val FROZEN: ResourceKey<EquipmentAsset> = createId("frozen")

    private fun createId(name: String): ResourceKey<EquipmentAsset> =
        ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, name))
}
