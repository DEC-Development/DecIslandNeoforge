package com.dec.decisland.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.Identifier

data class FractalSurfaceSettings(
    val baseHeight: Double,
    val baseFrequency: Double,
    val baseAmplitude: Double,
    val maxLayers: Int,
    val lacunarity: Double,
    val persistence: Double,
    val selectorFrequency: Double,
    val smoothPasses: Int,
    val gradientStep: Int,
    val macro: MacroTerrainSettings,
    val underground: UndergroundSettings,
    val minY: Int,
    val genDepth: Int,
    val seaLevel: Int,
    val snowLine: Int,
    val biomeSlopeParams: Map<Identifier, SlopeParams>,
) {
    companion object {
        val CODEC: Codec<FractalSurfaceSettings> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.DOUBLE.fieldOf("base_height").forGetter(FractalSurfaceSettings::baseHeight),
                Codec.DOUBLE.fieldOf("base_frequency").forGetter(FractalSurfaceSettings::baseFrequency),
                Codec.DOUBLE.fieldOf("base_amplitude").forGetter(FractalSurfaceSettings::baseAmplitude),
                Codec.INT.fieldOf("max_layers").forGetter(FractalSurfaceSettings::maxLayers),
                Codec.DOUBLE.fieldOf("lacunarity").forGetter(FractalSurfaceSettings::lacunarity),
                Codec.DOUBLE.fieldOf("persistence").forGetter(FractalSurfaceSettings::persistence),
                Codec.DOUBLE.fieldOf("selector_frequency").forGetter(FractalSurfaceSettings::selectorFrequency),
                Codec.INT.fieldOf("smooth_passes").forGetter(FractalSurfaceSettings::smoothPasses),
                Codec.INT.fieldOf("gradient_step").forGetter(FractalSurfaceSettings::gradientStep),
                MacroTerrainSettings.CODEC.fieldOf("macro").forGetter(FractalSurfaceSettings::macro),
                UndergroundSettings.CODEC.fieldOf("underground").forGetter(FractalSurfaceSettings::underground),
                Codec.INT.fieldOf("min_y").forGetter(FractalSurfaceSettings::minY),
                Codec.INT.fieldOf("gen_depth").forGetter(FractalSurfaceSettings::genDepth),
                Codec.INT.fieldOf("sea_level").forGetter(FractalSurfaceSettings::seaLevel),
                Codec.INT.fieldOf("snow_line").forGetter(FractalSurfaceSettings::snowLine),
                Codec.unboundedMap(Identifier.CODEC, SlopeParams.CODEC).fieldOf("biome_slope_params").forGetter(FractalSurfaceSettings::biomeSlopeParams),
            ).apply(instance, ::FractalSurfaceSettings)
        }
    }
}
