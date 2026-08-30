package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.client.model.FashionArmorModel
import com.dec.decisland.item.category.Fashion
import net.minecraft.client.Minecraft
import net.minecraft.client.model.Model
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.client.resources.model.EquipmentClientInfo
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

object FashionArmorClientItemExtensions : IClientItemExtensions {
    private val cachedModels = mutableMapOf<Fashion.ModelKind, FashionArmorModel<HumanoidRenderState>>()

    @JvmStatic
    fun textureFor(stack: ItemStack): Identifier = Identifier.fromNamespaceAndPath(
        DecIsland.MOD_ID,
        "textures/armor/fashion/${stack.itemDescriptionIdPath()}.png",
    )

    override fun getHumanoidArmorModel(
        itemStack: ItemStack,
        layerType: EquipmentClientInfo.LayerType,
        original: Model<*>,
    ): Model<*> {
        val definition = Fashion.definitionOf(itemStack) ?: return original
        if (definition.modelKind == Fashion.ModelKind.VANILLA) {
            return original
        }

        val layerLocation = when (definition.modelKind) {
            Fashion.ModelKind.CLOTHES -> FashionArmorModel.CLOTHES_LAYER_LOCATION
            Fashion.ModelKind.CLOTHES_WITH_HOOD -> FashionArmorModel.CLOTHES_WITH_HOOD_LAYER_LOCATION
            Fashion.ModelKind.HAT -> FashionArmorModel.HAT_LAYER_LOCATION
            Fashion.ModelKind.WITCH_HAT -> FashionArmorModel.WITCH_HAT_LAYER_LOCATION
            Fashion.ModelKind.CHRISTMAS_CAP -> FashionArmorModel.CHRISTMAS_CAP_LAYER_LOCATION
            Fashion.ModelKind.WINGS_FROM_DEEP -> FashionArmorModel.WINGS_FROM_DEEP_LAYER_LOCATION
            Fashion.ModelKind.GIANT_BAT_WINGS -> FashionArmorModel.GIANT_BAT_WINGS_LAYER_LOCATION
            Fashion.ModelKind.VANILLA -> return original
        }

        if (definition.modelKind == Fashion.ModelKind.GIANT_BAT_WINGS) {
            val baked = Minecraft.getInstance().entityModels.bakeLayer(layerLocation)
            FashionArmorModel.attachTextureMeshes(baked, definition.modelKind)
            return FashionArmorModel<HumanoidRenderState>(baked)
        }

        return cachedModels.getOrPut(definition.modelKind) {
            val baked = Minecraft.getInstance().entityModels.bakeLayer(layerLocation)
            FashionArmorModel.attachTextureMeshes(baked, definition.modelKind)
            FashionArmorModel<HumanoidRenderState>(baked)
        }
    }

    override fun getArmorTexture(
        stack: ItemStack,
        type: EquipmentClientInfo.LayerType,
        layer: EquipmentClientInfo.Layer,
        _default: Identifier,
    ): Identifier = textureFor(stack)

    override fun getArmorLayerTintColor(
        stack: ItemStack,
        layer: EquipmentClientInfo.Layer,
        layerIdx: Int,
        fallbackColor: Int,
    ): Int = EquipmentLayerRenderer.getColorForLayer(layer, fallbackColor)

    private fun ItemStack.itemDescriptionIdPath(): String =
        net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item).path
}
