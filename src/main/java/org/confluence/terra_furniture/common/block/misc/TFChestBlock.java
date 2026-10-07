package org.confluence.terra_furniture.common.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.terra_furniture.common.block.func.BlockSetGetter;
import org.confluence.terra_furniture.common.block.func.set.TFBlockSetType;
import org.confluence.terra_furniture.common.init.TFBlocks;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 套装内统一的箱子方块，与 vanilla 一致支持双箱，使用 Geo 模型渲染。
 * 模型与贴图按方块 id 定位：{@code geo/block/<id>[_left|_right].geo.json} 与 {@code textures/block/<id>[_left|_right].png}。
 */
public class TFChestBlock extends ChestBlock implements BlockSetGetter<TFChestBlock> {
    private final TFBlockSetType type;

    public TFChestBlock(TFBlockSetType type, Properties properties) {
        super(properties, TFBlocks.CHEST_ENTITY::get);
        this.type = type;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new Entity(pos, state);
    }

    @Override
    public TFBlockSetType getType() {
        return type;
    }

    @Override
    public boolean hasParticle(TFChestBlock block) {
        return false;
    }

    /**
     * 箱子实体，盖子的开合动画由 {@code TFChestRenderer} 处理。
     */
    public static class Entity extends ChestBlockEntity implements GeoBlockEntity {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public Entity(BlockPos pos, BlockState state) {
            super(TFBlocks.CHEST_ENTITY.get(), pos, state);
        }

        @Override
        protected Component getDefaultName() {
            return Component.translatable(getBlockState().getBlock().getDescriptionId());
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        }

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }
    }
}
