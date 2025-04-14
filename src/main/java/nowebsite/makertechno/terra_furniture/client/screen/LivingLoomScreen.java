package nowebsite.makertechno.terra_furniture.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.menu.LivingLoomMenu;
import org.confluence.lib.client.screen.ShapedAmountContainerScreen4x;

public class LivingLoomScreen extends ShapedAmountContainerScreen4x<LivingLoomMenu> {
    private static final ResourceLocation BACKGROUND = TerraFurniture.asResource("textures/gui/container/living_loom.png");

    public LivingLoomScreen(LivingLoomMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected ResourceLocation background() {
        return BACKGROUND;
    }
}
