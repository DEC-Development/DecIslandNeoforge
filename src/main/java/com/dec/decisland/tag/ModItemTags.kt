package com.dec.decisland.tag

import com.dec.decisland.DecIsland
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object ModItemTags {
    @JvmField
    val RANGE_WEAPON: TagKey<Item> = create("range_weapon")

    @JvmField
    val MAGIC_WEAPON: TagKey<Item> = create("magic_weapon")

    @JvmField
    val THROWN_WEAPON: TagKey<Item> = create("thrown_weapon")

    @JvmField
    val MELEE_WEAPON: TagKey<Item> = create("melee_weapon")

    @JvmField
    val GUN: TagKey<Item> = create("gun")

    @JvmField
    val CATAPULT: TagKey<Item> = create("catapult")

    @JvmField
    val MAGIC_BOOK: TagKey<Item> = create("magic_book")

    @JvmField
    val STAFF: TagKey<Item> = create("staff")

    @JvmField
    val DART: TagKey<Item> = create("dart")

    @JvmField
    val SUNDRIES: TagKey<Item> = create("sundries")

    @JvmField
    val SWORD: TagKey<Item> = create("sword")

    @JvmField
    val SICKLE: TagKey<Item> = create("sickle")

    @JvmField
    val KATANA: TagKey<Item> = create("katana")

    @JvmField
    val AXE: TagKey<Item> = create("axe")

    @JvmField
    val REPAIRS_AMETHYST_ARMOR: TagKey<Item> = create("repairs_amethyst_armor")

    @JvmField
    val REPAIRS_FASHION: TagKey<Item> = create("repairs_fashion")

    private fun create(name: String): TagKey<Item> =
        TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, name))
}
