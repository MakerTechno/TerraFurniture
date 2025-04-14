package org.confluence.terra_furniture.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.confluence.terra_furniture.common.block.func.BasePropertyHorizontalDirectionBlock;
import org.jetbrains.annotations.NotNull;

public class SinkBlock extends BasePropertyHorizontalDirectionBlock<SinkBlock> {
    protected static final VoxelShape SINK_X;
    protected static final VoxelShape SINK_Z;
    protected static final VoxelShape PIPE;

    public SinkBlock(BlockState state, Properties pProperties) {
        super(state, pProperties);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> Shapes.or(SINK_X.move(0, 0, 3 / 16.0), PIPE.move(0, 0, 6 / 16.0));
            case SOUTH -> Shapes.or(SINK_X.move(0, 0, -3 / 16.0), PIPE.move(0, 0, -6 / 16.0));
            case WEST -> Shapes.or(SINK_Z.move(3 / 16.0, 0, 0), PIPE.move(6 / 16.0, 0, 0));
            case EAST -> Shapes.or(SINK_Z.move(-3 / 16.0, 0, 0), PIPE.move(-6 / 16.0, 0, 0));
            default -> Shapes.or(SINK_X, PIPE);
        };
    }

    protected @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        Direction direction = state.getValue(FACING);
        switch (mirror) {
            case LEFT_RIGHT -> {
                if (direction.getAxis() == Direction.Axis.Z) {
                    return state.rotate(Rotation.CLOCKWISE_180);
                }
            }
            case FRONT_BACK -> {
                if (direction.getAxis() == Direction.Axis.X) {
                    return state.rotate(Rotation.CLOCKWISE_180);
                }
            }
        }
        return super.mirror(state, mirror);
    }

    @Override
    protected BasePropertyHorizontalDirectionBlock<SinkBlock> createNewInstance(BlockState baseState, Properties properties) {
        return new SinkBlock(baseState, properties);
    }

    static {
        SINK_X = Block.box(1.0, 10.0, 3.0, 15.0, 15.0, 13.0);
        SINK_Z = Block.box(3.0, 10.0, 1.0, 13.0, 15.0, 15.0);
        PIPE = Block.box(7.0, 0.0, 7.0, 9.0, 10.0, 9.0);
    }

    @Override
    public String getSpecificName() {
        return "";
    }

    @Override
    public String parentName() {
        return "";
    }
}
