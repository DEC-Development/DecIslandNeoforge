package com.dec.decisland.worldgen

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.Climate
import java.util.stream.Stream
import kotlin.math.abs

class VoidBiomeSource private constructor(
    private val plains: Holder<Biome>,
    private val snowyPlains: Holder<Biome>,
    private val snowySlopes: Holder<Biome>,
    private val forest: Holder<Biome>,
    private val birchForest: Holder<Biome>,
    private val snowyTaiga: Holder<Biome>,
    private val grove: Holder<Biome>,
    private val meadow: Holder<Biome>,
    private val river: Holder<Biome>,
    private val frozenRiver: Holder<Biome>,
    private val iceSpikes: Holder<Biome>,
    private val climateScale: Int,
) : BiomeSource() {
    override fun codec(): MapCodec<out BiomeSource> = CODEC

    override fun collectPossibleBiomes(): Stream<Holder<Biome>> =
        Stream.of(plains, snowyPlains, snowySlopes, forest, birchForest, snowyTaiga, grove, meadow, river, frozenRiver, iceSpikes)

    override fun getNoiseBiome(x: Int, y: Int, z: Int, sampler: Climate.Sampler): Holder<Biome> {
        val scale = if (climateScale <= 0) 1 else climateScale
        val target = sampler.sample(x * scale, y, z * scale)

        val temp = Climate.unquantizeCoord(target.temperature())
        val humidity = Climate.unquantizeCoord(target.humidity())
        val continentalness = Climate.unquantizeCoord(target.continentalness())
        val erosion = Climate.unquantizeCoord(target.erosion())
        val weirdness = abs(Climate.unquantizeCoord(target.weirdness()))

        val isCold = temp < 0.20f
        val isVeryCold = temp < 0.05f

        if (abs(continentalness) < 0.08f && erosion < -0.15f) {
            return if (isCold) frozenRiver else river
        }
        if (weirdness > 0.78f) {
            return if (isVeryCold) iceSpikes else snowySlopes
        }
        if (weirdness > 0.60f) {
            return if (isCold) grove else meadow
        }
        if (isCold) {
            return if (humidity > 0.25f) snowyTaiga else snowyPlains
        }
        if (humidity > 0.50f) {
            return forest
        }
        if (humidity > 0.28f) {
            return birchForest
        }
        return plains
    }

    companion object {
        val CODEC: MapCodec<VoidBiomeSource> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Biome.CODEC.fieldOf("plains").forGetter(VoidBiomeSource::plains),
                Biome.CODEC.fieldOf("snowy_plains").forGetter(VoidBiomeSource::snowyPlains),
                Biome.CODEC.fieldOf("snowy_slopes").forGetter(VoidBiomeSource::snowySlopes),
                Biome.CODEC.fieldOf("forest").forGetter(VoidBiomeSource::forest),
                Biome.CODEC.fieldOf("birch_forest").forGetter(VoidBiomeSource::birchForest),
                Biome.CODEC.fieldOf("snowy_taiga").forGetter(VoidBiomeSource::snowyTaiga),
                Biome.CODEC.fieldOf("grove").forGetter(VoidBiomeSource::grove),
                Biome.CODEC.fieldOf("meadow").forGetter(VoidBiomeSource::meadow),
                Biome.CODEC.fieldOf("river").forGetter(VoidBiomeSource::river),
                Biome.CODEC.fieldOf("frozen_river").forGetter(VoidBiomeSource::frozenRiver),
                Biome.CODEC.fieldOf("ice_spikes").forGetter(VoidBiomeSource::iceSpikes),
                Codec.INT.fieldOf("climate_scale").forGetter(VoidBiomeSource::climateScale),
            ).apply(instance, ::VoidBiomeSource)
        }

        fun defaultKeys(): Set<ResourceKey<Biome>> =
            setOf(
                Biomes.PLAINS,
                Biomes.SNOWY_PLAINS,
                Biomes.SNOWY_SLOPES,
                Biomes.FOREST,
                Biomes.BIRCH_FOREST,
                Biomes.SNOWY_TAIGA,
                Biomes.GROVE,
                Biomes.MEADOW,
                Biomes.RIVER,
                Biomes.FROZEN_RIVER,
                Biomes.ICE_SPIKES,
            )
    }
}
