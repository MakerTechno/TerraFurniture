package nowebsite.makertechno.terra_furniture.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import nowebsite.makertechno.terra_furniture.common.init.TFRegistries;
import org.confluence.lib.common.recipe.AbstractAmountRecipe;
import org.confluence.lib.common.recipe.MenuRecipeInput;
import org.jetbrains.annotations.Nullable;

public class LivingLoomRecipe extends AbstractAmountRecipe<MenuRecipeInput> {
    public final ShapedRecipePattern pattern;

    public LivingLoomRecipe(ItemStack result, ShapedRecipePattern pattern) {
        super(result, pattern.ingredients());
        this.pattern = pattern;
        pattern.symmetrical = true;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= pattern.width() && height >= pattern.height();
    }

    @Override
    public boolean matches(MenuRecipeInput input, Level pLevel) {
        return pattern.matches(input.asCraftingInput(false));
    }

    @Override
    public ItemStack assembleAndExtract(MenuRecipeInput input, HolderLookup.@Nullable Provider registries) {
        consumeShaped(input, 4, 4, pattern);
        return assemble(input, registries);
    }

    @Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter(ingredient -> !ingredient.isEmpty()).anyMatch(Ingredient::hasNoItems);
    }

    @Override
    protected int maxIngredientSize() {
        return 16;
    }

    @Override
    public String getGroup() {
        return "living_loom";
    }

    @Override
    public ItemStack getToastSymbol() {
        return TFBlocks.LIVING_LOOM.toStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TFRegistries.LIVING_LOOM_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TFRegistries.LIVING_LOOM_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<LivingLoomRecipe> {
        public static final MapCodec<LivingLoomRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern)
        ).apply(instance, LivingLoomRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, LivingLoomRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<LivingLoomRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, LivingLoomRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static LivingLoomRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            return new LivingLoomRecipe(itemstack, shapedrecipepattern);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, LivingLoomRecipe recipe) {
            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
        }
    }
}
