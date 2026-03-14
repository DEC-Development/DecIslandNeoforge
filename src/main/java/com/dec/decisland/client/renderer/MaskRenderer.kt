package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.item.custom.Mask
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.resources.Identifier
import software.bernie.geckolib.model.GeoModel
import software.bernie.geckolib.renderer.GeoArmorRenderer
import software.bernie.geckolib.renderer.base.GeoRenderState

class MaskRenderer<R>(mask: Mask) : GeoArmorRenderer<Mask, R>(ClothesGeoModel(mask))
where R : HumanoidRenderState, R : GeoRenderState {
    class ClothesGeoModel(private val maskItem: Mask) : GeoModel<Mask>() {
        override fun getModelResource(geoRenderState: GeoRenderState): Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "armor/clothes")

        override fun getTextureResource(geoRenderState: GeoRenderState): Identifier {
            val itemName = maskItem.descriptionId.replace("item.${DecIsland.MOD_ID}.", "")
            return Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/armor/mask/$itemName.png")
        }

        override fun getAnimationResource(animatable: Mask): Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "animations/item/clothes.animation.json")
    }
}
