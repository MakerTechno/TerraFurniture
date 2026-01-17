package org.confluence.terra_furniture.common.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 部分失去独立类的方块的碰撞箱信息
 */
public enum BlockShapeType {
    LAMP, CANDLE, LANTERN, DEFAULT;
    public static final VoxelShape
        LAMP_SHAPE = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0),
        CANDLE_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 7.0, 10.0),
        LANTERN_SHAPE = Block.box(5.0, 3.0, 5.0, 11.0, 11.0, 11.0);
    public VoxelShape getShape() {
        return switch (this) {
            case LAMP -> LAMP_SHAPE;
            case CANDLE -> CANDLE_SHAPE;
            case LANTERN -> LANTERN_SHAPE;
            case DEFAULT -> Shapes.block();
        };
    }
    public boolean isSupported(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos suspendingPos = pos.above();
        BlockPos groundingPos = pos.below();
        return switch (this) {
            case LANTERN -> level.getBlockState(suspendingPos).isFaceSturdy(level, suspendingPos, Direction.DOWN);
            case CANDLE, LAMP -> level.getBlockState(groundingPos).isFaceSturdy(level, groundingPos, Direction.UP);
            case DEFAULT -> true;
        };
    }
}
