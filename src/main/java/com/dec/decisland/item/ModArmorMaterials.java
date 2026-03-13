package com.dec.decisland.item;

import com.dec.decisland.tag.ModItemTags;
import com.google.common.collect.Maps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.Map;

public interface ModArmorMaterials {
    ArmorMaterial FASHION = new ArmorMaterial(
            4, makeDefense(1, 1, 1, 1, 0), 10,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, ModItemTags.REPAIRS_FASHION, ModEquipmentAssets.FASHION
    );
    ArmorMaterial AMETHYST = new ArmorMaterial(
            12, makeDefense(2, 2, 5, 2, 5), 30,
            SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, ModItemTags.REPAIRS_AMETHYST_ARMOR, ModEquipmentAssets.AMETHYST
    );
    ArmorMaterial FROZEN = new ArmorMaterial(
            14, makeDefense(2, 4, 6, 2, 5), 20,
            SoundEvents.ARMOR_EQUIP_GOLD, 0.1F, 0.0F, null, ModEquipmentAssets.FROZEN
    );

    private static Map<ArmorType, Integer> makeDefense(int boots, int leggings, int chestplate, int helmet, int body) {
        return Maps.newEnumMap(
                Map.of(
                        ArmorType.BOOTS,
                        boots,
                        ArmorType.LEGGINGS,
                        leggings,
                        ArmorType.CHESTPLATE,
                        chestplate,
                        ArmorType.HELMET,
                        helmet,
                        ArmorType.BODY,
                        body
                )
        );
    }
}
