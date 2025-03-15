package nowebsite.makertechno.terra_furniture.common.block.chair;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SofaBlock extends AbstractChairBlock {
    public static final MapCodec<ChairBlock> CODEC = simpleCodec(ChairBlock::new);

    public static final EnumProperty<StairsShape> SHAPE;
    public static final BooleanProperty LEFT_END;
    public static final BooleanProperty RIGHT_END;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape BOTTOM_AABB;
    protected static final VoxelShape BOTTOM_X;
    protected static final VoxelShape BOTTOM_Z;
    protected static final VoxelShape UP_STRAIGHT_X;
    protected static final VoxelShape UP_STRAIGHT_Z;
    protected static final VoxelShape BOTTOM_S;
    protected static final VoxelShape UP_STRAIGHT_S;
    @Override
    protected MapCodec<ChairBlock> codec() {
        return CODEC;
    }

    public SofaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SHAPE, StairsShape.STRAIGHT)
                .setValue(LEFT_END, true)
                .setValue(RIGHT_END, true)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public Vec3 sitPos() {
        return new Vec3(0, 0, 0);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape BottomDirection = BOTTOM_AABB;
        VoxelShape TopShape = Shapes.empty();
        VoxelShape TopShapeAdd = Shapes.empty();
        switch (state.getValue(SHAPE)){
            case STRAIGHT -> {
                switch (state.getValue(FACING)) {
                    case NORTH -> {
                        BottomDirection = BOTTOM_X;
                        TopShape = UP_STRAIGHT_X;
                    }
                    case SOUTH -> {
                        BottomDirection = BOTTOM_X.move(0, 0, 3 / 16.0);
                        TopShape = UP_STRAIGHT_X.move(0, 0, 11 / 16.0);
                    }
                    case WEST -> {
                        BottomDirection = BOTTOM_Z;
                        TopShape = UP_STRAIGHT_Z;
                    }
                    case EAST -> {
                        BottomDirection = BOTTOM_Z.move(3 / 16.0, 0, 0);
                        TopShape = UP_STRAIGHT_Z.move(11 / 16.0, 0, 0);
                    }
                }
            }
            case OUTER_LEFT -> {
                switch (state.getValue(FACING)) {
                    case NORTH -> {
                        BottomDirection = BOTTOM_S;
                        TopShape = UP_STRAIGHT_S;
                    }
                    case SOUTH -> {
                        BottomDirection = BOTTOM_S.move(3 / 16.0, 0, 3 / 16.0);
                        TopShape = UP_STRAIGHT_S.move(11 / 16.0, 0, 11 / 16.0);
                    }
                    case WEST -> {
                        BottomDirection = BOTTOM_S.move(0, 0, 3 / 16.0);
                        TopShape = UP_STRAIGHT_S.move(0, 0, 11 / 16.0);
                    }
                    case EAST -> {
                        BottomDirection = BOTTOM_S.move(3 / 16.0, 0, 0);
                        TopShape = UP_STRAIGHT_S.move(11 / 16.0, 0, 0);
                    }
                }
            }
            case OUTER_RIGHT -> {
                switch (state.getValue(FACING)) {
                    case NORTH -> {
                        BottomDirection = BOTTOM_S.move(3 / 16.0, 0, 0);
                        TopShape = UP_STRAIGHT_S.move(11 / 16.0, 0, 0);
                    }
                    case SOUTH -> {
                        BottomDirection = BOTTOM_S.move(0, 0, 3 / 16.0);
                        TopShape = UP_STRAIGHT_S.move(0, 0, 11 / 16.0);
                    }
                    case WEST -> {
                        BottomDirection = BOTTOM_S;
                        TopShape = UP_STRAIGHT_S;
                    }
                    case EAST -> {
                        BottomDirection = BOTTOM_S.move(3 / 16.0, 0, 3 / 16.0);
                        TopShape = UP_STRAIGHT_S.move(11 / 16.0, 0, 11 / 16.0);
                    }
                }
            }
        }
        switch (state.getValue(SHAPE)){
            case INNER_LEFT -> {
                switch (state.getValue(FACING)){
                    case NORTH -> TopShapeAdd = UP_STRAIGHT_Z;
                    case SOUTH -> TopShapeAdd = UP_STRAIGHT_Z.move(11 /16.0,0,0);
                    case WEST -> TopShapeAdd = UP_STRAIGHT_X.move(0,0,11 /16.0);
                    case EAST -> TopShapeAdd = UP_STRAIGHT_X;
                }
            }
            case INNER_RIGHT -> {
                switch (state.getValue(FACING)){
                    case NORTH -> TopShapeAdd = UP_STRAIGHT_Z.move(11 /16.0,0,0);
                    case SOUTH -> TopShapeAdd = UP_STRAIGHT_Z;
                    case WEST -> TopShapeAdd = UP_STRAIGHT_X;
                    case EAST -> TopShapeAdd = UP_STRAIGHT_X.move(0,0,11 /16.0);
                }
            }
        }
        return Shapes.or(BottomDirection,TopShape,TopShapeAdd);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.getLevel().getFluidState(blockpos);
        BlockState blockstate = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        return blockstate
                .setValue(SHAPE, getSofaShape(blockstate, context.getLevel(), blockpos))
                .setValue(LEFT_END, getSofaLeftEnd(blockstate, context.getLevel(), blockpos))
                .setValue(RIGHT_END, getSofaRightEnd(blockstate, context.getLevel(), blockpos));
    }
    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return facing.getAxis().isHorizontal()
                ? state
                .setValue(SHAPE, getSofaShape(state, level, currentPos))
                .setValue(LEFT_END, getSofaLeftEnd(state, level, currentPos))
                .setValue(RIGHT_END, getSofaRightEnd(state, level, currentPos))
                : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
    private static StairsShape getSofaShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockState blockstate = level.getBlockState(pos.relative(direction));
        if (isSofa(blockstate)) {
            Direction direction1 = blockstate.getValue(FACING);
            if (direction1.getAxis() != state.getValue(FACING).getAxis() && canTakeShape(state, level, pos, direction1.getOpposite())) {
                if (direction1 == direction.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }
        BlockState blockstate1 = level.getBlockState(pos.relative(direction.getOpposite()));
        if (isSofa(blockstate1)) {
            Direction direction2 = blockstate1.getValue(FACING);
            if (direction2.getAxis() != state.getValue(FACING).getAxis() && canTakeShape(state, level, pos, direction2)) {
                if (direction2 == direction.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }
                return StairsShape.INNER_RIGHT;
            }
        }
        return StairsShape.STRAIGHT;
    }
    private static boolean canTakeShape(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockstate = level.getBlockState(pos.relative(face));
        return !isSofa(blockstate) || blockstate.getValue(FACING) != state.getValue(FACING);
    }

    private static Boolean getSofaLeftEnd(BlockState state, BlockGetter level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockState blockstate = level.getBlockState(pos.relative(direction.getCounterClockWise()));
        if (isSofa(blockstate)) {
            Direction direction1 = blockstate.getValue(FACING);
            if (direction1 == state.getValue(FACING)) {
                return false;
            }
            return direction1 != direction.getCounterClockWise() && direction1 != direction.getClockWise();
        }
        return true;
    }
    private static Boolean getSofaRightEnd(BlockState state, BlockGetter level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockState blockstate = level.getBlockState(pos.relative(direction.getClockWise()));
        if (isSofa(blockstate)) {
            Direction direction1 = blockstate.getValue(FACING);
            if (direction1 == state.getValue(FACING)) {
                return false;
            }
            return direction1 != direction.getCounterClockWise() && direction1 != direction.getClockWise();
        }
        return true;
    }
    public static boolean isSofa(BlockState state) {
        return state.getBlock() instanceof SofaBlock;
    }
    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(FACING);
        StairsShape stairsshape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT -> {
                if (direction.getAxis() == Direction.Axis.Z) {
                    return switch (stairsshape) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        default -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
            }
            case FRONT_BACK -> {
                if (direction.getAxis() == Direction.Axis.X) {
                    return switch (stairsshape) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
            }
        }

        return super.mirror(state, mirror);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE, LEFT_END, RIGHT_END, WATERLOGGED);
    }
    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    static {
        SHAPE = BlockStateProperties.STAIRS_SHAPE;
        LEFT_END = BooleanProperty.create("left_end");
        RIGHT_END = BooleanProperty.create("right_end");
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

        BOTTOM_X = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 13.0);
        BOTTOM_Z = Block.box(0.0, 0.0, 0.0, 13.0, 8.0, 16.0);
        UP_STRAIGHT_X = Block.box(0.0, 8.0, 0.0, 16.0, 14.0, 5.0);
        UP_STRAIGHT_Z = Block.box(0.0, 8.0, 0.0, 5.0, 14.0, 16.0);
        BOTTOM_S = Block.box(0.0, 0.0, 0.0, 13.0, 8.0, 13.0);
        UP_STRAIGHT_S = Block.box(0.0, 8.0, 0.0, 5.0, 14.0, 5.0);
    }
}
