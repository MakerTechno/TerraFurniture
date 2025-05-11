package org.confluence.terra_furniture.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TFBlockTagsProvider extends BlockTagsProvider {
    public TFBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TerraFurniture.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(TFTags.GLASS_FURNITURE)
                .add(TFBlocks.GLASS_CHAIR.get())
                .add(TFBlocks.GLASS_SOFA.get())
                .add(TFBlocks.GLASS_TOILET.get())
                .add(TFBlocks.GLASS_SINK.get())
                .add(TFBlocks.GLASS_DOOR.get())
                .add(TFBlocks.GLASS_TABLE.get())
                .add(TFBlocks.GLASS_CANDLE.get())
                .add(TFBlocks.GLASS_CHANDELIER.get())
                .add(TFBlocks.GLASS_LANTERN.get())
                .add(TFBlocks.GLASS_LAMP.get())
                .add(TFBlocks.GLASS_CANDELABRAS.get())
                .add(TFBlocks.GLASS_CLOCK.get())
                .add(TFBlocks.GLASS_BATHTUB.get());
        tag(TFTags.DUNGEON_FURNITURE)
                .add(TFBlocks.BLUE_BRICK_CHAIR.get())
                .add(TFBlocks.BLUE_BRICK_SOFA.get())
                .add(TFBlocks.BLUE_BRICK_TOILET.get())
                .add(TFBlocks.BLUE_BRICK_SINK.get())
                .add(TFBlocks.BLUE_BRICK_DOOR.get())
                .add(TFBlocks.BLUE_BRICK_TABLE.get())
                .add(TFBlocks.BLUE_BRICK_CANDLE.get())
                .add(TFBlocks.BLUE_BRICK_CHANDELIER.get())
                .add(TFBlocks.BLUE_BRICK_LANTERN.get())
                .add(TFBlocks.BLUE_BRICK_LAMP.get())
                .add(TFBlocks.BLUE_BRICK_CANDELABRAS.get())
                .add(TFBlocks.BLUE_BRICK_CLOCK.get())
                .add(TFBlocks.BLUE_BRICK_BATHTUB.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(TFBlocks.PLASTIC_CHAIR.get())
                .addTag(TFTags.GLASS_FURNITURE)
                .addTag(TFTags.DUNGEON_FURNITURE);
    }
}
