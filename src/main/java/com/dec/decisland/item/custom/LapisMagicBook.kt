package com.dec.decisland.item.custom

class LapisMagicBook(properties: Properties) : ManaRestoreItem(properties) {
    override val manaRestoreAmount: Float = 1.0f
    override val useThreshold: Float = 59.0f
}
