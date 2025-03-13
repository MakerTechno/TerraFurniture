package nowebsite.makertechno.terra_furniture.common.block.light;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TRCandleBlock extends SwitchableLightBlock {
    protected static final MapCodec<SimpleParticleType> PARTICLE_OPTIONS_FIELD;
    public static final MapCodec<TRCandleBlock> CODEC;
    protected final SimpleParticleType flameParticle;

    public MapCodec<? extends TRCandleBlock> codec() {
        return CODEC;
    }

    public TRCandleBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
        super(properties);
        this.flameParticle = flameParticle;
    }
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockState blockstate = state.cycle(LIT);
        level.playSound(null, pos, blockstate.getValue(LIT) ? SoundEvents.COPPER_BULB_TURN_ON : SoundEvents.COPPER_BULB_TURN_OFF, SoundSource.BLOCKS);
        level.setBlock(pos, blockstate, 3);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)){
            level.addParticle(this.flameParticle, pos.getX() + 0.5, pos.getY() + 0.4, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
        }
    }
    @Override
    protected VoxelShape getShape(BlockState p_304673_, BlockGetter p_304919_, BlockPos p_304930_, CollisionContext p_304757_) {
        return Block.box(6.0, 0.0, 6.0, 10.0, 7.0, 10.0);
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
        CODEC = RecordCodecBuilder.mapCodec((p_308842_) -> p_308842_.group(PARTICLE_OPTIONS_FIELD.forGetter((p_304762_) -> p_304762_.flameParticle), propertiesCodec()).apply(p_308842_, TRCandleBlock::new));

    }
}
