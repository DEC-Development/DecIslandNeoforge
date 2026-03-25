package com.dec.decisland.item.category

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.CustomItemProperties
import com.dec.decisland.item.CustomItemProperties.CooldownBonus
import com.dec.decisland.item.ItemConfig.WeaponCooldownCategory
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import com.dec.decisland.item.custom.accessory.DiamondRingItem
import com.dec.decisland.item.custom.accessory.EmeraldRingItem
import com.dec.decisland.item.custom.accessory.GoldRingItem
import com.dec.decisland.item.custom.accessory.HeartRingItem
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Function
import java.util.function.Supplier

object Accessory {
    @JvmField
    val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_ACCESSORIES_TAB

    private fun registerAccessory(
        name: String,
        durability: Int? = null,
        cooldownBonus: CooldownBonus? = null,
        func: Function<Item.Properties, out Item> = Function(::Item),
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .func(func)
                .props {
                    var properties = Item.Properties().stacksTo(1)
                    if (durability != null) {
                        properties = properties.durability(durability)
                    }
                    properties
                }
                .customProp(
                    CustomItemProperties.Builder()
                        .accessoryCooldownBonus(cooldownBonus)
                        .build(),
                )
                .modelTemplate(ModelTemplates.FLAT_ITEM)
                .creativeTab(creativeTab)
                .build(),
        )

    @JvmField
    val DIAMOND_RING: DeferredItem<Item> =
        registerAccessory(
            "diamond_ring",
            durability = 1652,
            cooldownBonus = CooldownBonus(WeaponCooldownCategory.MISSILE, 3.0f),
            func = Function(::DiamondRingItem),
        )

    @JvmField
    val EMERALD_RING: DeferredItem<Item> =
        registerAccessory(
            "emerald_ring",
            durability = 395,
            cooldownBonus = CooldownBonus(WeaponCooldownCategory.MISSILE, 4.0f),
            func = Function(::EmeraldRingItem),
        )

    @JvmField
    val GOLD_RING: DeferredItem<Item> =
        registerAccessory(
            "gold_ring",
            durability = 152,
            cooldownBonus = CooldownBonus(WeaponCooldownCategory.MISSILE, 4.0f),
            func = Function(::GoldRingItem),
        )

    @JvmField
    val HEART_RING: DeferredItem<Item> =
        registerAccessory(
            "heart_ring",
            durability = 394,
            cooldownBonus = CooldownBonus(WeaponCooldownCategory.MISSILE, 5.0f),
            func = Function(::HeartRingItem),
        )

    @JvmField
    val NATURAL_RING: DeferredItem<Item> =
        registerAccessory("natural_ring", cooldownBonus = CooldownBonus(WeaponCooldownCategory.MISSILE, 3.0f))

    @JvmField
    val DUST_RING: DeferredItem<Item> =
        registerAccessory("dust_ring", cooldownBonus = CooldownBonus(WeaponCooldownCategory.MISSILE, 7.0f))

    @JvmField
    val FIRE_RING: DeferredItem<Item> =
        registerAccessory("fire_ring", cooldownBonus = CooldownBonus(WeaponCooldownCategory.MISSILE, 6.0f))

    @JvmField
    val ENDER_RING: DeferredItem<Item> =
        registerAccessory("ender_ring", cooldownBonus = CooldownBonus(WeaponCooldownCategory.MISSILE, 7.0f))

    @JvmField
    val HERB_BAG: DeferredItem<Item> =
        registerAccessory("herb_bag", cooldownBonus = CooldownBonus(WeaponCooldownCategory.MAGIC_BOOK, 4.0f))

    @JvmField
    val STONES_BAG: DeferredItem<Item> =
        registerAccessory("stones_bag", cooldownBonus = CooldownBonus(WeaponCooldownCategory.CATAPULT, 5.0f))

    @JvmField
    val ARCHER_STONES_BAG: DeferredItem<Item> =
        registerAccessory("archer_stones_bag", cooldownBonus = CooldownBonus(WeaponCooldownCategory.CATAPULT, 13.0f))

    @JvmField
    val BULLET_BAG: DeferredItem<Item> =
        registerAccessory("bullet_bag", cooldownBonus = CooldownBonus(WeaponCooldownCategory.GUN, 2.0f))

    @JvmField
    val ARCHER_BULLET_BAG: DeferredItem<Item> =
        registerAccessory("archer_bullet_bag", cooldownBonus = CooldownBonus(WeaponCooldownCategory.GUN, 9.0f))

    @JvmField
    val HUNTER_BULLET_BAG: DeferredItem<Item> =
        registerAccessory("hunter_bullet_bag", cooldownBonus = CooldownBonus(WeaponCooldownCategory.GUN, 3.0f))

    @JvmField
    val PIRATE_BULLET_BAG: DeferredItem<Item> =
        registerAccessory("pirate_bullet_bag", cooldownBonus = CooldownBonus(WeaponCooldownCategory.GUN, 3.0f))

    @JvmField
    val LAVA_BULLET_BAG: DeferredItem<Item> =
        registerAccessory("lava_bullet_bag", cooldownBonus = CooldownBonus(WeaponCooldownCategory.GUN, 7.0f))

    @JvmField
    val BLOOD_BULLET_BAG: DeferredItem<Item> =
        registerAccessory("blood_bullet_bag", cooldownBonus = CooldownBonus(WeaponCooldownCategory.GUN, 4.0f))

    @JvmStatic
    fun load() {
    }
}
