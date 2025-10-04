package org.confluence.terra_furniture.common.block.func;

import org.jetbrains.annotations.Nullable;

public interface ILocalDefine {
    String parentName();
    String textureKey();
    @Nullable String textureName();
    default boolean fromVanilla() {
        return false;
    }
}
