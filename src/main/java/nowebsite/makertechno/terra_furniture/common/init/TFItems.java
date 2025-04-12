package nowebsite.makertechno.terra_furniture.common.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import nowebsite.makertechno.terra_furniture.TerraFurniture;

public final class TFItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraFurniture.MODID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(TerraFurniture.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
    }
}
