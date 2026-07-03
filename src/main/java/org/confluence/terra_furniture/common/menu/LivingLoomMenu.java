package org.confluence.terra_furniture.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.confluence.lib.common.menu.EitherAmountContainerMenu4x;
import org.confluence.lib.common.menu.ToggleAmountResultSlot;
import org.confluence.lib.common.recipe.MenuRecipeInput;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFRegistries;
import org.confluence.terra_furniture.common.recipe.LivingLoomRecipe;

public class LivingLoomMenu extends EitherAmountContainerMenu4x<MenuRecipeInput, LivingLoomRecipe, ToggleAmountResultSlot<LivingLoomRecipe>, ContainerLevelAccess> {
    public LivingLoomMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public LivingLoomMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(TFRegistries.LIVING_LOOM_MENU.get(), TFRegistries.LIVING_LOOM_RECIPE_TYPE.get(), containerId, inventory, access, MenuRecipeInput::new, ToggleAmountResultSlot.For4x::new);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, TFBlocks.LIVING_LOOM.get());
    }
}
