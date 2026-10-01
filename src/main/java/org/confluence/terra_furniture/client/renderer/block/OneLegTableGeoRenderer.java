package org.confluence.terra_furniture.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.confluence.terra_furniture.client.model.OneLegTableLegGeoModel;
import org.confluence.terra_furniture.common.block.misc.OneLegTableBlock;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class OneLegTableGeoRenderer implements BlockEntityRenderer<OneLegTableBlock.BEntity> {
    private final LegRenderer legRenderer = new LegRenderer();

    @Override
    public void render(OneLegTableBlock.BEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity == null || blockEntity.isRemoved()) {
            return;
        }

        if (!(blockEntity.getBlockState().getBlock() instanceof OneLegTableBlock tableBlock)) {
            return;
        }
        OneLegTableBlock.TableGroup group = tableBlock.findGroup(blockEntity);
        if (!blockEntity.getBlockPos().equals(group.anchor())) {
            return;
        }

        double offsetX = group.centerX() - (blockEntity.getBlockPos().getX() + 0.5);
        double offsetZ = group.centerZ() - (blockEntity.getBlockPos().getZ() + 0.5);
        poseStack.pushPose();
        poseStack.translate(offsetX, 0.0, offsetZ);
        legRenderer.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(OneLegTableBlock.BEntity blockEntity) {
        if (blockEntity != null && blockEntity.getBlockState().getBlock() instanceof OneLegTableBlock tableBlock) {
            OneLegTableBlock.TableGroup group = tableBlock.findGroup(blockEntity);
            return new AABB(group.minX(), blockEntity.getBlockPos().getY(), group.minZ(),
                    group.maxX() + 1.0, blockEntity.getBlockPos().getY() + 1.0, group.maxZ() + 1.0);
        }
        return blockEntity == null ? new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
                : new AABB(blockEntity.getBlockPos());
    }

    private static class LegRenderer extends GeoBlockRenderer<OneLegTableBlock.BEntity> {
        private LegRenderer() {
            super(new OneLegTableLegGeoModel());
        }

        @Override
        public @Nullable RenderType getRenderType(OneLegTableBlock.BEntity animatable, ResourceLocation texture,
                                                   @Nullable MultiBufferSource bufferSource, float partialTick) {
            return RenderType.entityCutout(texture);
        }
    }
}
