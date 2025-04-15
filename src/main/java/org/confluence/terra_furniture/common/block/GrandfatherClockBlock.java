package org.confluence.terra_furniture.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.confluence.lib.common.block.HorizontalDirectionalWithVerticalTwoPartBlock;

/**
 * 落地大摆钟
 */
public class GrandfatherClockBlock extends HorizontalDirectionalWithVerticalTwoPartBlock {
    public static final MapCodec<GrandfatherClockBlock> CODEC = simpleCodec(GrandfatherClockBlock::new);

    public GrandfatherClockBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<GrandfatherClockBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            player.sendSystemMessage(wrapMinute(level.getDayTime()));
        }
        return InteractionResult.SUCCESS;
    }

    private static Component wrapMinute(long dayTime) {
        dayTime = dayTime % 24000;
        long hour = dayTime / 1000 + 6;
        if (hour > 23) hour -= 24;
        long minute = (long) ((dayTime % 1000) * 0.06F);
        return Component.translatable("info.terra_furniture.time", format(hour), format(minute));
    }

    private static String format(long time) {
        return (time < 10 ? "0" : "") + time;
    }
}
