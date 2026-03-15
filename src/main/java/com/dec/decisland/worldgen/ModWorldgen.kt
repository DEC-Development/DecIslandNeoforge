package com.dec.decisland.worldgen

import com.dec.decisland.DecIsland
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModWorldgen {
    @JvmField
    val BIOME_SOURCES: DeferredRegister<com.mojang.serialization.MapCodec<out BiomeSource>> =
        DeferredRegister.create(BuiltInRegistries.BIOME_SOURCE, DecIsland.MOD_ID)

    @JvmField
    val CHUNK_GENERATORS: DeferredRegister<com.mojang.serialization.MapCodec<out ChunkGenerator>> =
        DeferredRegister.create(BuiltInRegistries.CHUNK_GENERATOR, DecIsland.MOD_ID)

    @JvmField
    val VOID_BIOMES: DeferredHolder<com.mojang.serialization.MapCodec<out BiomeSource>, com.mojang.serialization.MapCodec<out BiomeSource>> =
        BIOME_SOURCES.register("void_biomes", Supplier { VoidBiomeSource.CODEC })

    @JvmField
    val FRACTAL_SURFACE: DeferredHolder<com.mojang.serialization.MapCodec<out ChunkGenerator>, com.mojang.serialization.MapCodec<out ChunkGenerator>> =
        CHUNK_GENERATORS.register("fractal_surface", Supplier { FractalSurfaceChunkGenerator.CODEC })

    @JvmStatic
    fun register(eventBus: IEventBus) {
        BIOME_SOURCES.register(eventBus)
        CHUNK_GENERATORS.register(eventBus)
    }
}
