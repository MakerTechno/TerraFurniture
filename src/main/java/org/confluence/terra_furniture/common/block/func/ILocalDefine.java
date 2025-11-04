package org.confluence.terra_furniture.common.block.func;

import org.jetbrains.annotations.Nullable;

/**
 * 生成方块状态时可以使用的功能，在代码层面上填入部分模型信息
 */
public interface ILocalDefine {
    String parentName();
    String textureKey();
    @Nullable String textureName();
    default boolean fromVanilla() {
        return false;
    }
}
