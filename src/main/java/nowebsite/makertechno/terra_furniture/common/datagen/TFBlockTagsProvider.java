package nowebsite.makertechno.terra_furniture.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TFBlockTagsProvider extends BlockTagsProvider {
    public TFBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, TerraFurniture.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}
