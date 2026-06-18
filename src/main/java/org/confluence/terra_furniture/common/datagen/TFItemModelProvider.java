package org.confluence.terra_furniture.common.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.datagen.empowered.BlockDataGenerator;
import org.confluence.terra_furniture.common.init.TFBlocks;

import java.util.Objects;

public class TFItemModelProvider extends ItemModelProvider {
    public TFItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TerraFurniture.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleBlockItem(TFBlocks.GLASS_KILN.get());
        simpleBlockItem(TFBlocks.LIVING_LOOM.get());
        TFDataGenerator.GENERATORS.forEach((block, blockDataGenerator) -> invokeGenerator(block, blockDataGenerator, this));
    }

    public ItemModelBuilder simpleBlockItem(Block block) {
        return simpleBlockItem(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block)));
    }

    public ItemModelBuilder simpleBlockItem(ResourceLocation block) {
        return withExistingParent(block.toString(), ResourceLocation.fromNamespaceAndPath(block.getNamespace(), "block/" + block.getPath()));
    }

    @SuppressWarnings("unchecked")
    private <T extends Block> void invokeGenerator(Block block, BlockDataGenerator<?> generator, ItemModelProvider provider) {
        BlockDataGenerator<T> typedGenerator = (BlockDataGenerator<T>) generator;
        typedGenerator.buildItemWithTemplate((T) block, provider, existingFileHelper);
    }
}
