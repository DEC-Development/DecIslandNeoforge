package com.dec.decisland.item.custom

class MagicCrystal(properties: Properties) : ManaGainBoostItem(properties) {
    override fun getMinAttachmentLevel(): Float = 0.0f

    override fun getMaxAttachmentLevel(): Float = 19.0f

    override fun getNutrition(): Int = 0

    override fun getSaturationModifier(): Float = 0.0f

    override fun getBoostValue(): Float = 1.0f
}
