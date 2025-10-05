package org.confluence.terra_furniture.common.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

// 2025/10/4-22:06 TODO: Do we need this anymore? Only a shape define not make sense.
@Deprecated
public class TFLanternBlock extends SwitchableLightBlock{
    public TFLanternBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    @Override
    protected VoxelShape getShape(BlockState p_304673_, BlockGetter p_304919_, BlockPos p_304930_, CollisionContext p_304757_) {
        return Block.box(5.0, 3.0, 5.0, 11.0, 11.0, 11.0);
    }

}
