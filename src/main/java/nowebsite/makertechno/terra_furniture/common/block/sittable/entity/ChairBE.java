package nowebsite.makertechno.terra_furniture.common.block.sittable.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import nowebsite.makertechno.terra_furniture.common.block.func.BaseSittableBE;
import org.jetbrains.annotations.NotNull;

public abstract class ChairBE<T extends ChairBE<T>> extends BaseSittableBE<T> {
    public ChairBE(@NotNull DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
}
