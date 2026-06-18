package org.confluence.terra_furniture.api.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.confluence.terra_furniture.api.client.model.CacheBlockModel;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.util.RenderUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/// 扩展的渲染类, 实现对Geo模型的自定义渲染类型。
///
/// @author MakerTechno
public class MultiRenderTypeGeoBER<T extends BlockEntity & GeoBlockEntity> extends BaseFunctionalGeoBER<T> {
    /// 用于构建 [MultiRenderTypeGeoBER] 实例的构建器。
    /// 相比Base模式额外支持了注册渲染模式和发光层目标的功能。
    ///
    /// 该构建器旨在减少多种方块实体在渲染逻辑上的类数量开销，
    /// 提供统一的注册入口以实现模块化、可扩展的渲染行为。
    ///
    /// 使用示例请参照[BaseFunctionalGeoBER.Builder]
    ///
    /// @param <B> 方块实体类型，需同时实现 BlockEntity 与 GeoBlockEntity 接口
    /// @param <T> 任意[MultiRenderTypeGeoBER]或其子类
    public static class Builder<B extends BlockEntity & GeoBlockEntity, T extends MultiRenderTypeGeoBER<B>> extends BaseFunctionalGeoBER.Builder<B, T> {
        protected Builder(Supplier<T> constructor) {
            super(constructor);
        }

        public static <O extends BlockEntity & GeoBlockEntity> Builder<O, MultiRenderTypeGeoBER<O>> ofMRT(GeoModel<O> model) {
            return new Builder<>(() -> new MultiRenderTypeGeoBER<>(model));
        }

        public static <O extends BlockEntity & GeoBlockEntity> Builder<O, MultiRenderTypeGeoBER<O>> ofMRT() {
            return new Builder<>(() -> new MultiRenderTypeGeoBER<>(new CacheBlockModel<>()));
        }

        @Override
        public Builder<B, T> addOperationBindRule(int floor, Predicate<GeoBone> selector, BiConsumer<GeoBone, B> operation) {
            return (Builder<B, T>) super.addOperationBindRule(floor, selector, operation);
        }

        @Override
        public Builder<B, T> addRenderHook(IRenderFunctionHook<B> hook) {
            return (Builder<B, T>) super.addRenderHook(hook);
        }

        @Override
        public Builder<B, T> renderBox(Function<BlockPos, AABB> applied) {
            return (Builder<B, T>) super.renderBox(applied);
        }

        @Override
        public Builder<B, T> shouldNotRenderOffScreen() {
            return (Builder<B, T>) super.shouldNotRenderOffScreen();
        }

        /// 添加骨骼渲染规则，用于在指定模型层根据选择器使用特殊的渲染类型对目标骨骼进行渲染。
        /// <b style="color:red">
        /// 警告: 诸如Iris这类渲染模组会对内容进行合批后顺序渲染, 请确保你所需要的渲染阶段正确!!!
        /// 假如你希望使用无阴影模型再套发光, 不要使用text+eyes! 在原版这没什么, 但在iris存在时, eyes的渲染顺序高于text, 会导致发光被覆盖而无效!!!
        /// 我们在[org.confluence.terra_furniture.api.client.renderer.TFRenderType]提供了entityCutout的无阴影版! 请使用它!
        /// 对别的使用方案，请关注[net.minecraft.client.renderer.RenderStateShard]!!!
        /// **
        ///
        /// @param floor      模型的层级，0表示模型最顶层所有元素，1表示向下的一层，以此类推
        /// @param selector   骨骼选择器，用于筛选目标骨骼
        /// @param renderType 目标骨骼的渲染类型
        /// @return 构建器自身
        public Builder<B, T> addRenderRule(Integer floor, Predicate<GeoBone> selector, Function<ResourceLocation, RenderType> renderType) {
            renderer.addRenderRule(new Pair<>(floor, selector), renderType);
            return this;
        }

        /// 添加骨骼发光层规则，用于在指定模型层根据选择器对目标骨骼添加发光层。
        ///
        /// @param floor    模型的层级，0表示模型最顶层所有元素，1表示向下的一层，以此类推
        /// @param selector 骨骼选择器，用于筛选目标骨骼
        /// @return 构建器自身
        public Builder<B, T> addGlowingLayerBindRule(Integer floor, Predicate<GeoBone> selector) {
            renderer.addGlowingBindRule(new Pair<>(floor, selector));
            return this;
        }

