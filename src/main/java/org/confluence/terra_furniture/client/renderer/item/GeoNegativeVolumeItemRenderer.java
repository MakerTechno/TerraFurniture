
package org.confluence.terra_furniture.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

import java.util.function.Consumer;

public class GeoNegativeVolumeItemRenderer<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {
    private final Consumer<BakedGeoModel> process;
    public GeoNegativeVolumeItemRenderer(GeoModel<T> model, Consumer<BakedGeoModel> process) {
        super(model);
        this.process = process;
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        process.accept(model);
        if(isReRender) return;
        super.preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, colour);
    }

    protected RenderType getGlowRenderType(ResourceLocation texture) {
        return RenderType.text(texture);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        // 非发光部分不渲染阴影
        return RenderType.text(texture);
    }
}

