package org.confluence.terra_furniture.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.common.menu.EitherAmountContainerMenu4x;
import org.confluence.lib.common.menu.ToggleAmountResultSlot;
import org.confluence.lib.common.recipe.AbstractAmountRecipe;
import org.confluence.lib.common.recipe.MenuRecipeInput;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFRegistries;
import org.confluence.terra_furniture.common.recipe.LivingLoomRecipe;

public class LivingLoomMenu extends EitherAmountContainerMenu4x<MenuRecipeInput, LivingLoomRecipe, ToggleAmountResultSlot<LivingLoomRecipe>, ContainerLevelAccess> {
    public LivingLoomMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public LivingLoomMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(TFRegistries.LIVING_LOOM_MENU.get(), TFRegistries.LIVING_LOOM_RECIPE_TYPE.get(), containerId, inventory, access, MenuRecipeInput::new,
                LivingLoomRecipeToggleAmountResultSlot::new);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, TFBlocks.LIVING_LOOM.get());
    }

    private static class LivingLoomRecipeToggleAmountResultSlot extends ToggleAmountResultSlot<LivingLoomRecipe> {
        private final Runnable setup;

        public LivingLoomRecipeToggleAmountResultSlot(MenuRecipeInput input, ResultContainer container, Integer slot, Integer x, Integer y, Runnable setup) {
            super(input, container, slot, x, y);
            this.setup = setup;
        }

        @Override
        protected void updateMenu() {
            setup.run();
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            if (recipe != null) {
                recipe.either
                        .ifLeft(pattern -> AbstractAmountRecipe.consumeShaped(input, 4, 4, pattern))
                        .ifRight(ingredients -> AbstractAmountRecipe.consumeShapeless(input, ingredients));
                input.setChanged();
                updateMenu();
            }
        }
    }
}
