package com.dec.decisland.item.custom

open class InfiniteCrystalNucleus1(properties: Properties) : ManaRestoreItem(properties) {
    override val manaRestoreAmount: Float = 1.0f
    override val useThreshold: Float = 39.0f
    override val damageOnEveryUse: Boolean = true
    override val glint: Boolean = true
}
