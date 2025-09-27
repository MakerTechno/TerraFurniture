package org.confluence.terra_furniture.client.event;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.client.model.PlasticChairModel;
import org.confluence.terra_furniture.client.renderer.block.ClockRenderer;
import org.confluence.terra_furniture.client.renderer.block.HangingPotBER;
import org.confluence.terra_furniture.client.renderer.block.LargeChandelierBlockRenderer;
import org.confluence.terra_furniture.client.screen.GlassKilnScreen;
import org.confluence.terra_furniture.client.screen.IceMachineScreen;
import org.confluence.terra_furniture.client.screen.LivingLoomScreen;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFEntities;
import org.confluence.terra_furniture.common.init.TFRegistries;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@EventBusSubscriber(modid = TerraFurniture.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class TFModClient {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TFEntities.NULL_RIDE.get(), NoopRenderer::new);

        event.registerBlockEntityRenderer(TFBlocks.PLASTIC_CHAIR_ENTITY.get(), context -> new GeoBlockRenderer<>(new PlasticChairModel()));
        event.registerBlockEntityRenderer(TFBlocks.CLOCK_ENTITY.get(), context -> new ClockRenderer());
        event.registerBlockEntityRenderer(TFBlocks.LARGE_CHANDELIER_ENTITY.get(), context -> new LargeChandelierBlockRenderer());
        event.registerBlockEntityRenderer(TFBlocks.HANGING_POT_ENTITY.get(), context -> new HangingPotBER());
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(TFRegistries.GLASS_KILN_MENU.get(), GlassKilnScreen::new);
        event.register(TFRegistries.LIVING_LOOM_MENU.get(), LivingLoomScreen::new);
        event.register(TFRegistries.ICE_MACHINE_MENU.get(), IceMachineScreen::new);
    }

    @SubscribeEvent
    public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        TFRegistries.RECIPE_TYPES.getEntries().forEach(holder -> event.registerRecipeCategoryFinder(holder.get(), recipeHolder -> RecipeBookCategories.UNKNOWN));
    }
}
