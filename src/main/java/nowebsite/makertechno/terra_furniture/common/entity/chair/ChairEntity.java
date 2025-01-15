package nowebsite.makertechno.terra_furniture.common.entity.chair;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import nowebsite.makertechno.terra_furniture.common.block.chair.AbstractChairBlock;
import org.jetbrains.annotations.NotNull;

public class ChairEntity extends Entity {
    public ChairEntity(EntityType<ChairEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        Block chair = level().getBlockState(getOnPos()).getBlock();
        if (getPassengers().isEmpty() || !(chair instanceof AbstractChairBlock)) {
            discard();
        }
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(@NotNull LivingEntity passenger) {
        return new Vec3(getX(), getY() + 1.0D, getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {}
}
