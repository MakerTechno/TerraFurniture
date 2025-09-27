
package org.confluence.terra_furniture.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class GeoNegativeVolumeBlockRenderer<T extends BlockEntity & GeoAnimatable> extends GeoBlockRenderer<T> {

    public GeoNegativeVolumeBlockRenderer(GeoModel<T> model) {
        super(model);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this){
            @Override
            protected RenderType getRenderType(T animatable, @Nullable MultiBufferSource bufferSource) {
                return GeoNegativeVolumeBlockRenderer.this.getGlowRenderType(this.getTextureResource(animatable));
            }
        });
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        if(isReRender) return;
        super.preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, colour);
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

