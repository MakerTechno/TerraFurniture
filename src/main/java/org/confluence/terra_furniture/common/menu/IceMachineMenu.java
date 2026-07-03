package org.confluence.terra_furniture.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.confluence.lib.common.menu.EitherAmountContainerMenu4x;
import org.confluence.lib.common.menu.ToggleAmountResultSlot;
import org.confluence.lib.common.recipe.MenuRecipeInput;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFRegistries;
import org.confluence.terra_furniture.common.recipe.IceMachineRecipe;

public class IceMachineMenu extends EitherAmountContainerMenu4x<MenuRecipeInput, IceMachineRecipe, ToggleAmountResultSlot<IceMachineRecipe>, ContainerLevelAccess> {
    public IceMachineMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public IceMachineMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(TFRegistries.ICE_MACHINE_MENU.get(), TFRegistries.ICE_MACHINE_RECIPE_TYPE.get(), containerId, inventory, access, MenuRecipeInput::new, ToggleAmountResultSlot.For4x::new);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, TFBlocks.ICE_MACHINE.get());
    }
}
