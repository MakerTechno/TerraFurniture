package org.confluence.terra_furniture.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.confluence.terra_furniture.client.renderer.item.SpruceChestItemRenderer;

import java.util.function.Consumer;

public class SpruceChestItem extends BlockItem {
    public SpruceChestItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new SpruceChestItemRenderer();
                }
                return renderer;
            }
        });
    }
}
