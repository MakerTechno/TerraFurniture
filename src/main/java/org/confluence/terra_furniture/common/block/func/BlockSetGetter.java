package org.confluence.terra_furniture.common.block.func;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public interface BlockSetGetter<T extends Block> {
    /**
     * 方块所属的材质类型
     */
    BlockSetType getType();
    boolean hasParticle(T block);
}
