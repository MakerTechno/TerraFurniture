package org.confluence.terra_furniture.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.confluence.terra_furniture.common.block.misc.HangingPotBlock;

public class HangingPotBER implements BlockEntityRenderer<HangingPotBlock.BEntity> {
    @Override
    public void render(HangingPotBlock.BEntity bEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (bEntity.getFeatureStack().isEmpty()) return;
        BlockState state = ((BlockItem)bEntity.getFeatureStack().getItem()).getBlock().defaultBlockState();
        if (state.getBlock() instanceof TorchBlock torchBlock) {
            if (torchBlock.equals(Blocks.TORCH)) state = Blocks.FIRE.defaultBlockState();
            else if (torchBlock.equals(Blocks.SOUL_TORCH)) state = Blocks.SOUL_FIRE.defaultBlockState();
        }

        // pose stack......

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
            state,
            poseStack,
            multiBufferSource,
            i,
            i1,
            ModelData.EMPTY,
            RenderType.CUTOUT
        );
    }
}
