package nowebsite.makertechno.terra_furniture.common.block.sittable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ToiletBlock extends ChairBlock {
    public ToiletBlock(BlockState state, Properties properties) {
        super(state, properties);
    }
    @Override
    protected double getYOffset() {
        return 0.2;
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(4.0, 0.0, 4.0, 12.0, 11.0, 12.0);
    }
}
