package org.confluence.terra_furniture.common.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.confluence.terra_furniture.common.block.func.set.TFBlockSetType;
import org.confluence.terra_furniture.common.datagen.empowered.BlockDataGenerator;
import org.jetbrains.annotations.Nullable;

/** A switchable light whose model, blockstate and fitted outline are supplied by hand. */
public class ModelLightBlock extends SwitchableLightBlock {
    private final VoxelShape shape;

    public ModelLightBlock(TFBlockSetType type, Properties properties, BlockShapeType supportType, VoxelShape shape) {
        super(type, properties, supportType);
        this.shape = shape;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public @Nullable BlockDataGenerator<? super SwitchableLightBlock> getGenerator() {
        return null;
    }
}
