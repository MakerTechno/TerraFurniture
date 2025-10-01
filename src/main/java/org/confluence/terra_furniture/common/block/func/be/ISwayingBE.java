package org.confluence.terra_furniture.common.block.func.be;

import net.minecraft.world.phys.Vec3;

public interface ISwayingBE {
    void applyDelta(Vec3 input);
    float getSwingX();
    float getSwingZ();
    Vec3 getAnchorPoint();
}