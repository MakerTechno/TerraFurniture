package org.confluence.terra_furniture.common.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.confluence.lib.common.data.gen.AbstractRecipeProvider;
import org.confluence.lib.common.recipe.AmountIngredient;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFRegistries;
import org.confluence.terra_furniture.common.recipe.GlassKilnRecipe;
import org.confluence.terra_furniture.common.recipe.IceMachineRecipe;
import org.confluence.terra_furniture.common.recipe.LivingLoomRecipe;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.common.PortTags;
import org.mesdag.portlib.wrapper.world.item.crafting.PortShapedRecipePattern;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TFRecipeProvider extends AbstractRecipeProvider {
    public TFRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        Ingredient glass = Ingredient.of(PortTags.Items.GLASS_BLOCKS_COLORLESS);
        glassKiln(writer, TFBlocks.GLASS_SET.DOOR.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', glass
        ), List.of(
                "GG",
                "GG",
                "GG"
        )), 0.1F, 200);
        glassKiln(writer, TFBlocks.GLASS_SET.CHAIR.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', glass
        ), List.of(
                "G  ",
                "GGG",
                "G G"
        )), 0.1F, 200);
        glassKiln(writer, TFBlocks.GLASS_SET.TOILET.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', glass
        ), List.of(
                "G  ",
                "G G",
                "GG "
        )), 0.1F, 200);
        glassKiln(writer, TFBlocks.GLASS_SET.TABLE.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', glass,
                'A', AmountIngredient.of(2, Items.GLASS)
        ), List.of(
                "GGGG",
                " AA "
        )), 0.1F, 200);
        glassKiln(writer, TFBlocks.GLASS_SET.SOFA.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', glass,
                'A', Ingredient.of(ItemTags.WOOL)
        ), List.of(
                "GAAG",
                "GGGG"
        )), 0.1F, 200);
        glassKiln(writer, TFBlocks.GLASS_SET.SINK.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', glass,
                'A', Ingredient.of(Items.WATER_BUCKET)
        ), List.of(
                "G  ",
                "GAG",
                " G "
        )), 0.1F, 200);
        glassKiln(writer, TFBlocks.GLASS_SET.CANDLE.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', glass,
                'A', Ingredient.of(Items.TORCH)
        ), List.of(
                " G ",
                "GAG"
        )), 0.1F, 200);
        glassKiln(writer, TFBlocks.GLASS_SET.LANTERN.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', glass,
                'A', Ingredient.of(Items.TORCH)
        ), List.of(
                " G ",
                "GAG",
                "GGG"
        )), 0.1F, 200);
        glassKiln(writer, TFBlocks.GLASS_SET.LAMP.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', glass,
                'A', Ingredient.of(Items.TORCH)
        ), List.of(
                "A",
                "G",
                "G",
                "G"
        )), 0.1F, 200);
        glassKiln(writer, TFBlocks.GLASS_SET.CANDELABRAS.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', glass,
                'A', AmountIngredient.of(2, Items.TORCH)
        ), List.of(
                "AGA",
                "GGG",
                " G "
        )), 0.1F, 200);
        glassKiln(writer, TFBlocks.GLASS_SET.CLOCK.toStack(), PortShapedRecipePattern.of(Map.of(
                'G', AmountIngredient.of(2, Items.GLASS),
                'I', Ingredient.of(Items.IRON_INGOT)
        ), List.of(// 上面三格玻璃是时钟固有材料，下面五格在做其他钟时替换为其他材料.
                "IGI",
                "GIG",
                "G G",
                "GGG"
        )), 0.1F, 200);
    }

    protected void glassKiln(Consumer<FinishedRecipe> writer, ItemStack result, PortShapedRecipePattern pattern, float experience, int cookingTime) {
        ResourceLocation id = TerraFurniture.asResource("glass_kiln/" + getItemName(result.getItem()));
        GlassKilnRecipe recipe = new GlassKilnRecipe(result, pattern, experience, cookingTime);
        recipe.setId(id);
        writer.accept(new FinishedRecipe() {
            private static final Codec<GlassKilnRecipe> CODEC = GlassKilnRecipe.Serializer.CODEC.codec();

            @Override
            public void serializeRecipeData(JsonObject json) {
                CODEC.encodeStart(JsonOps.INSTANCE, recipe).result().ifPresent((element) -> {
                    if (element.isJsonObject()) {
                        for(Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                            json.add(entry.getKey(), entry.getValue());
                        }
                    }
                });
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return TFRegistries.GLASS_KILN_RECIPE_SERIALIZER.get();
            }

            @Override
            public @Nullable JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public @Nullable ResourceLocation getAdvancementId() {
                return null;
            }
        });

    }

    protected void livingLoom(Consumer<FinishedRecipe> writer, ItemStack result, PortShapedRecipePattern pattern) {
        ResourceLocation id = TerraFurniture.asResource("living_loom/" + getItemName(result.getItem()));
        LivingLoomRecipe recipe = new LivingLoomRecipe(result, pattern);
        recipe.setId(id);
        writer.accept(new FinishedRecipe() {
            private static final Codec<LivingLoomRecipe> CODEC = LivingLoomRecipe.Serializer.CODEC.codec();

            @Override
            public void serializeRecipeData(JsonObject json) {
                CODEC.encodeStart(JsonOps.INSTANCE, recipe).result().ifPresent((element) -> {
                    if (element.isJsonObject()) {
                        for(Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                            json.add(entry.getKey(), entry.getValue());
                        }
                    }
                });
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return TFRegistries.LIVING_LOOM_RECIPE_SERIALIZER.get();
            }

            @Override
            public @Nullable JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public @Nullable ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }

    protected void iceMachine(Consumer<FinishedRecipe> writer, ItemStack result, PortShapedRecipePattern pattern) {
        ResourceLocation id = TerraFurniture.asResource("ice_machine/" + getItemName(result.getItem()));
        IceMachineRecipe recipe = new IceMachineRecipe(result, pattern);
        recipe.setId(id);
        writer.accept(new FinishedRecipe() {
            private static final Codec<IceMachineRecipe> CODEC = IceMachineRecipe.Serializer.CODEC.codec();

            @Override
            public void serializeRecipeData(JsonObject json) {
                CODEC.encodeStart(JsonOps.INSTANCE, recipe).result().ifPresent((element) -> {
                    if (element.isJsonObject()) {
                        for(Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                            json.add(entry.getKey(), entry.getValue());
                        }
                    }
                });
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return TFRegistries.ICE_MACHINE_RECIPE_SERIALIZER.get();
            }

            @Override
            public @Nullable JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public @Nullable ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}
