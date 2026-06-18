package org.confluence.terra_furniture.network.s2c;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.func.be.BaseSwayingBE;
import org.joml.Vector3f;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

/**
 * 同步玩家加速度信息，一般应用在摇晃方块中。
 *
 * @apiNote 远程玩家在本地没有加速度！注意应通过{@link net.minecraft.world.entity.player.Player#getPosition(float)}在1f和0f时的向量差值计算。
 */
public record PlayerCrossDeltaS2C(Vector3f delta, BlockPos pos) implements IPortPacket.S2C {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TerraFurniture.MODID, "player_delta");
    public static final PortStreamCodec<FriendlyByteBuf, PlayerCrossDeltaS2C> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.VECTOR3F, PlayerCrossDeltaS2C::delta,
            PortByteBufCodecs.BLOCK_POS, PlayerCrossDeltaS2C::pos,
            PlayerCrossDeltaS2C::new
    );

    @Override
    public void work(Player player) {
        if (player.level().getBlockEntity(pos) instanceof BaseSwayingBE cast) {
            cast.applyMovingAffectedDelta(new Vec3(delta.x, delta.y, delta.z));
        }
    }

    @Override
    public ResourceLocation identifier() {
        return ID;
    }
}
