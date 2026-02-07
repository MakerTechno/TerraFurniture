package org.confluence.terra_furniture.client.model;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.confluence.terra_furniture.api.client.model.CacheBlockModel;
import org.confluence.terra_furniture.api.client.model.CacheItemRefBlockModel;
import org.confluence.terra_furniture.common.block.func.BlockSetGetter;
import software.bernie.geckolib.animatable.GeoItem;

public class CacheTBSIRBModel<T extends BlockItem & GeoItem> extends CacheItemRefBlockModel<T> {
    public String getBlockType(T animatable) {
        Block block = animatable.getBlock();
        return block instanceof BlockSetGetter<?> ? ((BlockSetGetter<?>) block).getType().name() + "/" : "";
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return CacheBlockModel.MODEL.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlock()).getPath(), block -> pathApplier.apply("geo/block/" + getBlockType(animatable) + block + ".geo.json"));
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return CacheBlockModel.TEXTURE.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlock()).getPath(), block -> pathApplier.apply("textures/block/" + getBlockType(animatable) + block + ".png"));
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return CacheBlockModel.ANIMATION.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlock()).getPath(), block -> pathApplier.apply("animations/block/" + getBlockType(animatable) + block + ".animation.json"));
    }
}
