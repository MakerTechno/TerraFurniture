package nowebsite.makertechno.terra_furniture.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;

public class TFBlockStateProvider extends BlockStateProvider {
    private final ExistingFileHelper helper;

    public TFBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, TerraFurniture.MODID, helper);
        this.helper = helper;
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(TFBlocks.GLASS_KILN.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(new ModelFile.ExistingModelFile(TerraFurniture.asResource("block/glass_kiln"), helper))
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .build());
    }
}
