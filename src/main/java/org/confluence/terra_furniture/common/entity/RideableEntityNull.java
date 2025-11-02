package org.confluence.terra_furniture.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.confluence.terra_furniture.common.block.func.be.BaseSittableBE;
import org.jetbrains.annotations.NotNull;

/**
 * 一个仅用来乘坐的实体，与{@link BaseSittableBE}相互作用以确保自身在无乘客时卸载。
 */
public class RideableEntityNull extends VehicleEntity implements IEntityWithComplexSpawn {
    private BlockPos blockEntityPos;
    public RideableEntityNull(EntityType<? extends VehicleEntity> entityType, Level level) {
        this(entityType, level, BlockPos.ZERO);
    }
    public RideableEntityNull(EntityType<? extends VehicleEntity> entityType, Level level, BlockPos blockEntityPos) {
        super(entityType, level);
        this.blockEntityPos = blockEntityPos;
        setNoGravity(true);
        setInvisible(true);
        horizontalCollision = false;
        minorHorizontalCollision = false;
        verticalCollision = false;
        verticalCollisionBelow = false;
    }
    @Override
    protected Item getDropItem() {
        return Items.AIR;
    }
    @Override
    public void tick() {
        super.tick();
        BlockEntity blockEntity = level().getBlockEntity(blockEntityPos);
        if (!(blockEntity instanceof BaseSittableBE<?> cast)){
            this.remove(RemovalReason.DISCARDED);
        } else {
            if (canAddPassenger(this)) cast.cleanSeat();
        }
    }
    private int count;
    @Override
    public void baseTick() {
        super.baseTick();
        if (count ==50) {
            BlockEntity blockEntity = level().getBlockEntity(blockEntityPos);
            if (!(blockEntity instanceof BaseSittableBE<?> cast)){
                this.remove(RemovalReason.DISCARDED);
            } else {
                if (canAddPassenger(this)) cast.cleanSeat();
            }
        } else {
            count++;
        }
    }
    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {}
    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {}
    @Override
    public void writeSpawnData(@NotNull RegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockEntityPos);
    }
    @Override
    public void readSpawnData(@NotNull RegistryFriendlyByteBuf additionalData) {
        blockEntityPos = additionalData.readBlockPos();
    }
}
