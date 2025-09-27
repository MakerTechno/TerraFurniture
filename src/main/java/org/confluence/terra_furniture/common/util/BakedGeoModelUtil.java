package org.confluence.terra_furniture.common.util;

import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.ArrayList;
import java.util.List;

public class BakedGeoModelUtil {
    public List<GeoBone> getAllBones(BakedGeoModel model) {
        List<GeoBone> allBones = new ArrayList<>();
        for (GeoBone rootBone : model.topLevelBones()) {
            collectBones(rootBone, allBones);
        }
        return allBones;
    }

    private void collectBones(GeoBone bone, List<GeoBone> collector) {
        collector.add(bone);
        for (GeoBone child : bone.getChildBones()) {
            collectBones(child, collector);
        }
    }

}
