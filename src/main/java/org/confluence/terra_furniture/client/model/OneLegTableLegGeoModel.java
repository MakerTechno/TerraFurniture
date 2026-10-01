package org.confluence.terra_furniture.client.model;

import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.misc.OneLegTableBlock;
import software.bernie.geckolib.model.GeoModel;

public class OneLegTableLegGeoModel extends GeoModel<OneLegTableBlock.BEntity> {
    private static final ResourceLocation MODEL = TerraFurniture.asResource("geo/block/one_leg_table/leg.geo.json");
    private static final ResourceLocation TEXTURE = TerraFurniture.asResource("textures/block/one_leg_table_leg.png");
    private static final ResourceLocation ANIMATION = TerraFurniture.asResource("animations/block/one_leg_table.animation.json");

    @Override
    public ResourceLocation getModelResource(OneLegTableBlock.BEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(OneLegTableBlock.BEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(OneLegTableBlock.BEntity animatable) {
        return ANIMATION;
    }
}
