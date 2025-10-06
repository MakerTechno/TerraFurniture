package org.confluence.terra_furniture.common.block.misc;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

@Deprecated
// Excuse me?
public class FishBowlBlock extends Block {
    public FishBowlBlock(Properties properties) {
        super(properties);
    }

    public static class Item extends BlockItem {
        public Item(Block block, Properties properties) {
            super(block, properties);
        }

        @Override
        public EquipmentSlot getEquipmentSlot(@NotNull ItemStack stack) {
            return EquipmentSlot.HEAD;
        }
    }
}
