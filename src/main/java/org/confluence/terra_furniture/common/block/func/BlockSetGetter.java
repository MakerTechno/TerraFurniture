package org.confluence.terra_furniture.common.block.func;

import net.minecraft.world.level.block.Block;
import org.confluence.terra_furniture.common.block.func.set.TFBlockSetType;

public interface BlockSetGetter<T extends Block> {
    /**
     * 方块所属的材质类型
     */
    TFBlockSetType getType();
    boolean hasParticle(T block);
    default boolean isSpecialParticleTexture(T block) {
        return false;
    }
    default boolean isLayeredItemTexture() {
        return false;
    }
}
