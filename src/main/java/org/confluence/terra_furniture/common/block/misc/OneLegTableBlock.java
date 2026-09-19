package org.confluence.terra_furniture.common.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

/**
 * A standalone connectable table whose connected component shares one leg at
 * its geometric center. Existing {@link TableBlock} furniture is deliberately
 * not part of this connection system.
 */
public class OneLegTableBlock extends CrossCollisionBlock implements EntityBlock {
    private static final VoxelShape TOP_SHAPE = Shapes.box(0.0, 0.8125, 0.0, 1.0, 1.0, 1.0);
    private static final double LEG_HALF_WIDTH = 5.0 / 16.0;
    private static final double LEG_HEIGHT = 13.0 / 16.0;
    private static final int MAX_TABLE_SPAN = 3;
    private static final int MAX_CONNECTED_BLOCKS = 64;

    public OneLegTableBlock(Properties properties) {
        super(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, properties);
        registerDefaultState(stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BEntity(pos, state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState baseState = super.getStateForPlacement(context);
        if (baseState == null) {
            return null;
        }

        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluidState = level.getFluidState(pos);
        return baseState
                .setValue(NORTH, isSameTable(level.getBlockState(pos.north())))
                .setValue(EAST, isSameTable(level.getBlockState(pos.east())))
                .setValue(SOUTH, isSameTable(level.getBlockState(pos.south())))
                .setValue(WEST, isSameTable(level.getBlockState(pos.west())))
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (direction.getAxis().isHorizontal()) {
            return state.setValue(PROPERTY_BY_DIRECTION.get(direction), isSameTable(neighborState));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    private boolean isSameTable(BlockState state) {
        return state != null && state.getBlock() == this;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getCombinedShape(level, pos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getCombinedShape(level, pos);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return getCombinedShape(level, pos);
    }

    private VoxelShape getCombinedShape(BlockGetter level, BlockPos pos) {
        TableGroup group = findGroup(level, pos);
        double localMinX = Math.max(0.0, group.centerX() - LEG_HALF_WIDTH - pos.getX());
        double localMaxX = Math.min(1.0, group.centerX() + LEG_HALF_WIDTH - pos.getX());
        double localMinZ = Math.max(0.0, group.centerZ() - LEG_HALF_WIDTH - pos.getZ());
        double localMaxZ = Math.min(1.0, group.centerZ() + LEG_HALF_WIDTH - pos.getZ());

        if (localMinX >= localMaxX || localMinZ >= localMaxZ) {
            return TOP_SHAPE;
        }
        return Shapes.or(TOP_SHAPE, Shapes.box(localMinX, 0.0, localMinZ, localMaxX, LEG_HEIGHT, localMaxZ));
    }

    /**
     * Finds the table group containing {@code start}. A physically connected
     * component is split into deterministic 3x3 cells, so no table group can
     * span more than three blocks along either horizontal axis. Disconnected
     * islands inside the same cell remain separate groups.
     * <p>
     * The hard block limit prevents malformed or enormous structures from
     * causing unbounded work; an over-limit or invalid component safely falls
     * back to a single table.
     */
    public TableGroup findGroup(@Nullable BlockGetter level, BlockPos start) {
        if (level == null || start == null || !isSameTable(level.getBlockState(start))) {
            return TableGroup.single(start == null ? BlockPos.ZERO : start);
        }

        Set<BlockPos> component = new HashSet<>();
        ArrayDeque<BlockPos> pending = new ArrayDeque<>();
        pending.add(start.immutable());

        while (!pending.isEmpty()) {
            BlockPos current = pending.removeFirst();
            if (component.contains(current) || !isSameTable(level.getBlockState(current))) {
                continue;
            }
            component.add(current);
            if (component.size() > MAX_CONNECTED_BLOCKS) {
                return TableGroup.single(start);
            }
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos neighbor = current.relative(direction);
                if (!component.contains(neighbor) && isSameTable(level.getBlockState(neighbor))) {
                    pending.addLast(neighbor.immutable());
                }
            }
        }

        if (component.isEmpty()) {
            return TableGroup.single(start);
        }

        int componentMinX = Integer.MAX_VALUE;
        int componentMinZ = Integer.MAX_VALUE;
        for (BlockPos member : component) {
            componentMinX = Math.min(componentMinX, member.getX());
            componentMinZ = Math.min(componentMinZ, member.getZ());
        }

        int groupMinX = componentMinX
                + Math.floorDiv(start.getX() - componentMinX, MAX_TABLE_SPAN) * MAX_TABLE_SPAN;
        int groupMinZ = componentMinZ
                + Math.floorDiv(start.getZ() - componentMinZ, MAX_TABLE_SPAN) * MAX_TABLE_SPAN;
        int groupMaxX = groupMinX + MAX_TABLE_SPAN - 1;
        int groupMaxZ = groupMinZ + MAX_TABLE_SPAN - 1;

        Set<BlockPos> groupMembers = new HashSet<>();
        pending.add(start.immutable());
        while (!pending.isEmpty()) {
            BlockPos current = pending.removeFirst();
            if (groupMembers.contains(current) || !component.contains(current)
                    || current.getX() < groupMinX || current.getX() > groupMaxX
                    || current.getZ() < groupMinZ || current.getZ() > groupMaxZ) {
                continue;
            }
            groupMembers.add(current);
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                pending.addLast(current.relative(direction).immutable());
            }
        }

        if (groupMembers.isEmpty()) {
            return TableGroup.single(start);
        }

        BlockPos anchor = null;
        double totalX = 0.0;
        double totalZ = 0.0;
        int minX = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (BlockPos member : groupMembers) {
            totalX += member.getX() + 0.5;
            totalZ += member.getZ() + 0.5;
            minX = Math.min(minX, member.getX());
            minZ = Math.min(minZ, member.getZ());
            maxX = Math.max(maxX, member.getX());
            maxZ = Math.max(maxZ, member.getZ());
            if (anchor == null || member.getX() < anchor.getX()
                    || member.getX() == anchor.getX() && member.getZ() < anchor.getZ()) {
                anchor = member;
            }
        }

        int count = groupMembers.size();
        return new TableGroup(anchor == null ? start.immutable() : anchor.immutable(),
                totalX / count, totalZ / count, minX, minZ, maxX, maxZ, count);
    }

    public TableGroup findGroup(BEntity blockEntity) {
        if (blockEntity == null) {
            return TableGroup.single(BlockPos.ZERO);
        }
        return findGroup(blockEntity.getLevel(), blockEntity.getBlockPos());
    }

    public record TableGroup(BlockPos anchor, double centerX, double centerZ,
                             int minX, int minZ, int maxX, int maxZ, int size) {
        private static TableGroup single(BlockPos pos) {
            BlockPos safePos = pos == null ? BlockPos.ZERO : pos.immutable();
            return new TableGroup(safePos, safePos.getX() + 0.5, safePos.getZ() + 0.5,
                    safePos.getX(), safePos.getZ(), safePos.getX(), safePos.getZ(), 1);
        }
    }

    public static class BEntity extends BlockEntity implements GeoBlockEntity {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public BEntity(BlockPos pos, BlockState state) {
            super(TFBlocks.ONE_LEG_TABLE_ENTITY.get(), pos, state);
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
            // The supplied GEO models are static.
        }

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }
    }
}
