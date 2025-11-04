package org.confluence.terra_furniture.common.block.func.be;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terra_furniture.common.entity.RideableEntityNull;
import org.confluence.terra_furniture.common.init.TFEntities;

/**
 * 可乘坐方块实体基类，提供一个座椅并在有乘客时阻止其他人乘坐。
 * @apiNote 记得手动触发tickAtServer
 * @apiNote 当玩家交互时，正常检测后直接调用useAct即可
 */
public abstract class BaseSittableBE<T extends BaseSittableBE<T>> extends BlockEntity {
    public RideableEntityNull sit = null;
    public final BlockState containerBlock;
    private int count = 0;
    public BaseSittableBE(DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> type, BlockPos pos, BlockState blockState) {
        super(type.get(), pos, blockState);
        containerBlock = blockState;
    }
    public void tickAtServer() {
        if (this.sit != null && this.sit.getFirstPassenger() == null && this.count >= 10) {
            this.sit.remove(Entity.RemovalReason.DISCARDED);
            this.sit = null;
        }
        else if (this.count <= 10) {
            ++this.count;
        }
    }
    /**
     * @apiNote 不提供空值检查等基础功能，需由外部包装。
     */
    public InteractionResult useAct(Level level, BlockPos pos, Player player) {
        if (this.sit == null) {
            this.count = 0;
            this.sit = new RideableEntityNull(TFEntities.NULL_RIDE.get(), level, this.worldPosition);
            this.sit.setPos((double)pos.getX() + 0.5, (double)pos.getY() + getYSvOffset(), (double)pos.getZ() + 0.5);
            level.addFreshEntity(this.sit);
            if (!player.startRiding(this.sit, true)) {
                player.displayClientMessage(Component.translatable("msg.terra_furniture.sit"), true);
                this.sit.remove(Entity.RemovalReason.DISCARDED);
                this.sit = null;
                return InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }
    public void cleanSeat() {
        if (this.sit != null) {
            this.sit.remove(Entity.RemovalReason.DISCARDED);
            this.sit = null;
        }
    }
    public abstract double getYSvOffset();
}
