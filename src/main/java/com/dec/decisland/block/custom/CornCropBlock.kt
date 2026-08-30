package com.dec.decisland.block.custom

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.DoublePlantBlock
import net.minecraft.world.level.block.FarmBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.common.CommonHooks

class CornCropBlock(properties: BlockBehaviour.Properties) : DoublePlantBlock(properties), BonemealableBlock {
    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(HARVESTABLE, false),
        )
    }

    override fun codec(): MapCodec<CornCropBlock> = CODEC

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState = defaultBlockState()

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(AGE, HARVESTABLE)
        super.createBlockStateDefinition(builder)
    }

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = when (state.getValue(HALF)) {
        DoubleBlockHalf.LOWER -> LOWER_SHAPES[state.getValue(AGE)]
        DoubleBlockHalf.UPPER -> UPPER_SHAPES[state.getValue(AGE).coerceAtMost(UPPER_MAX_AGE)]
    }

    override fun canSurvive(
        state: BlockState,
        level: LevelReader,
        pos: BlockPos,
    ): Boolean {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return super.canSurvive(state, level, pos)
        }
        val soilDecision = level.getBlockState(pos.below()).canSustainPlant(level, pos.below(), Direction.UP, state)
        if (!soilDecision.isDefault()) return soilDecision.isTrue()
        return CropBlock.hasSufficientLight(level, pos) && super.canSurvive(state, level, pos)
    }

    override fun mayPlaceOn(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
    ): Boolean = state.`is`(Blocks.FARMLAND)

    override fun updateShape(
        state: BlockState,
        level: LevelReader,
        tickAccess: ScheduledTickAccess,
        pos: BlockPos,
        direction: Direction,
        neighborPos: BlockPos,
        neighborState: BlockState,
        random: RandomSource,
    ): BlockState {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return super.updateShape(state, level, tickAccess, pos, direction, neighborPos, neighborState, random)
        }
        return if (state.canSurvive(level, pos)) state else Blocks.AIR.defaultBlockState()
    }

    override fun isRandomlyTicking(state: BlockState): Boolean = when (state.getValue(HALF)) {
        DoubleBlockHalf.LOWER -> state.getValue(AGE) < LOWER_MAX_AGE
        DoubleBlockHalf.UPPER -> state.getValue(AGE) < UPPER_MAX_AGE
    }

    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        if (!level.isAreaLoaded(pos, 1)) return
        if (level.getRawBrightness(pos, 0) < 9) return
        if (!canGrowStep(level, state, pos)) return
        val growthSpeed = getGrowthSpeed(state, level, pos)
        if (!CommonHooks.canCropGrow(level, pos, state, random.nextInt((25.0f / growthSpeed).toInt() + 1) == 0)) return
        growStep(level, state, pos)
        CommonHooks.fireCropGrowPost(level, pos, state)
    }

    override fun isValidBonemealTarget(
        level: LevelReader,
        pos: BlockPos,
        state: BlockState,
    ): Boolean = canGrowStep(level, state, pos)

    override fun isBonemealSuccess(
        level: Level,
        random: RandomSource,
        pos: BlockPos,
        state: BlockState,
    ): Boolean = true

    override fun performBonemeal(
        level: ServerLevel,
        random: RandomSource,
        pos: BlockPos,
        state: BlockState,
    ) {
        growStep(level, state, pos)
    }

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

    private fun canGrowStep(
        level: LevelReader,
        state: BlockState,
        pos: BlockPos,
    ): Boolean = when (state.getValue(HALF)) {
        DoubleBlockHalf.LOWER -> {
            val age = state.getValue(AGE)
            when {
                age < SPAWN_UPPER_AGE -> true
                age == SPAWN_UPPER_AGE -> state.getValue(HARVESTABLE) || level.getBlockState(pos.above()).isAir
                else -> false
            }
        }
        DoubleBlockHalf.UPPER -> state.getValue(AGE) < UPPER_MAX_AGE
    }

    private fun growStep(
        level: ServerLevel,
        state: BlockState,
        pos: BlockPos,
    ) {
        val age = state.getValue(AGE)
        when (state.getValue(HALF)) {
            DoubleBlockHalf.LOWER -> when {
                age < SPAWN_UPPER_AGE -> level.setBlock(pos, state.setValue(AGE, age + 1), UPDATE_CLIENTS)
                !state.getValue(HARVESTABLE) -> spawnUpper(level, state, pos)
                age < LOWER_MAX_AGE -> level.setBlock(pos, state.setValue(AGE, age + 1), UPDATE_CLIENTS)
                else -> {}
            }
            DoubleBlockHalf.UPPER -> if (age < UPPER_MAX_AGE) {
                level.setBlock(pos, state.setValue(AGE, age + 1), UPDATE_CLIENTS)
            }
        }
    }

    private fun spawnUpper(
        level: ServerLevel,
        state: BlockState,
        pos: BlockPos,
    ) {
        val abovePos = pos.above()
        if (!level.getBlockState(abovePos).isAir) return
        val upperState = defaultBlockState()
            .setValue(HALF, DoubleBlockHalf.UPPER)
            .setValue(HARVESTABLE, true)
        level.setBlock(abovePos, upperState, UPDATE_ALL)
        val placedState = level.getBlockState(abovePos)
        if (placedState.`is`(this) && placedState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            level.setBlock(pos, state.setValue(HARVESTABLE, true), UPDATE_CLIENTS)
        }
    }

    private fun canHarvest(state: BlockState): Boolean = when (state.getValue(HALF)) {
        DoubleBlockHalf.LOWER -> state.getValue(AGE) >= LOWER_MAX_AGE
        DoubleBlockHalf.UPPER -> state.getValue(AGE) >= UPPER_MAX_AGE
    }

    private fun harvest(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
    ): InteractionResult {
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
            val resetAge =
                if (state.getValue(HALF) == DoubleBlockHalf.LOWER) LOWER_HARVEST_RESET_AGE else UPPER_HARVEST_RESET_AGE
            val newState = state.setValue(AGE, resetAge)
            level.setBlock(pos, newState, UPDATE_CLIENTS)
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState))
        }
        return InteractionResult.SUCCESS
    }

    companion object {
        val CODEC: MapCodec<CornCropBlock> = simpleCodec(::CornCropBlock)
        val AGE: IntegerProperty = BlockStateProperties.AGE_4
        val HARVESTABLE: BooleanProperty = BooleanProperty.create("harvestable")

        const val LOWER_MAX_AGE: Int = 4
        const val UPPER_MAX_AGE: Int = 3
        private const val SPAWN_UPPER_AGE: Int = 3
        private const val LOWER_HARVEST_RESET_AGE: Int = 3
        private const val UPPER_HARVEST_RESET_AGE: Int = 2

        private val LOWER_SHAPES: Array<VoxelShape> = arrayOf(
            Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0),
            Block.box(4.5, 0.0, 4.5, 11.5, 9.0, 11.5),
            Block.box(4.0, 0.0, 4.0, 12.0, 14.0, 12.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0),
        )

        private val UPPER_SHAPES: Array<VoxelShape> = arrayOf(
            Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 14.0, 13.0),
            Block.box(2.5, 0.0, 2.5, 13.5, 13.0, 13.5),
            Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0),
        )

        private fun getGrowthSpeed(
            state: BlockState,
            level: BlockGetter,
            pos: BlockPos,
        ): Float {
            var speed = 1.0f
            val soilBase = pos.below()
            for (dx in -1..1) {
                for (dz in -1..1) {
                    var soilBonus = 0.0f
                    val soilPos = soilBase.offset(dx, 0, dz)
                    val soilState = level.getBlockState(soilPos)
                    val soilDecision = soilState.canSustainPlant(level, soilPos, Direction.UP, state)
                    val canSustain =
                        if (soilDecision.isDefault()) soilState.block is FarmBlock else soilDecision.isTrue()
                    if (canSustain) {
                        soilBonus = 1.0f
                        if (soilState.isFertile(level, pos.offset(dx, 0, dz))) {
                            soilBonus = 3.0f
                        }
                    }
                    if (dx != 0 || dz != 0) {
                        soilBonus /= 4.0f
                    }
                    speed += soilBonus
                }
            }
            val north = pos.north()
            val south = pos.south()
            val west = pos.west()
            val east = pos.east()
            val blockedHorizontal =
                level.getBlockState(west).`is`(state.block) || level.getBlockState(east).`is`(state.block)
            val blockedVertical =
                level.getBlockState(north).`is`(state.block) || level.getBlockState(south).`is`(state.block)
            if (blockedHorizontal && blockedVertical) {
                speed /= 2.0f
            } else {
                val blockedDiagonal =
                    level.getBlockState(west.north()).`is`(state.block) ||
                        level.getBlockState(east.north()).`is`(state.block) ||
                        level.getBlockState(east.south()).`is`(state.block) ||
                        level.getBlockState(west.south()).`is`(state.block)
                if (blockedDiagonal) {
                    speed /= 2.0f
                }
            }
            return speed
        }
    }
}
