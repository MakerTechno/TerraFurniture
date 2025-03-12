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
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nowebsite.makertechno.terra_furniture.common.block.chair.AbstractChairBlock;

import java.util.stream.IntStream;

public class SofaBlock extends AbstractChairBlock {
    public static final MapCodec<ChairBlock> CODEC = simpleCodec(ChairBlock::new);

    public static final DirectionProperty FACING;
    public static final EnumProperty<StairsShape> SHAPE;
    public static final BooleanProperty LEFT_END;
    public static final BooleanProperty RIGHT_END;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape BOTTOM_AABB;
    protected static final VoxelShape OCTET_NPN;
    protected static final VoxelShape OCTET_NPP;
    protected static final VoxelShape OCTET_PPN;
    protected static final VoxelShape OCTET_PPP;
    protected static final VoxelShape[] SHAPES;
    private static final int[] SHAPE_BY_STATE;
    @Override
    protected MapCodec<ChairBlock> codec() {
        return CODEC;
    }

    private static VoxelShape[] makeShapes() {
        return IntStream.range(0, 16).mapToObj(SofaBlock::makeSofaShape).toArray(VoxelShape[]::new);
    }
    private static VoxelShape makeSofaShape(int bitfield) {
        VoxelShape voxelshape = SofaBlock.BOTTOM_AABB;
        if ((bitfield & 1) != 0) {
            voxelshape = Shapes.or(SofaBlock.BOTTOM_AABB, SofaBlock.OCTET_NPN);
        }

        if ((bitfield & 2) != 0) {
            voxelshape = Shapes.or(voxelshape, SofaBlock.OCTET_PPN);
        }

        if ((bitfield & 4) != 0) {
            voxelshape = Shapes.or(voxelshape, SofaBlock.OCTET_NPP);
        }

        if ((bitfield & 8) != 0) {
            voxelshape = Shapes.or(voxelshape, SofaBlock.OCTET_PPP);
        }

        return voxelshape;
    }

    public SofaBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
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
        return SHAPES[SHAPE_BY_STATE[this.getShapeIndex(state)]];
    }

    private int getShapeIndex(BlockState state) {
        return state.getValue(SHAPE).ordinal() * 4 + state.getValue(FACING).get2DDataValue();
    }
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
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
        FACING = HorizontalDirectionalBlock.FACING;
        SHAPE = BlockStateProperties.STAIRS_SHAPE;
        LEFT_END = BooleanProperty.create("left_end");
        RIGHT_END = BooleanProperty.create("right_end");
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        OCTET_NPN = Block.box(0.0, 8.0, 0.0, 8.0, 16.0, 8.0);
        OCTET_NPP = Block.box(0.0, 8.0, 8.0, 8.0, 16.0, 16.0);
        OCTET_PPN = Block.box(8.0, 8.0, 0.0, 16.0, 16.0, 8.0);
        OCTET_PPP = Block.box(8.0, 8.0, 8.0, 16.0, 16.0, 16.0);
        SHAPES = makeShapes();
        SHAPE_BY_STATE = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};
    }
}
