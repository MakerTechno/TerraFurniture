package nowebsite.makertechno.terra_furniture.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nowebsite.makertechno.terra_furniture.common.block.func.BasePropertyHorizontalDirectionBlock;
import org.jetbrains.annotations.NotNull;

public class SinkBlock extends BasePropertyHorizontalDirectionBlock<SinkBlock> implements SimpleWaterloggedBlock {
    protected static final VoxelShape SINK_X;
    protected static final VoxelShape SINK_Z;
    protected static final VoxelShape PIPE;
    public SinkBlock(BlockState state, Properties pProperties) {
        super(state, pProperties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        switch (state.getValue(FACING)){
            case NORTH -> {
                return Shapes.or(SINK_X.move(0,0, 3/16.0),PIPE.move(0,0, 6/16.0));
            }
            case SOUTH -> {
                return Shapes.or(SINK_X.move(0,0, - 3/16.0),PIPE.move(0,0, - 6/16.0));
            }
            case WEST -> {
                return Shapes.or(SINK_Z.move(3/16.0,0, 0),PIPE.move(6/16.0,0, 0));
            }
            case EAST -> {
                return Shapes.or(SINK_Z.move(- 3/16.0,0, 0),PIPE.move(- 6/16.0,0, 0));
            }
        }
        return Shapes.or(SINK_X,PIPE);
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
