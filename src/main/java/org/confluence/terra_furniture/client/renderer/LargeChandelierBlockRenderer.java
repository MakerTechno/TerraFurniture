package org.confluence.terra_furniture.client.renderer;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.light.LargeChandelierBlock;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;

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
}