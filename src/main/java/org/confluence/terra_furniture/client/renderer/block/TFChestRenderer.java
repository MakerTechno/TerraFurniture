package org.confluence.terra_furniture.client.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.api.client.renderer.block.BaseFunctionalGeoBER;
import org.confluence.terra_furniture.common.block.misc.TFChestBlock;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

/**
 * 所有套装箱子共用的 Geo 渲染器，与 vanilla 的双箱一致：左/右半箱使用各自 id 的模型与贴图，
 * 例如 {@code geo/block/spruce_chest_left.geo.json} 与 {@code textures/block/spruce_chest_left.png}；
 * 半箱贴图缺失时回退到整箱贴图。
 */
public class TFChestRenderer extends BaseFunctionalGeoBER<TFChestBlock.Entity> {
    public TFChestRenderer(BlockEntityRendererProvider.Context context) {
        super(new TFChestGeoModel(), false);
    }

    private static class TFChestGeoModel extends GeoModel<TFChestBlock.Entity> {
        /* 半箱使用 <id>_left / <id>_right 的模型与贴图 */
        private static String path(TFChestBlock.Entity chest) {
            BlockState state = chest.getBlockState();
            String block = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getPath();
            if (state.hasProperty(ChestBlock.TYPE)) {
                return switch (state.getValue(ChestBlock.TYPE)) {
                    case LEFT -> block + "_left";
                    case RIGHT -> block + "_right";
                    default -> block;
                };
            }
            return block;
        }

        /* 半箱贴图缺失时回退到整箱贴图，避免画出缺失材质 */
        private static ResourceLocation texture(TFChestBlock.Entity chest) {
            ResourceLocation location = TerraFurniture.asResource("textures/block/" + path(chest) + ".png");
            if (Minecraft.getInstance().getResourceManager().getResource(location).isEmpty()) {
                String block = BuiltInRegistries.BLOCK.getKey(chest.getBlockState().getBlock()).getPath();
                location = TerraFurniture.asResource("textures/block/" + block + ".png");
            }
            return location;
        }

        @Override
        public ResourceLocation getModelResource(TFChestBlock.Entity chest) {
            return TerraFurniture.asResource("geo/block/" + path(chest) + ".geo.json");
        }

        @Override
        public ResourceLocation getTextureResource(TFChestBlock.Entity chest) {
            // 每次使用当前资源管理器查询，避免资源重载后保留旧的回退结果。
            return texture(chest);
        }

        /* 箱盖由 setCustomAnimations 驱动，沿用 GeoModel 的动画资源接口。 */
        @Override
        public ResourceLocation getAnimationResource(TFChestBlock.Entity chest) {
            String block = BuiltInRegistries.BLOCK.getKey(chest.getBlockState().getBlock()).getPath();
            return TerraFurniture.asResource("animations/block/" + block + ".animation.json");
        }

        @Override
        public void setCustomAnimations(TFChestBlock.Entity chest, long instanceId,
                                        AnimationState<TFChestBlock.Entity> animationState) {
            super.setCustomAnimations(chest, instanceId, animationState);
            float openness = openness(chest, animationState.getPartialTick());
            float closed = 1.0F - openness;
            float eased = 1.0F - closed * closed * closed;
            // down 骨骼控制箱盖，绕后侧铰链打开。
            getBone("down").ifPresent(lid -> lid.setRotX(-eased * (float) (Math.PI / 2.0)));
        }

        /* 双箱两半共用盖子开合进度，与 vanilla 的 ChestRenderer 一致 */
        private static float openness(TFChestBlock.Entity chest, float partialTick) {
            Level level = chest.getLevel();
            BlockState state = chest.getBlockState();
            if (level != null && state.getBlock() instanceof ChestBlock chestBlock) {
                return chestBlock.combine(state, level, chest.getBlockPos(), true)
                        .apply(ChestBlock.opennessCombiner(chest)).get(partialTick);
            }
            return chest.getOpenNess(partialTick);
        }
    }
}
