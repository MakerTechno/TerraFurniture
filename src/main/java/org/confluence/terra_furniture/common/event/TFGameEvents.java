package org.confluence.terra_furniture.common.event;

import net.minecraft.world.entity.LivingEntity;
import org.confluence.terra_furniture.common.block.sleep.BathtubBlock;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.player.PortCanContinueSleepingEvent;
import org.mesdag.portlib.event.entity.player.PortCanPlayerSleepEvent;

public final class TFGameEvents {
    public static void init() {
        PortEventHandler.addListener(TFGameEvents::canPlayerSleep);
        PortEventHandler.addListener(TFGameEvents::canContinueSleeping);
    }

    private static void canPlayerSleep(PortCanPlayerSleepEvent event) {
        if (event.getState().getBlock() instanceof BathtubBlock) {
            event.setProblem(null);
        }
    }

    private static void canContinueSleeping(PortCanContinueSleepingEvent event) {
        if (event.mayContinueSleeping()) return;
        LivingEntity living = event.getEntity();
        living.getSleepingPos().ifPresent(pos -> {
            if (living.level().getBlockState(pos).getBlock() instanceof BathtubBlock) {
                event.setContinueSleeping(true);
            }
        });
    }
}
