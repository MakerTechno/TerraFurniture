package org.confluence.terra_furniture.common.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.init.TFBlocks;

public class TFItemModelProvider extends ItemModelProvider {
    public TFItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TerraFurniture.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleBlockItem(TFBlocks.GLASS_KILN.get());
        simpleBlockItem(TFBlocks.LIVING_LOOM.get());
        simpleBlockItem(TFBlocks.EYE_OF_CTHULHU_RELIC.get());
        simpleBlockItem(TFBlocks.KING_SLIME_RELIC.get());
    }
}
