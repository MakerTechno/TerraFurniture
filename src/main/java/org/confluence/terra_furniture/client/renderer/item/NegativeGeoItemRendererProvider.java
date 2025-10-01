package org.confluence.terra_furniture.client.renderer.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

public class NegativeGeoItemRendererProvider<T extends Item & GeoAnimatable> extends SimpleGeoItemRendererProvider<T> {
    public NegativeGeoItemRendererProvider(GeoModel<T> model) {
        super(model);
    }

    @Override
    public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
        if (renderer == null) {
            this.renderer = new GeoNegativeVolumeItemRenderer<>(model, this::process);
        }
        return renderer;
    }
    public void process(BakedGeoModel model) {}
}
