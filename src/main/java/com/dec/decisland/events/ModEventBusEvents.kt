package com.dec.decisland.events

import com.dec.decisland.DecIsland
import com.dec.decisland.client.model.EmptyModel
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
