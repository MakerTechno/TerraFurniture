package org.confluence.terra_furniture.network.s2c;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import org.confluence.terra_furniture.common.block.light.LargeChandelierBlock;
import org.confluence.terra_furniture.network.s2c.packet.PlayerCrossDeltaData;
import org.jetbrains.annotations.NotNull;

public class PlayerVelocityHandler implements IPayloadHandler<PlayerCrossDeltaData> {

    @Override
    public void handle(@NotNull PlayerCrossDeltaData data, @NotNull IPayloadContext context) {
        context.enqueueWork(() -> function(context, data))
            .exceptionally(e -> {
                context.disconnect(Component.translatable("confluence.networking.failed", e.getMessage()));
                return null;
            });
    }
    public static void function(@NotNull IPayloadContext context, @NotNull PlayerCrossDeltaData data){
        BlockPos pos = BlockPos.containing(data.pos().getX(), data.pos().getY(), data.pos().getZ());
        if (context.player().level().getBlockEntity(pos) instanceof LargeChandelierBlock.BEntity cast) {
            cast.applyDelta(data.delta());
        }
    }
}
