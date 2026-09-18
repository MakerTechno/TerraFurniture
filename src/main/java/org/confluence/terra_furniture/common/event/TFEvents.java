package org.confluence.terra_furniture.common.event;

import org.confluence.terra_furniture.network.s2c.PlayerCrossDeltaS2C;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.network.PortRegisterPayloadHandlersEvent;

public class TFEvents {
    public static void init() {
        PortEventHandler.addListener(TFEvents::registerPayloadHandlers);
    }

    private static void registerPayloadHandlers(PortRegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .registerInGameS2C(PlayerCrossDeltaS2C.class, PlayerCrossDeltaS2C.ID, PlayerCrossDeltaS2C.STREAM_CODEC);
    }

// todo   private static void registerCapabilities(RegisterCapabilitiesEvent event) {
//        event.registerBlockEntity(
//                Capabilities.ItemHandler.BLOCK,
//                TFBlocks.HANGING_POT_ENTITY.get(),
//                (blockEntity, direction) -> blockEntity.getLazyItemHandler().get()
//        );
//    }
}
