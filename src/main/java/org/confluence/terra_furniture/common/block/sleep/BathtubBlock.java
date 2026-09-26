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
import org.confluence.lib.common.block.StateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class BathtubBlock extends TFBedBlock {
    private static final VoxelShape SOUTH_SHAPE = Shapes.join(box(0, 0, 0, 16, 12, 16), box(1, 1, 0, 15, 12, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape WEST_SHAPE = Shapes.join(box(0, 0, 0, 16, 12, 16), box(1, 1, 1, 16, 12, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape NORTH_SHAPE = Shapes.join(box(0, 0, 0, 16, 12, 16), box(1, 1, 1, 15, 12, 16), BooleanOp.ONLY_FIRST);
    private static final VoxelShape EAST_SHAPE = Shapes.join(box(0, 0, 0, 16, 12, 16), box(0, 1, 1, 15, 12, 15), BooleanOp.ONLY_FIRST);
    private static final VoxelShape[] BASE_SHAPES = new VoxelShape[]{SOUTH_SHAPE, WEST_SHAPE, NORTH_SHAPE, EAST_SHAPE};
    private static final VoxelShape[] FORWARD_SHAPES = new VoxelShape[]{NORTH_SHAPE, EAST_SHAPE, SOUTH_SHAPE, WEST_SHAPE};

    /// 按开口方向排列的自定义缸体碰撞箱；为 null 时使用上面那套默认形状
    private final VoxelShape @Nullable [] customShapes;
    /// 两半是否共用一张贴图
    private final boolean singleTexture;

    public BathtubBlock(TFBlockSetType type, Properties properties) {
        this(type, properties, null, true);
    }

    /**
     * @param shapes        自定义碰撞箱，见 {@link #tubShapes(int, int)}；为 null 时使用默认的 12 像素高缸体形状
     * @param singleTexture 两半是否共用一张贴图（{@code block/<材质组>/<方块id>.png}）。为 false 时沿用床的贴图规范：base / forward 各一张贴图，且物品模型自带贴图
     */
    public BathtubBlock(TFBlockSetType type, Properties properties, VoxelShape @Nullable [] shapes, boolean singleTexture) {
        super(type, properties);
        this.customShapes = shapes;
        this.singleTexture = singleTexture;
    }

    /**
     * 生成朝相邻半块开口的缸体碰撞箱。
     */
    public static VoxelShape[] tubShapes(int height, int thickness) {
        VoxelShape outer = box(0, 0, 0, 16, height, 16);
        return new VoxelShape[]{
                Shapes.join(outer, box(thickness, thickness, thickness, 16 - thickness, height, 16), BooleanOp.ONLY_FIRST),
                Shapes.join(outer, box(0, thickness, thickness, 16 - thickness, height, 16 - thickness), BooleanOp.ONLY_FIRST),
                Shapes.join(outer, box(thickness, thickness, 0, 16 - thickness, height, 16 - thickness), BooleanOp.ONLY_FIRST),
                Shapes.join(outer, box(thickness, thickness, thickness, 16, height, 16 - thickness), BooleanOp.ONLY_FIRST)
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (customShapes != null) {
            int openDirection = StateProperties.ForwardTwoPart.getConnectedDirection(state).get2DDataValue();
            return customShapes[openDirection];
        }
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
        return singleTexture;
    }

    @Override
    public void addExtraTags(HashSet<TagKey<Block>> keys) {
        super.addExtraTags(keys);
        keys.add(TFTags.BATHTUBS);
    }

    @Override
    public boolean hasParticle(TFBedBlock block) {
        return singleTexture;
    }

    @Override
    public boolean needItemTexture() {
        return singleTexture;
    }
}
