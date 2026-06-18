package org.confluence.terra_furniture.common.init;

import org.confluence.terra_furniture.TerraFurniture;
import org.mesdag.portlib.registries.PortItemRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;

public final class TFItems {
    public static void init() {}

    public static final PortItemRegistration ITEMS = PortRegisterHandler.item(TerraFurniture.MODID);
    public static final PortItemRegistration BLOCK_ITEMS = PortRegisterHandler.item(TerraFurniture.MODID);
}
