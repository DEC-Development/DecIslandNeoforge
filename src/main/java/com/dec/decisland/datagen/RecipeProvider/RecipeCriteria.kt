package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ItemPredicate
import net.minecraft.advancements.criterion.InventoryChangeTrigger
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

object RecipeCriteria {
    fun hasItem(item: ItemLike): UnlockCriterion =
        UnlockCriterion.of(hasItemName(item)) {
            InventoryChangeTrigger.TriggerInstance.hasItems(item)
        }

    fun hasTag(tag: TagKey<Item>): UnlockCriterion =
        UnlockCriterion.of(hasTagName(tag)) { items ->
            InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, tag))
        }

    fun custom(name: String, criterion: Criterion<*>): UnlockCriterion =
        UnlockCriterion.of(name) { criterion }

    fun hasItemName(item: ItemLike): String = "has_${item.asItem().descriptionId.substringAfterLast('.')}"

    fun hasTagName(tag: TagKey<Item>): String = "has_${tag.location.path.replace('/', '_')}"
}
