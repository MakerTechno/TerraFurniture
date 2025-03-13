package nowebsite.makertechno.terra_furniture.common.block.chair;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ToiletBlock extends AbstractChairBlock {
    public static final MapCodec<ToiletBlock> CODEC = simpleCodec(ToiletBlock::new);
    public ToiletBlock(Properties properties) {
        super(properties);
    }
    @Override
    public Vec3 sitPos() {
        return new Vec3(0, 0.2, 0);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(4.0, 0.0, 4.0, 12.0, 11.0, 12.0);
    }

    @Override
    protected MapCodec<ToiletBlock> codec() {
        return CODEC;
    }
}
