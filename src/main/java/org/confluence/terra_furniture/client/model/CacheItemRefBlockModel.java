package org.confluence.terra_furniture.client.model;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;

import java.util.function.Function;

public class CacheItemRefBlockModel<T extends BlockItem & GeoItem> extends GeoModel<T> {
    private final Function<String, ResourceLocation> pathApplier;
    public CacheItemRefBlockModel(Function<String, ResourceLocation> pathApplier) {
        this.pathApplier = pathApplier;
    }
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return CacheBlockModel.MODEL.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlock()).getPath(), block -> pathApplier.apply("geo/block/" + block + ".geo.json"));
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return CacheBlockModel.TEXTURE.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlock()).getPath(), block -> pathApplier.apply("textures/block/" + block + ".png"));
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return CacheBlockModel.ANIMATION.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlock()).getPath(), block -> pathApplier.apply("animations/block/" + block + ".animation.json"));
    }
}
