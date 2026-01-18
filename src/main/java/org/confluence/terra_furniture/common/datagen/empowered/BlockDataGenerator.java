package org.confluence.terra_furniture.common.datagen.empowered;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_furniture.TerraFurniture;

import java.util.List;

public interface BlockDataGenerator<T extends Block> {
    String TEMPLATE_FOLDER = "templates";

    void buildBlockWithTemplate(T block, BlockStateProvider builderProvider, ExistingFileHelper helper);

    void buildItemWithTemplate(T block, ItemModelProvider provider, ExistingFileHelper helper);

    String getTemplateType(T block);

    List<TagKey<Block>> getRegBlockTags(T block, BlockTagsProvider provider);

    List<TagKey<Item>> getRegItemTags(ItemTagsProvider provider);

    default String getTemplateLoc(T block) {
        return TEMPLATE_FOLDER + "/" + getTemplateType(block);
    }

    default String prefix(String loc, AccessType type) {
        return type.prefix(loc);
    }

    default ResourceLocation toResourceLocation(String loc) {
        return TerraFurniture.asResource(loc);
    }

    default String getBlockPath(T block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    default void warn(String targetId, String id) {
        TerraFurniture.LOGGER.warn("Can't get {} for {} ! Using default texture!", targetId, id);
    }

    default void report(String targetId) {
        TerraFurniture.LOGGER.error("I can't find target {} for default usage!", targetId); // TODO: location
    }
    default void report(String targetId, Exception e) {
        TerraFurniture.LOGGER.error("I can't find target {} for default usage!", targetId, e); // TODO: location
    }

    enum AccessType {
        BLOCK("block"),
        ITEM("item");

        private final String type;
        AccessType(String type) {
            this.type = type;
        }

        public String prefix(String loc) {
            return this.type + "/" + loc;
        }

    }
}
