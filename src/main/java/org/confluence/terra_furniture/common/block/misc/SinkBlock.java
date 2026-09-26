package org.confluence.terra_furniture.common.block.misc;

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
import org.confluence.terra_furniture.common.block.func.set.TFBlockSetType;
import org.confluence.terra_furniture.common.datagen.empowered.BlockDataGenerator;
import org.confluence.terra_furniture.common.init.TFBlockSetTypes;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SinkBlock extends BasePropertyHorizontalDirectionBlock<SinkBlock> {
    protected static final VoxelShape SINK_X;
    protected static final VoxelShape SINK_Z;
    protected static final VoxelShape PIPE;
    protected static final VoxelShape IRON_BASE;
    protected static final VoxelShape SPRUCE_BASE;

    public SinkBlock(TFBlockSetType type, BlockState state, Consumer<Properties> extraProperties) {
        super(type, state, extraProperties);
    }

    public SinkBlock(TFBlockSetType type, BlockState state, Properties properties) {
        super(type, state, properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (getType().equals(TFBlockSetTypes.IRON)) {
            return modelFittedShape(IRON_BASE, state.getValue(FACING));
        }
        if (getType().equals(TFBlockSetTypes.SPRUCE)) {
            return modelFittedShape(SPRUCE_BASE, state.getValue(FACING));
        }
        return switch (state.getValue(FACING)) {
            case NORTH -> Shapes.or(SINK_X.move(0, 0, 3 / 16.0), PIPE.move(0, 0, 6 / 16.0));
            case SOUTH -> Shapes.or(SINK_X.move(0, 0, -3 / 16.0), PIPE.move(0, 0, -6 / 16.0));
            case WEST -> Shapes.or(SINK_Z.move(3 / 16.0, 0, 0), PIPE.move(6 / 16.0, 0, 0));
            case EAST -> Shapes.or(SINK_Z.move(-3 / 16.0, 0, 0), PIPE.move(-6 / 16.0, 0, 0));
            default -> Shapes.or(SINK_X, PIPE);
        };
    }

    private static VoxelShape modelFittedShape(VoxelShape base, Direction facing) {
        VoxelShape upright;
        VoxelShape spout;
        switch (facing) {
            case SOUTH -> {
                upright = Block.box(7, 14, 1, 9, 19, 3);
                spout = Block.box(7, 16, 3, 9, 19, 7);
            }
            case WEST -> {
                upright = Block.box(13, 14, 7, 15, 19, 9);
                spout = Block.box(9, 16, 7, 13, 19, 9);
            }
            case EAST -> {
                upright = Block.box(1, 14, 7, 3, 19, 9);
                spout = Block.box(3, 16, 7, 7, 19, 9);
            }
            default -> {
                upright = Block.box(7, 14, 13, 9, 19, 15);
                spout = Block.box(7, 16, 9, 9, 19, 13);
            }
        }
        return Shapes.or(base, upright, spout);
    }

    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror) {
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
        return new SinkBlock(getType(), baseState, properties);
    }

    static {
        SINK_X = Block.box(1.0, 10.0, 3.0, 15.0, 15.0, 13.0);
        SINK_Z = Block.box(3.0, 10.0, 1.0, 13.0, 15.0, 15.0);
        PIPE = Block.box(7.0, 0.0, 7.0, 9.0, 10.0, 9.0);
        IRON_BASE = Shapes.or(
                Block.box(4, 0, 4, 12, 2, 12),
                Block.box(6, 0, 6, 10, 9, 10),
                Block.box(0, 9, 0, 16, 14, 16)
        );
        SPRUCE_BASE = Shapes.or(
                Block.box(0, 0, 0, 16, 10, 16),
                Block.box(0, 10, 0, 16, 14, 1),
                Block.box(0, 10, 15, 16, 14, 16),
                Block.box(0, 10, 1, 1, 14, 15),
                Block.box(15, 10, 1, 16, 14, 15)
        );
    }

    @Override
    public @Nullable BlockDataGenerator<? super SinkBlock> getGenerator() {
        return null;/* new HorizontalBDG<>() {
            @Override
            public String getTemplateType(SinkBlock block) {
                return "sink";
            }
        };*/
    }

    @Override
    public boolean hasParticle(SinkBlock block) {
        return false;
    }
}
