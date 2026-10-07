package org.confluence.terra_furniture.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChestBlock;
import org.confluence.terra_furniture.common.block.misc.SpruceChestBlock;
import org.confluence.terra_furniture.common.init.TFBlocks;

/// 物品展示复用云杉木箱的方块实体渲染器。
public class SpruceChestItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final SpruceChestBlock.Entity chest = new SpruceChestBlock.Entity(BlockPos.ZERO,
            TFBlocks.SPRUCE_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));

    public SpruceChestItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack,
                             MultiBufferSource buffers, int packedLight, int packedOverlay) {
        Minecraft.getInstance().getBlockEntityRenderDispatcher()
                .renderItem(chest, poseStack, buffers, packedLight, packedOverlay);
    }
}
