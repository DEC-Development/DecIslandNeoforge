package com.dec.decisland.world.portal;

import com.dec.decisland.block.ModBlocks;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.BlockUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;

public final class SnowPortalShape {
    public static final int MAX_SIZE = 21;
    private static final int MIN_VERTICAL_SIZE = 2;
    private static final int MIN_HORIZONTAL_SIZE = 2;

    private final BlockPos lowerCorner;
    private final Direction.Axis axis;
    private final Direction.Axis axis1;
    private final Direction.Axis axis2;
    private final int axis1Size;
    private final int axis2Size;
    private final int portalBlocks;

    private SnowPortalShape(BlockPos lowerCorner, Direction.Axis axis, Direction.Axis axis1, Direction.Axis axis2, int axis1Size, int axis2Size, int portalBlocks) {
        this.lowerCorner = lowerCorner;
        this.axis = axis;
        this.axis1 = axis1;
        this.axis2 = axis2;
        this.axis1Size = axis1Size;
        this.axis2Size = axis2Size;
        this.portalBlocks = portalBlocks;
    }

    public BlockPos lowerCorner() {
        return this.lowerCorner;
    }

    public Direction.Axis axis() {
        return this.axis;
    }

    public Direction.Axis axis1() {
        return this.axis1;
    }

    public Direction.Axis axis2() {
        return this.axis2;
    }

    public int axis1Size() {
        return this.axis1Size;
    }

    public int axis2Size() {
        return this.axis2Size;
    }

    public boolean isComplete() {
        return this.portalBlocks == this.axis1Size * this.axis2Size;
    }

    public BlockUtil.FoundRectangle asRectangle() {
        return new BlockUtil.FoundRectangle(this.lowerCorner, this.axis1Size, this.axis2Size);
    }

    public void light(LevelAccessor level) {
        BlockState portalState = ModBlocks.SNOW_PORTAL.get().defaultBlockState().setValue(com.dec.decisland.block.custom.SnowPortalBlock.AXIS, this.axis);
        for (int a = 0; a < this.axis1Size; a++) {
            for (int b = 0; b < this.axis2Size; b++) {
                level.setBlock(this.offset(a, b), portalState, 18);
            }
        }
    }

    public Vec3 getRelativePosition(Entity entity) {
        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        if (this.axis == Direction.Axis.Y) {
            double width = Math.max(0.0, this.axis1Size - dimensions.width());
            double depth = Math.max(0.0, this.axis2Size - dimensions.width());
            double x = width > 0.0 ? Mth.clamp(Mth.inverseLerp(entity.getX(), this.lowerCorner.getX(), this.lowerCorner.getX() + width), 0.0, 1.0) : 0.5;
            double z = depth > 0.0 ? Mth.clamp(Mth.inverseLerp(entity.getZ(), this.lowerCorner.getZ(), this.lowerCorner.getZ() + depth), 0.0, 1.0) : 0.5;
            return new Vec3(x, 0.0, z);
        }
        return PortalShape.getRelativePosition(this.asRectangle(), this.axis, entity.position(), dimensions);
    }

    public Vec3 getTeleportPos(ServerLevel level, Entity entity, Vec3 relativePos, Direction.Axis sourceAxis) {
        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        if (this.axis == Direction.Axis.Y) {
            double width = Math.max(0.0, this.axis1Size - dimensions.width());
            double depth = Math.max(0.0, this.axis2Size - dimensions.width());
            double x = Mth.lerp(relativePos.x, this.lowerCorner.getX(), this.lowerCorner.getX() + width);
            double z = Mth.lerp(relativePos.z, this.lowerCorner.getZ(), this.lowerCorner.getZ() + depth);
            double y = this.lowerCorner.getY() + 0.125;
            return PortalShape.findCollisionFreePosition(new Vec3(x, y, z), level, entity, dimensions);
        }

        BlockState blockState = level.getBlockState(this.lowerCorner);
        Direction.Axis destAxis = blockState.getOptionalValue(com.dec.decisland.block.custom.SnowPortalBlock.AXIS).orElse(Direction.Axis.X);
        double width = this.axis1Size;
        double height = this.axis2Size;
        double localWidth = dimensions.width() / 2.0 + (width - dimensions.width()) * relativePos.x();
        double localHeight = (height - dimensions.height()) * relativePos.y();
        double normalOffset = 0.5 + relativePos.z();
        boolean xAxis = destAxis == Direction.Axis.X;
        Vec3 pos = new Vec3(
            this.lowerCorner.getX() + (xAxis ? localWidth : normalOffset),
            this.lowerCorner.getY() + localHeight,
            this.lowerCorner.getZ() + (xAxis ? normalOffset : localWidth)
        );
        return PortalShape.findCollisionFreePosition(pos, level, entity, dimensions);
    }

