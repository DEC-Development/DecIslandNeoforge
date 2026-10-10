package com.dec.decisland.block.custom

import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.level.BlockGetter
import net.minecraft.core.BlockPos

/** A shaped block whose model follows the horizontal direction used at placement. */
class HorizontalShapedBlock(
    properties: BlockBehaviour.Properties,
    private val shape: VoxelShape,
) : Block(properties) {
    companion object {
        val FACING = BlockStateProperties.HORIZONTAL_FACING
    }

    init {
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.SOUTH))
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState =
        defaultBlockState().setValue(FACING, context.horizontalDirection.opposite)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape = rotatedShape(state)

    override fun getCollisionShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape = rotatedShape(state)

    private fun rotatedShape(state: BlockState): VoxelShape {
        val turns = when (state.getValue(FACING)) {
            Direction.SOUTH -> 0
            Direction.WEST -> 1
            Direction.NORTH -> 2
            Direction.EAST -> 3
            else -> 0
        }
        if (turns == 0) return shape

        var result = Shapes.empty()
        shape.toAabbs().forEach { original ->
            var box = original
            repeat(turns) {
                box = AABB(
                    1.0 - box.maxZ, box.minY, box.minX,
                    1.0 - box.minZ, box.maxY, box.maxX,
                )
            }
            result = Shapes.or(result, Shapes.box(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ))
        }
        return result
    }
}
