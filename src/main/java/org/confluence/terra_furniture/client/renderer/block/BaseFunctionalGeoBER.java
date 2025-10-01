package org.confluence.terra_furniture.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.confluence.terra_furniture.client.model.CacheBlockModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;


public class BaseFunctionalGeoBER<T extends BlockEntity & GeoBlockEntity> extends GeoBlockRenderer<T> {
    public static final class Builder<B extends BlockEntity & GeoBlockEntity> {
        private final BaseFunctionalGeoBER<B> renderer;
        private Builder(GeoModel<B> model, boolean isNegative) {
            renderer = new BaseFunctionalGeoBER<>(model, isNegative);
        }
        public static <O extends BlockEntity & GeoBlockEntity> Builder<O> of(GeoModel<O> model, boolean isNegative) {
            return new Builder<>(model, isNegative);
        }
        public static <O extends BlockEntity & GeoBlockEntity> Builder<O> of(boolean isNegative) {
            return new Builder<>(new CacheBlockModel<>(), isNegative);
        }
        public Builder<B> addHideRule(int floor, Predicate<GeoBone> selector, BiPredicate<GeoBone, B> shouldHide) {
            renderer.addHideRule(new Pair<>(floor, selector), shouldHide);
            return this;
        }
        public Builder<B> addRenderHook(IRenderFunctionHook<B> hook) {
            renderer.addRenderHook(hook);
            return this;
        }
        public Builder<B> renderBox(Function<BlockPos, AABB> applied) {
            renderer.applied = applied;
            return this;
        }
        public BaseFunctionalGeoBER<B> build() {
            renderer.processMaxFloor();
            return renderer;
        }
    }
    private final Map<Integer, VisibilityRule> rules = new HashMap<>();
    private final Map<GeoBone, Integer> cachedBones = new HashMap<>();
    private final List<IRenderFunctionHook<T>> hooks = new ArrayList<>();
    private final boolean isNegative;
    private Function<BlockPos, AABB> applied = null;
    private int maxFloor = 0;

    private class VisibilityRule {
        final Pair<Integer, Predicate<GeoBone>> selector;
        final BiPredicate<GeoBone, T> shouldHide;
        VisibilityRule(Pair<Integer, Predicate<GeoBone>> selector, BiPredicate<GeoBone, T> shouldHide) {
            this.selector = selector;
            this.shouldHide = shouldHide;
        }
    }

    private BaseFunctionalGeoBER(GeoModel<T> model, boolean isNegative) {
        super(model);
        this.isNegative = isNegative;
        if (isNegative) {
            this.addRenderLayer(new AutoGlowingGeoLayer<>(this){
                @Override
                protected RenderType getRenderType(T animatable, @Nullable MultiBufferSource bufferSource) {
                    return BaseFunctionalGeoBER.this.getGlowRenderType(this.getTextureResource(animatable));
                }
            });
        }
    }

    private void addHideRule(Pair<Integer, Predicate<GeoBone>> selector, BiPredicate<GeoBone, T> shouldHide) {
        rules.put(rules.size(), new VisibilityRule(selector, shouldHide));
    }

    private void addRenderHook(IRenderFunctionHook<T> hook) {
        hooks.add(hook);
    }

    private void processMaxFloor() {
        maxFloor = rules.entrySet().stream()
            .max(Comparator.comparingInt(value -> value.getValue().selector.getFirst()))
            .map(ruleEntry -> ruleEntry.getValue().selector.getFirst())
            .orElse(0);
    }

    private void setupCacheAndProcess(BakedGeoModel model) {
        for (GeoBone root : model.topLevelBones()) {
            computeBone(root, 0, maxFloor);
        }
    }

    private void computeBone(GeoBone bone, int depth, int maxFloor) {
        if (depth > maxFloor) return;

        rules.entrySet().stream()
            .filter(ruleEntry -> ruleEntry.getValue().selector.getFirst().equals(depth))
            .findFirst()
            .filter(ruleEntry -> ruleEntry.getValue().selector.getSecond().test(bone))
            .ifPresent(ruleEntry -> cachedBones.put(bone, ruleEntry.getKey()));

        for (GeoBone child : bone.getChildBones()) {
            computeBone(child, depth + 1, maxFloor);
        }
    }

    protected RenderType getGlowRenderType(ResourceLocation texture) {
        return RenderType.eyes(texture);
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if (!cachedBones.isEmpty()) cachedBones.forEach((bone, strategyId) -> bone.setHidden(rules.get(strategyId).shouldHide.test(bone, animatable)));
        else setupCacheAndProcess(model);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void defaultRender(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        for (IRenderFunctionHook<T> hook : hooks) {
            hook.processBefore(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
        }
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
        for (int i = hooks.size() - 1; i >= 0; i--) {
            hooks.get(i).processAfter(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
        }
    }

    @Override
    public @Nullable RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return isNegative ? RenderType.text(texture) : super.getRenderType(animatable, texture, bufferSource, partialTick);
    }

    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return true;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(T blockEntity) {
        return applied != null ? applied.apply(blockEntity.getBlockPos()) : super.getRenderBoundingBox(blockEntity);
    }
}