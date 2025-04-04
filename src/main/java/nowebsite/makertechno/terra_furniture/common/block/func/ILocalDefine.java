package nowebsite.makertechno.terra_furniture.common.block.func;

public interface ILocalDefine {
    String parentName();
    String textureKey();
    String textureName();
    default boolean fromVanilla() {
        return false;
    }
}
