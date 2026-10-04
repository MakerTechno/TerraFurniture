package org.confluence.terra_furniture.common.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.terra_furniture.common.init.TFBlocks;

public class SpruceChestBlock extends ChestBlock {
    public SpruceChestBlock(Properties properties) {
        super(properties, TFBlocks.SPRUCE_CHEST_ENTITY::get);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new Entity(pos, state);
    }

    public static class Entity extends ChestBlockEntity {
        public Entity(BlockPos pos, BlockState state) {
            super(TFBlocks.SPRUCE_CHEST_ENTITY.get(), pos, state);
        }

        @Override
        protected Component getDefaultName() {
            return Component.translatable(getBlockState().getBlock().getDescriptionId());
        }
    }
}
