package com.dec.decisland.item.custom

open class CrystalNucleus(
    properties: Properties,
) : ManaRestoreItem(properties) {
    override val manaRestoreAmount: Float = 20.0f
    override val useThreshold: Float = 60.0f
    override val particleBursts: Int = 5
    override val consumeOnSuccessfulUse: Boolean = true
    override val glint: Boolean = true
}
