package org.confluence.terra_furniture.client.model;

import net.minecraft.resources.ResourceLocation;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.sittable.PlasticChairBlock;
import software.bernie.geckolib.model.GeoModel;

public class PlasticChairModel extends GeoModel<PlasticChairBlock.Entity> {
    public static final ResourceLocation MODEL = TerraFurniture.asResource("geo/block/plastic_chair.geo.json");
    public static final ResourceLocation TEXTURE = TerraFurniture.asResource("textures/block/plastic_chair.png");

    @Override
    public ResourceLocation getModelResource(PlasticChairBlock.Entity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(PlasticChairBlock.Entity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(PlasticChairBlock.Entity animatable) {
        return null;
    }
}
