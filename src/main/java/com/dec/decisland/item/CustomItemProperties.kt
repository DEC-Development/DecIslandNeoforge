package com.dec.decisland.item

class CustomItemProperties private constructor(builder: Builder) {
    data class CooldownBonus(
        val category: ItemConfig.WeaponCooldownCategory,
        val ticksPerFourTicks: Float,
    )

    @JvmField
    val burnTime: Int = builder.burnTime

    @JvmField
    val compostableChance: Float = builder.compostableChance

    @JvmField
    val accessoryCooldownBonus: CooldownBonus? = builder.accessoryCooldownBonus

    class Builder {
        internal var burnTime: Int = 0
        internal var compostableChance: Float = 0.0f
        internal var accessoryCooldownBonus: CooldownBonus? = null

        fun burnTime(burnTime: Int): Builder = apply {
            this.burnTime = burnTime
        }

        fun compostableChance(compostableChance: Float): Builder = apply {
            this.compostableChance = compostableChance
        }

        fun accessoryCooldownBonus(accessoryCooldownBonus: CooldownBonus?): Builder = apply {
            this.accessoryCooldownBonus = accessoryCooldownBonus
        }

        fun build(): CustomItemProperties = CustomItemProperties(this)
    }
}
