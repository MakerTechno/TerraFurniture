package org.confluence.terra_furniture.network.s2c.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_furniture.TerraFurniture;
import org.jetbrains.annotations.NotNull;

public record PlayerCrossDeltaData(Vec3 delta, BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerCrossDeltaData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(TerraFurniture.MODID, "player_delta"));
    public static final StreamCodec<ByteBuf, PlayerCrossDeltaData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.fromCodec(Vec3.CODEC),
        PlayerCrossDeltaData::delta,
        ByteBufCodecs.fromCodec(BlockPos.CODEC),
        PlayerCrossDeltaData::pos,
        PlayerCrossDeltaData::new
    );
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
