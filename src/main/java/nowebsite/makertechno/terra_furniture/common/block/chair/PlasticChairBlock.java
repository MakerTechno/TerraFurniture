package nowebsite.makertechno.terra_furniture.common.block.chair;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PlasticChairBlock extends AbstractChairBlock implements EntityBlock {
    public static final MapCodec<PlasticChairBlock> CODEC = simpleCodec(PlasticChairBlock::new);
    private static final VoxelShape SHAPE = Shapes.box(0.1875, 0.0, 0.1875, 0.8125, 1.0, 0.8125);
    private static final Vec3 SIT_POS = new Vec3(0, 0.16, 0);

    public PlasticChairBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Vec3 sitPos() {
        return SIT_POS;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new Entity(blockPos, blockState);
    }

    @Override
    protected MapCodec<PlasticChairBlock> codec() {
        return CODEC;
    }

    public static class Entity extends BlockEntity implements GeoBlockEntity {
        private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);

        public Entity(BlockPos pPos, BlockState pBlockState) {
            super(TFBlocks.PLASTIC_CHAIR_ENTITY.get(), pPos, pBlockState);
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return CACHE;
        }
    }

    public static class Item extends BlockItem implements GeoItem {
        private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);

        public Item(PlasticChairBlock pBlock) {
            super(pBlock, new Properties());
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return CACHE;
        }
    }
}
