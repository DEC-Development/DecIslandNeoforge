package com.dec.decisland.item

import com.dec.decisland.DecIsland
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.tags.TagKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import java.util.function.Function
import java.util.function.Supplier

class ItemConfig private constructor(builder: Builder) {
    @JvmField
    val name: String = builder.name

    @JvmField
    val langMap: Map<String, String> = builder.langMap

    @JvmField
    val func: Function<Item.Properties, out Item> = builder.func

    @JvmField
    val props: Supplier<Item.Properties> = builder.props

    @JvmField
    val creativeTab: Supplier<CreativeModeTab>? = builder.creativeTab

    @JvmField
    val modelTemplate: ModelTemplate = builder.modelTemplate

    @JvmField
    val customProp: CustomItemProperties = builder.customProp

    @JvmField
    val tags: List<TagKey<Item>> = builder.tags

    class Builder(
        @JvmField val name: String,
        @JvmField val langMap: Map<String, String>,
    ) {
        internal var func: Function<Item.Properties, out Item> = Function { properties -> Item(properties) }
        internal var props: Supplier<Item.Properties> = Supplier { Item.Properties() }
        internal var creativeTab: Supplier<CreativeModeTab>? = null
        internal var modelTemplate: ModelTemplate = ModelTemplates.FLAT_ITEM
        internal var customProp: CustomItemProperties = CustomItemProperties.Builder().build()
        internal var tags: List<TagKey<Item>> = emptyList()

        fun func(func: Function<Item.Properties, out Item>): Builder = apply {
            this.func = func
        }

        fun props(props: Supplier<Item.Properties>): Builder = apply {
            this.props = props
        }

        fun creativeTab(creativeTab: Supplier<CreativeModeTab>?): Builder = apply {
            this.creativeTab = creativeTab
        }

        fun modelTemplate(modelTemplate: ModelTemplate): Builder = apply {
            this.modelTemplate = modelTemplate
        }

        fun customProp(customItemProps: CustomItemProperties): Builder = apply {
            this.customProp = customItemProps
        }

        fun tags(tags: List<TagKey<Item>>): Builder = apply {
            this.tags = tags
        }

        fun build(): ItemConfig = ItemConfig(this)
    }

    companion object {
        @JvmStatic
        fun getConfig(name: String?): ItemConfig? =
            ModItems.getItemConfigs().firstOrNull { "item.${DecIsland.MOD_ID}.${it.name}" == name }

        @JvmStatic
        fun getConfig(item: Item): ItemConfig? = getConfig(item.descriptionId)
    }
}
