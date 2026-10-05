package org.confluence.terra_furniture.client.model;

import org.confluence.terra_furniture.api.client.model.CacheBlockModel;
import org.confluence.terra_furniture.common.block.misc.CherryChestBlock;
import software.bernie.geckolib.animation.AnimationState;

public class CherryChestGeoModel extends CacheBlockModel<CherryChestBlock.Entity> {
    @Override
    public void setCustomAnimations(CherryChestBlock.Entity chest, long instanceId,
                                    AnimationState<CherryChestBlock.Entity> animationState) {
        super.setCustomAnimations(chest, instanceId, animationState);
        float openness = chest.getOpenNess(animationState.getPartialTick());
        float eased = 1.0F - (float) Math.pow(1.0F - openness, 3);
        // The source cubes are flipped around Z, so "down" is the visible lid.
        getBone("down").ifPresent(lid -> lid.setRotX(-eased * (float) (Math.PI / 2.0)));
    }
}
