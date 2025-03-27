package nowebsite.makertechno.terra_furniture.common.block.sittable;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nowebsite.makertechno.terra_furniture.client.model.block.PlasticChairModel;
import nowebsite.makertechno.terra_furniture.common.block.func.BaseSittableBE;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class PlasticChairBlock extends ChairBlock {
    private static final VoxelShape SHAPE = Shapes.box(0.1875, 0.0, 0.1875, 0.8125, 0.8, 0.8125);

    public PlasticChairBlock(Properties properties) {
        super(Blocks.BEDROCK.defaultBlockState(), properties);
    }


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new Entity(blockPos, blockState);
    }
    public static class Entity extends BaseSittableBE<Entity> implements GeoBlockEntity {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public Entity(BlockPos pPos, BlockState pBlockState) {
            super(TFBlocks.PLASTIC_CHAIR_ENTITY, pPos, pBlockState);
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }

        @Override
        public void newOneFromBlock() {
            if (containerBlock.getBlock() instanceof PlasticChairBlock chairBlock){
                chairBlock.newBlockEntity(this.getBlockPos(), containerBlock);
            }
        }

        @Override
        public double getYSvOffset() {
            return 0.4;
        }
    }

    public static class Item extends BlockItem implements GeoItem {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public Item(PlasticChairBlock pBlock) {
            super(pBlock, new Properties());
        }

        @Override
        public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
            consumer.accept(new GeoRenderProvider() {
                private GeoItemRenderer<Item> renderer;
                @Override
                public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                    if (renderer == null) {
                        this.renderer = new GeoItemRenderer<>(new GeoModel<>() {
                            @Override
                            public ResourceLocation getModelResource(PlasticChairBlock.Item animatable) {
                                return PlasticChairModel.MODEL;
                            }

                            @Override
                            public ResourceLocation getTextureResource(PlasticChairBlock.Item animatable) {
                                return PlasticChairModel.TEXTURE;
                            }

                            @Override
                            public @Nullable ResourceLocation getAnimationResource(PlasticChairBlock.Item animatable) {
                                return null;
                            }
                        });
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
