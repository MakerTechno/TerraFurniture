package org.confluence.terra_furniture.client.model;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.model.GeoModel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class CacheBlockModel<T extends BlockEntity & GeoBlockEntity> extends GeoModel<T> {
    private final Function<String, ResourceLocation> pathApplier;
    protected static final ConcurrentHashMap<String, ResourceLocation> MODEL = new ConcurrentHashMap<>();
    protected static final ConcurrentHashMap<String, ResourceLocation> TEXTURE = new ConcurrentHashMap<>();
    protected static final ConcurrentHashMap<String, ResourceLocation> ANIMATION = new ConcurrentHashMap<>();
    public CacheBlockModel(Function<String, ResourceLocation> pathApplier) {
        this.pathApplier = pathApplier;
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return MODEL.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlockState().getBlock()).getPath(), block -> pathApplier.apply("geo/block/" + block + ".geo.json"));
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return TEXTURE.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlockState().getBlock()).getPath(), block -> pathApplier.apply("textures/block/" + block + ".png"));
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ANIMATION.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlockState().getBlock()).getPath(), block -> pathApplier.apply("animations/block/" + block + ".animation.json"));
    }
}
