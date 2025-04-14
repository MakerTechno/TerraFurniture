package nowebsite.makertechno.terra_furniture.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import nowebsite.makertechno.terra_furniture.common.init.TFRegistries;
import nowebsite.makertechno.terra_furniture.common.recipe.LivingLoomRecipe;
import org.confluence.lib.common.menu.ShapedAmountContainerMenu4x;
import org.confluence.lib.common.menu.ToggleAmountResultSlot;
import org.confluence.lib.common.recipe.AbstractAmountRecipe;
import org.confluence.lib.common.recipe.MenuRecipeInput;

public class LivingLoomMenu extends ShapedAmountContainerMenu4x<MenuRecipeInput, LivingLoomRecipe, ToggleAmountResultSlot<LivingLoomRecipe>, ContainerLevelAccess> {
    public LivingLoomMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public LivingLoomMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(TFRegistries.LIVING_LOOM_MENU.get(), TFRegistries.LIVING_LOOM_RECIPE_TYPE.get(), containerId, inventory, access, MenuRecipeInput::new,
                (input, container, slot, x, y, setup) -> new ToggleAmountResultSlot<>(input, container, slot, x, y) {
                    @Override
                    protected void updateMenu() {
                        setup.run();
                    }

                    @Override
                    public void onTake(Player player, ItemStack stack) {
                        if (recipe != null) {
                            AbstractAmountRecipe.consumeShaped(input, 4, 4, recipe.pattern);
                            input.setChanged();
                            updateMenu();
                        }
                    }
                });
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, TFBlocks.LIVING_LOOM.get());
    }
}
