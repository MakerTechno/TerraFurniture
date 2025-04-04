package nowebsite.makertechno.terra_furniture.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import nowebsite.makertechno.terra_furniture.TerraFurniture;

public final class TFTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, TerraFurniture.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FURNITURE = TABS.register("furniture", () -> CreativeModeTab.builder().icon(TFBlocks.PLASTIC_CHAIR::toStack)
            .title(Component.translatable("creativetab.terra_furniture"))
            .displayItems((parameters, output) -> {
                TFItems.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
                TFBlocks.BLOCKS.getEntries().forEach(block -> output.accept(block.get()));
            })
            .withTabsAfter(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath("confluence", "mechanical")))
            .withTabsBefore(ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath("confluence", "building_blocks")))
            .build());

}
