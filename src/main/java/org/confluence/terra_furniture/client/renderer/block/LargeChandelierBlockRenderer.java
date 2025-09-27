package org.confluence.terra_furniture.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.light.LargeChandelierBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Collection;

public class LargeChandelierBlockRenderer extends AbstractSwayingGeoNegativeRenderer<LargeChandelierBlock.BEntity> {
    public static final ResourceLocation MODEL = TerraFurniture.asResource("geo/block/blue_dungeon_chandelier.geo.json");
    public static final ResourceLocation TEXTURE = TerraFurniture.asResource("textures/block/blue_dungeon_chandelier.png");
    public static final ResourceLocation ANIMATION = TerraFurniture.asResource("animations/block/blue_dungeon_chandelier.animation.json");

    public LargeChandelierBlockRenderer() {
        super(new GeoModel<>() {
            @Override
            public ResourceLocation getModelResource(LargeChandelierBlock.BEntity animatable) {
                return MODEL;
            }

            @Override
            public ResourceLocation getTextureResource(LargeChandelierBlock.BEntity animatable) {
                return TEXTURE;
            }

            @Override
            public ResourceLocation getAnimationResource(LargeChandelierBlock.BEntity animatable) {
                return ANIMATION;
            }
        });
    }

    @Override
    public boolean shouldRenderOffScreen(LargeChandelierBlock.@NotNull BEntity pBlockEntity) {
        return true;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(LargeChandelierBlock.@NotNull BEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX() -1, pos.getY(), pos.getZ()-1, pos.getX() +1, pos.getY() -1, pos.getZ() +1);
    }

    @Override
    public void preRender(PoseStack poseStack, LargeChandelierBlock.BEntity animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        processHide(model, !animatable.getBlockState().getValue(LargeChandelierBlock.LIT));
        if(isReRender) return;
        super.preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, colour);
    }



    protected void processHide(BakedGeoModel model, boolean hidden) {
        model.topLevelBones().getFirst().getChildBones().stream()
            .map(GeoBone::getChildBones).flatMap(Collection::stream)
            .map(GeoBone::getChildBones).flatMap(Collection::stream)
            .forEach(geoBone -> {
                if (geoBone.getName().startsWith("flame")) geoBone.setHidden(hidden);
            });
    }
}