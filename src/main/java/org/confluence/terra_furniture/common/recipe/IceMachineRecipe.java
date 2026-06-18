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

public class IceMachineRecipe extends EitherAmountRecipe4x<MenuRecipeInput> {
    public IceMachineRecipe(ItemStack result, PortShapedRecipePattern pattern) {
        super(result, pattern);
    }

    @Override
    public String getGroup() {
        return "ice_machine";
    }

    @Override
    public ItemStack getToastSymbol() {
        return TFBlocks.ICE_MACHINE.toStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TFRegistries.ICE_MACHINE_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TFRegistries.ICE_MACHINE_RECIPE_TYPE.get();
    }

    public static class Serializer extends SimpleRecipeSerializer<IceMachineRecipe> {
        public static final MapCodec<IceMachineRecipe> CODEC = EitherAmountRecipe4x.shapedSerializerMapCodec(IceMachineRecipe::new);
        public static final PortStreamCodec<PortRegistryFriendlyByteBuf, IceMachineRecipe> STREAM_CODEC = EitherAmountRecipe4x.shapedSerializerSteamCodec(IceMachineRecipe::new);

        @Override
        protected MapCodec<IceMachineRecipe> getCodec() {
            return CODEC;
        }

        @Override
        protected PortStreamCodec<PortRegistryFriendlyByteBuf, IceMachineRecipe> getStreamCodec() {
            return STREAM_CODEC;
        }
    }
}
