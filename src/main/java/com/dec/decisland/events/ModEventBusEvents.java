package com.dec.decisland.events;

import com.dec.decisland.DecIsland;
import com.dec.decisland.client.model.ClothesModel;
import com.dec.decisland.client.model.EmptyModel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = DecIsland.MOD_ID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EmptyModel.LAYER_LOCATION, EmptyModel::createBodyLayer);
//        event.registerLayerDefinition(ClothesModel.LAYER_LOCATION, ClothesModel::createBodyLayer);
    };

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
    // 若实体有属性则注册属性
//        event.put(ModEntities.BLIZZARD_ENERGY.get(), ModEntities.BLIZZARD_ENERGY.createAttributes().build);
    }
    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        // 为所有玩家实体添加 MAX_HEALTH 属性，并将默认值设为 10.0
        event.add(EntityType.PLAYER, Attributes.MAX_HEALTH, 10.0);
    }
}
