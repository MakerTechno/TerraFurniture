package org.confluence.terra_furniture.client.model;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.confluence.terra_furniture.TerraFurniture;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.model.GeoModel;

import java.util.concurrent.ConcurrentHashMap;

public class CacheBlockModel<T extends BlockEntity & GeoBlockEntity> extends GeoModel<T> {
    private final ConcurrentHashMap<Block, ResourceLocation> model = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Block, ResourceLocation> texture = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Block, ResourceLocation> animation = new ConcurrentHashMap<>();

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return model.computeIfAbsent(animatable.getBlockState().getBlock(), block -> TerraFurniture.asResource("geo/block/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + ".geo.json"));
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return texture.computeIfAbsent(animatable.getBlockState().getBlock(), block -> TerraFurniture.asResource("textures/block/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + ".png"));
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return animation.computeIfAbsent(animatable.getBlockState().getBlock(), block -> TerraFurniture.asResource("animations/block/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + ".animation.json"));
    }
}
