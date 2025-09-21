package org.confluence.terra_furniture.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_furniture.common.block.func.BaseSwayingBE;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public abstract class AbstractSwayingGeoNegativeRenderer<T extends BaseSwayingBE & GeoAnimatable> extends GeoNegativeVolumeRenderer<T> {

    public AbstractSwayingGeoNegativeRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void defaultRender(@NotNull PoseStack poseStack, @NotNull T animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        animatable.trigController();
        poseStack.pushPose();

        // 平移到锚点位置
        Vec3 anchor = animatable.getAnchorPoint();
        poseStack.translate(anchor.x, anchor.y, anchor.z);

        // 应用旋转（绕X和Z轴）
        poseStack.mulPose(Axis.XP.rotation(animatable.getSwingX()));
        poseStack.mulPose(Axis.ZP.rotation(animatable.getSwingZ()));

        // 平移回原点
        poseStack.translate(-anchor.x, -anchor.y, -anchor.z);

        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);

        poseStack.popPose();
    }

}
