package org.confluence.terra_furniture.client.renderer.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SimpleGeoItemRendererProvider<T extends Item & GeoAnimatable> implements GeoRenderProvider {
    protected final GeoModel<T> model;
    protected GeoItemRenderer<T> renderer;

    public SimpleGeoItemRendererProvider(GeoModel<T> model) {
        this.model = model;
    }

    @Override
    public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
        if (renderer == null) {
            this.renderer = new GeoItemRenderer<>(model);
        }
        return renderer;
    }
}
