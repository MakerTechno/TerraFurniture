package org.confluence.terra_furniture.common.block.func.be;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SimpleModelGeoBE extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final boolean hasAnim;

    public SimpleModelGeoBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState, boolean hasAnim) {
        super(type, pos, blockState);
        this.hasAnim = hasAnim;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        if (hasAnim) {
            RawAnimation aDefault = RawAnimation.begin().thenLoop("default");
            controllers.add(new AnimationController<>(this, state -> state.setAndContinue(aDefault)));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
