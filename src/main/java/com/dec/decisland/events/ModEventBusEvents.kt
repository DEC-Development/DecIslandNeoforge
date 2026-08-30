package com.dec.decisland.events

import com.dec.decisland.DecIsland
import com.dec.decisland.client.model.ClothesModel
import com.dec.decisland.client.model.EmptyModel
import com.dec.decisland.client.model.FashionArmorModel
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent

@EventBusSubscriber(modid = DecIsland.MOD_ID)
object ModEventBusEvents {
    @SubscribeEvent
    @JvmStatic
    fun registerLayers(event: EntityRenderersEvent.RegisterLayerDefinitions) {
        event.registerLayerDefinition(EmptyModel.Companion.LAYER_LOCATION) { EmptyModel.createBodyLayer() }
        event.registerLayerDefinition(ClothesModel.Companion.LAYER_LOCATION) { ClothesModel.createBodyLayer() }
        event.registerLayerDefinition(FashionArmorModel.CLOTHES_LAYER_LOCATION) { FashionArmorModel.createClothesBodyLayer() }
        event.registerLayerDefinition(FashionArmorModel.CLOTHES_WITH_HOOD_LAYER_LOCATION) { FashionArmorModel.createClothesWithHoodBodyLayer() }
        event.registerLayerDefinition(FashionArmorModel.HAT_LAYER_LOCATION) { FashionArmorModel.createHatBodyLayer() }
        event.registerLayerDefinition(FashionArmorModel.WITCH_HAT_LAYER_LOCATION) { FashionArmorModel.createWitchHatBodyLayer() }
        event.registerLayerDefinition(FashionArmorModel.CHRISTMAS_CAP_LAYER_LOCATION) { FashionArmorModel.createChristmasCapBodyLayer() }
        event.registerLayerDefinition(FashionArmorModel.WINGS_FROM_DEEP_LAYER_LOCATION) { FashionArmorModel.createWingsFromDeepBodyLayer() }
        event.registerLayerDefinition(FashionArmorModel.GIANT_BAT_WINGS_LAYER_LOCATION) { FashionArmorModel.createGiantBatWingsBodyLayer() }
    }

    @SubscribeEvent
    @JvmStatic
    fun registerAttributes(event: EntityAttributeCreationEvent) {
    }

    @SubscribeEvent
    @JvmStatic
    fun modifyEntityAttributes(event: EntityAttributeModificationEvent) {
        event.add(EntityType.PLAYER, Attributes.MAX_HEALTH, 10.0)
    }
}
