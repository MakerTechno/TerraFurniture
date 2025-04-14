package nowebsite.makertechno.terra_furniture.client.event;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.client.model.block.PlasticChairModel;
import nowebsite.makertechno.terra_furniture.client.screen.GlassKilnScreen;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import nowebsite.makertechno.terra_furniture.common.init.TFEntities;
import nowebsite.makertechno.terra_furniture.common.init.TFRegistries;
import org.confluence.lib.client.screen.ShapedAmountContainerScreen4x;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@EventBusSubscriber(modid = TerraFurniture.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class TFModClient {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TFEntities.NULL_RIDE.get(), NoopRenderer::new);

        event.registerBlockEntityRenderer(TFBlocks.PLASTIC_CHAIR_ENTITY.get(), context -> new GeoBlockRenderer<>(new PlasticChairModel()));
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(TFRegistries.GLASS_KILN_MENU.get(), GlassKilnScreen::new);
        ShapedAmountContainerScreen4x.register(TFRegistries.LIVING_LOOM_MENU.get(), event::register);
    }
}
