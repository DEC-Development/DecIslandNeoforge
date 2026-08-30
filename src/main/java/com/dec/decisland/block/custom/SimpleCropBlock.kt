package com.dec.decisland.block.custom

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Supplier

abstract class SimpleCropBlock(
    properties: BlockBehaviour.Properties,
    private val seedItem: Supplier<out ItemLike>,
    private val ageToStage: IntArray,
    private val stageShapes: Array<VoxelShape>,
) : CropBlock(properties) {
    init {
        require(ageToStage.size == MAX_AGE + 1) {
            "Expected ${MAX_AGE + 1} age entries, got ${ageToStage.size}"
        }
        require(stageShapes.isNotEmpty()) {
            "Crop needs at least one stage shape"
        }
    }

    override fun getBaseSeedId(): ItemLike = seedItem.get()

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = stageShapes[stageForAge(getAge(state))]

    private fun stageForAge(age: Int): Int {
        val stage = ageToStage[age]
        return stage.coerceIn(0, stageShapes.lastIndex)
    }

    companion object {
        private const val MAX_AGE: Int = 7
    }
}
