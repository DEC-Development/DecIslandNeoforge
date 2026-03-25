package com.dec.decisland.item

import net.minecraft.world.item.Item
import java.util.function.Supplier

class CreativeTabConfig private constructor(builder: Builder) {
    @JvmField
    val name: String = builder.name

    @JvmField
    val langMap: Map<String, String> = builder.langMap

    @JvmField
    val iconItem: Supplier<Item>? = builder.iconItem

    class Builder(
        @JvmField val name: String,
        @JvmField val langMap: Map<String, String> = emptyMap(),
    ) {
        internal var iconItem: Supplier<Item>? = null

        fun iconItem(iconItem: Supplier<Item>): Builder = apply {
            this.iconItem = iconItem
        }

        fun build(): CreativeTabConfig = CreativeTabConfig(this)
    }
}
