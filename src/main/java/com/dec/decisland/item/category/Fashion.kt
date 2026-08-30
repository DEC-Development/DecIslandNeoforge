package com.dec.decisland.item.category

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModArmorMaterials
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.equipment.ArmorType
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

object Fashion {
    enum class ModelKind {
        VANILLA,
        CLOTHES,
        CLOTHES_WITH_HOOD,
        HAT,
        WITCH_HAT,
        CHRISTMAS_CAP,
        WINGS_FROM_DEEP,
        GIANT_BAT_WINGS,
    }

    data class Definition(
        val name: String,
        val armorType: ArmorType,
        val modelKind: ModelKind,
    )

    @JvmField
    val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_ACCESSORIES_TAB

    private val definitionsByName = linkedMapOf<String, Definition>()
    private val registeredItems = mutableListOf<DeferredItem<Item>>()

    private fun register(definition: Definition): DeferredItem<Item> {
        definitionsByName[definition.name] = definition
        return ModItems.registerItem(
            ItemConfig.Builder(definition.name)
                .props { Item.Properties().humanoidArmor(ModArmorMaterials.FASHION, definition.armorType) }
                .creativeTab(creativeTab)
                .build(),
        ).also(registeredItems::add)
    }

    private fun helmet(name: String, modelKind: ModelKind): DeferredItem<Item> =
        register(Definition(name, ArmorType.HELMET, modelKind))

    private fun chest(name: String, modelKind: ModelKind): DeferredItem<Item> =
        register(Definition(name, ArmorType.CHESTPLATE, modelKind))

    private fun legs(name: String, modelKind: ModelKind): DeferredItem<Item> =
        register(Definition(name, ArmorType.LEGGINGS, modelKind))

    private fun boots(name: String, modelKind: ModelKind): DeferredItem<Item> =
        register(Definition(name, ArmorType.BOOTS, modelKind))

    @JvmStatic
    fun allItems(): Array<Item> = registeredItems.map { it.get() }.toTypedArray()

    @JvmStatic
    fun definitionOf(stack: ItemStack): Definition? = definitionOf(stack.item)

    @JvmStatic
    fun definitionOf(item: Item): Definition? = definitionByName(BuiltInRegistries.ITEM.getKey(item).path)

    @JvmStatic
    fun definitionByName(name: String): Definition? = definitionsByName[name]

    @JvmField
    val ARCHER_HAT: DeferredItem<Item> = helmet("archer_hat", ModelKind.HAT)

    @JvmField
    val BLACK_COURTESY: DeferredItem<Item> = helmet("black_courtesy", ModelKind.HAT)

    @JvmField
    val BLUE_CAT_EARS: DeferredItem<Item> = helmet("blue_cat_ears", ModelKind.CLOTHES)

    @JvmField
    val BLUE_GEM_HAT: DeferredItem<Item> = helmet("blue_gem_hat", ModelKind.HAT)

    @JvmField
    val CHRISTMAS_CAP: DeferredItem<Item> = helmet("christmas_cap", ModelKind.CHRISTMAS_CAP)

    @JvmField
    val FARMER_HAT: DeferredItem<Item> = helmet("farmer_hat", ModelKind.HAT)

    @JvmField
    val GLASS_TANK: DeferredItem<Item> = helmet("glass_tank", ModelKind.VANILLA)

    @JvmField
    val KNIGHT_HELMET: DeferredItem<Item> = helmet("knight_helmet", ModelKind.VANILLA)

    @JvmField
    val RED_GEM_HAT: DeferredItem<Item> = helmet("red_gem_hat", ModelKind.HAT)

    @JvmField
    val STRAY_HELMET: DeferredItem<Item> = helmet("stray_helmet", ModelKind.CLOTHES)

    @JvmField
    val WITCH_HAT: DeferredItem<Item> = helmet("witch_hat", ModelKind.WITCH_HAT)

    @JvmField
    val DARK_BLUE_JACKET: DeferredItem<Item> = chest("dark_blue_jacket", ModelKind.CLOTHES_WITH_HOOD)

