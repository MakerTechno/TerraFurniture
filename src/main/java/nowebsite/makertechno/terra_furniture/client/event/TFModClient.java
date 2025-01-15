package nowebsite.makertechno.terra_furniture.client.event;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.client.model.block.PlasticChairModel;
import nowebsite.makertechno.terra_furniture.client.renderer.entity.EmptyEntityRenderer;
import nowebsite.makertechno.terra_furniture.common.block.chair.PlasticChairBlock;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import nowebsite.makertechno.terra_furniture.common.init.TFEntities;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = TerraFurniture.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class TFModClient {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TFEntities.CHAIR.get(), EmptyEntityRenderer::new);

        event.registerBlockEntityRenderer(TFBlocks.PLASTIC_CHAIR_ENTITY.get(), context -> new GeoBlockRenderer<>(new PlasticChairModel()));
    }

    @SubscribeEvent
    public static void r(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private GeoItemRenderer<PlasticChairBlock.Item> renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    this.renderer = new GeoItemRenderer<>(new GeoModel<>() {
                        @Override
                        public ResourceLocation getModelResource(PlasticChairBlock.Item animatable) {
                            return PlasticChairModel.MODEL;
                        }

                        @Override
                        public ResourceLocation getTextureResource(PlasticChairBlock.Item animatable) {
                            return PlasticChairModel.TEXTURE;
                        }

                        @Override
                        public @Nullable ResourceLocation getAnimationResource(PlasticChairBlock.Item animatable) {
                            return null;
                        }
                    });
                }
                return renderer;
            }
        }, TFBlocks.PLASTIC_CHAIR.asItem());
    }
}
