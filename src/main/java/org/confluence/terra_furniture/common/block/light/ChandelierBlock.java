package org.confluence.terra_furniture.common.block.light;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChandelierBlock extends SwitchableLightBlock{

    public static final MapCodec<ChandelierBlock> CODEC;
    public ChandelierBlock(SimpleParticleType flameParticle, Properties properties) {
        super(flameParticle,properties);
    }
    public ChandelierBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)){
            level.addParticle(this.flameParticle, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
            for (int L = 1; L <=9; L++,L++){
                for (int W = 1; W <=9; W++,W++){
                    if (L == 1 || W == 1 || L == 9 || W == 9) {
                        level.addParticle(this.flameParticle, pos.getX() + (L / 10.0), pos.getY() + 13/16.0, pos.getZ() + (W / 10.0), 0.0, 0.0, 0.0);
                    }
                }
            }
            for (int L = 2; L <=8; L++,L++){
                for (int W = 2; W <=8; W++,W++){
                    if (L == 2 || W == 2 || L == 8 || W == 8) {
                        level.addParticle(this.flameParticle, pos.getX() + (L / 10.0), pos.getY() + 9/16.0, pos.getZ() + (W / 10.0), 0.0, 0.0, 0.0);
                    }
                }
            }
            for (int L = 3; L <=7; L++,L++){
                for (int W = 3; W <=7; W++,W++){
                    if (L == 3 || W == 3 || L == 7 || W == 7) {
                        level.addParticle(this.flameParticle, pos.getX() + (L / 10.0), pos.getY() + 5/16.0, pos.getZ() + (W / 10.0), 0.0, 0.0, 0.0);
                    }
                }
            }
            for (int L = 4; L <=6; L++,L++){
                for (int W = 4; W <=6; W++,W++){
                    if (L == 4 || W == 4 || L == 6 || W == 6) {
                        level.addParticle(this.flameParticle, pos.getX() + (L / 10.0), pos.getY() + 2/16.0, pos.getZ() + (W / 10.0), 0.0, 0.0, 0.0);
                    }
                }
            }
        }
    }
    @Override
    protected VoxelShape getShape(BlockState p_304673_, BlockGetter p_304919_, BlockPos p_304930_, CollisionContext p_304757_) {
        return Block.box(2.0, 2.0, 2.0, 14.0, 16.0, 14.0);
    }

    public MapCodec<? extends ChandelierBlock> codec() {
        return CODEC;
    }
    static {
        CODEC = RecordCodecBuilder.mapCodec((p_308842_) -> p_308842_.group(PARTICLE_OPTIONS_FIELD.forGetter((p_304762_) -> p_304762_.flameParticle), propertiesCodec()).apply(p_308842_, ChandelierBlock::new));
    }
}
