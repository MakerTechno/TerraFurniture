package org.confluence.terra_furniture.common.block.sleep;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.confluence.terra_furniture.common.block.func.set.TFBlockSetType;
import org.confluence.terra_furniture.common.block.func.set.TFBlockType;
import org.confluence.terra_furniture.common.init.TFTags;

import java.util.HashSet;

public class BathtubBlock extends TFBedBlock {
    private static final VoxelShape SOUTH_SHAPE = Shapes.join(box(0, 0, 0, 16, 12, 16), box(1, 1, 0, 15, 12, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape WEST_SHAPE = Shapes.join(box(0, 0, 0, 16, 12, 16), box(1, 1, 1, 16, 12, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape NORTH_SHAPE = Shapes.join(box(0, 0, 0, 16, 12, 16), box(1, 1, 1, 15, 12, 16), BooleanOp.ONLY_FIRST);
    private static final VoxelShape EAST_SHAPE = Shapes.join(box(0, 0, 0, 16, 12, 16), box(0, 1, 1, 15, 12, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape[] BASE_SHAPES = new VoxelShape[]{SOUTH_SHAPE, WEST_SHAPE, NORTH_SHAPE, EAST_SHAPE};
    private static final VoxelShape[] FORWARD_SHAPES = new VoxelShape[]{NORTH_SHAPE, EAST_SHAPE, SOUTH_SHAPE, WEST_SHAPE};

    public BathtubBlock(TFBlockSetType type, Properties properties) {
        super(type, properties);
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int index = state.getValue(FACING).get2DDataValue();
        return switch (state.getValue(PART)) {
            case BASE -> BASE_SHAPES[index];
            case FORWARD -> FORWARD_SHAPES[index];
        };
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
    }

    @Override
    public TFBlockType<? extends BathtubBlock> getBlockType() {
        return TFBlockType.BATHTUB;
    }

    @Override
    public boolean isSingleTexture() {
        return true;
    }

    @Override
    public void addExtraTags(HashSet<TagKey<Block>> keys) {
        super.addExtraTags(keys);
        keys.add(TFTags.BATHTUBS);
    }

    @Override
    public boolean hasParticle(TFBedBlock block) {
        return true;
    }

    @Override
    public boolean needItemTexture() {
        return true;
    }
}
