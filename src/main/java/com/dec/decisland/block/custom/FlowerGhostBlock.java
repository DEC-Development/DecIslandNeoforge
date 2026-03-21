package com.dec.decisland.block.custom;

import com.dec.decisland.DecIsland;
import com.dec.decisland.network.Networking;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FlowerGhostBlock extends Block {
    private static final int MIN_LIFETIME_TICKS = 20;
    private static final int MAX_LIFETIME_TICKS = 40;
    private static final Identifier DISAPPEAR_PARTICLE = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "flower_ghost_block_particle");

    public FlowerGhostBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide() && !state.is(oldState.getBlock())) {
            int lifetime = MIN_LIFETIME_TICKS + level.getRandom().nextInt(MAX_LIFETIME_TICKS - MIN_LIFETIME_TICKS + 1);
            level.scheduleTick(pos, this, lifetime);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.getBlockState(pos).is(this)) {
            return;
        }
        level.setBlockAndUpdate(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState());
        Networking.sendBedrockEmitterToNearby(level, DISAPPEAR_PARTICLE, Vec3.atCenterOf(pos), 64.0, 6);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return ItemStack.EMPTY;
    }
}
