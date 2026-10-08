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
import org.confluence.terra_furniture.common.entity.RideableEntityNull;
import org.confluence.terra_furniture.common.init.TFEntities;
import org.jetbrains.annotations.Nullable;

/**
 * 可乘坐方块实体基类，提供一个座椅并在有乘客时阻止其他人乘坐。
 * @apiNote 记得手动触发tickAtServer。
 * @apiNote 当玩家交互时，正常检测后直接调用useAct即可。
 */
public abstract class BaseSittableBE<T extends BaseSittableBE<T>> extends BlockEntity {
    protected static final int PLAYER_SIT_ON_CHECK_DELAY = 10;

    protected @Nullable RideableEntityNull sit;
    protected int delayer = 0;

    public BaseSittableBE(BlockEntityType<? extends T> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    /**
     * 请在方块ticker的服务端提交本更新方法，用来检查是否有玩家占用椅子。
     */
    public void tickAtServer() {
        if (level == null || level.isClientSide) {
            return;
        }
        if (delayer < PLAYER_SIT_ON_CHECK_DELAY) {
            delayer++;
            return;
        }
        if (sit != null && (sit.isRemoved() || !sit.isVehicle())) {
            cleanSeat();
        }
    }

    /**
     * @apiNote 无需空值检查，但注意外部必须实现{@link #tickAtServer}才能保证及时解除占用状态。
     */
    public InteractionResult useAct(Level level, BlockPos pos, Player player) {
        if (level.isClientSide || isRemoved()) {
            return InteractionResult.PASS;
        }
        if (sit != null && sit.isRemoved()) {
            cleanSeat();
        }
        if (sit != null) {
            return InteractionResult.PASS;
        }

        delayer = 0;
        RideableEntityNull seat = new RideableEntityNull(TFEntities.NULL_RIDE.get(), level, worldPosition);
        seat.setPos(pos.getX() + 0.5, pos.getY() + getYSvOffset(), pos.getZ() + 0.5);
        sit = seat;
        // Entity spawn and mount events may remove the block or replace its seat.
        boolean mounted = level.addFreshEntity(seat) && sit == seat && !seat.isRemoved()
                && player.startRiding(seat, true);
        if (!mounted || seat.isRemoved() || sit != seat) {
            player.displayClientMessage(Component.translatable("msg.terra_furniture.sit"), true);
            if (sit == seat) {
                cleanSeat();
            } else {
                seat.discard();
            }
            return InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * 获取当前乘坐者，当无法获取时返回null。
     */
    public @Nullable Entity getPassenger() {
        return sit == null ? null : sit.getFirstPassenger();
    }

    /**
     * 获取实际乘坐的实体......我想起了弹射座椅。
     */
    public @Nullable RideableEntityNull getSit() {
        return sit;
    }

    /**
     * 清除当前座椅；卸载方块、乘客离开或乘坐失败时调用。
     */
    public void cleanSeat() {
        RideableEntityNull seat = sit;
        sit = null;
        if (seat != null) {
            seat.discard();
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null && !level.isClientSide) {
            cleanSeat();
        }
    }

    /**
     * 继承以设置玩家乘坐位置。
     */
    public abstract double getYSvOffset();
}
