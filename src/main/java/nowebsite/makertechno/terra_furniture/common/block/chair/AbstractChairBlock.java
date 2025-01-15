package nowebsite.makertechno.terra_furniture.common.block.chair;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import nowebsite.makertechno.terra_furniture.common.entity.chair.ChairEntity;
import nowebsite.makertechno.terra_furniture.common.init.TFEntities;

public abstract class AbstractChairBlock extends HorizontalDirectionalBlock {
    protected AbstractChairBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
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
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
