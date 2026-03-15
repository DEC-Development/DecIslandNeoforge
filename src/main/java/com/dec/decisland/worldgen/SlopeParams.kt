package com.dec.decisland.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class SlopeParams(
    val slopeStart: Double,
    val slopeEnd: Double,
    val attenuationPower: Double,
    val minLayers: Int,
    val maxLayers: Int,
) {
    companion object {
        val CODEC: Codec<SlopeParams> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.DOUBLE.fieldOf("slope_start").forGetter(SlopeParams::slopeStart),
                Codec.DOUBLE.fieldOf("slope_end").forGetter(SlopeParams::slopeEnd),
                Codec.DOUBLE.fieldOf("attenuation_power").forGetter(SlopeParams::attenuationPower),
                Codec.INT.fieldOf("min_layers").forGetter(SlopeParams::minLayers),
                Codec.INT.fieldOf("max_layers").forGetter(SlopeParams::maxLayers),
            ).apply(instance, ::SlopeParams)
        }
    }
}
