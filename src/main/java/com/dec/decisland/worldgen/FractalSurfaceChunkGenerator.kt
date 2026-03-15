package com.dec.decisland.worldgen

import com.dec.decisland.DecIsland
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.QuartPos
import net.minecraft.resources.Identifier
import net.minecraft.server.level.WorldGenRegion
import net.minecraft.util.Mth
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.NoiseColumn
import net.minecraft.world.level.StructureManager
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.RandomState
import net.minecraft.world.level.levelgen.blending.Blender
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class FractalSurfaceChunkGenerator(
    biomeSource: net.minecraft.world.level.biome.BiomeSource,
    private val settings: FractalSurfaceSettings
) : ChunkGenerator(biomeSource) {
    override fun codec(): MapCodec<out ChunkGenerator> = CODEC

    private var noiseA: FractalPerlin2D? = null
    private var noiseB: FractalPerlin2D? = null
    private var selectorNoise: FractalPerlin2D? = null
    private var macroNoise: FractalPerlin2D? = null
    private var caveNoise: FractalPerlin3D? = null

    private val heightCache: java.util.concurrent.ConcurrentHashMap<Long, IntArray> = java.util.concurrent.ConcurrentHashMap()

    private fun computeInnerHeightsForChunk(chunkX: Int, chunkZ: Int, randomState: RandomState): IntArray {
        ensureNoise(randomState)
        val noiseA = requireNotNull(noiseA)
        val noiseB = requireNotNull(noiseB)
        val selectorNoise = requireNotNull(selectorNoise)
        val macroNoise = requireNotNull(macroNoise)
        val sampler = randomState.sampler()

        val gradientStep = max(1, settings.gradientStep)
        val margin = max(2, gradientStep + 1)
        val size = 16 + margin * 2

        val heights = DoubleArray(size * size) { settings.baseHeight }
        val temp = DoubleArray(size * size)
        val smoothed = DoubleArray(size * size)
        val gradient = DoubleArray(size * size)
        val glacierMask = DoubleArray(size * size)

        fun heightIndex(ix: Int, iz: Int): Int = iz * size + ix

        val biomeParamsByIndex = IntArray(size * size)
        val slopeParamPalette = ArrayList<SlopeParams>()
        val slopeParamIndexById = HashMap<Identifier, Int>()

        fun slopeIndexFor(biomeId: Identifier): Int {
            val existing = slopeParamIndexById[biomeId]
            if (existing != null) return existing
            val params = settings.biomeSlopeParams[biomeId]
                ?: error("Missing biome_slope_params entry for biome '$biomeId'")
            val idx = slopeParamPalette.size
            slopeParamPalette.add(params)
            slopeParamIndexById[biomeId] = idx
            return idx
        }

        fun computeGradient(src: DoubleArray, dst: DoubleArray) {
            val e = gradientStep
            for (z in 0 until size) {
                for (x in 0 until size) {
                    val idx = heightIndex(x, z)
                    if (x - e < 0 || x + e >= size || z - e < 0 || z + e >= size) {
                        dst[idx] = 0.0
                        continue
                    }
                    val dx = src[heightIndex(x + e, z)] - src[heightIndex(x - e, z)]
                    val dz = src[heightIndex(x, z + e)] - src[heightIndex(x, z - e)]
                    dst[idx] = sqrt(dx * dx + dz * dz) / (2.0 * e.toDouble())
                }
            }
        }

        fun smoothInPlace(src: DoubleArray) {
            val passes = max(0, settings.smoothPasses)
            if (passes == 0) return
            repeat(passes) {
                for (z in 0 until size) {
                    for (x in 0 until size) {
                        val idx = heightIndex(x, z)
                        if (x == 0 || z == 0 || x == size - 1 || z == size - 1) {
                            temp[idx] = src[idx]
                            continue
                        }
                        val c = src[idx]
                        val n = src[heightIndex(x, z - 1)]
                        val s = src[heightIndex(x, z + 1)]
                        val w = src[heightIndex(x - 1, z)]
                        val e = src[heightIndex(x + 1, z)]
                        val nw = src[heightIndex(x - 1, z - 1)]
                        val ne = src[heightIndex(x + 1, z - 1)]
                        val sw = src[heightIndex(x - 1, z + 1)]
                        val se = src[heightIndex(x + 1, z + 1)]
                        temp[idx] = (4.0 * c + 2.0 * (n + s + w + e) + (nw + ne + sw + se)) / 16.0
                    }
                }
                temp.copyInto(src)
            }
        }

        fun lerp(t: Double, a: Double, b: Double): Double = a + t * (b - a)
        fun clamp01(v: Double): Double = Mth.clamp(v, 0.0, 1.0)

        fun layerNoise(worldX: Int, worldZ: Int, freq: Double, layer: Int): Double {
            val selX = (worldX + layer * 131).toDouble() * settings.selectorFrequency
            val selZ = (worldZ - layer * 173).toDouble() * settings.selectorFrequency
            val alpha = (selectorNoise.perlin(selX, selZ) + 1.0) * 0.5

            val x1 = (worldX + layer * 911).toDouble() * freq
            val z1 = (worldZ - layer * 357).toDouble() * freq
            val x2 = (worldX - layer * 577).toDouble() * freq
            val z2 = (worldZ + layer * 911).toDouble() * freq
            val n1 = noiseA.perlin(x1, z1)
            val n2 = noiseB.perlin(x2, z2)
            return lerp(alpha, n1, n2)
        }

        fun allowedLayers(params: SlopeParams, g: Double): Int {
            val t = smoothstep(params.slopeStart, params.slopeEnd, g)
            val target = lerp(t, params.maxLayers.toDouble(), params.minLayers.toDouble())
            return Mth.clamp(target.toInt(), params.minLayers, params.maxLayers)
        }

        fun attenuation(params: SlopeParams, g: Double): Double {
            val t = smoothstep(params.slopeStart, params.slopeEnd, g)
            return (1.0 - t).pow(params.attenuationPower)
        }

        fun ridged(n: Double): Double = 1.0 - abs(n)

        val chunkMinBlockX = chunkX shl 4
        val chunkMinBlockZ = chunkZ shl 4
        for (lz in 0 until size) {
            val worldZ = chunkMinBlockZ + (lz - margin)
            val qz = QuartPos.fromBlock(worldZ)
            for (lx in 0 until size) {
                val worldX = chunkMinBlockX + (lx - margin)
                val qx = QuartPos.fromBlock(worldX)
                val biome = biomeSource.getNoiseBiome(qx, 0, qz, sampler)
                val biomeId = biome.unwrapKey()
                    .orElseThrow { IllegalStateException("Biome holder has no key") }
                    .identifier()
                biomeParamsByIndex[heightIndex(lx, lz)] = slopeIndexFor(biomeId)
            }
        }

        var freq = settings.baseFrequency
        var amp = settings.baseAmplitude
        val maxLayers = max(1, settings.maxLayers)
        for (layer in 0 until maxLayers) {
            if (layer > 0) {
                smoothInPlace(heights)
                computeGradient(heights, gradient)
            }
            for (lz in 0 until size) {
                val worldZ = chunkMinBlockZ + (lz - margin)
                for (lx in 0 until size) {
                    val idx = heightIndex(lx, lz)
                    val params = slopeParamPalette[biomeParamsByIndex[idx]]
                    if (layer > 0) {
                        val g = gradient[idx]
                        val localAllowed = min(maxLayers, allowedLayers(params, g))
                        if (layer >= localAllowed) continue
                        val att = attenuation(params, g)
                        heights[idx] += att * amp * layerNoise(chunkMinBlockX + (lx - margin), worldZ, freq, layer)
                    } else {
                        heights[idx] += amp * layerNoise(chunkMinBlockX + (lx - margin), worldZ, freq, layer)
                    }
                }
            }
            freq *= settings.lacunarity
            amp *= settings.persistence
        }

        for (lz in 0 until size) {
            val worldZ = chunkMinBlockZ + (lz - margin)
            for (lx in 0 until size) {
                val idx = heightIndex(lx, lz)
                val worldX = chunkMinBlockX + (lx - margin)

                val ridgeN = macroNoise.perlin(worldX.toDouble() * settings.macro.ridgeFrequency, worldZ.toDouble() * settings.macro.ridgeFrequency)
                val ridge = ridged(ridgeN).pow(settings.macro.ridgeSharpness)
                heights[idx] += ridge * settings.macro.ridgeAmplitude

                val valleyN = macroNoise.perlin((worldX + 10127).toDouble() * settings.macro.valleyFrequency, (worldZ - 9323).toDouble() * settings.macro.valleyFrequency)
                val valley = clamp01((valleyN + 1.0) * 0.5)
                heights[idx] -= valley * settings.macro.valleyDepth

                val riverN = macroNoise.perlin((worldX - 2819).toDouble() * settings.macro.riverFrequency, (worldZ + 7717).toDouble() * settings.macro.riverFrequency)
                val riverDist = abs(riverN)
                val river = 1.0 - smoothstep(0.0, settings.macro.riverWidth, riverDist)
                heights[idx] -= river * settings.macro.riverDepth

                val glacierN = macroNoise.perlin((worldX + 4441).toDouble() * settings.macro.glacierFrequency, (worldZ + 1559).toDouble() * settings.macro.glacierFrequency)
                val gBase = clamp01((glacierN + 1.0) * 0.5)
                val g = smoothstep(settings.macro.glacierThreshold, 1.0, gBase)
                glacierMask[idx] = g
            }
        }

        heights.copyInto(smoothed)
        if (settings.macro.glacierSmoothStrength > 0.0) {
            val extraPasses = max(1, settings.smoothPasses + 1)
            repeat(extraPasses) {
                for (z in 0 until size) {
                    for (x in 0 until size) {
                        val idx = heightIndex(x, z)
                        if (x == 0 || z == 0 || x == size - 1 || z == size - 1) {
                            temp[idx] = smoothed[idx]
                            continue
                        }
                        val c = smoothed[idx]
                        val n = smoothed[heightIndex(x, z - 1)]
                        val s = smoothed[heightIndex(x, z + 1)]
                        val w = smoothed[heightIndex(x - 1, z)]
                        val e = smoothed[heightIndex(x + 1, z)]
                        val nw = smoothed[heightIndex(x - 1, z - 1)]
                        val ne = smoothed[heightIndex(x + 1, z - 1)]
                        val sw = smoothed[heightIndex(x - 1, z + 1)]
                        val se = smoothed[heightIndex(x + 1, z + 1)]
                        temp[idx] = (4.0 * c + 2.0 * (n + s + w + e) + (nw + ne + sw + se)) / 16.0
                    }
                }
                temp.copyInto(smoothed)
            }
            for (i in heights.indices) {
                val t = clamp01(glacierMask[i] * settings.macro.glacierSmoothStrength)
                heights[i] = lerp(t, heights[i], smoothed[i])
            }
        }

        val minY = settings.minY
        val maxY = settings.minY + settings.genDepth
        val inner = IntArray(16 * 16)
        for (z in 0 until 16) {
            for (x in 0 until 16) {
                val idx = heightIndex(x + margin, z + margin)
                inner[z * 16 + x] = Mth.clamp(heights[idx].toInt(), minY, maxY - 1)
            }
        }
        return inner
    }

    private fun ensureNoise(randomState: RandomState) {
        if (noiseA != null && noiseB != null && selectorNoise != null && macroNoise != null && caveNoise != null) return
        val factory = randomState.getOrCreateRandomFactory(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "fractal_surface"))
        noiseA = FractalPerlin2D(factory.at(0, 0, 0).nextLong())
        noiseB = FractalPerlin2D(factory.at(1, 0, 0).nextLong())
        selectorNoise = FractalPerlin2D(factory.at(2, 0, 0).nextLong())
        macroNoise = FractalPerlin2D(factory.at(3, 0, 0).nextLong())
        caveNoise = FractalPerlin3D(factory.at(4, 0, 0).nextLong())
    }

    private fun smoothstep(edge0: Double, edge1: Double, x: Double): Double {
        if (edge0 == edge1) return if (x < edge0) 0.0 else 1.0
        val t = Mth.clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0)
        return t * t * (3.0 - 2.0 * t)
    }

    override fun applyCarvers(
        region: WorldGenRegion,
        seed: Long,
        randomState: RandomState,
        biomeManager: BiomeManager,
        structureManager: StructureManager,
        chunk: ChunkAccess
    ) {
        // No caves/carvers for now (surface-first iteration).
    }

    override fun buildSurface(region: WorldGenRegion, structureManager: StructureManager, randomState: RandomState, chunk: ChunkAccess) {
        // Surface blocks are authored in fillFromNoise for this generator.
    }

    override fun spawnOriginalMobs(region: WorldGenRegion) {
        // No-op.
    }

    override fun getGenDepth(): Int = settings.genDepth

    override fun getSeaLevel(): Int = settings.seaLevel

    override fun getMinY(): Int = settings.minY

    override fun fillFromNoise(
        blender: Blender,
        randomState: RandomState,
        structureManager: StructureManager,
        chunk: ChunkAccess
    ): java.util.concurrent.CompletableFuture<ChunkAccess> {
        ensureNoise(randomState)
        val noiseA = requireNotNull(noiseA)
        val noiseB = requireNotNull(noiseB)
        val selectorNoise = requireNotNull(selectorNoise)
        val macroNoise = requireNotNull(macroNoise)
        val caveNoise = requireNotNull(caveNoise)
        val sampler = randomState.sampler()

        val minY = settings.minY
        val maxY = settings.minY + settings.genDepth
        val chunkPos = chunk.pos

        val gradientStep = max(1, settings.gradientStep)
        val margin = max(2, gradientStep + 1)
        val size = 16 + margin * 2

        val heights = DoubleArray(size * size) { settings.baseHeight }
        val temp = DoubleArray(size * size)
        val smoothed = DoubleArray(size * size)
        val gradient = DoubleArray(size * size)
        val glacierMask = DoubleArray(size * size)
        val riverMask = DoubleArray(size * size)
        val iceRiverMask = DoubleArray(size * size)

        val biomeParamsByIndex = IntArray(size * size)
        val slopeParamPalette = ArrayList<SlopeParams>()
        val slopeParamIndexById = HashMap<Identifier, Int>()

        fun slopeIndexFor(biomeId: Identifier): Int {
            val existing = slopeParamIndexById[biomeId]
            if (existing != null) return existing
            val params = settings.biomeSlopeParams[biomeId]
                ?: error("Missing biome_slope_params entry for biome '$biomeId'")
            val idx = slopeParamPalette.size
            slopeParamPalette.add(params)
            slopeParamIndexById[biomeId] = idx
            return idx
        }

        fun heightIndex(ix: Int, iz: Int): Int = iz * size + ix

        // Precompute biome -> slope params index for the extended area.
        for (lz in 0 until size) {
            val worldZ = chunkPos.minBlockZ + (lz - margin)
            val qz = QuartPos.fromBlock(worldZ)
            for (lx in 0 until size) {
                val worldX = chunkPos.minBlockX + (lx - margin)
                val qx = QuartPos.fromBlock(worldX)
                val biome = biomeSource.getNoiseBiome(qx, 0, qz, sampler)
                val biomeId = biome.unwrapKey()
                    .orElseThrow { IllegalStateException("Biome holder has no key") }
                    .identifier()
                biomeParamsByIndex[heightIndex(lx, lz)] = slopeIndexFor(biomeId)
            }
        }

        fun computeGradient(src: DoubleArray, dst: DoubleArray) {
            val e = gradientStep
            for (z in 0 until size) {
                for (x in 0 until size) {
                    val idx = heightIndex(x, z)
                    if (x - e < 0 || x + e >= size || z - e < 0 || z + e >= size) {
                        dst[idx] = 0.0
                        continue
                    }
                    val dx = src[heightIndex(x + e, z)] - src[heightIndex(x - e, z)]
                    val dz = src[heightIndex(x, z + e)] - src[heightIndex(x, z - e)]
                    dst[idx] = sqrt(dx * dx + dz * dz) / (2.0 * e.toDouble())
                }
            }
        }

        fun smoothInPlace(src: DoubleArray) {
            val passes = max(0, settings.smoothPasses)
            if (passes == 0) return
            repeat(passes) {
                for (z in 0 until size) {
                    for (x in 0 until size) {
                        val idx = heightIndex(x, z)
                        if (x == 0 || z == 0 || x == size - 1 || z == size - 1) {
                            temp[idx] = src[idx]
                            continue
                        }
                        val c = src[idx]
                        val n = src[heightIndex(x, z - 1)]
                        val s = src[heightIndex(x, z + 1)]
                        val w = src[heightIndex(x - 1, z)]
                        val e = src[heightIndex(x + 1, z)]
                        val nw = src[heightIndex(x - 1, z - 1)]
                        val ne = src[heightIndex(x + 1, z - 1)]
                        val sw = src[heightIndex(x - 1, z + 1)]
                        val se = src[heightIndex(x + 1, z + 1)]
                        temp[idx] = (4.0 * c + 2.0 * (n + s + w + e) + (nw + ne + sw + se)) / 16.0
                    }
                }
                temp.copyInto(src)
            }
        }

        fun lerp(t: Double, a: Double, b: Double): Double = a + t * (b - a)

        fun clamp01(v: Double): Double = Mth.clamp(v, 0.0, 1.0)

        fun layerNoise(worldX: Int, worldZ: Int, freq: Double, layer: Int): Double {
            val selX = (worldX + layer * 131).toDouble() * settings.selectorFrequency
            val selZ = (worldZ - layer * 173).toDouble() * settings.selectorFrequency
            val alpha = (selectorNoise.perlin(selX, selZ) + 1.0) * 0.5

            val x1 = (worldX + layer * 911).toDouble() * freq
            val z1 = (worldZ - layer * 357).toDouble() * freq
            val x2 = (worldX - layer * 577).toDouble() * freq
            val z2 = (worldZ + layer * 911).toDouble() * freq
            val n1 = noiseA.perlin(x1, z1)
            val n2 = noiseB.perlin(x2, z2)
            return lerp(alpha, n1, n2)
        }

        fun ridged(n: Double): Double = 1.0 - abs(n)

        fun allowedLayers(params: SlopeParams, g: Double): Int {
            val t = smoothstep(params.slopeStart, params.slopeEnd, g)
            val target = lerp(t, params.maxLayers.toDouble(), params.minLayers.toDouble())
            return Mth.clamp(target.toInt(), params.minLayers, params.maxLayers)
        }

        fun attenuation(params: SlopeParams, g: Double): Double {
            val t = smoothstep(params.slopeStart, params.slopeEnd, g)
            return (1.0 - t).pow(params.attenuationPower)
        }

        // Spec:
        // 1) Generate first layer a from (fractal) Perlin.
        // 2) Smooth a, compute gradient b.
        // 3) b larger -> fewer layers to add; add next layer; repeat.
        var freq = settings.baseFrequency
        var amp = settings.baseAmplitude
        val maxLayers = max(1, settings.maxLayers)
        for (layer in 0 until maxLayers) {
            if (layer > 0) {
                smoothInPlace(heights)
                computeGradient(heights, gradient)
            }

            for (lz in 0 until size) {
                val worldZ = chunkPos.minBlockZ + (lz - margin)
                for (lx in 0 until size) {
                    val idx = heightIndex(lx, lz)
                    val params = slopeParamPalette[biomeParamsByIndex[idx]]

                    if (layer > 0) {
                        val g = gradient[idx]
                        val localAllowed = min(maxLayers, allowedLayers(params, g))
                        if (layer >= localAllowed) continue
                        val att = attenuation(params, g)
                        heights[idx] += att * amp * layerNoise(chunkPos.minBlockX + (lx - margin), worldZ, freq, layer)
                    } else {
                        heights[idx] += amp * layerNoise(chunkPos.minBlockX + (lx - margin), worldZ, freq, layer)
                    }
                }
            }

            freq *= settings.lacunarity
            amp *= settings.persistence
        }

        // Macro terrain shaping: peaks/ridges, valleys, rivers, glaciers.
        for (lz in 0 until size) {
            val worldZ = chunkPos.minBlockZ + (lz - margin)
            for (lx in 0 until size) {
                val idx = heightIndex(lx, lz)
                val worldX = chunkPos.minBlockX + (lx - margin)

                val ridgeN = macroNoise.perlin(worldX.toDouble() * settings.macro.ridgeFrequency, worldZ.toDouble() * settings.macro.ridgeFrequency)
                val ridge = ridged(ridgeN).pow(settings.macro.ridgeSharpness)
                heights[idx] += ridge * settings.macro.ridgeAmplitude

                val valleyN = macroNoise.perlin((worldX + 10127).toDouble() * settings.macro.valleyFrequency, (worldZ - 9323).toDouble() * settings.macro.valleyFrequency)
                val valley = clamp01((valleyN + 1.0) * 0.5)
                heights[idx] -= valley * settings.macro.valleyDepth

                val riverN = macroNoise.perlin((worldX - 2819).toDouble() * settings.macro.riverFrequency, (worldZ + 7717).toDouble() * settings.macro.riverFrequency)
                val riverDist = abs(riverN) // 0 is river centerline
                val river = 1.0 - smoothstep(0.0, settings.macro.riverWidth, riverDist)
                heights[idx] -= river * settings.macro.riverDepth
                riverMask[idx] = river

                val glacierN = macroNoise.perlin((worldX + 4441).toDouble() * settings.macro.glacierFrequency, (worldZ + 1559).toDouble() * settings.macro.glacierFrequency)
                val gBase = clamp01((glacierN + 1.0) * 0.5)
                val g = smoothstep(settings.macro.glacierThreshold, 1.0, gBase)
                glacierMask[idx] = g
                iceRiverMask[idx] = river * g
            }
        }

        // Glacier smoothing: blend towards a smoothed heightfield where glacierMask is strong.
        heights.copyInto(smoothed)
        if (settings.macro.glacierSmoothStrength > 0.0) {
            val oldPasses = settings.smoothPasses
            // Reuse smoothing kernel by applying a couple extra passes on the copy.
            val extraPasses = max(1, oldPasses + 1)
            repeat(extraPasses) {
                for (z in 0 until size) {
                    for (x in 0 until size) {
                        val idx = heightIndex(x, z)
                        if (x == 0 || z == 0 || x == size - 1 || z == size - 1) {
                            temp[idx] = smoothed[idx]
                            continue
                        }
                        val c = smoothed[idx]
                        val n = smoothed[heightIndex(x, z - 1)]
                        val s = smoothed[heightIndex(x, z + 1)]
                        val w = smoothed[heightIndex(x - 1, z)]
                        val e = smoothed[heightIndex(x + 1, z)]
                        val nw = smoothed[heightIndex(x - 1, z - 1)]
                        val ne = smoothed[heightIndex(x + 1, z - 1)]
                        val sw = smoothed[heightIndex(x - 1, z + 1)]
                        val se = smoothed[heightIndex(x + 1, z + 1)]
                        temp[idx] = (4.0 * c + 2.0 * (n + s + w + e) + (nw + ne + sw + se)) / 16.0
                    }
                }
                temp.copyInto(smoothed)
            }

            for (i in heights.indices) {
                val t = clamp01(glacierMask[i] * settings.macro.glacierSmoothStrength)
                heights[i] = lerp(t, heights[i], smoothed[i])
            }
        }

        val stone = Blocks.STONE.defaultBlockState()
        val dirt = Blocks.DIRT.defaultBlockState()
        val grass = Blocks.GRASS_BLOCK.defaultBlockState()
        val snowBlock = Blocks.SNOW_BLOCK.defaultBlockState()
        val snowLayer = Blocks.SNOW.defaultBlockState()
        val water = Blocks.WATER.defaultBlockState()
        val packedIce = Blocks.PACKED_ICE.defaultBlockState()
        val blueIce = Blocks.BLUE_ICE.defaultBlockState()
        val ice = Blocks.ICE.defaultBlockState()

        // Fill blocks for the inner 16x16 columns.
        for (z in 0 until 16) {
            for (x in 0 until 16) {
                val idx = heightIndex(x + margin, z + margin)
                val hRaw = heights[idx]
                val h = Mth.clamp(hRaw.toInt(), minY, maxY - 1)

                val worldX = chunkPos.minBlockX + x
                val worldZ = chunkPos.minBlockZ + z
                val biome = biomeSource.getNoiseBiome(QuartPos.fromBlock(worldX), 0, QuartPos.fromBlock(worldZ), sampler)

                val biomeId = biome.unwrapKey().orElseThrow().identifier()
                val isPlains = biomeId == Biomes.PLAINS.identifier()
                val shouldSnowCover = !isPlains
                val topIsSnow = shouldSnowCover && h >= settings.snowLine
                val isGlacier = glacierMask[idx] > 0.65 && h >= settings.snowLine - 10
                val isRiver = riverMask[idx] > 0.55
                val isIceRiver = iceRiverMask[idx] > settings.macro.iceRiverThreshold && !isPlains
                val isForestLike = biomeId == Biomes.FOREST.identifier() || biomeId == Biomes.BIRCH_FOREST.identifier() || biomeId == Biomes.SNOWY_TAIGA.identifier() || biomeId == Biomes.GROVE.identifier()
                val isIceSpikesBiome = biomeId == Biomes.ICE_SPIKES.identifier()

                val topIsGrass = isPlains || biomeId == Biomes.MEADOW.identifier() || biomeId == Biomes.FOREST.identifier() || biomeId == Biomes.BIRCH_FOREST.identifier() || biomeId == Biomes.SNOWY_TAIGA.identifier() || biomeId == Biomes.GROVE.identifier()
                val topStateDefault = if (topIsGrass) grass else snowBlock

                // Solid terrain.
                for (y in minY until (h + 1)) {
                    val depthBelowSurface = h - y

                    val carve = settings.underground.enabled &&
                        depthBelowSurface >= settings.underground.surfaceProtection &&
                        y in settings.underground.minY..settings.underground.maxY &&
                        caveNoise.perlin(
                            (worldX + 113 * (chunkPos.x + 1)).toDouble() * settings.underground.frequency,
                            y.toDouble() * settings.underground.yFrequency,
                            (worldZ - 97 * (chunkPos.z - 1)).toDouble() * settings.underground.frequency
                        ) > settings.underground.threshold

                    if (carve) {
                        chunk.setBlockState(BlockPos(worldX, y, worldZ), Blocks.AIR.defaultBlockState(), 2)
                        continue
                    }

                    val state: BlockState = when {
                        y == h -> when {
                            isIceRiver -> if (iceRiverMask[idx] > settings.macro.iceRiverBlueIceThreshold) blueIce else packedIce
                            isGlacier -> if (glacierMask[idx] > 0.88) blueIce else packedIce
                            topIsSnow -> snowBlock
                            else -> topStateDefault
                        }
                        y >= h - 3 -> dirt
                        else -> stone
                    }
                    chunk.setBlockState(BlockPos(worldX, y, worldZ), state, 2)
                }

                // Water fill (optional lowlands).
                val riverFillTop = min(settings.seaLevel, h + 3)
                if (isRiver && riverFillTop > h) {
                    val fill = if (isIceRiver) ice else water
                    for (y in (h + 1)..riverFillTop) {
                        if (y < maxY) {
                            chunk.setBlockState(BlockPos(worldX, y, worldZ), fill, 2)
                        }
                    }
                    if (!isIceRiver && shouldSnowCover && riverFillTop + 1 < maxY) {
                        // Light snow cover around cold rivers.
                        chunk.setBlockState(BlockPos(worldX, riverFillTop + 1, worldZ), snowLayer, 2)
                    }
                } else if (h < settings.seaLevel) {
                    val fill = if (isIceRiver) ice else water
                    for (y in (h + 1)..settings.seaLevel) {
                        if (y < maxY) {
                            chunk.setBlockState(BlockPos(worldX, y, worldZ), fill, 2)
                        }
                    }
                } else if (shouldSnowCover && !topIsSnow) {
                    val y = h + 1
                    if (y in minY until maxY) {
                        if (!isGlacier && !isIceRiver) {
                            chunk.setBlockState(BlockPos(worldX, y, worldZ), snowLayer, 2)
                        }
                    }
                }

                // Ice spikes / cones: tall vertical features on glacier-ish areas.
                if ((isGlacier || isIceSpikesBiome) && glacierMask[idx] > 0.72 && !isPlains) {
                    val spikeNoise = macroNoise.perlin(worldX.toDouble() * 0.03, worldZ.toDouble() * 0.03)
                    if (spikeNoise > 0.62) {
                        val base = if (spikeNoise > 0.82) blueIce else packedIce
                        val height = (12 + ((spikeNoise - 0.62) / 0.38 * 80.0)).toInt()
                        val topY = min(maxY - 1, h + height)
                        for (yy in (h + 1)..topY) {
                            chunk.setBlockState(BlockPos(worldX, yy, worldZ), base, 2)
                        }
                    }
                }
            }
        }

        // Cache inner heights for structure placement queries.
        val innerHeights = IntArray(16 * 16)
        for (z in 0 until 16) {
            for (x in 0 until 16) {
                val idx = heightIndex(x + margin, z + margin)
                innerHeights[z * 16 + x] = Mth.clamp(heights[idx].toInt(), minY, maxY - 1)
            }
        }
        val key = net.minecraft.world.level.ChunkPos.asLong(chunkPos.x, chunkPos.z)
        heightCache[key] = innerHeights
        if (heightCache.size > 512) {
            heightCache.clear()
        }

        return java.util.concurrent.CompletableFuture.completedFuture(chunk)
    }

    override fun getBaseHeight(
        x: Int,
        z: Int,
        type: Heightmap.Types,
        level: LevelHeightAccessor,
        randomState: RandomState
    ): Int {
        val chunkX = x shr 4
        val chunkZ = z shr 4
        val key = net.minecraft.world.level.ChunkPos.asLong(chunkX, chunkZ)
        val cached = heightCache[key]
        if (cached != null) {
            val lx = x and 15
            val lz = z and 15
            return cached[lz * 16 + lx]
        }

        val computed = computeInnerHeightsForChunk(chunkX, chunkZ, randomState)
        heightCache[key] = computed
        if (heightCache.size > 512) {
            heightCache.clear()
        }
        val lx = x and 15
        val lz = z and 15
        return computed[lz * 16 + lx]
    }

    override fun getBaseColumn(
        x: Int,
        z: Int,
        level: LevelHeightAccessor,
        randomState: RandomState
    ): NoiseColumn {
        val minY = settings.minY
        val maxY = settings.minY + settings.genDepth
        val height = getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, level, randomState)
        val states = Array(maxY - minY) { idx ->
            val y = minY + idx
            when {
                y > height -> Blocks.AIR.defaultBlockState()
                y == height -> Blocks.GRASS_BLOCK.defaultBlockState()
                y >= height - 3 -> Blocks.DIRT.defaultBlockState()
                else -> Blocks.STONE.defaultBlockState()
            }
        }
        return NoiseColumn(minY, states)
    }

    override fun addDebugScreenInfo(strings: MutableList<String>, randomState: RandomState, pos: BlockPos) {
        strings.add("POW fractal_surface")
    }

    companion object {
        val CODEC: MapCodec<FractalSurfaceChunkGenerator> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                net.minecraft.world.level.biome.BiomeSource.CODEC.fieldOf("biome_source").forGetter { it.biomeSource },
                FractalSurfaceSettings.CODEC.fieldOf("settings").forGetter { it.settings }
            ).apply(instance, ::FractalSurfaceChunkGenerator)
        }
    }
}

