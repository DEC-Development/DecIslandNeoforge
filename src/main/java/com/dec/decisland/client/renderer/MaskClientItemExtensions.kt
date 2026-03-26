package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.client.model.ClothesModel
import net.minecraft.client.Minecraft
import net.minecraft.client.model.Model
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer
import net.minecraft.client.resources.model.EquipmentClientInfo
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

object MaskClientItemExtensions : IClientItemExtensions {
    private var cachedModel: ClothesModel<net.minecraft.client.renderer.entity.state.HumanoidRenderState>? = null

    override fun getHumanoidArmorModel(
        itemStack: ItemStack,
        layerType: EquipmentClientInfo.LayerType,
        original: Model<*>,
    ): Model<*> {
        if (layerType != EquipmentClientInfo.LayerType.HUMANOID) {
            return original
        }

        val model = cachedModel
        if (model != null) {
            return model
        }

        val baked = Minecraft.getInstance().entityModels.bakeLayer(ClothesModel.LAYER_LOCATION)
        return ClothesModel<net.minecraft.client.renderer.entity.state.HumanoidRenderState>(baked).also {
            cachedModel = it
        }
    }

    override fun getArmorTexture(
        stack: ItemStack,
        type: EquipmentClientInfo.LayerType,
        layer: EquipmentClientInfo.Layer,
        _default: Identifier,
    ): Identifier = Identifier.fromNamespaceAndPath(
        DecIsland.MOD_ID,
        "textures/armor/mask/${BuiltInRegistries.ITEM.getKey(stack.item).path}.png",
    )

    override fun getArmorLayerTintColor(
        stack: ItemStack,
        layer: EquipmentClientInfo.Layer,
        layerIdx: Int,
        fallbackColor: Int,
    ): Int = EquipmentLayerRenderer.getColorForLayer(layer, fallbackColor)
}
