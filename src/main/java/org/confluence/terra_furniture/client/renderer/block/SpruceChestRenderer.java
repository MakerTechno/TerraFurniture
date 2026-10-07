package org.confluence.terra_furniture.client.renderer.block;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.misc.SpruceChestBlock;

/// 使用云杉贴图渲染原版单箱及双箱模型。
public class SpruceChestRenderer extends ChestRenderer<SpruceChestBlock.Entity> {
    private static final Material SINGLE = material("spruce_chest");
    private static final Material LEFT = material("spruce_chest_left");
    private static final Material RIGHT = material("spruce_chest_right");

    public SpruceChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    private static Material material(String name) {
        return new Material(Sheets.CHEST_SHEET, TerraFurniture.asResource("block/spruce/" + name));
    }

    @Override
    protected Material getMaterial(SpruceChestBlock.Entity chest, ChestType type) {
        return switch (type) {
            case LEFT -> LEFT;
            case RIGHT -> RIGHT;
            default -> SINGLE;
        };
    }
}
