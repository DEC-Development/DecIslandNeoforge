package com.dec.decisland.block.custom

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.BushBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class SimplePlantBlock(
    properties: BlockBehaviour.Properties,
    private val placeOn: (BlockState) -> Boolean,
    selectionWidth: Double,
    selectionHeight: Double,
) : BushBlock(properties) {
    private val shape: VoxelShape = centeredShape(selectionWidth, selectionHeight)

    override fun mayPlaceOn(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
    ): Boolean = placeOn(state)

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = shape.move(state.getOffset(pos))

    companion object {
        private fun centeredShape(
            width: Double,
            height: Double,
        ): VoxelShape {
            val min = (16.0 - width) / 2.0
            val max = min + width
            return Block.box(min, 0.0, min, max, height, max)
        }
    }
}
