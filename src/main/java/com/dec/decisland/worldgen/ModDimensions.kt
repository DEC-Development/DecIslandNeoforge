package com.dec.decisland.worldgen

import com.dec.decisland.DecIsland
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.dimension.DimensionType

object ModDimensions {
    @JvmField
    val VOID_LEVEL: ResourceKey<Level> =
        ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "void"))

    @JvmField
    val VOID_TYPE: ResourceKey<DimensionType> =
        ResourceKey.create(Registries.DIMENSION_TYPE, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "void"))
}
