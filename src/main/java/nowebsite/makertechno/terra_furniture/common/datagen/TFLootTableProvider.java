package nowebsite.makertechno.terra_furniture.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TFLootTableProvider extends LootTableProvider {
    public TFLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(BlockSub::new, LootContextParamSets.BLOCK)
        ), registries);
    }

    public static class BlockSub extends BlockLootSubProvider {
        protected BlockSub(HolderLookup.Provider provider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
        }

        @Override
        protected void generate() {
            TFBlocks.BLOCKS.getEntries().forEach(block -> dropSelf(block.get()));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return TFBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toUnmodifiableList());
        }
    }
}
