package nowebsite.makertechno.terra_furniture.common.block.chair;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChairBlock extends AbstractChairBlock {
    public static final MapCodec<ChairBlock> CODEC = simpleCodec(ChairBlock::new);
    public ChairBlock(Properties properties) {
        super(properties);
    }
    @Override
    public Vec3 sitPos() {
        return new Vec3(0, 0, 0);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    }

    @Override
    protected MapCodec<ChairBlock> codec() {
        return CODEC;
    }
}
