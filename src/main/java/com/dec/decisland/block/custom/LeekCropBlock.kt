package com.dec.decisland.block.custom

import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Supplier

class LeekCropBlock(
    properties: BlockBehaviour.Properties,
    seedItem: Supplier<out ItemLike>,
) : SimpleCropBlock(properties, seedItem, AGE_TO_STAGE, SHAPES, HARVEST_RESET_AGE) {
    companion object {
        private const val HARVEST_RESET_AGE: Int = 0

        private val AGE_TO_STAGE: IntArray = intArrayOf(0, 0, 1, 1, 1, 2, 2, 2)

        private val SHAPES: Array<VoxelShape> = arrayOf(
            cropShape(10.0, 4.0),
            cropShape(10.0, 7.0),
            cropShape(16.0, 12.0),
        )

        private fun cropShape(
            width: Double,
            height: Double,
        ): VoxelShape {
            val min = (16.0 - width) / 2.0
            val max = min + width
            return Block.box(min, 0.0, min, max, height, max)
        }
    }
}
