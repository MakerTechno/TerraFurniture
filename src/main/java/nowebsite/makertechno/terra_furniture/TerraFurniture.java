package nowebsite.makertechno.terra_furniture;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import nowebsite.makertechno.terra_furniture.common.init.TFEntities;
import nowebsite.makertechno.terra_furniture.common.init.TFItems;
import nowebsite.makertechno.terra_furniture.common.init.TFTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TerraFurniture.MODID)
public class TerraFurniture {
    // TODO: 要是换DataGen多好啊( 变种方块json会累死人的
    public static final String MODID = "terra_furniture";
    public static final Logger LOGGER = LoggerFactory.getLogger("Terra Furniture");

    public TerraFurniture(IEventBus modEventBus, ModContainer modContainer) {
        TFItems.register(modEventBus);
        TFBlocks.register(modEventBus);
        TFEntities.ENTITIES.register(modEventBus);
        TFTabs.TABS.register(modEventBus);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
