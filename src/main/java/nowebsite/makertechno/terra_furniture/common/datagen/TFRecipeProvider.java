package nowebsite.makertechno.terra_furniture.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import nowebsite.makertechno.terra_furniture.common.recipe.GlassKilnRecipe;
import nowebsite.makertechno.terra_furniture.common.recipe.LivingLoomRecipe;
import org.confluence.lib.common.data.gen.AbstractRecipeProvider;
import org.confluence.lib.common.recipe.AmountIngredient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TFRecipeProvider extends AbstractRecipeProvider {
    public TFRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput, HolderLookup.Provider holderLookup) {
        glassKiln(recipeOutput, TFBlocks.GLASS_DOOR.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS)
        ), List.of(
                "GG",
                "GG",
                "GG"
        )), 0.1F, 200);
        glassKiln(recipeOutput, TFBlocks.GLASS_CHAIR.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS)
        ), List.of(
                "G  ",
                "GGG",
                "G G"
        )), 0.1F, 200);
        glassKiln(recipeOutput, TFBlocks.GLASS_TOILET.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS)
        ), List.of(
                "G  ",
                "G G",
                "GG "
        )), 0.1F, 200);
        glassKiln(recipeOutput, TFBlocks.GLASS_TABLE.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS),
                'A', AmountIngredient.of(2, Items.GLASS)
        ), List.of(
                "GGGG",
                " AA "
        )), 0.1F, 200);
        glassKiln(recipeOutput, TFBlocks.GLASS_SOFA.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS),
                'A', Ingredient.of(ItemTags.WOOL)
        ), List.of(
                "GAAG",
                "GGGG"
        )), 0.1F, 200);
        glassKiln(recipeOutput, TFBlocks.GLASS_CHANDELIER.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS),
                'A', Ingredient.of(Items.CHAIN),
                'B', AmountIngredient.of(4, Items.TORCH)
        ), List.of(
                " A ",
                "GBG",
                " G "
        )), 0.1F, 200);
        glassKiln(recipeOutput, TFBlocks.GLASS_SINK.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS),
                'A', Ingredient.of(Items.WATER_BUCKET)
        ), List.of(
                "G  ",
                "GAG",
                " G "
        )), 0.1F, 200);
        glassKiln(recipeOutput, TFBlocks.GLASS_CANDLE.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS),
                'A', Ingredient.of(Items.TORCH)
        ), List.of(
                " G ",
                "GAG"
        )), 0.1F, 200);
        glassKiln(recipeOutput, TFBlocks.GLASS_LANTERN.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS),
                'A', Ingredient.of(Items.TORCH)
        ), List.of(
                " G ",
                "GAG",
                "GGG"
        )), 0.1F, 200);
        glassKiln(recipeOutput, TFBlocks.GLASS_LAMP.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS),
                'A', Ingredient.of(Items.TORCH)
        ), List.of(
                "A",
                "G",
                "G",
                "G"
        )), 0.1F, 200);
        glassKiln(recipeOutput, TFBlocks.GLASS_CANDELABRAS.toStack(), ShapedRecipePattern.of(Map.of(
                'G', Ingredient.of(Items.GLASS),
                'A', AmountIngredient.of(2, Items.TORCH)
        ), List.of(
                "AGA",
                "GGG",
                " G "
        )), 0.1F, 200);
    }

    protected void glassKiln(RecipeOutput recipeOutput, ItemStack result, ShapedRecipePattern pattern, float experience, int cookingTime) {
        ResourceLocation id = TerraFurniture.asResource(getItemName(result.getItem()));
        recipeOutput.accept(id, new GlassKilnRecipe(result, pattern, experience, cookingTime), null);

    }

    protected void livingLoom(RecipeOutput recipeOutput, ItemStack result, ShapedRecipePattern pattern) {
        ResourceLocation id = TerraFurniture.asResource(getItemName(result.getItem()));
        recipeOutput.accept(id, new LivingLoomRecipe(result, pattern), null);
    }
}
