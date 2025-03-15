package nowebsite.makertechno.terra_furniture.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nowebsite.makertechno.terra_furniture.common.block.chair.ChairBlock;
import nowebsite.makertechno.terra_furniture.common.entity.chair.ChairEntity;
import nowebsite.makertechno.terra_furniture.common.init.TFEntities;

public class SinkBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<SinkBlock> CODEC = simpleCodec(SinkBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SINK_X;
    protected static final VoxelShape SINK_Z;
    protected static final VoxelShape PIPE;
    public SinkBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED,FACING);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        return defaultBlockState()
                .setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    protected BlockState mirror(BlockState state, Mirror mirror) {
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
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected MapCodec<SinkBlock> codec() {
        return CODEC;
    }
    static {
        SINK_X = Block.box(1.0, 10.0, 3.0, 15.0, 15.0, 13.0);
        SINK_Z = Block.box(3.0, 10.0, 1.0, 13.0, 15.0, 15.0);
        PIPE = Block.box(7.0, 0.0, 7.0, 9.0, 10.0, 9.0);
    }
}
