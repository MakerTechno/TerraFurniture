package nowebsite.makertechno.terra_furniture.common.block.light;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class TFCandleBlock extends SwitchableLightBlock {

    public static final MapCodec<TFCandleBlock> CODEC;
    public TFCandleBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
        super(flameParticle,properties);
    }

    @Override
    public void animateTick(@NotNull BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)){
            level.addParticle(this.flameParticle, pos.getX() + 0.5, pos.getY() + 0.4, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
        }
    }
    @Override
    protected VoxelShape getShape(BlockState p_304673_, BlockGetter p_304919_, BlockPos p_304930_, CollisionContext p_304757_) {
        return Block.box(6.0, 0.0, 6.0, 10.0, 7.0, 10.0);
    }

    public MapCodec<? extends TFCandleBlock> codec() {
        return CODEC;
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec((p_308842_) -> p_308842_.group(PARTICLE_OPTIONS_FIELD.forGetter((p_304762_) -> p_304762_.flameParticle), propertiesCodec()).apply(p_308842_, TFCandleBlock::new));
    }
}
