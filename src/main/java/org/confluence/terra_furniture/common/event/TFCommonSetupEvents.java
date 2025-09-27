package org.confluence.terra_furniture.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.network.s2c.PlayerVelocityHandler;
import org.confluence.terra_furniture.network.s2c.packet.PlayerCrossDeltaData;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = TerraFurniture.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TFCommonSetupEvents {
    @SubscribeEvent // on the mod event bus
    public static void register(@NotNull RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.NETWORK);
        registrar.playToClient(
            PlayerCrossDeltaData.TYPE,
            PlayerCrossDeltaData.STREAM_CODEC,
            new PlayerVelocityHandler()
        );
    }

    @SubscribeEvent
    public static void registerCapabilities(@NotNull RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            TFBlocks.HANGING_POT_ENTITY.get(),
            (blockEntity, direction) -> blockEntity.getLazyItemHandler().get()
        );
    }
}
