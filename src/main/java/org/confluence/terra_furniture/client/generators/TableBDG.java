package org.confluence.terra_furniture.client.generators;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_furniture.common.block.misc.TableBlock;
import org.jetbrains.annotations.Nullable;

public abstract class TableBDG extends DefaultBlockDataGenerator<TableBlock> {

    public <B extends ModelBuilder<B>, P extends ModelProvider<B>> @Nullable B processTogether(TableBlock block, P provider, ExistingFileHelper helper, @Nullable String part) {
        Pair<ResourceState, B> mb = processModel(block, provider, helper, part, AccessType.BLOCK);
        if (mb.getFirst().equals(ResourceState.NOT_EXIST)) return null;
        mb = processTexture(block, mb.getSecond(), null, AccessType.BLOCK);
        if (!mb.getFirst().equals(ResourceState.EXIST)) isBlockValid = false;
        return mb.getSecond();
    }

    public void buildTemplate1(TableBlock block, BlockModelProvider provider, MultiPartBlockStateBuilder builder, ExistingFileHelper helper) {
        isBlockValid = true;
        BlockModelBuilder top =  processTogether(block, provider, helper, "top");
        BlockModelBuilder side = processTogether(block, provider, helper, "side");
        BlockModelBuilder leg = processTogether(block, provider, helper, "leg");
        BlockModelBuilder leg_layer = processTogether(block, provider, helper, "leg_layer");
        BlockModelBuilder corner = processTogether(block, provider, helper, "corner");
        BlockModelBuilder layer = processTogether(block, provider, helper, "layer");
        BlockModelBuilder layer_corner = processTogether(block, provider, helper, "layer_corner");
        BlockModelBuilder layer_corner_y = processTogether(block, provider, helper, "layer_corner_y");
        BlockModelBuilder layer_only = processTogether(block, provider, helper, "layer_only");

        if (
                top == null || side == null || leg == null || leg_layer == null || corner == null ||layer == null || layer_corner == null
                        || layer_corner_y == null || layer_only == null
        ) {
            isBlockValid = false;
            return;
        }

        builder.part().modelFile(top).addModel();
        processStepFacing(builder, side);
        processStepFacingBi(builder, corner);
        processStepFacingBi(builder, leg);
        processStepFacingBi(builder, leg_layer);
        processStepFacingTri(builder, layer_only, false, false);
        processStepFacingTri(builder, layer, true, true);
        processStepFacingTri(builder, layer_corner, true, false);
        processStepFacingTri(builder, layer_corner_y , false, true);
    }

    public static void processStepFacing(MultiPartBlockStateBuilder builder, ModelFile file) {
        builder.part().modelFile(file).addModel().condition(BlockStateProperties.NORTH, false);
        builder.part().modelFile(file).rotationY(90).addModel().condition(BlockStateProperties.EAST, false);
        builder.part().modelFile(file).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, false);
        builder.part().modelFile(file).rotationY(270).addModel().condition(BlockStateProperties.WEST, false);
    }

    public static void processStepFacingBi(MultiPartBlockStateBuilder builder, ModelFile file) {
        builder.part().modelFile(file).addModel()
                .condition(BlockStateProperties.NORTH, false)
                .condition(BlockStateProperties.WEST, false);
        builder.part().modelFile(file).rotationY(90).addModel()
                .condition(BlockStateProperties.EAST, false)
                .condition(BlockStateProperties.NORTH, false);
        builder.part().modelFile(file).rotationY(180).addModel()
                .condition(BlockStateProperties.SOUTH, false)
                .condition(BlockStateProperties.EAST, false);
        builder.part().modelFile(file).rotationY(270).addModel()
                .condition(BlockStateProperties.WEST, false)
                .condition(BlockStateProperties.SOUTH, false);
    }

    public static void processStepFacingTri(MultiPartBlockStateBuilder builder, ModelFile file, boolean control2, boolean control3) {
        builder.part().modelFile(file).addModel()
                .condition(BlockStateProperties.NORTH, false)
                .condition(BlockStateProperties.EAST, control2)
                .condition(BlockStateProperties.WEST, control3);
        builder.part().modelFile(file).rotationY(90).addModel()
                .condition(BlockStateProperties.EAST, false)
                .condition(BlockStateProperties.SOUTH, control2)
                .condition(BlockStateProperties.NORTH, control3);
        builder.part().modelFile(file).rotationY(180).addModel()
                .condition(BlockStateProperties.SOUTH, false)
                .condition(BlockStateProperties.WEST, control2)
                .condition(BlockStateProperties.EAST, control3);
        builder.part().modelFile(file).rotationY(270).addModel()
                .condition(BlockStateProperties.WEST, false)
                .condition(BlockStateProperties.NORTH, control2)
                .condition(BlockStateProperties.SOUTH, control3);
    }

    @Override
    public void buildBlockWithTemplate(TableBlock block, BlockStateProvider builderProvider, ExistingFileHelper helper) {
        buildTemplate1(block, builderProvider.models(), builderProvider.getMultipartBuilder(block), helper);
    }

}
