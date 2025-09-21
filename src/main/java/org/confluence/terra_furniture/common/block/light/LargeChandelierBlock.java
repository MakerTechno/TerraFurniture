package org.confluence.terra_furniture.common.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.lib.common.block.HorizontalDirectionalWithHorizontalTenPartBlock;
import org.confluence.lib.common.block.StateProperties;
import org.confluence.terra_furniture.client.renderer.LargeChandelierBlockRenderer;
import org.confluence.terra_furniture.common.block.func.BaseSwayingBE;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.item.SimpleGeoItemRenderer;
import org.confluence.terra_furniture.network.s2c.packet.PlayerCrossDeltaData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class LargeChandelierBlock extends HorizontalDirectionalWithHorizontalTenPartBlock implements EntityBlock {
    public LargeChandelierBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected @NotNull VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.block();
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        BlockPos base = state.getValue(PART).toBase(pos, state.getValue(FACING), false);
        if (level.isClientSide && level.getBlockEntity(base) instanceof BEntity blockEntity) {
            blockEntity.applyDelta(entity.getDeltaMovement());
        } else if (!level.isClientSide && entity instanceof Player) {
            PacketDistributor.sendToPlayersTrackingEntity(entity, new PlayerCrossDeltaData(entity.getPosition(1).subtract(entity.getPosition(0)), base));
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide() ? (level1, pos, blockState, t) -> {
            if (t instanceof BEntity blockEntity) {
                blockEntity.tickAtClient();
            }
        } : null;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        if (blockState.getValue(PART).equals(StateProperties.HorizontalTenPart.UP)) return new BEntity(blockPos, blockState);
        else return null;
    }


    public static final class BEntity extends BaseSwayingBE implements GeoBlockEntity {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
        private static final Vec3 ANCHOR = new Vec3(0.5, 1, 0.5);

        public BEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState);
        }

        public BEntity(BlockPos pos, BlockState state) {
            super(TFBlocks.LARGE_CHANDELIER_ENTITY.get(), pos, state);
        }

        @Override
        public @NotNull Vec3 getAnchorPoint() {
            return ANCHOR;
        }

        @Override
        public void registerControllers(AnimatableManager.@NotNull ControllerRegistrar controllers) {
            controllers.add(new AnimationController<>(this, "controller", state ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
            );
        }

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {return this.cache;}
    }

    public static class Item extends BlockItem implements GeoItem {
        private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);

        public Item(LargeChandelierBlock block, Properties properties) {
            super(block, properties);
        }

        @Override
        public void createGeoRenderer(@NotNull Consumer<GeoRenderProvider> consumer) {
            consumer.accept(new SimpleGeoItemRenderer<>(LargeChandelierBlockRenderer.MODEL, LargeChandelierBlockRenderer.TEXTURE, LargeChandelierBlockRenderer.ANIMATION));
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return CACHE;
        }
    }
}
