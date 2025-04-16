package org.confluence.terra_furniture.common.block;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FishBowlBlock extends Block {
    public FishBowlBlock(Properties properties) {
        super(properties);
    }

    public static class Item extends BlockItem {
        public Item(Block block, Properties properties) {
            super(block, properties);
        }

        @Override
        public EquipmentSlot getEquipmentSlot(ItemStack stack) {
            return EquipmentSlot.HEAD;
        }
    }
}
