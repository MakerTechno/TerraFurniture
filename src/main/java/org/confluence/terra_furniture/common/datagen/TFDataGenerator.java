package org.confluence.terra_furniture.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.confluence.terra_furniture.TerraFurniture;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TerraFurniture.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class TFDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        boolean client = event.includeClient();
        generator.addProvider(client, new TFChineseProvider(output));
        generator.addProvider(client, new TFEnglishProvider(output));
        generator.addProvider(client, new TFBlockStateProvider(output, helper));
        generator.addProvider(client, new TFItemModelProvider(output, helper));


        boolean server = event.includeServer();
        TFBlockTagsProvider blockTagsProvider = new TFBlockTagsProvider(output, lookup, helper);
        generator.addProvider(server, blockTagsProvider);
        generator.addProvider(server, new TFItemTagsProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(server, new TFLootTableProvider(output, lookup));
        generator.addProvider(server, new TFRecipeProvider(output, lookup));
    }
}
