package nowebsite.makertechno.terra_furniture.common.block.func;

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
import nowebsite.makertechno.terra_furniture.common.entity.RideableEntityNull;
import nowebsite.makertechno.terra_furniture.common.init.TFEntities;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class BaseSittableBE<T extends BaseSittableBE<T>> extends BlockEntity {
    public RideableEntityNull sit = null;
    public final BlockState containerBlock;
    private int count = 0;
    public BaseSittableBE(@NotNull DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> type, BlockPos pos, BlockState blockState) {
        super(type.get(), pos, blockState);
        containerBlock = blockState;
    }
    public void tickAtServer() {
        if (this.sit != null && this.sit.getFirstPassenger() == null && this.count >= 10) {
            this.sit.remove(Entity.RemovalReason.DISCARDED);
            this.sit = null;
        } else if (this.count <= 10) {
            ++this.count;
        }
    }
    public InteractionResult useAct(Level level, BlockPos pos, Player player) {
        if (this.sit == null) {
            this.count = 0;
            this.sit = new RideableEntityNull(TFEntities.NULL_RIDE.get(), level, this.worldPosition);
            this.sit.setPos((double)pos.getX() + 0.5, (double)pos.getY() + getYSvOffset(), (double)pos.getZ() + 0.5);
            /*int rotate = switch (direction) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            sit.setYRot(180 + rotate);*/
            level.addFreshEntity(this.sit);
            if (!player.startRiding(this.sit, true)) {
                player.displayClientMessage(Component.translatable("msg.furnitureplan.sit"), true);
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
    @Override
    public void setRemoved() {
        Objects.requireNonNull(getLevel()).removeBlockEntity(getBlockPos());
        newOneFromBlock();
        if (this.sit != null) {
            this.sit.remove(Entity.RemovalReason.DISCARDED);
            this.sit = null;
        }
    }
    public abstract void newOneFromBlock();
    public abstract double getYSvOffset();
}
