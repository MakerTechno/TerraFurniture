package nowebsite.makertechno.terra_furniture.common.block.sittable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SofaBlock extends ChairBlock {
    public static final VoxelShape BOTTOM_AABB;
    public static final VoxelShape BOTTOM_S, BOTTOM_X, BOTTOM_Z, TOP_STRAIGHT_S, TOP_STRAIGHT_X, TOP_STRAIGHT_Z;
    static {
        BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

        BOTTOM_X = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 13.0);
        BOTTOM_Z = Block.box(0.0, 0.0, 0.0, 13.0, 8.0, 16.0);
        BOTTOM_S = Block.box(0.0, 0.0, 0.0, 13.0, 8.0, 13.0);
        TOP_STRAIGHT_X = Block.box(0.0, 8.0, 0.0, 16.0, 14.0, 5.0);
        TOP_STRAIGHT_Z = Block.box(0.0, 8.0, 0.0, 5.0, 14.0, 16.0);
        TOP_STRAIGHT_S = Block.box(0.0, 8.0, 0.0, 5.0, 14.0, 5.0);
    }
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final BooleanProperty LEFT_END = BooleanProperty.create("left_end");
    public static final BooleanProperty RIGHT_END =  BooleanProperty.create("right_end");

    public SofaBlock(BlockState state, Properties properties) {
        super(state, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SHAPE, StairsShape.STRAIGHT)
                .setValue(LEFT_END, true)
                .setValue(RIGHT_END, true)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected double getYOffset() {
        return 0.55;
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
                        TopShape = TOP_STRAIGHT_X;
                    }
                    case SOUTH -> {
                        BottomDirection = BOTTOM_X.move(0, 0, 3 / 16.0);
                        TopShape = TOP_STRAIGHT_X.move(0, 0, 11 / 16.0);
                    }
                    case WEST -> {
                        BottomDirection = BOTTOM_Z;
                        TopShape = TOP_STRAIGHT_Z;
                    }
                    case EAST -> {
                        BottomDirection = BOTTOM_Z.move(3 / 16.0, 0, 0);
                        TopShape = TOP_STRAIGHT_Z.move(11 / 16.0, 0, 0);
                    }
                }
            }
            case OUTER_LEFT -> {
                switch (state.getValue(FACING)) {
                    case NORTH -> {
                        BottomDirection = BOTTOM_S;
                        TopShape = TOP_STRAIGHT_S;
                    }
                    case SOUTH -> {
                        BottomDirection = BOTTOM_S.move(3 / 16.0, 0, 3 / 16.0);
                        TopShape = TOP_STRAIGHT_S.move(11 / 16.0, 0, 11 / 16.0);
                    }
                    case WEST -> {
                        BottomDirection = BOTTOM_S.move(0, 0, 3 / 16.0);
                        TopShape = TOP_STRAIGHT_S.move(0, 0, 11 / 16.0);
                    }
                    case EAST -> {
                        BottomDirection = BOTTOM_S.move(3 / 16.0, 0, 0);
                        TopShape = TOP_STRAIGHT_S.move(11 / 16.0, 0, 0);
                    }
                }
            }
            case OUTER_RIGHT -> {
                switch (state.getValue(FACING)) {
                    case NORTH -> {
                        BottomDirection = BOTTOM_S.move(3 / 16.0, 0, 0);
                        TopShape = TOP_STRAIGHT_S.move(11 / 16.0, 0, 0);
                    }
                    case SOUTH -> {
                        BottomDirection = BOTTOM_S.move(0, 0, 3 / 16.0);
                        TopShape = TOP_STRAIGHT_S.move(0, 0, 11 / 16.0);
                    }
                    case WEST -> {
                        BottomDirection = BOTTOM_S;
                        TopShape = TOP_STRAIGHT_S;
                    }
                    case EAST -> {
                        BottomDirection = BOTTOM_S.move(3 / 16.0, 0, 3 / 16.0);
                        TopShape = TOP_STRAIGHT_S.move(11 / 16.0, 0, 11 / 16.0);
                    }
                }
            }
        }
        switch (state.getValue(SHAPE)){
            case INNER_LEFT -> {
                switch (state.getValue(FACING)){
                    case NORTH -> TopShapeAdd = TOP_STRAIGHT_Z;
                    case SOUTH -> TopShapeAdd = TOP_STRAIGHT_Z.move(11 /16.0,0,0);
                    case WEST -> TopShapeAdd = TOP_STRAIGHT_X.move(0,0,11 /16.0);
                    case EAST -> TopShapeAdd = TOP_STRAIGHT_X;
                }
            }
            case INNER_RIGHT -> {
                switch (state.getValue(FACING)){
                    case NORTH -> TopShapeAdd = TOP_STRAIGHT_Z.move(11 /16.0,0,0);
                    case SOUTH -> TopShapeAdd = TOP_STRAIGHT_Z;
                    case WEST -> TopShapeAdd = TOP_STRAIGHT_X;
                    case EAST -> TopShapeAdd = TOP_STRAIGHT_X.move(0,0,11 /16.0);
                }
            }
        }
        return Shapes.or(BottomDirection,TopShape,TopShapeAdd);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = super.getStateForPlacement(context);
        return blockstate
                .setValue(SHAPE, getSofaShape(blockstate, context.getLevel(), blockpos))
                .setValue(LEFT_END, getSofaLeftEnd(blockstate, context.getLevel(), blockpos))
                .setValue(RIGHT_END, getSofaRightEnd(blockstate, context.getLevel(), blockpos));
    }
    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        BlockState state1 = super.updateShape(state ,facing, facingState, level, currentPos, facingPos);
        return facing.getAxis().isHorizontal()
                ? state1
                .setValue(SHAPE, getSofaShape(state1, level, currentPos))
                .setValue(LEFT_END, getSofaLeftEnd(state1, level, currentPos))
                .setValue(RIGHT_END, getSofaRightEnd(state1, level, currentPos))
                : super.updateShape(state1, facing, facingState, level, currentPos, facingPos);
    }
    private static StairsShape getSofaShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockState blockState = level.getBlockState(pos.relative(direction));
        if (isSofa(blockState)) {
            Direction direction1 = blockState.getValue(FACING);
            if (direction1.getAxis() != state.getValue(FACING).getAxis() && canTakeShape(state, level, pos, direction1.getOpposite())) {
                if (direction1 == direction.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }
        BlockState blockState1 = level.getBlockState(pos.relative(direction.getOpposite()));
        if (isSofa(blockState1)) {
            Direction direction2 = blockState1.getValue(FACING);
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

    @SuppressWarnings("deprecation")
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
        super.createBlockStateDefinition(builder);
        builder.add(SHAPE, LEFT_END, RIGHT_END);
    }
    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

}
