package nowebsite.makertechno.terra_furniture.common.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import nowebsite.makertechno.terra_furniture.TerraFurniture;

public class TFBlockStateProvider extends BlockStateProvider {
    public TFBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, TerraFurniture.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
