package org.confluence.terra_furniture.common.block.sittable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.func.BasePropertyHorizontalDirectionBlock;
import org.confluence.terra_furniture.common.block.func.be.BaseSittableBE;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.jetbrains.annotations.Nullable;

/**
 * 椅子方块基础类，提供乘坐的基本功能、乘坐位置调态和碰撞箱调态。
 */
public class ChairBlock extends BasePropertyHorizontalDirectionBlock<ChairBlock> implements EntityBlock {
    public final VoxelShape shapeCollision;
    private final float yOff;

    public ChairBlock(BlockState state, Properties properties, float yOff) {
        super(state, properties);
        this.yOff = yOff;
        shapeCollision = Block.box(4.0, 0.0, 4.0, 12.0, 16 * yOff, 12.0);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shapeCollision;
    }
/* Seems not affected, could anyone explain that?

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }
*/

/*  When the behavior goes wrong, check if parent class still use this.

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
*/

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionResult resultA;
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BaseSittableBE<?> chairBlock) {
                resultA = chairBlock.useAct(level, pos, player);
            } else {
                TerraFurniture.LOGGER.error("Sittable block entity is missing, it's an unexpected state.");
                resultA = InteractionResult.FAIL;
            }
        } else {
            resultA = InteractionResult.PASS;
        }
        return resultA;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new Entity(blockPos, blockState);
    }

    @Override
    @Nullable
    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(Level level, BlockState state, BlockEntityType<E> blockEntityType) {
        return level.isClientSide() ? null : (level1, pos, blockState, t) -> {
            if (t instanceof BaseSittableBE<?> blockEntity) {
                blockEntity.tickAtServer();
            }
        };
    }

    @Override
    protected BasePropertyHorizontalDirectionBlock<ChairBlock> createNewInstance(BlockState baseState, Properties properties) {
        return new ChairBlock(baseState, properties, yOff);
    }

    @Override
    public String getSpecificName() {
        return "";
    }

    @Override
    public String parentName() {
        return "";
    }

    public static class Entity extends BaseSittableBE<Entity> {
        private double yOffset = 0.0;

        public Entity(BlockPos pos, BlockState blockState) {
            super(TFBlocks.CHAIR_ENTITY, pos, blockState);
            if (blockState.getBlock() instanceof ChairBlock chairBlock) yOffset = chairBlock.yOff;
        }

        @Override
        public double getYSvOffset() {
            return yOffset;
        }
    }
}
