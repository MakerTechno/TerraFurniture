package org.confluence.terra_furniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.confluence.lib.common.block.StateProperties;
import org.confluence.terra_furniture.client.model.CacheBlockModel;
import org.confluence.terra_furniture.common.block.ClockBlock;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ClockRenderer extends GeoBlockRenderer<ClockBlock.Entity> {
    public ClockRenderer() {
        super(new CacheBlockModel<>());
    }

    @Override
    public void defaultRender(PoseStack poseStack, ClockBlock.Entity animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        if (animatable.getBlockState().getValue(StateProperties.VERTICAL_TWO_PART).isBase()) {
            super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
        }
    }
}
