package org.confluence.terra_furniture.common.block.func;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.confluence.terra_furniture.common.block.func.set.TFBlockSetType;
import org.confluence.terra_furniture.common.datagen.empowered.AutoGenBlockData;
import org.confluence.terra_furniture.common.init.TFBlockSetTypes;

import java.util.function.Consumer;

/**
 * 基础的多种材料属性的变体方块，预留了很多有关生成的方法。
 */
public abstract class BasePropertyExtendedBlock<T extends BasePropertyExtendedBlock<T>> extends Block implements SimpleWaterloggedBlock, BlockSetGetter<T>, AutoGenBlockData<T> {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public final Block base;
    private final BlockState baseState;
    private final TFBlockSetType type;

    public static Properties calcProperties(Block block, Consumer<Properties> extraPropApplier) {
        Properties newProp = Properties.copy(block);
        extraPropApplier.accept(newProp);
        return newProp;
    }

    public BasePropertyExtendedBlock(TFBlockSetType type, BlockState state, Consumer<Properties> extraProperties) {
        super(calcProperties(state.getBlock(), extraProperties));
        this.type = type;
        this.base = state.getBlock();
        this.baseState = state;
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    /**
     * 仅供给CODEC使用
     */
    public BasePropertyExtendedBlock(TFBlockSetType type, BlockState state, Properties properties) {
        super(properties);
        this.type = type;
        this.base = state.getBlock();
        this.baseState = state;
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return getType().equals(TFBlockSetTypes.GLASS) || super.propagatesSkylightDown(state, reader, pos);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        return
            blockState == null ?
                defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
                :
                blockState.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getExplosionResistance() {
        return this.base.getExplosionResistance();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    protected abstract BasePropertyExtendedBlock<T> createNewInstance(BlockState baseState, Properties properties);

    public TFBlockSetType getType() {
        return type;
    }


    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getType().equals(TFBlockSetTypes.GLASS) ? Shapes.empty() : super.getVisualShape(state, level, pos, context);
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return getType().equals(TFBlockSetTypes.GLASS) ? 1.0F : super.getShadeBrightness(state, level, pos);
    }

}
