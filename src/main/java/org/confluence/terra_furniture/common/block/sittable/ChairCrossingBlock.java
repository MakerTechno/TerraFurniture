package org.confluence.terra_furniture.common.block.sittable;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.confluence.terra_furniture.common.block.func.BasePropertyHorizontalDirectionBlock;
import org.confluence.terra_furniture.common.datagen.empowered.BlockDataGenerator;
import org.confluence.terra_furniture.api.client.model.CacheItemRefBlockModel;
import org.confluence.terra_furniture.common.init.TFBlockSetTypes;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

/**
 * 交叉腿椅子方块 - 使用GEO模型
 */
public class ChairCrossingBlock extends ChairBlock {
    private static final VoxelShape SHAPE = net.minecraft.world.phys.shapes.Shapes.box(0.1875, 0.0, 0.1875, 0.8125, 0.8, 0.8125);

    public ChairCrossingBlock(Consumer<Properties> propApplier) {
        super(TFBlockSetTypes.UNBREAKABLE, Blocks.BEDROCK.defaultBlockState(), propApplier, 0.9f);
    }

    public ChairCrossingBlock(Properties properties) {
        super(TFBlockSetTypes.UNBREAKABLE, Blocks.BEDROCK.defaultBlockState(), properties, 0.9f);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ChairCrossingBE(blockPos, blockState);
    }

    @Override
    protected BasePropertyHorizontalDirectionBlock<ChairBlock> createNewInstance(BlockState baseState, Properties properties) {
        return new ChairCrossingBlock(properties);
    }

    @Override
    public @Nullable BlockDataGenerator<? super ChairBlock> getGenerator() {
        return null;
    }

    public static class ChairCrossingBE extends ChairBE implements GeoBlockEntity {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public ChairCrossingBE(BlockPos pos, BlockState blockState) {
            super(TFBlocks.CHAIR_CROSSING_ENTITY.get(), pos, blockState);
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return this.cache;
        }

        @Override
        public double getYSvOffset() {
            return 0.5;
        }
    }

    public static class Item extends BlockItem implements GeoItem {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public Item(ChairCrossingBlock pBlock) {
            super(pBlock, new Properties());
        }

        @Override
        public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
            consumer.accept(new GeoRenderProvider() {
                private GeoItemRenderer<Item> renderer;
                @Override
                public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                    if (renderer == null) {
                        this.renderer = new GeoItemRenderer<>(new CacheItemRefBlockModel<>());
                    }
                    return renderer;
                }
            });
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }
    }
}
