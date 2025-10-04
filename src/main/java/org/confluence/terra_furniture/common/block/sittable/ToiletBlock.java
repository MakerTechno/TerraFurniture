package org.confluence.terra_furniture.common.block.sittable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ToiletBlock extends ChairBlock {
    public static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 11.0, 12.0);

    public ToiletBlock(BlockState state, Properties properties, float yOff) {
        super(state, properties, yOff);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
}
