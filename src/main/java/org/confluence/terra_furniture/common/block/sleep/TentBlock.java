package org.confluence.terra_furniture.common.block.sleep;

import com.mojang.datafixers.util.Either;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.EventHooks;
import org.confluence.terra_furniture.api.client.model.CacheItemRefBlockModel;
import org.confluence.terra_furniture.api.client.renderer.item.BaseGeoItemRendererProvider;
import org.confluence.terra_furniture.common.block.func.multi.HorizontalDirectionalWithHorizontalSixPartBlock;
import org.confluence.terra_furniture.common.block.func.multi.TFStateProperties;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class TentBlock extends HorizontalDirectionalWithHorizontalSixPartBlock implements EntityBlock {

    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    public TentBlock(Properties properties) {
        super(properties.noCollission());
        registerDefaultState(defaultBlockState().setValue(OCCUPIED, false));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
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
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(OCCUPIED));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
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

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.CONSUME;

        if (state.getValue(OCCUPIED)) {
            if (!kickVillagerOutOfBed(level, pos)) {
                player.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
            }
        } else {
            pos = state.getValue(PART).toBase(pos, state.getValue(FACING), false);
            sleepWithoutSetRespawnServer((ServerPlayer)player, (ServerLevel)level, pos).ifLeft(problem -> {
                if (problem.getMessage() != null) {
                    player.displayClientMessage(problem.getMessage(), true);
                }
            });
        }
        return InteractionResult.SUCCESS;
    }

    /** 模仿ServerPlayer进行操作 */
    private Either<Player.BedSleepingProblem, Unit> sleepWithoutSetRespawnServer(ServerPlayer player, ServerLevel level, BlockPos at) {
        var vanillaResult = ((java.util.function.Supplier<Either<Player.BedSleepingProblem, net.minecraft.util.Unit>>) () -> {
            
            Direction direction = level.getBlockState(at).getValue(HorizontalDirectionalBlock.FACING);
            if (player.isSleeping() || !player.isAlive()) {
                return Either.left(Player.BedSleepingProblem.OTHER_PROBLEM);
            } else if (!level.dimensionType().natural()) {
                return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
            } else if (!player.bedInRange(at, direction)) {
                return Either.left(Player.BedSleepingProblem.TOO_FAR_AWAY);
            } else if (player.bedBlocked(at, direction)) {
                return Either.left(Player.BedSleepingProblem.OBSTRUCTED);
            } else {
                // player.setRespawnPosition(level.dimension(), at, player.getYRot(), false, true);
                if (level.isDay()) {
                    return Either.left(Player.BedSleepingProblem.NOT_POSSIBLE_NOW);
                } else {
                    if (!player.isCreative()) {
                        double d0 = 8.0;
                        double d1 = 5.0;
                        Vec3 vec3 = Vec3.atBottomCenterOf(at);
                        List<Monster> list = level
                            .getEntitiesOfClass(
                                Monster.class,
                                new AABB(vec3.x() - d0, vec3.y() - d1, vec3.z() - d0, vec3.x() + d0, vec3.y() + d1, vec3.z() + d0),
                                p_9062_ -> p_9062_.isPreventingPlayerRest(player)
                            );
                        if (!list.isEmpty()) {
                            return Either.left(Player.BedSleepingProblem.NOT_SAFE);
                        }
                    }
                }
            }
            return Either.right(net.minecraft.util.Unit.INSTANCE);
        }).get();

        // Fire the event. Return the error if one exists after the event, otherwise use the vanilla logic to start sleeping.
        vanillaResult = EventHooks.canPlayerStartSleeping(player, at, vanillaResult);
        if (vanillaResult.left().isPresent()) {
            return vanillaResult;
        }

        {
            {
                // Start vanilla code
                Either<Player.BedSleepingProblem, net.minecraft.util.Unit> either = sleepWithoutSetRespawnSuperLivingEntity(player, level, at).ifRight(p_9029_ -> {
                    player.awardStat(Stats.SLEEP_IN_BED);
                    CriteriaTriggers.SLEPT_IN_BED.trigger(player);
                });
                if (!level.canSleepThroughNights()) {
                    player.displayClientMessage(Component.translatable("sleep.not_possible"), true);
                }

                level.updateSleepingPlayerList();
                return either;
            }
        }
    }

    private Either<Player.BedSleepingProblem, Unit> sleepWithoutSetRespawnSuperLivingEntity(ServerPlayer player, ServerLevel level, BlockPos at) {

        if (player.isPassenger()) {
            player.stopRiding();
        }

        BlockState state = level.getBlockState(at);
        if (state.isBed(level, at, player)) {
            state.setBedOccupied(level, at, player, true);
        }

        float xFix = switch (state.getValue(FACING)) {
            case NORTH -> 1F;
            case EAST -> -0.5F;
            case SOUTH -> 0F;
            case WEST -> 1.5F;
            default -> 0.0F;
        };
        float zFix = switch (state.getValue(FACING)) {
            case NORTH -> 1.5F;
            case EAST -> 1F;
            case SOUTH -> -0.5F;
            case WEST -> 0F;
            default -> 0.0F;
        };

        player.setPose(Pose.SLEEPING);
        player.setPos((double)at.getX() + xFix, at.getY(), (double)at.getZ() + zFix);
        player.setSleepingPos(at);
        player.setDeltaMovement(Vec3.ZERO);
        player.hasImpulse = true;

        player.sleepCounter = 0;
        return Either.right(Unit.INSTANCE);
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
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART).equals(TFStateProperties.HorizontalSixPart.LEFT_CENTER)) return new BEntity(pos, state);
        else return null;
    }

    public static final class BEntity extends BlockEntity implements GeoBlockEntity {

        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public BEntity(BlockPos pos, BlockState blockState) {
            super(TFBlocks.TENT_BLOCK_ENTITY.get(), pos, blockState);
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return this.cache;
        }
    }
    public static class BItem extends BlockItem implements GeoItem {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public BItem(TentBlock pBlock) {
            super(pBlock, new Properties());
        }

        @Override
        public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
            consumer.accept(new BaseGeoItemRendererProvider<BItem>(new CacheItemRefBlockModel<>(), true));
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }
    }
}
