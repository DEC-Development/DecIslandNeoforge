package com.dec.decisland.tag;

import com.dec.decisland.DecIsland;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> REPAIRS_AMETHYST_ARMOR = create("repairs_amethyst_armor");
    public static final TagKey<Item> REPAIRS_FASHION = create("repairs_fashion");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, name));
    }
}
