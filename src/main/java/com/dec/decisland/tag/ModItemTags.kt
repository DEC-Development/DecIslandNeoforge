package com.dec.decisland.tag

import com.dec.decisland.DecIsland
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object ModItemTags {
    @JvmField
    val REPAIRS_AMETHYST_ARMOR: TagKey<Item> = create("repairs_amethyst_armor")

    @JvmField
    val REPAIRS_FASHION: TagKey<Item> = create("repairs_fashion")

    private fun create(name: String): TagKey<Item> =
        TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, name))
}
