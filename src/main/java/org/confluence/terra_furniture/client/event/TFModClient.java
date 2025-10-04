package org.confluence.terra_furniture.client.event;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.client.model.PlasticChairModel;
import org.confluence.terra_furniture.client.renderer.block.*;
import org.confluence.terra_furniture.client.screen.GlassKilnScreen;
import org.confluence.terra_furniture.client.screen.IceMachineScreen;
import org.confluence.terra_furniture.client.screen.LivingLoomScreen;
import org.confluence.terra_furniture.common.block.light.LargeChandelierBlock;
import org.confluence.terra_furniture.common.block.misc.HangingPotBlock;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.confluence.terra_furniture.common.init.TFEntities;
import org.confluence.terra_furniture.common.init.TFRegistries;
import software.bernie.geckolib.renderer.GeoBlockRenderer;


@EventBusSubscriber(modid = TerraFurniture.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class TFModClient {
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TFEntities.NULL_RIDE.get(), NoopRenderer::new);

        event.registerBlockEntityRenderer(TFBlocks.PLASTIC_CHAIR_ENTITY.get(), context -> new GeoBlockRenderer<>(new PlasticChairModel()));
        event.registerBlockEntityRenderer(TFBlocks.CLOCK_ENTITY.get(), context -> new ClockRenderer());
        event.registerBlockEntityRenderer(
                TFBlocks.LARGE_CHANDELIER_ENTITY.get(),
                context -> BaseFunctionalGeoBER.Builder.<LargeChandelierBlock.BEntity>of(true)
                        .addHideRule(3,
                                geoBone -> geoBone.getName().startsWith("flame"),
                                (geoBone, entity) -> !entity.getBlockState().getValue(BlockStateProperties.LIT)
                        )
                        .addRenderHook((IRenderFunctionHook<LargeChandelierBlock.BEntity>) CommonRenderHooks.SWAYING)
                        .renderBox(pos -> new AABB(pos.getX() -1, pos.getY(), pos.getZ()-1, pos.getX() +1, pos.getY() -1, pos.getZ() +1))
                        .build()
        );
        event.registerBlockEntityRenderer(
            TFBlocks.HANGING_POT_ENTITY.get(),
            context -> BaseFunctionalGeoBER.Builder.<HangingPotBlock.BEntity>of(false)
                .addRenderHook((IRenderFunctionHook<HangingPotBlock.BEntity>) CommonRenderHooks.SWAYING)
                .addRenderHook(new HangingPotBlock.AddedRenderer<>())
                .build()
        );
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
