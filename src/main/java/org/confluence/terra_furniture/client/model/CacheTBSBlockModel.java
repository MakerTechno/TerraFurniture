package org.confluence.terra_furniture.client.model;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.confluence.terra_furniture.api.client.model.CacheBlockModel;
import org.confluence.terra_furniture.common.block.func.BlockSetGetter;
import software.bernie.geckolib.animatable.GeoBlockEntity;

public class CacheTBSBlockModel<T extends BlockEntity & GeoBlockEntity> extends CacheBlockModel<T> {
    public String getBlockType(T animatable) {
        Block block = animatable.getBlockState().getBlock();
        return block instanceof BlockSetGetter<?> ? ((BlockSetGetter<?>) block).getType().name() + "/" : "";
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return MODEL.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlockState().getBlock()).getPath(), block -> pathApplier.apply("geo/block/" + getBlockType(animatable) + block + ".geo.json"));
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return TEXTURE.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlockState().getBlock()).getPath(), block -> pathApplier.apply("textures/block/" + getBlockType(animatable) + block + ".png"));
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return ANIMATION.computeIfAbsent(BuiltInRegistries.BLOCK.getKey(animatable.getBlockState().getBlock()).getPath(), block -> pathApplier.apply("animations/block/" + getBlockType(animatable) + block + ".animation.json"));
    }
}
