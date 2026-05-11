package org.confluence.terra_furniture.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.datagen.empowered.BlockDataGenerator;
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
        TFDataGenerator.GENERATORS.forEach((block, blockDataGenerator) -> invokeGenerator(block, blockDataGenerator, this));
        tag(TFTags.GLASS_FURNITURE)
                .add(TFBlocks.GLASS_SET.SOFA.get())
                .add(TFBlocks.GLASS_SET.SINK.get())
                .add(TFBlocks.GLASS_SET.CANDLE.get())
                .add(TFBlocks.GLASS_SET.LANTERN.get())
                .add(TFBlocks.GLASS_SET.LAMP.get())
                .add(TFBlocks.GLASS_SET.CANDELABRAS.get())
                .add(TFBlocks.GLASS_SET.CLOCK.get());

        tag(TFTags.DUNGEON_FURNITURE)
                .add(TFBlocks.BLUE_DUNGEON_SET.SOFA.get())
                .add(TFBlocks.BLUE_DUNGEON_SET.SINK.get())
                .add(TFBlocks.BLUE_DUNGEON_SET.CANDLE.get())
                .add(TFBlocks.BLUE_DUNGEON_SET.LANTERN.get())
                .add(TFBlocks.BLUE_DUNGEON_SET.LAMP.get())
                .add(TFBlocks.BLUE_DUNGEON_SET.CANDELABRAS.get())
                .add(TFBlocks.BLUE_DUNGEON_SET.CLOCK.get());

        tag(TFTags.SINKS)
                .add(TFBlocks.BLUE_DUNGEON_SET.SINK.get())
                .add(TFBlocks.GLASS_SET.SINK.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(TFBlocks.PLASTIC_CHAIR.get())
                .add(TFBlocks.TRASH_CAN.get())
                .addTag(TFTags.GLASS_FURNITURE)
                .addTag(TFTags.DUNGEON_FURNITURE);

        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(TFTags.WOODEN_FURNITURE);

        tag(TFTags.HOUSE_CHAIR).add(
                TFBlocks.GLASS_SET.SOFA.get(),
                TFBlocks.BLUE_DUNGEON_SET.SOFA.get(),
                TFBlocks.PLASTIC_CHAIR.get(),
                TFBlocks.ACACIA_SET.CHAIR.get(),
                TFBlocks.JUNGLE_SET.CHAIR.get(),
                TFBlocks.DARK_OAK_SET.CHAIR.get(),
                TFBlocks.SPRUCE_SET.CHAIR.get());
    }

    @SuppressWarnings("unchecked")
    private <T extends Block> void invokeGenerator(Block block, BlockDataGenerator<?> generator, BlockTagsProvider provider) {
        BlockDataGenerator<T> typedGenerator = (BlockDataGenerator<T>) generator;
        typedGenerator.getRegBlockTags((T) block, provider).forEach(tagKey -> tag(tagKey).add(block));
    }
}
