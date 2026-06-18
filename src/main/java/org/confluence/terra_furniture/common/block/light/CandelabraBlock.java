package org.confluence.terra_furniture.common.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.data.BlockTagsProvider;
import org.confluence.terra_furniture.client.generators.DefaultBlockDataGenerator;
import org.confluence.terra_furniture.common.block.func.set.TFBlockSetType;
import org.confluence.terra_furniture.common.block.func.set.TFBlockType;
import org.confluence.terra_furniture.common.datagen.empowered.BlockDataGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class CandelabraBlock extends SwitchableLightBlock {
    public static final VoxelShape SHAPE = Block.box(3, 0, 3, 13 , 16, 13);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public CandelabraBlock(TFBlockSetType type, Properties properties) {
        super(type, properties, BlockShapeType.DEFAULT);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING,WATERLOGGED,POWERED,LIT);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        return defaultBlockState()
                .setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)
                .setValue(POWERED, false)
                .setValue(LIT, true)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockDataGenerator<? super SwitchableLightBlock> getGenerator() {
        return new DefaultBlockDataGenerator<>() {
            @Override
            public TFBlockType<? extends SwitchableLightBlock> getTemplateType(SwitchableLightBlock block) {
                return TFBlockType.CANDELABRAS;
            }
            @Override
            public void addBlockTags(SwitchableLightBlock block, BlockTagsProvider provider, HashSet<TagKey<Block>> keys) {
            }
        };
    }
}
