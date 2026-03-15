package com.dec.decisland.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class MacroTerrainSettings(
    val ridgeFrequency: Double,
    val ridgeAmplitude: Double,
    val ridgeSharpness: Double,
    val valleyFrequency: Double,
    val valleyDepth: Double,
    val riverFrequency: Double,
    val riverWidth: Double,
    val riverDepth: Double,
    val glacierFrequency: Double,
    val glacierThreshold: Double,
    val glacierSmoothStrength: Double,
    val iceRiverThreshold: Double,
    val iceRiverBlueIceThreshold: Double,
) {
    companion object {
        val CODEC: Codec<MacroTerrainSettings> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.DOUBLE.fieldOf("ridge_frequency").forGetter(MacroTerrainSettings::ridgeFrequency),
                Codec.DOUBLE.fieldOf("ridge_amplitude").forGetter(MacroTerrainSettings::ridgeAmplitude),
                Codec.DOUBLE.fieldOf("ridge_sharpness").forGetter(MacroTerrainSettings::ridgeSharpness),
                Codec.DOUBLE.fieldOf("valley_frequency").forGetter(MacroTerrainSettings::valleyFrequency),
                Codec.DOUBLE.fieldOf("valley_depth").forGetter(MacroTerrainSettings::valleyDepth),
                Codec.DOUBLE.fieldOf("river_frequency").forGetter(MacroTerrainSettings::riverFrequency),
                Codec.DOUBLE.fieldOf("river_width").forGetter(MacroTerrainSettings::riverWidth),
                Codec.DOUBLE.fieldOf("river_depth").forGetter(MacroTerrainSettings::riverDepth),
                Codec.DOUBLE.fieldOf("glacier_frequency").forGetter(MacroTerrainSettings::glacierFrequency),
                Codec.DOUBLE.fieldOf("glacier_threshold").forGetter(MacroTerrainSettings::glacierThreshold),
                Codec.DOUBLE.fieldOf("glacier_smooth_strength").forGetter(MacroTerrainSettings::glacierSmoothStrength),
                Codec.DOUBLE.fieldOf("ice_river_threshold").forGetter(MacroTerrainSettings::iceRiverThreshold),
                Codec.DOUBLE.fieldOf("ice_river_blue_ice_threshold").forGetter(MacroTerrainSettings::iceRiverBlueIceThreshold),
            ).apply(instance, ::MacroTerrainSettings)
        }
    }
}
