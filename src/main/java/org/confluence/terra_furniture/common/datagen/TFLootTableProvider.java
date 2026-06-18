package org.confluence.terra_furniture.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.List;
import java.util.Set;

public class TFLootTableProvider extends LootTableProvider {
    public TFLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(BlockSub::new, LootContextParamSets.BLOCK)
        ));
    }

    public static class BlockSub extends BlockLootSubProvider {
        List<Block> blockList = TFBlocks.BLOCKS.getEntries()
                .stream()
                .map(PortRegistryEntry::value)
                .toList();

        protected BlockSub() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            for (Block block : blockList) {
                if (!(block instanceof DoorBlock)) {
                    dropSelf(block);
                } else {
                    map.put(block.getLootTable(), createDoorTable(block));
                }
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return TFBlocks.BLOCKS.getEntries().stream().map(PortRegistryEntry::value).toList();
        }
    }
}
