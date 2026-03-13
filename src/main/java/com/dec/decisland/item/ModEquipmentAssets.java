package com.dec.decisland.item;

import com.dec.decisland.DecIsland;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public interface ModEquipmentAssets {
    ResourceKey<EquipmentAsset> FASHION = createId("fashion");
    ResourceKey<EquipmentAsset> AMETHYST = createId("amethyst");
    ResourceKey<EquipmentAsset> FROZEN = createId("frozen");

    static ResourceKey<EquipmentAsset> createId(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, name));
    }
}
