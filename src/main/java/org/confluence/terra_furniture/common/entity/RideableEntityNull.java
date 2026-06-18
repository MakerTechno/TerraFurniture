package org.confluence.terra_furniture.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.confluence.terra_furniture.common.block.func.be.BaseSittableBE;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.wrapper.entity.IPortEntityWithComplexSpawn;
import org.mesdag.portlib.wrapper.world.entity.vehicle.PortVehicleEntity;

/**
 * 一个仅用来乘坐的实体，与{@link BaseSittableBE}相互作用以确保自身在无乘客时卸载。<p>
 * 当对应方块实体存在，应通过方块实体卸载本实体。<p>
 * 当对应方块实体脱离访问范围，实体应卸载自己。
 */
public class RideableEntityNull extends PortVehicleEntity implements IPortEntityWithComplexSpawn {
    private BlockPos blockEntityPos;

    public RideableEntityNull(EntityType<? extends PortVehicleEntity> entityType, Level level) {
        this(entityType, level, BlockPos.ZERO);
    }

    public RideableEntityNull(EntityType<? extends PortVehicleEntity> entityType, Level level, BlockPos blockEntityPos) {
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
        if (!(blockEntity instanceof BaseSittableBE<?> cast)) {
            this.remove(RemovalReason.DISCARDED);
        } else {
            if (canAddPassenger(this)) cast.cleanSeat();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {}

    @Override
    public void writeSpawnData(PortRegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockEntityPos);
    }

    @Override
    public void readSpawnData(PortRegistryFriendlyByteBuf additionalData) {
        blockEntityPos = additionalData.readBlockPos();
    }
}
