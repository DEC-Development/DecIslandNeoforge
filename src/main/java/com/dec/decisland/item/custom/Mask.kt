package com.dec.decisland.item.custom

import com.dec.decisland.client.renderer.MaskRenderer
import com.dec.decisland.item.ModArmorMaterials
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.client.GeoRenderProvider
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animatable.manager.AnimatableManager
import software.bernie.geckolib.renderer.GeoArmorRenderer
import software.bernie.geckolib.renderer.base.GeoRenderState
import software.bernie.geckolib.util.GeckoLibUtil
import java.util.function.Consumer

class Mask(properties: Properties) : Item(properties.humanoidArmor(ModArmorMaterials.FASHION, ArmorType.HELMET)), GeoItem {
    private val geoCache: AnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this)

    private abstract class MaskRenderState : HumanoidRenderState(), GeoRenderState

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = geoCache

    override fun createGeoRenderer(consumer: Consumer<GeoRenderProvider>) {
        consumer.accept(
            object : GeoRenderProvider {
                private var renderer: GeoArmorRenderer<Mask, *>? = null

                override fun getGeoArmorRenderer(itemStack: ItemStack, equipmentSlot: EquipmentSlot): GeoArmorRenderer<*, *> {
                    if (renderer == null) {
                        renderer = MaskRenderer<MaskRenderState>(this@Mask)
                    }

                    return renderer!!
                }
            },
        )
    }
}
