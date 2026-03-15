package com.dec.decisland.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class UndergroundSettings(
    val enabled: Boolean,
    val frequency: Double,
    val yFrequency: Double,
    val threshold: Double,
    val minY: Int,
    val maxY: Int,
    val surfaceProtection: Int,
) {
    companion object {
        val CODEC: Codec<UndergroundSettings> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.BOOL.fieldOf("enabled").forGetter(UndergroundSettings::enabled),
                Codec.DOUBLE.fieldOf("frequency").forGetter(UndergroundSettings::frequency),
                Codec.DOUBLE.fieldOf("y_frequency").forGetter(UndergroundSettings::yFrequency),
                Codec.DOUBLE.fieldOf("threshold").forGetter(UndergroundSettings::threshold),
                Codec.INT.fieldOf("min_y").forGetter(UndergroundSettings::minY),
                Codec.INT.fieldOf("max_y").forGetter(UndergroundSettings::maxY),
                Codec.INT.fieldOf("surface_protection").forGetter(UndergroundSettings::surfaceProtection),
            ).apply(instance, ::UndergroundSettings)
        }
    }
}
