package org.confluence.terra_furniture.client.model;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.confluence.terra_furniture.TerraFurniture;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.model.GeoModel;

import java.util.concurrent.ConcurrentHashMap;

public class CacheBlockModel<T extends BlockEntity & GeoBlockEntity> extends GeoModel<T> {
    protected static final ConcurrentHashMap<String, ResourceLocation> MODEL = new ConcurrentHashMap<>();
    protected static final ConcurrentHashMap<String, ResourceLocation> TEXTURE = new ConcurrentHashMap<>();
    protected static final ConcurrentHashMap<String, ResourceLocation> ANIMATION = new ConcurrentHashMap<>();

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return MODEL.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlockState().getBlock()).getPath(), block -> TerraFurniture.asResource("geo/block/" + block + ".geo.json"));
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return TEXTURE.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlockState().getBlock()).getPath(), block -> TerraFurniture.asResource("textures/block/" + block + ".png"));
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ANIMATION.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlockState().getBlock()).getPath(), block -> TerraFurniture.asResource("animations/block/" + block + ".animation.json"));
    }
}
