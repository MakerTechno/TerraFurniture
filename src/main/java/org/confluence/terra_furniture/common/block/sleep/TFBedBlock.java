package org.confluence.terra_furniture.common.block.sleep;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.confluence.lib.common.block.HorizontalDirectionalWithForwardTwoPartBlock;
import org.confluence.lib.common.block.StateProperties;
import org.confluence.terra_furniture.client.generators.SingleMulStateBDG;
import org.confluence.terra_furniture.common.block.func.BlockSetGetter;
import org.confluence.terra_furniture.common.block.func.MulStateGetter;
import org.confluence.terra_furniture.common.block.func.TFBlockSetType;
import org.confluence.terra_furniture.common.datagen.empowered.AutoGenBlockData;
import org.confluence.terra_furniture.common.datagen.empowered.BlockDataGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;

public class TFBedBlock extends HorizontalDirectionalWithForwardTwoPartBlock implements AutoGenBlockData<TFBedBlock>, BlockSetGetter<TFBedBlock>, MulStateGetter<StateProperties.ForwardTwoPart> {
    public static final VoxelShape PANE = Shapes.box(0, 0.1875, 0, 1, 0.5625, 1);
    public static final VoxelShape FORWARD_LEG1 = Shapes.box(0, 0, 0.8125, 0.1875, 0.1875, 1);
    public static final VoxelShape FORWARD_LEG2 = Shapes.box(0, 0, 0.8125, 0.1875, 0.1875, 1);
    public static final VoxelShape BASE_LEG1 = Shapes.box(0, 0, 0, 0.1875, 0.1875, 0.1875);
    public static final VoxelShape BASE_LEG2 = Shapes.box(0.8125, 0, 0, 1, 0.1875, 0.1875);

    public static final VoxelShape FORWARD = Shapes.or(PANE, FORWARD_LEG1, FORWARD_LEG2);
    public static final VoxelShape BASE = Shapes.or(PANE, BASE_LEG1, BASE_LEG2);

    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    protected final TFBlockSetType type;
    public TFBedBlock(TFBlockSetType type, Properties properties) {
        super(properties);
        this.type = type;
        registerDefaultState(defaultBlockState().setValue(OCCUPIED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(PART).isBase() ? BASE : FORWARD;
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(level, entity);
        } else {
            this.bounceUp(entity);
        }
    }

    private void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < 0.0) {
            double d0 = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setDeltaMovement(vec3.x, -vec3.y * 0.66F * d0, vec3.z);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(OCCUPIED));
    }

    @Override
    public boolean isBed(BlockState state, BlockGetter level, BlockPos pos, LivingEntity sleeper) {
        return true;
    }

    @Override
    public Direction getBedDirection(BlockState state, LevelReader level, BlockPos pos) {
        return state.getValue(FACING).getOpposite();
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moveByPiston) {
        if (!state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, moveByPiston);
        }
    }

    public static boolean canSetSpawn(Level level) {
        return level.dimensionType().bedWorks();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.CONSUME;

        if (!canSetSpawn(level)) {
            level.removeBlock(pos, false);
            BlockPos blockpos = pos.relative(state.getValue(FACING).getOpposite());
            if (level.getBlockState(blockpos).is(this)) {
                level.removeBlock(blockpos, false);
            }

            Vec3 vec3 = pos.getCenter();
            level.explode(null, level.damageSources().badRespawnPointExplosion(vec3), null, vec3, 5.0F, true, Level.ExplosionInteraction.BLOCK);
            return InteractionResult.SUCCESS;
        } else if (state.getValue(OCCUPIED)) {
            if (!kickVillagerOutOfBed(level, pos)) {
                player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
            }
        } else {
            if (state.getValue(PART).isBase()) {
                pos = pos.relative(StateProperties.ForwardTwoPart.getConnectedDirection(state));
            }
            player.startSleepInBed(pos).ifLeft(problem -> {
                if (problem.getMessage() != null) {
                    player.displayClientMessage(problem.getMessage(), true);
                }
            });
        }
        return InteractionResult.SUCCESS;
    }

    private boolean kickVillagerOutOfBed(Level level, BlockPos pos) {
        List<Villager> list = level.getEntitiesOfClass(Villager.class, new AABB(pos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        } else {
            list.getFirst().stopSleeping();
            return true;
        }
    }

    @Override
    public TFBlockSetType getType() {
        return type;
    }

    @Override
    public boolean hasParticle(TFBedBlock block) {
        return false;
    }

    @Override
    public EnumProperty<StateProperties.ForwardTwoPart> getContainer() {
        return PART;
    }

    public String getBlockTypeName() {
        return "bed";
    }

    public boolean isSingleTexture() {
        return false;
    }

    public void addExtraTags(HashSet<TagKey<Block>> keys) {}

    @Override
    public @Nullable BlockDataGenerator<? super TFBedBlock> getGenerator() {
        return new SingleMulStateBDG<>() {
            @Override
            public String getTemplateType(TFBedBlock block) {
                return getBlockTypeName();
            }

            @Override
            public void addBlockTags(TFBedBlock block, BlockTagsProvider provider, HashSet<TagKey<Block>> keys) {
                super.addBlockTags(block, provider, keys);
                keys.add(BlockTags.BEDS);
                addExtraTags(keys);
            }

            @Override
            public boolean isSingleTexture() {
                return TFBedBlock.this.isSingleTexture();
            }
        };
    }
}
