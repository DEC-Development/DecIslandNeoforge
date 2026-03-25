package com.dec.decisland.datagen.RecipeProvider

import net.minecraft.advancements.Criterion
import net.minecraft.core.HolderGetter
import net.minecraft.world.item.Item

class UnlockCriterion private constructor(
    @JvmField val name: String,
    private val criterionFactory: (HolderGetter<Item>) -> Criterion<*>,
) {
    fun build(items: HolderGetter<Item>): Criterion<*> = criterionFactory(items)

    companion object {
        @JvmStatic
        fun of(
            name: String,
            criterionFactory: (HolderGetter<Item>) -> Criterion<*>,
        ): UnlockCriterion = UnlockCriterion(name, criterionFactory)
    }
}
