package org.confluence.terra_furniture.client.generators;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.lib.common.block.StateProperties;
import org.confluence.terra_furniture.common.block.func.BlockSetGetter;

public abstract class BiHorizontalBDG<T extends Block & BlockSetGetter<T>> extends HorizontalBDG<T> {
    @Override
    public void buildBlockWithTemplate(T block, BlockStateProvider builderProvider, ExistingFileHelper helper) {
        isBlockValid = true; // Reversed state
        ModelFile base = processTogether(block, builderProvider.models(), helper, StateProperties.HorizontalTwoPart.BASE.getSerializedName(), true);
        ModelFile right = processTogether(block, builderProvider.models(), helper, StateProperties.HorizontalTwoPart.RIGHT.getSerializedName(), true);

        if (base == null || right == null) {
            isBlockValid = false;
            return;
        }

        forAllStates(builderProvider.getVariantBuilder(block), state -> ConfiguredModel.builder()
                .modelFile(isBase(state) ? base : right)
                .rotationY(toY(state))
                .build()
        );
    }

    public static boolean isBase(BlockState state) {
        return state.getValue(StateProperties.HORIZONTAL_TWO_PART).isBase();
    }
}
