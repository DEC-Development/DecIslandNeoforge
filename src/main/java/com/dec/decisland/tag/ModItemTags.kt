package com.dec.decisland.tag

import com.dec.decisland.DecIsland
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object ModItemTags {
    @JvmField
    val WEAPON_GUN: TagKey<Item> = create("weapon_gun")

    @JvmField
    val WEAPON_CATAPULT: TagKey<Item> = create("weapon_catapult")

    @JvmField
    val WEAPON_MAGIC_BOOK: TagKey<Item> = create("weapon_magic_book")

    @JvmField
    val WEAPON_MISSILE: TagKey<Item> = create("weapon_missile")

    @JvmField
    val WEAPON_THROWN_DART: TagKey<Item> = create("weapon_thrown_dart")

    @JvmField
    val WEAPON_THROWN_SUNDRIES: TagKey<Item> = create("weapon_thrown_sundries")

    @JvmField
    val WEAPON_MAGIC_STAFF: TagKey<Item> = create("weapon_magic_staff")

    @JvmField
    val WEAPON_MELEE_SWORD: TagKey<Item> = create("weapon_melee_sword")

    @JvmField
    val WEAPON_MELEE_KATANA: TagKey<Item> = create("weapon_melee_katana")

    @JvmField
    val WEAPON_MELEE_AXE: TagKey<Item> = create("weapon_melee_axe")

    @JvmField
    val REPAIRS_AMETHYST_ARMOR: TagKey<Item> = create("repairs_amethyst_armor")

    @JvmField
    val REPAIRS_FASHION: TagKey<Item> = create("repairs_fashion")

    private fun create(name: String): TagKey<Item> =
        TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, name))
}