    private BlockPos offset(int axis1Offset, int axis2Offset) {
        int x = this.lowerCorner.getX();
        int y = this.lowerCorner.getY();
        int z = this.lowerCorner.getZ();
        x += this.axis1 == Direction.Axis.X ? axis1Offset : 0;
        y += this.axis1 == Direction.Axis.Y ? axis1Offset : 0;
        z += this.axis1 == Direction.Axis.Z ? axis1Offset : 0;
        x += this.axis2 == Direction.Axis.X ? axis2Offset : 0;
        y += this.axis2 == Direction.Axis.Y ? axis2Offset : 0;
        z += this.axis2 == Direction.Axis.Z ? axis2Offset : 0;
        return new BlockPos(x, y, z);
    }

    public static Optional<SnowPortalShape> find(BlockGetter level, BlockPos pos, Direction.Axis axis) {
        return axis == Direction.Axis.Y ? findHorizontal(level, pos) : findVertical(level, pos, axis);
    }

    public static Optional<SnowPortalShape> findAny(BlockGetter level, BlockPos pos) {
        for (Direction.Axis axis : new Direction.Axis[] {Direction.Axis.X, Direction.Axis.Z, Direction.Axis.Y}) {
            Optional<SnowPortalShape> shape = find(level, pos, axis);
            if (shape.isPresent()) {
                return shape;
            }
        }
        return Optional.empty();
    }

