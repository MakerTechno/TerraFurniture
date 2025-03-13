package nowebsite.makertechno.terra_furniture.common.block.light;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CopperBulbBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public abstract class SwitchableLightBlock extends CopperBulbBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected final SimpleParticleType flameParticle;
    protected static final MapCodec<SimpleParticleType> PARTICLE_OPTIONS_FIELD;
    public SwitchableLightBlock(SimpleParticleType flameParticle,BlockBehaviour.Properties properties) {
        super(properties);
        this.flameParticle = flameParticle;
        this.registerDefaultState(stateDefinition.any().setValue(LIT, true).setValue(POWERED, false).setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED,POWERED,LIT);
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockState blockstate = state.cycle(LIT);
        level.playSound(null, pos, blockstate.getValue(LIT) ? SoundEvents.COPPER_BULB_TURN_ON : SoundEvents.COPPER_BULB_TURN_OFF, SoundSource.BLOCKS);
        level.setBlock(pos, blockstate, 3);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        return defaultBlockState()
                .setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER)
                .setValue(POWERED, false)
                .setValue(LIT, true);
    }
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (oldState.getBlock() != state.getBlock() && level instanceof ServerLevel serverlevel) {
            this.checkAndFlip(state, serverlevel, pos);
        }
    }
    @Override
    public void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
        boolean flag = level.hasNeighborSignal(pos);
        if (flag != state.getValue(POWERED)) {
            BlockState blockstate = state.cycle(LIT);
            level.playSound(null, pos, blockstate.getValue(LIT) ? SoundEvents.COPPER_BULB_TURN_ON : SoundEvents.COPPER_BULB_TURN_OFF, SoundSource.BLOCKS);
            level.setBlock(pos, blockstate.setValue(POWERED, flag), 3);
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    static {
        PARTICLE_OPTIONS_FIELD = BuiltInRegistries.PARTICLE_TYPE.byNameCodec().comapFlatMap((p_304958_) -> {
            DataResult var10000;
            if (p_304958_ instanceof SimpleParticleType simpleparticletype) {
                var10000 = DataResult.success(simpleparticletype);
            } else {
                var10000 = DataResult.error(() -> {
                    return "Not a SimpleParticleType: " + String.valueOf(p_304958_);
                });
            }
            return var10000;
        }, (p_304720_) -> {return (ParticleType<?>) p_304720_;}).fieldOf("particle_options");
    }
}
