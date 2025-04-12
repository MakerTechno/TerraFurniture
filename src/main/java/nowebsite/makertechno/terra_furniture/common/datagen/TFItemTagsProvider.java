package nowebsite.makertechno.terra_furniture.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TFItemTagsProvider extends ItemTagsProvider {
    public TFItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, TerraFurniture.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}
