package org.confluence.terra_furniture.client.renderer.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

public class NegativeGeoItemRendererProvider<T extends Item & GeoAnimatable> extends SimpleGeoItemRendererProvider<T> {
    public NegativeGeoItemRendererProvider(ResourceLocation model, ResourceLocation texture, @Nullable ResourceLocation animation) {
        super(model, texture, animation);
    }

    @Override
    public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
        if (renderer == null) {
            this.renderer = new GeoNegativeVolumeItemRenderer<>(new GeoModel<>() {
                @Override
                public ResourceLocation getModelResource(T animatable) {
                    return model;
                }

                @Override
                public ResourceLocation getTextureResource(T animatable) {
                    return texture;
                }

                @Override
                public @Nullable ResourceLocation getAnimationResource(T animatable) {
                    return animation;
                }

            }, this::process);
        }
        return renderer;
    }
    public void process(BakedGeoModel model) {}
}
