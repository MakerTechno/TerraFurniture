package nowebsite.makertechno.terra_furniture.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import nowebsite.makertechno.terra_furniture.common.recipe.GlassKilnRecipe;
import org.confluence.lib.common.data.gen.AbstractRecipeProvider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TFRecipeProvider extends AbstractRecipeProvider {
    public TFRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider holderLookup) {
        recipe(recipeOutput, TFBlocks.GLASS_DOOR.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS)
        ), List.of(
                "GG",
                "GG",
                "GG"
        )), 0.1F, 200);
    }

    protected void recipe(RecipeOutput recipeOutput, ItemStack result, ShapedRecipePattern pattern, float experience, int cookingTime) {
        ResourceLocation id = TerraFurniture.asResource(getItemName(result.getItem()));
        recipeOutput.accept(id, new GlassKilnRecipe(TFBlocks.GLASS_DOOR.toStack(), pattern, experience, cookingTime), null);
    }
}
