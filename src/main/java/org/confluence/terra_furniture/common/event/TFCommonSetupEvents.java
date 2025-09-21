package org.confluence.terra_furniture.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.network.s2c.PlayerVelocityHandler;
import org.confluence.terra_furniture.network.s2c.packet.PlayerCrossDeltaData;

@EventBusSubscriber(modid = TerraFurniture.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TFCommonSetupEvents {
    @SubscribeEvent // on the mod event bus
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.NETWORK);
        registrar.playToClient(
            PlayerCrossDeltaData.TYPE,
            PlayerCrossDeltaData.STREAM_CODEC,
            new PlayerVelocityHandler()
        );
    }
}
