package org.confluence.terra_furniture.common.recipe;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.confluence.lib.common.recipe.AbstractAmountRecipe;
import org.confluence.lib.common.recipe.SimpleRecipeSerializer;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFRegistries;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.world.item.crafting.PortCraftingInput;
import org.mesdag.portlib.wrapper.world.item.crafting.PortShapedRecipePattern;

public class GlassKilnRecipe extends AbstractAmountRecipe<PortCraftingInput> {
    public final PortShapedRecipePattern pattern;
    protected final float experience;
    protected final int cookingTime;

    public GlassKilnRecipe(ItemStack result, PortShapedRecipePattern pattern, float experience, int cookingTime) {
        super(result, pattern.ingredients());
        this.pattern = pattern;
        this.experience = experience;
        this.cookingTime = cookingTime;
        pattern.setNonSymmetricalMatching();
    }

    public float getExperience() {
        return experience;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= pattern.width() && height >= pattern.height();
    }

    @Override
    public boolean matches(PortCraftingInput input, Level pLevel) {
        return pattern.matches(input);
    }

    @Override
    public ItemStack assembleAndExtract(PortCraftingInput input, RegistryAccess registryAccess) {
        consumeShaped(input, 4, 4, pattern);
        return assemble(input, registryAccess);
    }

    @Override
    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter(ingredient -> !ingredient.isEmpty()).anyMatch(Ingredient::isEmpty);
    }

    @Override
    protected int maxIngredientSize() {
        return 16;
    }

    @Override
    public String getGroup() {
        return "glass_kiln";
    }

    @Override
    public ItemStack getToastSymbol() {
        return TFBlocks.GLASS_KILN.toStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TFRegistries.GLASS_KILN_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TFRegistries.GLASS_KILN_RECIPE_TYPE.get();
    }

    public static class Serializer extends SimpleRecipeSerializer<GlassKilnRecipe> {
        public static final MapCodec<GlassKilnRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(AbstractAmountRecipe::getResult),
                PortShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                PortCodecExtension.lenientOptionalFieldOf(Codec.FLOAT, "experience", 0.0F).forGetter(GlassKilnRecipe::getExperience),
                PortCodecExtension.lenientOptionalFieldOf(Codec.INT, "cookingtime", 100).forGetter(GlassKilnRecipe::getCookingTime)
        ).apply(instance, GlassKilnRecipe::new));
        public static final PortStreamCodec<PortRegistryFriendlyByteBuf, GlassKilnRecipe> STREAM_CODEC = PortStreamCodec.composite(
                ItemStack.STREAM_CODEC, AbstractAmountRecipe::getResult,
                PortShapedRecipePattern.STREAM_CODEC, recipe -> recipe.pattern,
                PortByteBufCodecs.FLOAT, GlassKilnRecipe::getExperience,
                PortByteBufCodecs.VAR_INT, GlassKilnRecipe::getCookingTime,
                GlassKilnRecipe::new
        );

        @Override
        protected MapCodec<GlassKilnRecipe> getCodec() {
            return CODEC;
        }

        @Override
        protected PortStreamCodec<PortRegistryFriendlyByteBuf, GlassKilnRecipe> getStreamCodec() {
            return STREAM_CODEC;
        }
    }
}
