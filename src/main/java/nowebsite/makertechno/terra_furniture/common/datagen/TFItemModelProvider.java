package nowebsite.makertechno.terra_furniture.common.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;

public class TFItemModelProvider extends ItemModelProvider {
    public TFItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TerraFurniture.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleBlockItem(TFBlocks.GLASS_KILN.get());
    }
}
