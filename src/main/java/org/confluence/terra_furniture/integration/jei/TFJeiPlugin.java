package org.confluence.terra_furniture.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import org.confluence.lib.common.recipe.AmountIngredient;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.client.screen.GlassKilnScreen;
import org.confluence.terra_furniture.client.screen.IceMachineScreen;
import org.confluence.terra_furniture.client.screen.LivingLoomScreen;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFRegistries;

import java.util.List;

@JeiPlugin
public final class TFJeiPlugin implements IModPlugin {
    public static final ResourceLocation UID = TerraFurniture.asResource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeCategories(new LivingLoomCategory(jeiHelpers));
        registration.addRecipeCategories(new GlassKilnCategory(jeiHelpers));
        registration.addRecipeCategories(new IceMachineCategory(jeiHelpers));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        RecipeManager recipeManager = level.getRecipeManager();
        registration.addRecipes(LivingLoomCategory.TYPE, getAllRecipesFor(recipeManager, TFRegistries.LIVING_LOOM_RECIPE_TYPE.get()));
        registration.addRecipes(GlassKilnCategory.TYPE, getAllRecipesFor(recipeManager, TFRegistries.GLASS_KILN_RECIPE_TYPE.get()));
        registration.addRecipes(IceMachineCategory.TYPE, getAllRecipesFor(recipeManager, TFRegistries.ICE_MACHINE_RECIPE_TYPE.get()));
    }

    private static <I extends RecipeInput, T extends Recipe<I>> List<T> getAllRecipesFor(RecipeManager recipeManager, RecipeType<T> recipeType) {
        return recipeManager.getAllRecipesFor(recipeType).stream().map(RecipeHolder::value).toList();
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(TFBlocks.LIVING_LOOM.toStack(), LivingLoomCategory.TYPE);
        registration.addRecipeCatalyst(TFBlocks.GLASS_KILN.toStack(), GlassKilnCategory.TYPE, RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(TFBlocks.ICE_MACHINE.toStack(), IceMachineCategory.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(LivingLoomScreen.class, 95, 32, 28, 23, LivingLoomCategory.TYPE);
        registration.addRecipeClickArea(GlassKilnScreen.class, 95, 44, 28, 23, GlassKilnCategory.TYPE);
        registration.addRecipeClickArea(IceMachineScreen.class, 95, 44, 28, 23, IceMachineCategory.TYPE);
    }

    static void addInput(IRecipeLayoutBuilder builder, int x, int y, Ingredient ingredient) {
        if (!ingredient.isEmpty()) {
            if (ingredient.getCustomIngredient() instanceof AmountIngredient amountIngredient) {
                builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(VanillaTypes.ITEM_STACK, amountIngredient.getItems().toList());
            } else {
                builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(ingredient);
            }
        }
    }
}
