package org.confluence.terra_furniture.common.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.confluence.terra_furniture.TerraFurniture;

public class TFTags {
    public static final TagKey<Block> GLASS_FURNITURE = register("glass_furniture");
    public static final TagKey<Block> DUNGEON_FURNITURE = register("dungeon_furniture");
    private static TagKey<Block> register(String id) {
        return BlockTags.create(TerraFurniture.asResource(id));
    }
}
