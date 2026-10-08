package org.confluence.terra_furniture.common.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.confluence.terra_furniture.common.block.func.set.TFBlockSetType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/// 模型灯具随放置方向旋转，交互形状与模型保持一致。
public class DirectionalModelLightBlock extends ModelLightBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final VoxelShape[] shapes = new VoxelShape[4];
    private final List<Vec3> flamePositions;

    public DirectionalModelLightBlock(TFBlockSetType type, Properties properties,
                                      BlockShapeType supportType, VoxelShape northShape, Vec3... flamePositions) {
        super(type, properties, supportType, northShape);
        this.flamePositions = List.of(flamePositions);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        shapes[0] = northShape;
        for (int i = 1; i < shapes.length; i++) {
            VoxelShape[] rotated = {Shapes.empty()};
            shapes[i - 1].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                    rotated[0] = Shapes.or(rotated[0], Shapes.box(
                            1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            shapes[i] = rotated[0].optimize();
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (flamePositions.isEmpty()) {
            super.animateTick(state, level, pos, random);
            return;
        }
        if (!state.getValue(LIT) || state.getValue(WATERLOGGED)) {
            return;
        }
        Direction facing = state.getValue(FACING);
        for (Vec3 flame : flamePositions) {
            Vec3 rotated = switch (facing) {
                case EAST -> new Vec3(16 - flame.z, flame.y, flame.x);
                case SOUTH -> new Vec3(16 - flame.x, flame.y, 16 - flame.z);
                case WEST -> new Vec3(flame.z, flame.y, 16 - flame.x);
                default -> flame;
            };
            level.addParticle(ParticleTypes.SMALL_FLAME,
                    pos.getX() + rotated.x / 16,
                    pos.getY() + rotated.y / 16,
                    pos.getZ() + rotated.z / 16,
                    0, 0, 0);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapes[switch (state.getValue(FACING)) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        }];
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }
}
