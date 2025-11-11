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
 * 给牢枕的桌子模板组单独写的工具类，生成各种桌子，不过你得把贴图传对了，不然会变成默认贴图的<p>
 * <b>注意，所有贴图必须位于<font color="green">textures/block/方块注册id/</font>下</b><p>
 * 检查一下你是不是按命名规范把贴图塞好了:
 * <ul>
 *     <li>corner.png</li>
 *     <li>layer.png</li>
 *     <li>layer_corner.png</li>
 *     <li>layer_only.png</li>
 *     <li>leg.png</li>
 *     <li>leg_layer.png</li>
 *     <li>side.png</li>
 * </ul>
 * 并且:
 * <ul>
 *     <li>当isStrict为false时，你得加上<b>top.png</b></li>
 *     <li>
 *         当isStrict为true时，你得加上:
 *         <ul>
 *             <li>texture_top.png</li>
 *             <li>texture_side.png</li>
 *             <li>texture_bottom.png</li>
 *         </ul>
 *     </li>
 * </ul>
 */
public class SubTableProviderStatic {
    public static final String TABLE_RES = "table";
    public static void buildTemplate1(TableBlock block, boolean isStrict, BlockModelProvider provider, MultiPartBlockStateBuilder builder) {
        ModelFile top = isStrict
                ? buildTableModelForBlock(provider, block, "/top", Pair.of("top", "texture_top"), Pair.of("side", "texture_side"), Pair.of("bottom", "texture_bottom"))
                : buildTableModelForBlock(provider, block, "/top", Pair.of("top", "top"), Pair.of("side", "top"), Pair.of("bottom", "top"));
        ModelFile side = buildTableModelForBlock(provider, block, "/side", "side");
        ModelFile leg = buildTableModelForBlock(provider, block, "/leg", "leg", "leg_layer");
        ModelFile corner = buildTableModelForBlock(provider, block, "/corner", "corner");
        ModelFile layer = buildTableModelForBlock(provider, block, "/layer", "layer");
        ModelFile layerCorner = buildTableModelForBlock(provider, block, "/layer_corner", "layer_corner");
        ModelFile layerCornerY = buildTableModelForBlock(provider, block, "/layer_corner_y", "layer_corner");
        ModelFile layerOnly = buildTableModelForBlock(provider, block, "/layer_only", "layer_only");

        builder.part().modelFile(top).addModel();
        processStepFacing(builder, side);
        processStepFacingBi(builder, corner);
        processStepFacingBi(builder, leg);
        processStepFacingTri(builder, layerOnly, false, false);
        processStepFacingTri(builder, layer, true, true);
        processStepFacingTri(builder, layerCorner, true, false);
        processStepFacingTri(builder, layerCornerY , false, true);
    }

    @SuppressWarnings("unchecked")
    public static void buildTemplate1Item(TableBlock block, boolean isStrict, ItemModelProvider provider) {
        List<Pair<String,String>> impl = new ArrayList<>();
        if (isStrict) {
            impl.add(Pair.of("top", "texture_top"));
            impl.add(Pair.of("side", "texture_side"));
            impl.add(Pair.of("bottom", "texture_bottom"));
        } else {
            impl.add(Pair.of("top", "top"));
            impl.add(Pair.of("side", "top"));
            impl.add(Pair.of("bottom", "top"));
        }
        impl.add(Pair.of("corner", "corner"));
        impl.add(Pair.of("leg", "leg"));
        impl.add(Pair.of("leg_layer", "leg_layer"));
        impl.add(Pair.of("layer_only", "layer_only"));

        buildTableModelForItem(provider, block, impl.toArray(Pair[]::new));
    }

    @SuppressWarnings("unchecked")
    public static ModelFile buildTableModelForBlock(BlockModelProvider provider, TableBlock block, String prefix, String ...keys) {
        BlockModelBuilder builder = provider.withExistingParent(ModelProvider.BLOCK_FOLDER + "/" + TABLE_RES + "/" + path(block) + prefix, asModBlock("table/template" + prefix));
        return buildTableModel(builder, block, Arrays.stream(keys).map(s -> new Pair<>(s, s)).toArray(Pair[]::new));
    }

    @SafeVarargs
    public static ModelFile buildTableModelForBlock(BlockModelProvider provider, TableBlock block, String prefix, Pair<String, String> ...keys) {
        BlockModelBuilder builder = provider.withExistingParent(ModelProvider.BLOCK_FOLDER + "/" + TABLE_RES + "/" + path(block) + prefix, asModBlock("table/template" + prefix));
        return buildTableModel(builder, block, keys);
    }

    @SafeVarargs
    public static void buildTableModelForItem(ItemModelProvider provider, TableBlock block, Pair<String, String> ...keys) {
        ItemModelBuilder builder = provider.withExistingParent(ModelProvider.ITEM_FOLDER + "/" + path(block), asModItem("template_table"));
        buildTableModel(builder, block, keys);
    }

    @SafeVarargs
    public static <T extends ModelBuilder<T>> ModelFile buildTableModel(ModelBuilder<T> builder, TableBlock block, Pair<String, String> ...keys) {
        for (Pair<String, String> key : keys) {
            try {
                builder.texture(key.getFirst(), asModBlock(TABLE_RES + "/" + path(block) + "/" + key.getSecond()));
            } catch (IllegalArgumentException ignore) {
                warn(path(block), key.getSecond());
                try {
                    builder.texture(key.getFirst(), asModBlock(TABLE_RES + "/default/" + key.getSecond()));
                } catch (IllegalArgumentException e) {
                    report(key.getSecond(), e);
                }
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
