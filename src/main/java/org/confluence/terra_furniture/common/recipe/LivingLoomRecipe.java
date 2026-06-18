package org.confluence.terra_furniture.common.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.confluence.lib.common.recipe.EitherAmountRecipe4x;
import org.confluence.lib.common.recipe.MenuRecipeInput;
import org.confluence.lib.common.recipe.SimpleRecipeSerializer;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFRegistries;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.world.item.crafting.PortShapedRecipePattern;

public class LivingLoomRecipe extends EitherAmountRecipe4x<MenuRecipeInput> {
    public LivingLoomRecipe(ItemStack result, PortShapedRecipePattern pattern) {
        super(result, pattern);
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

    public static class Serializer extends SimpleRecipeSerializer<LivingLoomRecipe> {
        public static final MapCodec<LivingLoomRecipe> CODEC = EitherAmountRecipe4x.shapedSerializerMapCodec(LivingLoomRecipe::new);
        public static final PortStreamCodec<PortRegistryFriendlyByteBuf, LivingLoomRecipe> STREAM_CODEC = EitherAmountRecipe4x.shapedSerializerSteamCodec(LivingLoomRecipe::new);

        @Override
        protected MapCodec<LivingLoomRecipe> getCodec() {
            return CODEC;
        }

        @Override
        protected PortStreamCodec<PortRegistryFriendlyByteBuf, LivingLoomRecipe> getStreamCodec() {
            return STREAM_CODEC;
        }
    }
}
