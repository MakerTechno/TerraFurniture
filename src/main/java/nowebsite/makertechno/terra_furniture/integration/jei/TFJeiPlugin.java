package nowebsite.makertechno.terra_furniture.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;
import nowebsite.makertechno.terra_furniture.TerraFurniture;

@JeiPlugin
public class TFJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = TerraFurniture.asResource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}
