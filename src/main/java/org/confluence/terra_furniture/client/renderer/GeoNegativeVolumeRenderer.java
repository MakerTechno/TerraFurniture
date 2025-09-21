
package org.confluence.terra_furniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;


/**
 * Geo负体积/局部发光 渲染器
 * <p>若需要定制渲染类型和复杂的骨骼，需要继承此类</p>
 */

public class GeoNegativeVolumeRenderer<T extends BlockEntity & GeoAnimatable> extends GeoBlockRenderer<T> {

    private boolean init = false;
    protected List<GeoBone> toHide = new ArrayList<>();
    protected List<GeoBone> notToHide = new ArrayList<>();

    List<String> toHideNames = new ArrayList<>();

    List<String> toHideGroupNames;
    List<String> notToHideGroupNames;

    InitStrategy initRunnable = null; // 初始化策略

    enum InitStrategy implements BiConsumer<BakedGeoModel, GeoNegativeVolumeRenderer<?>> {
        SIMPLE{
            @Override
            public void accept(BakedGeoModel model, GeoNegativeVolumeRenderer<?> renderer) {
                model.topLevelBones().getFirst().getChildBones().forEach(b -> {
                    if(renderer.toHideNames.contains(b.getName())) {
                        renderer.toHide.add(b);
                    } else {
                        renderer.notToHide.add(b);
                    }
                });
                renderer.toHideNames = null;
            }
        },
        COMPLEX{
            final TriConsumer<BakedGeoModel, List<GeoBone>, List<String>> process = (model, addTo, groupNames)-> groupNames.stream()
                    .map(s->model.getBone(s).orElse(null))
                    .filter(Objects::nonNull)
                    .forEach(addTo::add);
            @Override
            public void accept(BakedGeoModel model, GeoNegativeVolumeRenderer<?> renderer) {
                process.accept(model, renderer.toHide, renderer.toHideGroupNames);
                process.accept(model, renderer.notToHide, renderer.notToHideGroupNames);
                renderer.toHideGroupNames = null;
                renderer.notToHideGroupNames = null;
            }
        }
    }

    public GeoNegativeVolumeRenderer(GeoModel<T> model) {
        super(model);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this){
            @Override
            protected RenderType getRenderType(T animatable, @Nullable MultiBufferSource bufferSource) {
                return GeoNegativeVolumeRenderer.this.getGlowRenderType(this.getTextureResource(animatable));
            }
        });
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if(!init && !isReRender ){
            init = true;
            this.processInit(model);
        }

        this.processHide(isReRender);

        if(isReRender){
            return;
        }

        super.preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, colour);
    }


/**
     * 如果需要定制化隐藏骨骼，需要重写此方法
     */

    protected void processInit(BakedGeoModel model){
        if(this.initRunnable != null){
            this.initRunnable.accept(model, this);
            this.initRunnable = null;
        }
    }


/**
     * 默认不需要重新，重写上面的方法就行
     */

    protected void processHide(boolean isReRender){
        toHide.forEach(b -> b.setHidden(!isReRender));
        notToHide.forEach(b -> b.setHidden(isReRender));
    }

    protected RenderType getGlowRenderType(ResourceLocation texture) {
        // 发光部分
        return RenderType.eyes(texture);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        // 非发光部分不渲染阴影
        return RenderType.text(texture);
    }

}

