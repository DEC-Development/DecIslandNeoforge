package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * Fashion armor uses a per-item Bedrock texture instead of the material's
 * conventional `*_layer_1.png` armor texture.
 */
class FashionArmorItem(
    material: Holder<ArmorMaterial>,
    type: Type,
    properties: Item.Properties,
    private val textureName: String,
) : ArmorItem(material, type, properties) {
    override fun getArmorTexture(
        stack: ItemStack,
        entity: Entity,
        slot: EquipmentSlot,
        layer: ArmorMaterial.Layer,
        inner: Boolean,
    ): ResourceLocation = ResourceLocation.fromNamespaceAndPath(
        DecIsland.MOD_ID,
        "textures/armor/fashion/$textureName.png",
    )
}
