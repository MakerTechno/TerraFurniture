package org.confluence.terra_furniture;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFEntities;
import org.confluence.terra_furniture.common.init.TFItems;
import org.confluence.terra_furniture.common.init.TFRegistries;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TerraFurniture.MODID)
public class TerraFurniture {
    public static final String MODID = "terra_furniture";
    public static final Logger LOGGER = LoggerFactory.getLogger("Terra Furniture");

    public TerraFurniture(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();
        TFItems.register(eventBus);
        TFBlocks.register(eventBus);
        TFEntities.ENTITIES.register(eventBus);
        TFRegistries.register(eventBus);
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
