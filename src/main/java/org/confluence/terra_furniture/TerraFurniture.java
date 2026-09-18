package org.confluence.terra_furniture;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_furniture.client.event.TFModClient;
import org.confluence.terra_furniture.common.event.TFEvents;
import org.confluence.terra_furniture.common.event.TFGameEvents;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFEntities;
import org.confluence.terra_furniture.common.init.TFItems;
import org.confluence.terra_furniture.common.init.TFRegistries;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TerraFurniture.MODID)
public class TerraFurniture {
    public static final String MODID = "terra_furniture";
    public static final Logger LOGGER = LoggerFactory.getLogger("Terra Furniture");

    public TerraFurniture(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();
        TFItems.init();
        TFBlocks.register(eventBus);
        TFEntities.ENTITIES.register(eventBus);
        TFRegistries.register(eventBus);
        TFEvents.init();
        TFGameEvents.init();
        if (LibUtils.isPhysicalClient()) {
            TFModClient.init();
        }
    }

    @Contract("_ -> new")
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
