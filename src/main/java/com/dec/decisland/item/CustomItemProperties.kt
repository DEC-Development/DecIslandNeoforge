package com.dec.decisland.item

class CustomItemProperties private constructor(builder: Builder) {
    @JvmField
    val burnTime: Int = builder.burnTime

    @JvmField
    val compostableChance: Float = builder.compostableChance

    class Builder {
        internal var burnTime: Int = 0
        internal var compostableChance: Float = 0.0f

        fun burnTime(burnTime: Int): Builder = apply {
            this.burnTime = burnTime
        }

        fun compostableChance(compostableChance: Float): Builder = apply {
            this.compostableChance = compostableChance
        }

        fun build(): CustomItemProperties = CustomItemProperties(this)
    }
}
