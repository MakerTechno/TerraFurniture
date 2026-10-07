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
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.concurrent.ConcurrentHashMap;

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
        private static final ConcurrentHashMap<String, ResourceLocation> MODEL = new ConcurrentHashMap<>();
        private static final ConcurrentHashMap<String, ResourceLocation> TEXTURE = new ConcurrentHashMap<>();
        private static final ConcurrentHashMap<String, ResourceLocation> ANIMATION = new ConcurrentHashMap<>();

        /* 半箱使用 <id>_left / <id>_right 的模型、贴图与动画 */
        private static String path(TFChestBlock.Entity chest) {
            String block = BuiltInRegistries.BLOCK.getKey(chest.getBlockState().getBlock()).getPath();
            BlockState state = chest.getBlockState();
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
            if (!Minecraft.getInstance().getResourceManager().getResource(location).isPresent()) {
                String block = BuiltInRegistries.BLOCK.getKey(chest.getBlockState().getBlock()).getPath();
                location = TerraFurniture.asResource("textures/block/" + block + ".png");
            }
            return location;
        }

        @Override
        public ResourceLocation getModelResource(TFChestBlock.Entity chest) {
            return MODEL.computeIfAbsent(path(chest), id -> TerraFurniture.asResource("geo/block/" + id + ".geo.json"));
        }

        @Override
        public ResourceLocation getTextureResource(TFChestBlock.Entity chest) {
            return TEXTURE.computeIfAbsent(path(chest), id -> texture(chest));
        }

        /* 双箱两半共用整箱的动画文件，缺失时 GeckoLib 会自行忽略 */
        @Override
        public ResourceLocation getAnimationResource(TFChestBlock.Entity chest) {
            String block = BuiltInRegistries.BLOCK.getKey(chest.getBlockState().getBlock()).getPath();
            return ANIMATION.computeIfAbsent(block, id -> TerraFurniture.asResource("animations/block/" + id + ".animation.json"));
        }

        @Override
        public void setCustomAnimations(TFChestBlock.Entity chest, long instanceId,
                                        AnimationState<TFChestBlock.Entity> animationState) {
            super.setCustomAnimations(chest, instanceId, animationState);
            float openness = openness(chest, animationState.getPartialTick());
            float eased = 1.0F - (float) Math.pow(1.0F - openness, 3);
            // 源模型的方块绕 Z 轴翻转，因此可见的盖子是名为 down 的骨骼
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
