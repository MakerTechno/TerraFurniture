package org.confluence.terra_furniture.common.block.misc;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TableBlock extends CrossCollisionBlock {
    public static final MapCodec<TableBlock> CODEC = simpleCodec(TableBlock::new);
    private static final VoxelShape TABLE_TOP_SHAPE = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape TABLE_LEG_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 14.0, 10.0);
    private static final VoxelShape SHAPE = Shapes.or(TABLE_TOP_SHAPE, TABLE_LEG_SHAPE);

    public @NotNull MapCodec<TableBlock> codec() {
        return CODEC;
    }

    public TableBlock(BlockBehaviour.Properties properties) {
        super(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(WATERLOGGED, false));
    }

    @Override
    protected @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        if ((state.getValue(NORTH) && state.getValue(SOUTH)) || (state.getValue(WEST) && state.getValue(EAST))) {
            return TABLE_TOP_SHAPE;
        }
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return this.getOcclusionShape(pState, pLevel, pPos);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.getOcclusionShape(state, level, pos);
    }

    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
        return false;
    }

    public boolean connectsTo(@NotNull BlockState state, boolean isSideSolid) {
        return state.is(this);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockState blockstate = blockgetter.getBlockState(blockpos1);
        BlockState blockstate1 = blockgetter.getBlockState(blockpos2);
        BlockState blockstate2 = blockgetter.getBlockState(blockpos3);
        BlockState blockstate3 = blockgetter.getBlockState(blockpos4);
        return Objects.requireNonNull(super.getStateForPlacement(context)).setValue(NORTH, this.connectsTo(blockstate, blockstate.isFaceSturdy(blockgetter, blockpos1, Direction.SOUTH))).setValue(EAST, this.connectsTo(blockstate1, blockstate1.isFaceSturdy(blockgetter, blockpos2, Direction.WEST))).setValue(SOUTH, this.connectsTo(blockstate2, blockstate2.isFaceSturdy(blockgetter, blockpos3, Direction.NORTH))).setValue(WEST, this.connectsTo(blockstate3, blockstate3.isFaceSturdy(blockgetter, blockpos4, Direction.EAST))).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? state.setValue(PROPERTY_BY_DIRECTION.get(facing), this.connectsTo(facingState, facingState.isFaceSturdy(level, facingPos, facing.getOpposite()))) : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }
}
