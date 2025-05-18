package org.confluence.terra_furniture.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.recipe.LivingLoomRecipe;
import org.jetbrains.annotations.Nullable;

import static org.confluence.terra_furniture.integration.jei.TFJeiPlugin.addInput;

public class LivingLoomCategory implements IRecipeCategory<RecipeHolder<LivingLoomRecipe>> {
    public static final RecipeType<RecipeHolder<LivingLoomRecipe>> TYPE = RecipeType.createRecipeHolderType(TerraFurniture.asResource("living_loom"));
    private static final Component TITLE = Component.translatable("title.terra_furniture.living_loom");
    private static final ResourceLocation BACKGROUND = TerraFurniture.asResource("textures/gui/living_loom.png");
    private final IDrawable icon;

    public LivingLoomCategory(IJeiHelpers jeiHelpers) {
        this.icon = jeiHelpers.getGuiHelper().createDrawableItemStack(TFBlocks.LIVING_LOOM.toStack());
    }

    @Override
    public RecipeType<RecipeHolder<LivingLoomRecipe>> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 144;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<LivingLoomRecipe> recipe, IFocusGroup focuses) {
        ShapedRecipePattern pattern = recipe.value().either.orThrow();
        int width = pattern.width();
        int height = pattern.height();
        boolean symmetrical = pattern.symmetrical;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (symmetrical) {
                    addInput(builder, j * 18 + 6, i * 18 + 5, recipe.value().ingredients.get(width - j - 1 + i * width));
                } else {
                    addInput(builder, j * 18 + 6, i * 18 + 5, recipe.value().ingredients.get(j + i * width));
                }
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 117, 33).addItemStack(recipe.value().getResultItem(null));
    }

    @Override
    public void draw(RecipeHolder<LivingLoomRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(BACKGROUND, 0, 0, 0, 0, 144, 80, 144, 80);
    }
}
