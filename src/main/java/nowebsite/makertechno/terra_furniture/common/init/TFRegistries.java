package nowebsite.makertechno.terra_furniture.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.menu.GlassKilnMenu;
import nowebsite.makertechno.terra_furniture.common.recipe.GlassKilnRecipe;

import java.util.function.Supplier;

public final class TFRegistries {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, TerraFurniture.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, TerraFurniture.MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, TerraFurniture.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, TerraFurniture.MODID);

    public static final Supplier<RecipeSerializer<GlassKilnRecipe>> GLASS_KILN_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("glass_kiln", GlassKilnRecipe.Serializer::new);

    public static final Supplier<RecipeType<GlassKilnRecipe>> GLASS_KILN_RECIPE_TYPE = RECIPE_TYPES.register("glass_kiln", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return TerraFurniture.MODID + ":glass_kiln";
        }
    });

    public static final Supplier<MenuType<GlassKilnMenu>> GLASS_KILN_MENU = MENU_TYPES.register("glass_kiln", () -> new MenuType<>(GlassKilnMenu::new, FeatureFlags.VANILLA_SET));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FURNITURE = TABS.register("furniture", () -> CreativeModeTab.builder().icon(TFBlocks.PLASTIC_CHAIR::toStack)
            .title(Component.translatable("creativetab.terra_furniture"))
            .displayItems((parameters, output) -> {
                TFItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                TFBlocks.BLOCKS.getEntries().forEach(block -> output.accept(block.get()));
            })
            .withTabsAfter(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath("confluence", "mechanical")))
            .withTabsBefore(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath("confluence", "building_blocks")))
            .build());

    public static void register(IEventBus eventBus) {
        ShapedRecipePattern.setCraftingSize(4, 4);
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
        MENU_TYPES.register(eventBus);
        TABS.register(eventBus);
    }
}