    public static boolean tryCreatePortal(Level level, BlockPos origin) {
        for (BlockPos check : BlockPos.betweenClosed(origin.offset(-2, -2, -2), origin.offset(2, 2, 2))) {
            for (Direction.Axis axis : new Direction.Axis[] {Direction.Axis.X, Direction.Axis.Z, Direction.Axis.Y}) {
                Optional<SnowPortalShape> shape = find(level, check, axis);
                if (shape.isPresent() && !shape.get().isComplete()) {
                    shape.get().light(level);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean canFitMinimal(Level level, BlockPos lowerCorner, Direction.Axis axis) {
        if (axis == Direction.Axis.Y) {
            for (int x = -1; x <= 2; x++) {
                for (int z = -1; z <= 2; z++) {
                    BlockPos pos = lowerCorner.offset(x, 0, z);
                    boolean edge = x == -1 || x == 2 || z == -1 || z == 2;
                    if (edge) {
                        if (!canReplaceForCreation(level.getBlockState(pos))) {
                            return false;
                        }
                    } else {
                        if (!isInside(level.getBlockState(pos))) {
                            return false;
                        }
                        if (!isClearAbove(level.getBlockState(pos.above())) || !isClearAbove(level.getBlockState(pos.above(2)))) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        Direction widthDir = axis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
        for (int y = -1; y <= 2; y++) {
            for (int x = -1; x <= 2; x++) {
                BlockPos pos = lowerCorner.relative(widthDir, x).above(y);
                boolean edge = y == -1 || y == 2 || x == -1 || x == 2;
                if (edge) {
                    if (!canReplaceForCreation(level.getBlockState(pos))) {
                        return false;
                    }
                } else if (!isInside(level.getBlockState(pos))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Optional<SnowPortalShape> createMinimalPortal(Level level, BlockPos lowerCorner, Direction.Axis axis) {
        if (!canFitMinimal(level, lowerCorner, axis)) {
            return Optional.empty();
        }

        BlockState frame = Blocks.SNOW_BLOCK.defaultBlockState();
        if (axis == Direction.Axis.Y) {
            for (int x = -1; x <= 2; x++) {
                level.setBlockAndUpdate(lowerCorner.offset(x, 0, -1), frame);
                level.setBlockAndUpdate(lowerCorner.offset(x, 0, 2), frame);
            }
            for (int z = -1; z <= 2; z++) {
                level.setBlockAndUpdate(lowerCorner.offset(-1, 0, z), frame);
                level.setBlockAndUpdate(lowerCorner.offset(2, 0, z), frame);
            }
            for (int x = 0; x < 2; x++) {
                for (int z = 0; z < 2; z++) {
                    clearSolid(level, lowerCorner.offset(x, 1, z));
                    clearSolid(level, lowerCorner.offset(x, 2, z));
                    if (!level.getBlockState(lowerCorner.offset(x, -1, z)).isSolid()) {
                        level.setBlockAndUpdate(lowerCorner.offset(x, -1, z), frame);
                    }
                }
            }
        } else {
            Direction widthDir = axis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
            Direction sideDir = widthDir.getOpposite();
            for (int y = -1; y <= 2; y++) {
                level.setBlockAndUpdate(lowerCorner.relative(sideDir).above(y), frame);
                level.setBlockAndUpdate(lowerCorner.relative(widthDir, 2).above(y), frame);
            }
            for (int x = -1; x <= 2; x++) {
                level.setBlockAndUpdate(lowerCorner.relative(widthDir, x).below(), frame);
                level.setBlockAndUpdate(lowerCorner.relative(widthDir, x).above(2), frame);
            }
        }

        Optional<SnowPortalShape> shape = find(level, lowerCorner, axis);
        shape.ifPresent(found -> found.light(level));
        return shape;
    }

    private static Optional<SnowPortalShape> findVertical(BlockGetter level, BlockPos pos, Direction.Axis axis) {
        Direction widthDir = axis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
        Direction negativeWidth = widthDir.getOpposite();
        BlockPos cursor = pos;
        while (cursor.getY() > level.getMinY() && isInside(level.getBlockState(cursor.below()))) {
            cursor = cursor.below();
        }
        for (int i = 0; i < MAX_SIZE; i++) {
            BlockPos next = cursor.relative(negativeWidth);
            if (!isInside(level.getBlockState(next)) || !isFrame(level, next.below())) {
                break;
            }
            cursor = next;
        }

        int width = 0;
        while (width < MAX_SIZE) {
            BlockPos current = cursor.relative(widthDir, width);
            if (!isInside(level.getBlockState(current)) || !isFrame(level, current.below())) {
                break;
            }
            width++;
        }
        if (width < MIN_VERTICAL_SIZE || !isFrame(level, cursor.relative(widthDir, width))) {
            return Optional.empty();
        }

        int portalBlocks = 0;
        int height = 0;
        while (height < MAX_SIZE) {
            if (!isFrame(level, cursor.relative(negativeWidth).above(height)) || !isFrame(level, cursor.relative(widthDir, width).above(height))) {
                break;
            }
            boolean rowValid = true;
            for (int x = 0; x < width; x++) {
                BlockPos check = cursor.relative(widthDir, x).above(height);
                BlockState state = level.getBlockState(check);
                if (!isInside(state)) {
                    rowValid = false;
                    break;
                }
                if (isPortal(state)) {
                    portalBlocks++;
                }
            }
            if (!rowValid) {
                break;
            }
            height++;
        }

        if (height < MIN_VERTICAL_SIZE) {
            return Optional.empty();
        }
        for (int x = 0; x < width; x++) {
            if (!isFrame(level, cursor.relative(widthDir, x).above(height))) {
                return Optional.empty();
            }
        }
        return Optional.of(new SnowPortalShape(cursor, axis, axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X, Direction.Axis.Y, width, height, portalBlocks));
    }

    private static Optional<SnowPortalShape> findHorizontal(BlockGetter level, BlockPos pos) {
        BlockPos cursor = pos;
        while (isInside(level.getBlockState(cursor.west())) && hasHorizontalFloor(level, cursor.west())) {
            cursor = cursor.west();
        }
        while (isInside(level.getBlockState(cursor.north())) && hasHorizontalFloor(level, cursor.north())) {
            cursor = cursor.north();
        }

        int width = 0;
        while (width < MAX_SIZE && isInside(level.getBlockState(cursor.east(width))) && hasHorizontalFloor(level, cursor.east(width))) {
            width++;
        }
        int depth = 0;
        while (depth < MAX_SIZE && isInside(level.getBlockState(cursor.south(depth))) && hasHorizontalFloor(level, cursor.south(depth))) {
            depth++;
        }
        if (width < MIN_HORIZONTAL_SIZE || depth < MIN_HORIZONTAL_SIZE) {
            return Optional.empty();
        }

        int portalBlocks = 0;
        for (int x = -1; x <= width; x++) {
            for (int z = -1; z <= depth; z++) {
                BlockPos check = cursor.offset(x, 0, z);
                boolean edge = x == -1 || x == width || z == -1 || z == depth;
                if (edge) {
                    if (!isFrame(level, check)) {
                        return Optional.empty();
                    }
                } else {
                    BlockState state = level.getBlockState(check);
                    if (!isInside(state) || !hasHorizontalFloor(level, check) || !isClearAbove(level.getBlockState(check.above())) || !isClearAbove(level.getBlockState(check.above(2)))) {
                        return Optional.empty();
                    }
                    if (isPortal(state)) {
                        portalBlocks++;
                    }
                }
            }
        }

        return Optional.of(new SnowPortalShape(cursor, Direction.Axis.Y, Direction.Axis.X, Direction.Axis.Z, width, depth, portalBlocks));
    }

    private static boolean hasHorizontalFloor(BlockGetter level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState state = level.getBlockState(below);
        return !isInside(state) && (state.isSolid() || state.isRedstoneConductor(level, below));
    }

    private static boolean isFrame(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.SNOW_BLOCK);
    }

    private static boolean isPortal(BlockState state) {
        return state.is(ModBlocks.SNOW_PORTAL.get());
    }

    private static boolean isInside(BlockState state) {
        return isPortal(state) || (state.canBeReplaced() && state.getFluidState().isEmpty());
    }

    private static boolean isClearAbove(BlockState state) {
        return state.canBeReplaced() && state.getFluidState().isEmpty();
    }

    private static boolean canReplaceForCreation(BlockState state) {
        return state.is(Blocks.SNOW_BLOCK) || isPortal(state) || (state.canBeReplaced() && state.getFluidState().isEmpty());
    }

    private static void clearSolid(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if ((state.isSolid() || state.isRedstoneConductor(level, pos)) && !state.is(ModBlocks.SNOW_PORTAL.get())) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 16);
        }
    }
}
