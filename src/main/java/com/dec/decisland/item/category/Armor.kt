package com.dec.decisland.item.category

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModArmorMaterials
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.lang.Lang
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.equipment.ArmorMaterial
import net.minecraft.world.item.equipment.ArmorType
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

object Armor {
    @JvmField
    val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_WEAPONS_TAB

    private fun registerArmorPiece(
        name: String,
        material: ArmorMaterial,
        type: ArmorType,
        repairItem: Supplier<Item>? = null,
        repairTag: TagKey<Item>? = null,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name, Lang.item.get(name))
                .props {
                    val properties = Item.Properties().humanoidArmor(material, type)
                    when {
                        repairItem != null -> properties.repairable(repairItem.get())
                        repairTag != null -> properties.repairable(repairTag)
                        else -> properties
                    }
                }
                .creativeTab(creativeTab)
                .build(),
        )

    @JvmField
    val AMETHYST_HELMET: DeferredItem<Item> =
        registerArmorPiece("amethyst_helmet", ModArmorMaterials.AMETHYST, ArmorType.HELMET)

    @JvmField
    val AMETHYST_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("amethyst_chestplate", ModArmorMaterials.AMETHYST, ArmorType.CHESTPLATE)

    @JvmField
    val AMETHYST_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("amethyst_leggings", ModArmorMaterials.AMETHYST, ArmorType.LEGGINGS)

    @JvmField
    val AMETHYST_BOOTS: DeferredItem<Item> =
        registerArmorPiece("amethyst_boots", ModArmorMaterials.AMETHYST, ArmorType.BOOTS)

    @JvmField
    val CRYING_HELMET: DeferredItem<Item> =
        registerArmorPiece("crying_helmet", ModArmorMaterials.CRYING, ArmorType.HELMET, repairItem = Supplier { Items.CRYING_OBSIDIAN })

    @JvmField
    val CRYING_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("crying_chestplate", ModArmorMaterials.CRYING, ArmorType.CHESTPLATE, repairItem = Supplier { Items.CRYING_OBSIDIAN })

    @JvmField
    val CRYING_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("crying_leggings", ModArmorMaterials.CRYING, ArmorType.LEGGINGS, repairItem = Supplier { Items.CRYING_OBSIDIAN })

    @JvmField
    val CRYING_BOOTS: DeferredItem<Item> =
        registerArmorPiece("crying_boots", ModArmorMaterials.CRYING, ArmorType.BOOTS, repairItem = Supplier { Items.CRYING_OBSIDIAN })

    @JvmField
    val DIRT_HELMET: DeferredItem<Item> =
        registerArmorPiece("dirt_helmet", ModArmorMaterials.DIRT, ArmorType.HELMET, repairItem = Supplier { Items.DIRT })

    @JvmField
    val DIRT_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("dirt_chestplate", ModArmorMaterials.DIRT, ArmorType.CHESTPLATE, repairItem = Supplier { Items.DIRT })

    @JvmField
    val DIRT_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("dirt_leggings", ModArmorMaterials.DIRT, ArmorType.LEGGINGS, repairItem = Supplier { Items.DIRT })

    @JvmField
    val DIRT_BOOTS: DeferredItem<Item> =
        registerArmorPiece("dirt_boots", ModArmorMaterials.DIRT, ArmorType.BOOTS, repairItem = Supplier { Items.DIRT })

    @JvmField
    val EMERALD_HELMET: DeferredItem<Item> =
        registerArmorPiece("emerald_helmet", ModArmorMaterials.EMERALD, ArmorType.HELMET, repairItem = Supplier { Items.EMERALD })

    @JvmField
    val EMERALD_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("emerald_chestplate", ModArmorMaterials.EMERALD, ArmorType.CHESTPLATE, repairItem = Supplier { Items.EMERALD })

    @JvmField
    val EMERALD_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("emerald_leggings", ModArmorMaterials.EMERALD, ArmorType.LEGGINGS, repairItem = Supplier { Items.EMERALD })

    @JvmField
    val EMERALD_BOOTS: DeferredItem<Item> =
        registerArmorPiece("emerald_boots", ModArmorMaterials.EMERALD, ArmorType.BOOTS, repairItem = Supplier { Items.EMERALD })

    @JvmField
    val EVERLASTING_WINTER_HELMET: DeferredItem<Item> =
        registerArmorPiece("everlasting_winter_helmet", ModArmorMaterials.EVERLASTING_WINTER, ArmorType.HELMET, repairItem = Supplier { Material.ICE_INGOT.get() })

    @JvmField
    val EVERLASTING_WINTER_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("everlasting_winter_chestplate", ModArmorMaterials.EVERLASTING_WINTER, ArmorType.CHESTPLATE, repairItem = Supplier { Material.ICE_INGOT.get() })

    @JvmField
    val EVERLASTING_WINTER_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("everlasting_winter_leggings", ModArmorMaterials.EVERLASTING_WINTER, ArmorType.LEGGINGS, repairItem = Supplier { Material.ICE_INGOT.get() })

    @JvmField
    val EVERLASTING_WINTER_BOOTS: DeferredItem<Item> =
        registerArmorPiece("everlasting_winter_boots", ModArmorMaterials.EVERLASTING_WINTER, ArmorType.BOOTS, repairItem = Supplier { Material.ICE_INGOT.get() })

    @JvmField
    val FROZEN_HELMET: DeferredItem<Item> =
        registerArmorPiece("frozen_helmet", ModArmorMaterials.FROZEN, ArmorType.HELMET)

    @JvmField
    val FROZEN_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("frozen_chestplate", ModArmorMaterials.FROZEN, ArmorType.CHESTPLATE)

    @JvmField
    val FROZEN_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("frozen_leggings", ModArmorMaterials.FROZEN, ArmorType.LEGGINGS)

    @JvmField
    val FROZEN_BOOTS: DeferredItem<Item> =
        registerArmorPiece("frozen_boots", ModArmorMaterials.FROZEN, ArmorType.BOOTS)

    @JvmField
    val LAVA_HELMET: DeferredItem<Item> =
        registerArmorPiece("lava_helmet", ModArmorMaterials.LAVA, ArmorType.HELMET, repairItem = Supplier { Material.LAVA_INGOT.get() })

    @JvmField
    val LAVA_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("lava_chestplate", ModArmorMaterials.LAVA, ArmorType.CHESTPLATE, repairItem = Supplier { Material.LAVA_INGOT.get() })

    @JvmField
    val LAVA_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("lava_leggings", ModArmorMaterials.LAVA, ArmorType.LEGGINGS, repairItem = Supplier { Material.LAVA_INGOT.get() })

    @JvmField
    val LAVA_BOOTS: DeferredItem<Item> =
        registerArmorPiece("lava_boots", ModArmorMaterials.LAVA, ArmorType.BOOTS, repairItem = Supplier { Material.LAVA_INGOT.get() })

    @JvmField
    val RUPERT_HELMET: DeferredItem<Item> =
        registerArmorPiece("rupert_helmet", ModArmorMaterials.RUPERT, ArmorType.HELMET, repairItem = Supplier { Items.GHAST_TEAR })

    @JvmField
    val RUPERT_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("rupert_chestplate", ModArmorMaterials.RUPERT, ArmorType.CHESTPLATE, repairItem = Supplier { Items.GHAST_TEAR })

    @JvmField
    val RUPERT_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("rupert_leggings", ModArmorMaterials.RUPERT, ArmorType.LEGGINGS, repairItem = Supplier { Items.GHAST_TEAR })

    @JvmField
    val RUPERT_BOOTS: DeferredItem<Item> =
        registerArmorPiece("rupert_boots", ModArmorMaterials.RUPERT, ArmorType.BOOTS, repairItem = Supplier { Items.GHAST_TEAR })

    @JvmField
    val STEEL_HELMET: DeferredItem<Item> =
        registerArmorPiece("steel_helmet", ModArmorMaterials.STEEL, ArmorType.HELMET, repairItem = Supplier { Material.STEEL_INGOT.get() })

    @JvmField
    val STEEL_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("steel_chestplate", ModArmorMaterials.STEEL, ArmorType.CHESTPLATE, repairItem = Supplier { Material.STEEL_INGOT.get() })

    @JvmField
    val STEEL_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("steel_leggings", ModArmorMaterials.STEEL, ArmorType.LEGGINGS, repairItem = Supplier { Material.STEEL_INGOT.get() })

    @JvmField
    val STEEL_BOOTS: DeferredItem<Item> =
        registerArmorPiece("steel_boots", ModArmorMaterials.STEEL, ArmorType.BOOTS, repairItem = Supplier { Material.STEEL_INGOT.get() })

    @JvmField
    val STONE_HELMET: DeferredItem<Item> =
        registerArmorPiece("stone_helmet", ModArmorMaterials.STONE, ArmorType.HELMET, repairItem = Supplier { Items.COBBLESTONE })

    @JvmField
    val STONE_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("stone_chestplate", ModArmorMaterials.STONE, ArmorType.CHESTPLATE, repairItem = Supplier { Items.COBBLESTONE })

    @JvmField
    val STONE_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("stone_leggings", ModArmorMaterials.STONE, ArmorType.LEGGINGS, repairItem = Supplier { Items.COBBLESTONE })

    @JvmField
    val STONE_BOOTS: DeferredItem<Item> =
        registerArmorPiece("stone_boots", ModArmorMaterials.STONE, ArmorType.BOOTS, repairItem = Supplier { Items.COBBLESTONE })

    @JvmField
    val WOOD_HELMET: DeferredItem<Item> =
        registerArmorPiece("wood_helmet", ModArmorMaterials.WOOD, ArmorType.HELMET, repairTag = ItemTags.PLANKS)

    @JvmField
    val WOOD_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("wood_chestplate", ModArmorMaterials.WOOD, ArmorType.CHESTPLATE, repairTag = ItemTags.PLANKS)

    @JvmField
    val WOOD_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("wood_leggings", ModArmorMaterials.WOOD, ArmorType.LEGGINGS, repairTag = ItemTags.PLANKS)

    @JvmField
    val WOOD_BOOTS: DeferredItem<Item> =
        registerArmorPiece("wood_boots", ModArmorMaterials.WOOD, ArmorType.BOOTS, repairTag = ItemTags.PLANKS)

    @JvmStatic
    fun load() {
    }
}