    @JvmField
    val ES_CLOTHES: DeferredItem<Item> = chest("es_clothes", ModelKind.CLOTHES)

    @JvmField
    val FORMAL_STYLE_CLOTHES: DeferredItem<Item> = chest("formal_style_clothes", ModelKind.CLOTHES)

    @JvmField
    val GLAMOR_CLOTHES: DeferredItem<Item> = chest("glamor_clothes", ModelKind.CLOTHES)

    @JvmField
    val HANYI_CLOTHES: DeferredItem<Item> = chest("hanyi_clothes", ModelKind.CLOTHES)

    @JvmField
    val JACKET: DeferredItem<Item> = chest("jacket", ModelKind.CLOTHES)

    @JvmField
    val JIUZHI_JACKET: DeferredItem<Item> = chest("jiuzhi_jacket", ModelKind.CLOTHES)

    @JvmField
    val LESTER_CLOTHES: DeferredItem<Item> = chest("lester_clothes", ModelKind.CLOTHES_WITH_HOOD)

    @JvmField
    val UNIFORM_CLOTHES: DeferredItem<Item> = chest("uniform_clothes", ModelKind.CLOTHES)

    @JvmField
    val WINGS_FROM_DEEP: DeferredItem<Item> = chest("wings_from_deep", ModelKind.WINGS_FROM_DEEP)

    @JvmField
    val GIANT_BAT_WINGS: DeferredItem<Item> = chest("giant_bat_wings", ModelKind.GIANT_BAT_WINGS)

    @JvmField
    val CAMOUFLAGE_PANTS: DeferredItem<Item> = legs("camouflage_pants", ModelKind.CLOTHES)

    @JvmField
    val ES_PANTS: DeferredItem<Item> = legs("es_pants", ModelKind.CLOTHES)

    @JvmField
    val FORMAL_STYLE_PANTS: DeferredItem<Item> = legs("formal_style_pants", ModelKind.CLOTHES)

    @JvmField
    val GLAMOR_PANTS: DeferredItem<Item> = legs("glamor_pants", ModelKind.CLOTHES)

    @JvmField
    val HANYI_PANTS: DeferredItem<Item> = legs("hanyi_pants", ModelKind.CLOTHES)

    @JvmField
    val JIUZHI_PANTS: DeferredItem<Item> = legs("jiuzhi_pants", ModelKind.CLOTHES)

    @JvmField
    val LESTER_PANTS: DeferredItem<Item> = legs("lester_pants", ModelKind.CLOTHES)

    @JvmField
    val STRIPED_PANTS: DeferredItem<Item> = legs("striped_pants", ModelKind.CLOTHES)

    @JvmField
    val UNIFORM_PANTS: DeferredItem<Item> = legs("uniform_pants", ModelKind.CLOTHES)

    @JvmField
    val BLACK_SHOES: DeferredItem<Item> = boots("black_shoes", ModelKind.CLOTHES)

    @JvmField
    val ES_SHOES: DeferredItem<Item> = boots("es_shoes", ModelKind.CLOTHES)

    @JvmField
    val GLAMOR_SHOES: DeferredItem<Item> = boots("glamor_shoes", ModelKind.CLOTHES)

    @JvmField
    val GRAY_SHOES: DeferredItem<Item> = boots("gray_shoes", ModelKind.CLOTHES)

    @JvmField
    val HANYI_SHOES: DeferredItem<Item> = boots("hanyi_shoes", ModelKind.CLOTHES)

    @JvmField
    val JIUZHI_SHOES: DeferredItem<Item> = boots("jiuzhi_shoes", ModelKind.CLOTHES)

    @JvmField
    val LESTER_SHOES: DeferredItem<Item> = boots("lester_shoes", ModelKind.CLOTHES)

    @JvmField
    val WHITE_SHOES: DeferredItem<Item> = boots("white_shoes", ModelKind.CLOTHES)

    @JvmStatic
    fun load() {
    }
}
