package org.confluence.terra_furniture.common.block.light;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.data.BlockTagsProvider;
import org.confluence.terra_furniture.client.generators.DefaultBlockDataGenerator;
import org.confluence.terra_furniture.common.block.func.set.TFBlockSetType;
import org.confluence.terra_furniture.common.block.func.set.TFBlockType;
import org.confluence.terra_furniture.common.datagen.empowered.BlockDataGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

import static org.confluence.terra_furniture.common.init.TFBlockSetTypes.BLUE_DUNGEON;
import static org.confluence.terra_furniture.common.init.TFBlockSetTypes.GLASS;
import static org.confluence.terra_furniture.common.init.TFBlockSetTypes.SPRUCE;

public class CandelabraBlock extends SwitchableLightBlock {
    public static final VoxelShape SHAPE = Block.box(3, 0, 3, 13 , 16, 13);
    private static final VoxelShape SPRUCE_SHAPE = Shapes.or(
            Block.box(4.75, 0, 6.75, 11.25, 4.5, 9.25),
            Block.box(4.75, 3.5, 6.75, 7.25, 10, 9.25),
            Block.box(8.75, 3.5, 6.75, 11.25, 12, 9.25)
    );
    private static final double[][] GLASS_FLAMES = {
            {8, 17.6, 8}, {3, 16.6, 8}, {13, 16.6, 8}
    };
    private static final double[][] BLUE_DUNGEON_FLAMES = {
            {8, 21.6, 8}, {2, 18.6, 8}, {14, 18.6, 8},
            {8, 18.6, 2}, {8, 18.6, 14}
    };
    private static final double[][] SPRUCE_FLAMES = {
            {6.06, 12.6, 8.06}, {10.06, 14.6, 8.06}
    };
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
        return getType() == SPRUCE ? SPRUCE_SHAPE : SHAPE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT) || state.getValue(WATERLOGGED)) {
            return;
        }

        if (getType() == GLASS) {
            addFlames(level, pos, state.getValue(FACING), GLASS_FLAMES, ParticleTypes.SMALL_FLAME, true);
        } else if (getType() == BLUE_DUNGEON) {
            addFlames(level, pos, Direction.NORTH, BLUE_DUNGEON_FLAMES, ParticleTypes.SOUL_FIRE_FLAME, false);
        } else if (getType() == SPRUCE) {
            addFlames(level, pos, Direction.NORTH, SPRUCE_FLAMES, ParticleTypes.SOUL_FIRE_FLAME, false);
        }
    }

    private static void addFlames(Level level, BlockPos pos, Direction facing, double[][] flames,
                                  ParticleOptions particle, boolean rotate) {
        for (double[] flame : flames) {
            double x = flame[0];
            double z = flame[2];
            if (rotate) {
                switch (facing) {
                    case SOUTH -> {
                        x = 16 - flame[0];
                        z = 16 - flame[2];
                    }
                    case EAST -> {
                        x = 16 - flame[2];
                        z = flame[0];
                    }
                    case WEST -> {
                        x = flame[2];
                        z = 16 - flame[0];
                    }
                }
            }
            level.addParticle(particle,
                    pos.getX() + x / 16,
                    pos.getY() + flame[1] / 16,
                    pos.getZ() + z / 16,
                    0, 0, 0);
        }
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
