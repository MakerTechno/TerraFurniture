package nowebsite.makertechno.terra_furniture.common.block.chair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import nowebsite.makertechno.terra_furniture.common.entity.chair.ChairEntity;
import nowebsite.makertechno.terra_furniture.common.init.TFEntities;

public abstract class AbstractChairBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected AbstractChairBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            ChairEntity chairEntity = new ChairEntity(TFEntities.CHAIR.get(), level);
            chairEntity.setPos(pos.getCenter().add(sitPos()));
            level.addFreshEntity(chairEntity);
            player.startRiding(chairEntity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public abstract Vec3 sitPos();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED,FACING);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        return defaultBlockState()
                .setValue(WATERLOGGED, Boolean.valueOf(levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER))
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
}