        /// 设置默认的渲染类型。
        ///
        /// **注意: 该方法只需调用一次，多次调用只会取最后一次填入的参数**
        ///
        /// @param defaultRenderType 默认的渲染类型
        /// @return 构建器自身
        public Builder<B, T> setDefaultRenderType(Function<ResourceLocation, RenderType> defaultRenderType) {
            renderer.setDefaultRenderType(defaultRenderType);
            return this;
        }

        @Override
        public T build() {
            return super.build();
        }
    }

    private final Map<GeoBone, RenderType> targetRTBones = new HashMap<>();
    private final Set<GeoBone> targetGlowingBones = new HashSet<>();
    private final Map<Pair<Integer, Predicate<GeoBone>>, Function<ResourceLocation, RenderType>> renderRules = new HashMap<>();
    private final Set<Pair<Integer, Predicate<GeoBone>>> glowingRules = new HashSet<>();
    private int maxRenderFloor;
    private boolean bindGlowing = false;
    private Function<ResourceLocation, RenderType> defaultRenderType = null;

    protected MultiRenderTypeGeoBER(GeoModel<T> model) {
        super(model, true);
    }

    void addRenderRule(Pair<Integer, Predicate<GeoBone>> selector, Function<ResourceLocation, RenderType> renderType) {
        renderRules.put(selector, renderType);
    }

    void addGlowingBindRule(Pair<Integer, Predicate<GeoBone>> selector) {
        if (!bindGlowing) {
            addRenderLayer(new AutoGlowingGeoLayer<>(this) {
                @Override
                protected RenderType getRenderType(T animatable) {
                    return RenderType.eyes(getTextureResource(animatable));
                }
            });
            bindGlowing = true;
        }
        glowingRules.add(selector);
    }

    void setDefaultRenderType(Function<ResourceLocation, RenderType> defaultRenderType) {
        this.defaultRenderType = defaultRenderType;
    }

    void processMaxFloor() {
        super.processMaxFloor();
        maxRenderFloor = renderRules.entrySet().stream()
                .max(Comparator.comparingInt(value -> value.getKey().getFirst()))
                .map(ruleEntry -> ruleEntry.getKey().getFirst())
                .orElse(-1);
    }

    private void processRenderFunc(BakedGeoModel model, T animatable) {
        ResourceLocation texture = getTextureLocation(animatable);
        for (GeoBone bone : model.topLevelBones()) {
            computeBone(bone, 0, maxRenderFloor, (geoBone, integer) -> {
                for (Map.Entry<Pair<Integer, Predicate<GeoBone>>, Function<ResourceLocation, RenderType>> entry : renderRules.entrySet()) {
                    if (entry.getKey().getFirst().equals(integer) && entry.getKey().getSecond().test(geoBone))
                        targetRTBones.put(geoBone, entry.getValue().apply(texture));
                }
                for (Pair<Integer, Predicate<GeoBone>> content : glowingRules) {
                    if (content.getFirst().equals(integer) && content.getSecond().test(geoBone))
                        targetGlowingBones.add(geoBone);
                }
                return false;
            });
        }
        renderRules.clear();
        glowingRules.clear();
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!renderRules.isEmpty() || !glowingRules.isEmpty()) processRenderFunc(model, animatable);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (isReRender) {
            if (bindGlowing && targetGlowingBones.contains(bone)) {
                super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, true, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            } else {
                /* Imitate GeoBlockRenderer */
                if (bone.isTrackingMatrices()) {
                    Matrix4f poseState = new Matrix4f(poseStack.last().pose());
                    Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.blockRenderTranslations);
                    Matrix4f worldState = new Matrix4f(localMatrix);
                    BlockPos pos = this.animatable.getBlockPos();

                    bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
                    bone.setLocalSpaceMatrix(localMatrix);
                    bone.setWorldSpaceMatrix(worldState.translate(new Vector3f(pos.getX(), pos.getY(), pos.getZ())));
                }
                /* Imitate GeoRenderer */
                poseStack.pushPose();
                RenderUtils.prepMatrixForBone(poseStack, bone);
                renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, true, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
                poseStack.popPose();
                /* Imitate GeoRenderer end. */
                /* Imitate GeoBlockRenderer end. */
            }
        } else {
            RenderType type = targetRTBones.getOrDefault(bone, renderType);
            super.renderRecursively(poseStack, animatable, bone, type, bufferSource, bufferSource.getBuffer(type), false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    @Override
    public @Nullable RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return defaultRenderType != null ? defaultRenderType.apply(texture) : super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
