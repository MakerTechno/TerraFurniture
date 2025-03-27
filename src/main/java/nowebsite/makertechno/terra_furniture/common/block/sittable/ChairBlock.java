package nowebsite.makertechno.terra_furniture.common.block.sittable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.block.func.BasePropertyHorizontalDirectionBlock;
import nowebsite.makertechno.terra_furniture.common.block.func.BaseSittableBE;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChairBlock extends BasePropertyHorizontalDirectionBlock<ChairBlock> implements EntityBlock {
    public ChairBlock(@NotNull BlockState state, Properties properties) {
        super(state, properties);
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return super.getPistonPushReaction(state);
    }

    protected double getYOffset() {
        return 0.4;
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
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
        return new Entity(blockPos, blockState, getYOffset());
    }

    @Override
    @Nullable
    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<E> blockEntityType) {
        return level.isClientSide() ? null : (level1, pos, blockState, t) -> {
            if (t instanceof Entity blockEntity) {
                blockEntity.tickAtServer();
            }
        };
    }
    @Override
    protected BasePropertyHorizontalDirectionBlock<ChairBlock> createNewInstance(BlockState baseState, Properties properties) {
        return new ChairBlock(baseState, properties);
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
        }
        public Entity(BlockPos pos, BlockState blockState, double yOffset) {
            super(TFBlocks.CHAIR_ENTITY, pos, blockState);
            this.yOffset = yOffset;
        }

        @Override
        public void newOneFromBlock() {
            if (containerBlock.getBlock() instanceof ChairBlock chairBlock){
                chairBlock.newBlockEntity(this.getBlockPos(), containerBlock);
            }
        }

        @Override
        public double getYSvOffset() {
            return yOffset;
        }
    }
}
