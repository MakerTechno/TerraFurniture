package org.confluence.terra_furniture.client.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.misc.OneLegTableBlock;
import software.bernie.geckolib.model.GeoModel;

public class OneLegTableGeoModel extends GeoModel<OneLegTableBlock.BEntity> {
    private static final ResourceLocation TEXTURE = TerraFurniture.asResource("textures/block/one_leg_table.png");
    private static final ResourceLocation ANIMATION = TerraFurniture.asResource("animations/block/one_leg_table.animation.json");

    @Override
    public ResourceLocation getModelResource(OneLegTableBlock.BEntity animatable) {
        return TerraFurniture.asResource("geo/block/one_leg_table/" + variant(animatable).modelName() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OneLegTableBlock.BEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(OneLegTableBlock.BEntity animatable) {
        return ANIMATION;
    }

    public static ModelVariant variant(OneLegTableBlock.BEntity animatable) {
        if (animatable == null) {
            return new ModelVariant("normal", 0);
        }
        BlockState state = animatable.getBlockState();
        int mask = 0;
        if (state.hasProperty(OneLegTableBlock.NORTH) && state.getValue(OneLegTableBlock.NORTH)) mask |= 1;
        if (state.hasProperty(OneLegTableBlock.EAST) && state.getValue(OneLegTableBlock.EAST)) mask |= 2;
        if (state.hasProperty(OneLegTableBlock.SOUTH) && state.getValue(OneLegTableBlock.SOUTH)) mask |= 4;
        if (state.hasProperty(OneLegTableBlock.WEST) && state.getValue(OneLegTableBlock.WEST)) mask |= 8;

        // The supplied GEO variants use different authored base orientations.
        // Keep these rotations explicit: the open edges must face connected neighbors.
        return switch (mask) {
            case 1 -> new ModelVariant("oneside", 3);
            case 2 -> new ModelVariant("oneside", 2);
            case 3 -> new ModelVariant("corner", 2);
            case 4 -> new ModelVariant("oneside", 1);
            case 5 -> new ModelVariant("straight", 1);
            case 6 -> new ModelVariant("corner", 1);
            case 7 -> new ModelVariant("side", 2);
            case 8 -> new ModelVariant("oneside", 0);
            case 9 -> new ModelVariant("corner", 3);
            case 10 -> new ModelVariant("straight", 0);
            case 11 -> new ModelVariant("side", 3);
            case 12 -> new ModelVariant("corner", 0);
            case 13 -> new ModelVariant("side", 0);
            case 14 -> new ModelVariant("side", 1);
            case 15 -> new ModelVariant("middle", 0);
            default -> new ModelVariant("normal", 0);
        };
    }

    public record ModelVariant(String modelName, int quarterTurns) {
    }
}
