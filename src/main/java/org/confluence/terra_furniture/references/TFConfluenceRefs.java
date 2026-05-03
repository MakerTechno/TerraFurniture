package org.confluence.terra_furniture.references;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.confluence.lib.util.consumer.Consumer3;
import org.confluence.terra_furniture.common.entity.RideableEntityNull;

public class TFConfluenceRefs {
    public static Consumer3<RideableEntityNull, ServerLevel, BlockPos> poop_task =
            (entityNull, serverLevel, pos) -> {};
}
