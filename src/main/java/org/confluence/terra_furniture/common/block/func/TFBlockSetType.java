package org.confluence.terra_furniture.common.block.func;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import java.util.List;
import java.util.function.Supplier;

public final class TFBlockSetType {
    private final BlockSetType type;
    private final Supplier<List<TagKey<Block>>> tagKeys;

    public TFBlockSetType(BlockSetType type, Supplier<List<TagKey<Block>>> key) {
        this.type = type;
        this.tagKeys = key;
    }

    public TFBlockSetType(String name, Supplier<List<TagKey<Block>>> key) {
        this.type = BlockSetType.register(new BlockSetType(name));
        this.tagKeys = key;
    }

    public TFBlockSetType(
            String name,
            boolean canOpenByHand,
            boolean canOpenByWindCharge,
            boolean canButtonBeActivatedByArrows,
            BlockSetType.PressurePlateSensitivity pressurePlateSensitivity,
            SoundType soundType,
            SoundEvent doorClose,
            SoundEvent doorOpen,
            SoundEvent trapdoorClose,
            SoundEvent trapdoorOpen,
            SoundEvent pressurePlateClickOff,
            SoundEvent pressurePlateClickOn,
            SoundEvent buttonClickOff,
            SoundEvent buttonClickOn,
            Supplier<List<TagKey<Block>>> key
    ) {
        this.type = BlockSetType.register(new BlockSetType(
                name,
                canOpenByHand,
                canOpenByWindCharge,
                canButtonBeActivatedByArrows,
                pressurePlateSensitivity,
                soundType,
                doorClose,
                doorOpen,
                trapdoorClose,
                trapdoorOpen,
                pressurePlateClickOff,
                pressurePlateClickOn,
                buttonClickOff,
                buttonClickOn
        ));
        this.tagKeys = key;
    }

    public BlockSetType getType() {
        return type;
    }

    public Supplier<List<TagKey<Block>>> getTagKeys() {
        return tagKeys;
    }

    public String name() {
        return type.name();
    }
}
