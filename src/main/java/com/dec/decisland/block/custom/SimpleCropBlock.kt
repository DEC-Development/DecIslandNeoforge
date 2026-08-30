package com.dec.decisland.block.custom

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Supplier

abstract class SimpleCropBlock(
    properties: BlockBehaviour.Properties,
    private val seedItem: Supplier<out ItemLike>,
    private val ageToStage: IntArray,
    private val stageShapes: Array<VoxelShape>,
    private val harvestResetAge: Int? = null,
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

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (canHarvest(state)) {
            return harvest(state, level, pos, player)
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult)
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (canHarvest(state)) {
            return harvest(state, level, pos, player)
        }
        return super.useWithoutItem(state, level, pos, player, hitResult)
    }

    private fun canHarvest(state: BlockState): Boolean = harvestResetAge != null && getAge(state) >= MAX_AGE

    private fun harvest(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ): InteractionResult {
        val resetAge = harvestResetAge ?: return InteractionResult.PASS
        if (level is ServerLevel) {
            for (drop in getDrops(state, level, pos, null)) {
                popResource(level, pos, drop)
            }
            level.playSound(
                null,
                pos,
                SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                SoundSource.BLOCKS,
                1.0f,
                0.8f + level.random.nextFloat() * 0.4f,
            )
            val newState = state.setValue(AGE, resetAge)
            level.setBlock(pos, newState, UPDATE_CLIENTS)
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState))
        }
        return InteractionResult.SUCCESS
    }

    private fun stageForAge(age: Int): Int {
        val stage = ageToStage[age]
        return stage.coerceIn(0, stageShapes.lastIndex)
    }

    companion object {
        private const val MAX_AGE: Int = 7
    }
}
