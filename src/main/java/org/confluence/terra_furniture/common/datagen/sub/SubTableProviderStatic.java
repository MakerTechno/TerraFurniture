package org.confluence.terra_furniture.common.datagen.sub;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.misc.TableBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给牢枕的桌子模板组单独写的工具类，生成各种桌子。我大修了模型，所以现在只需一张图就好。你知道吗？曾经每个桌子需要8张贴图！
 */
public class SubTableProviderStatic {
    public static final String TABLE_RES = "table";
    public static void buildTemplate1(TableBlock block, boolean isStrict, BlockModelProvider provider, MultiPartBlockStateBuilder builder) {
        ModelFile top =  buildTableModelForBlock(provider, block, "top");
        ModelFile side = buildTableModelForBlock(provider, block, "side");
        ModelFile leg = buildTableModelForBlock(provider, block, "leg");
        ModelFile leg_layer = buildTableModelForBlock(provider, block, "leg_layer");
        ModelFile corner = buildTableModelForBlock(provider, block, "corner");
        ModelFile layer = buildTableModelForBlock(provider, block, "layer");
        ModelFile layerCorner = buildTableModelForBlock(provider, block, "layer_corner");
        ModelFile layerCornerY = buildTableModelForBlock(provider, block, "layer_corner_y");
        ModelFile layerOnly = buildTableModelForBlock(provider, block, "layer_only");

        builder.part().modelFile(top).addModel();
        processStepFacing(builder, side);
        processStepFacingBi(builder, corner);
        processStepFacingBi(builder, leg);
        processStepFacingBi(builder, leg_layer);
        processStepFacingTri(builder, layerOnly, false, false);
        processStepFacingTri(builder, layer, true, true);
        processStepFacingTri(builder, layerCorner, true, false);
        processStepFacingTri(builder, layerCornerY , false, true);
    }

    @SuppressWarnings("unchecked")
    public static void buildTemplate1Item(TableBlock block, boolean isStrict, ItemModelProvider provider) {
        buildTableModelForItem(provider, block);
    }

    public static ModelFile buildTableModelForBlock(BlockModelProvider provider, TableBlock block, String prefix) {
        BlockModelBuilder builder = provider.withExistingParent(
                ModelProvider.BLOCK_FOLDER + "/" + TABLE_RES + "/" + path(block) + "/" + prefix,
                asModBlock(TABLE_RES + "/" + "template" + "/" + prefix)
        );
        return buildTableModelTexture(builder, block);
    }

    public static void buildTableModelForItem(ItemModelProvider provider, TableBlock block) {
        ItemModelBuilder builder = provider.withExistingParent(ModelProvider.ITEM_FOLDER + "/" + path(block), asModItem("template_table"));
        buildTableModelTexture(builder, block);
    }

    public static <T extends ModelBuilder<T>> ModelFile buildTableModelTexture(ModelBuilder<T> builder, TableBlock block) {
        String path = path(block);
        try {
            builder.texture("particle", asModBlock(TABLE_RES + "/" + path));
        } catch (IllegalArgumentException ignore) {
            warn(path(block), path);
            try {
                builder.texture("particle", asModBlock(TABLE_RES + "/texture_default"));
            } catch (IllegalArgumentException e) {
                report(path, e);
            }
        }
        return builder;
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
    public static ResourceLocation asModBlock(String id) {
        return TerraFurniture.asResource(ModelProvider.BLOCK_FOLDER + "/" + id);
    }
    public static ResourceLocation asModItem(String id) {
        return TerraFurniture.asResource(ModelProvider.ITEM_FOLDER + "/" + id);
    }
    public static ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
    public static String path(Block block) {
        return key(block).getPath();
    }

    public static void warn(String targetId, String id) {
        TerraFurniture.LOGGER.warn("Can't get texture {} for {} ! Using default texture!", targetId, id);
    }
    public static void report(String targetId, Exception e) {
        TerraFurniture.LOGGER.error("I can't find target image {} in folder textures/block/table/default for default usage!", targetId, e);
    }
}
