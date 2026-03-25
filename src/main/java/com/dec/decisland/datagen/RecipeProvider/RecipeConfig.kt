package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.advancements.Criterion
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

abstract class RecipeConfig protected constructor(builder: Builder<*>) {
    @JvmField
    val name: String = builder.name

    @JvmField
    val category: RecipeCategory = builder.category

    @JvmField
    val group: String? = builder.group

    @JvmField
    val unlockCriteria: List<UnlockCriterion> = builder.unlockCriteria.toList()

    abstract class Builder<T : Builder<T>>(
        @JvmField val name: String,
    ) {
        internal var category: RecipeCategory = RecipeCategory.MISC
        internal var group: String? = null
        internal val unlockCriteria: MutableList<UnlockCriterion> = mutableListOf()

        @Suppress("UNCHECKED_CAST")
        protected fun self(): T = this as T

        fun category(category: RecipeCategory): T = self().apply {
            this.category = category
        }

        fun group(group: String): T = self().apply {
            this.group = group
        }

        fun unlockedBy(item: ItemLike): T = unlockedBy(RecipeCriteria.hasItem(item))

        fun unlockedBy(tag: TagKey<Item>): T = unlockedBy(RecipeCriteria.hasTag(tag))

        fun unlockedBy(name: String, criterion: Criterion<*>): T = unlockedBy(RecipeCriteria.custom(name, criterion))

        fun unlockedBy(unlockCriterion: UnlockCriterion): T = self().apply {
            this.unlockCriteria.add(unlockCriterion)
        }
    }
}
