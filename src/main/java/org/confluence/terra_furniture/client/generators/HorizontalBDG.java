package org.confluence.terra_furniture.client.generators;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_furniture.common.block.func.BlockSetGetter;

public abstract class HorizontalBDG<T extends Block & BlockSetGetter<T>> extends DefaultBlockDataGenerator<T> {
    @Override
    public void buildBlockWithTemplate(T block, BlockStateProvider builderProvider, ExistingFileHelper helper) {
        Pair<ResourceState, BlockModelBuilder> builderResult = processModel(block, builderProvider.models(), helper, null, AccessType.BLOCK);
        if (builderResult.getFirst().equals(ResourceState.NOT_EXIST)) return;
        isBlockValid = true;
        builderResult = processTexture(block, builderResult.getSecond(), null, AccessType.BLOCK);
        if (!builderResult.getFirst().equals(ResourceState.NOT_EXIST)) horizontalDirectional(builderProvider.getVariantBuilder(block), builderResult.getSecond());
        if (!builderResult.getFirst().equals(ResourceState.EXIST)) isBlockValid = false;
    }

    public void horizontalDirectional(VariantBlockStateBuilder builder, ModelFile modelFile) {
        builder.forAllStates(state -> ConfiguredModel.builder().modelFile(modelFile).rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build());
    }
}
