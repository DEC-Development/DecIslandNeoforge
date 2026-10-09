package com.dec.decisland.events

import com.dec.decisland.DecIsland
import com.dec.decisland.client.model.ClothesModel
import com.dec.decisland.client.model.EmptyModel
import com.dec.decisland.client.model.FashionArmorModel
import com.dec.decisland.entity.GeneratedMobs
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.entity.custom.ElfOfLeaves
import com.dec.decisland.entity.custom.LeavesGolem
import com.dec.decisland.entity.custom.ZombieWarrior
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.SpawnPlacementTypes
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.level.levelgen.Heightmap
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent

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
        event.registerLayerDefinition(FashionArmorModel.FOLLOWING_PARTICLE_LAYER_LOCATION) { FashionArmorModel.createFollowingParticleBodyLayer() }
    }

    @SubscribeEvent
    @JvmStatic
    fun registerAttributes(event: EntityAttributeCreationEvent) {
        event.put(ModEntities.ZOMBIE_WARRIOR.get(), ZombieWarrior.createWarriorAttributes().build())
        event.put(ModEntities.LEAVES_GOLEM.get(), LeavesGolem.createGolemAttributes().build())
        event.put(ModEntities.ELF_OF_LEAVES.get(), ElfOfLeaves.createElfAttributes().build())
        GeneratedMobs.registerAttributes(event)
    }

    @SubscribeEvent
    @JvmStatic
    fun registerSpawnPlacements(event: RegisterSpawnPlacementsEvent) {
        event.register(
            ModEntities.ZOMBIE_WARRIOR.get(),
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules,
            RegisterSpawnPlacementsEvent.Operation.REPLACE,
        )
        GeneratedMobs.registerSpawnPlacements(event)
    }

    @SubscribeEvent
    @JvmStatic
    fun modifyEntityAttributes(event: EntityAttributeModificationEvent) {
        event.add(EntityType.PLAYER, Attributes.MAX_HEALTH, 10.0)
    }
}
