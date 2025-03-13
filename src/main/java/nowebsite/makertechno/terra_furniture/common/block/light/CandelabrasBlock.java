package nowebsite.makertechno.terra_furniture.common.block.light;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CandelabrasBlock extends SwitchableLightBlock {
    public static final DirectionProperty FACING;
    protected static final MapCodec<SimpleParticleType> PARTICLE_OPTIONS_FIELD;
    public static final MapCodec<CandelabrasBlock> CODEC;
    protected final SimpleParticleType flameParticle;

    public MapCodec<? extends CandelabrasBlock> codec() {
        return CODEC;
    }

    public CandelabrasBlock(SimpleParticleType flameParticle, Properties properties) {
        super(properties);
        this.flameParticle = flameParticle;
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
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        boolean FacingWE = false;
        switch (state.getValue(FACING)){
            case WEST, EAST -> {
                FacingWE = true;
            }
        }
        if (state.getValue(LIT)){
            level.addParticle(this.flameParticle, pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
            level.addParticle(this.flameParticle, pos.getX() + (!FacingWE ? 3.0/ 16.0 : 0.5) , pos.getY() + 0.8, pos.getZ() + (FacingWE ? 3.0/ 16.0 : 0.5), 0.0, 0.0, 0.0);
            level.addParticle(this.flameParticle, pos.getX() + (!FacingWE ? 13.0/ 16.0 : 0.5) , pos.getY() + 0.8, pos.getZ() + (FacingWE ? 13.0/ 16.0 : 0.5), 0.0, 0.0, 0.0);
        }
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)){
            case WEST, EAST -> {
                return Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);
            }
        }
        return Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    }
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
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
        CODEC = RecordCodecBuilder.mapCodec((p_308842_) -> p_308842_.group(PARTICLE_OPTIONS_FIELD.forGetter((p_304762_) -> p_304762_.flameParticle), propertiesCodec()).apply(p_308842_, CandelabrasBlock::new));

        FACING = BlockStateProperties.HORIZONTAL_FACING;
    }
}
