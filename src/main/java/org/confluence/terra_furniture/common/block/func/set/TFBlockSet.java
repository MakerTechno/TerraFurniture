package org.confluence.terra_furniture.common.block.func.set;

import com.google.common.base.Supplier;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.confluence.terra_furniture.common.block.light.BlockShapeType;
import org.confluence.terra_furniture.common.block.light.CandelabraBlock;
import org.confluence.terra_furniture.common.block.light.LargeChandelierBlock;
import org.confluence.terra_furniture.common.block.light.SwitchableLightBlock;
import org.confluence.terra_furniture.common.block.misc.ClockBlock;
import org.confluence.terra_furniture.common.block.misc.SinkBlock;
import org.confluence.terra_furniture.common.block.misc.TFDoorBlock;
import org.confluence.terra_furniture.common.block.misc.TableBlock;
import org.confluence.terra_furniture.common.block.sittable.ChairBlock;
import org.confluence.terra_furniture.common.block.sittable.SofaBlock;
import org.confluence.terra_furniture.common.block.sittable.ToiletBlock;
import org.confluence.terra_furniture.common.block.sleep.BathtubBlock;
import org.confluence.terra_furniture.common.block.sleep.TFBedBlock;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.registries.PortDeferredBlock;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class TFBlockSet {
    /* Vanilla support */
    public final PortDeferredBlock<ButtonBlock> BUTTON;
    public final PortDeferredBlock<PressurePlateBlock> PRESSURE_PLATE;
    public final PortDeferredBlock<SlabBlock> SLAB;
    public final PortDeferredBlock<StairBlock> STAIRS;
    public final PortDeferredBlock<TrapDoorBlock> TRAPDOOR;

    /* Special furniture */
    public final PortDeferredBlock<TFDoorBlock> DOOR; // This is a bit different from vanilla
    public final PortDeferredBlock<TableBlock> TABLE;
    public final PortDeferredBlock<ChairBlock> CHAIR;
    public final PortDeferredBlock<SofaBlock> SOFA;
    public final PortDeferredBlock<ToiletBlock> TOILET;
    public final PortDeferredBlock<TFBedBlock> BED;
    public final PortDeferredBlock<BathtubBlock> BATHTUB;
    public final PortDeferredBlock<SinkBlock> SINK;

    /* Not completed */
    public final PortDeferredBlock<ClockBlock> CLOCK;
    public final PortDeferredBlock<LargeChandelierBlock> LARGE_CHANDELIER; // This one uses GeoBER model
    public final PortDeferredBlock<SwitchableLightBlock> CANDLE;
    public final PortDeferredBlock<SwitchableLightBlock> LANTERN;
    public final PortDeferredBlock<SwitchableLightBlock> LAMP;
    public final PortDeferredBlock<CandelabraBlock> CANDELABRAS;

    protected TFBlockSet(Builder builder) {
        BUTTON = init(builder, TFBlockType.BUTTON);
        PRESSURE_PLATE = init(builder, TFBlockType.PRESSURE_PLATE);
        SLAB = init(builder, TFBlockType.SLAB);
        STAIRS = init(builder, TFBlockType.STAIRS);
        TRAPDOOR = init(builder, TFBlockType.TRAPDOOR);
        DOOR = init(builder, TFBlockType.DOOR);
        TABLE = init(builder, TFBlockType.TABLE);
        CHAIR = init(builder, TFBlockType.CHAIR);
        SOFA = init(builder, TFBlockType.SOFA);
        TOILET = init(builder, TFBlockType.TOILET);
        BED = init(builder, TFBlockType.BED);
        BATHTUB = init(builder, TFBlockType.BATHTUB);
        SINK = init(builder, TFBlockType.SINK);
        LARGE_CHANDELIER = initLargeChandelier(builder);
        CANDLE = init(builder, TFBlockType.CANDLE);
        LANTERN = init(builder, TFBlockType.LANTERN);
        LAMP = init(builder, TFBlockType.LAMP);
        CLOCK = init(builder, TFBlockType.CLOCK);
        CANDELABRAS = init(builder, TFBlockType.CANDELABRAS);
    }

    @SuppressWarnings("all")
    public static <T extends Block> PortDeferredBlock<T> init(Builder builder, TFBlockType<T> type) {
        Builder.TFBlockBuildEntry<T> entry = builder.getEntry(type);
        if (!entry.available) {
            return null;
        }
        PortDeferredBlock<T> block = TFBlocks.registerWithItem(entry.specialId != null ? entry.specialId : builder.materialType.name() + "_" + type.name(), entry.getEntryResult());
        type.register(block);
        return block;
    }

    @SuppressWarnings("all")
    public static PortDeferredBlock<LargeChandelierBlock> initLargeChandelier(Builder builder) {
        Builder.TFBlockBuildEntry<LargeChandelierBlock> entry = builder.getEntry(TFBlockType.LARGE_CHANDELIER);
        if (!entry.available) {
            return null;
        }
        PortDeferredBlock<LargeChandelierBlock> block = TFBlocks.registerLargeChandelier(entry.specialId != null ? entry.specialId : builder.materialType.name() + "_" + TFBlockType.LARGE_CHANDELIER.name(), entry.getEntryResult());
        TFBlockType.LARGE_CHANDELIER.register(block);
        return block;
    }

    public static class Builder {
        public static class TFBlockBuildEntry<T extends Block> {
            public boolean available = true;
            public @Nullable String specialId;
            public BiFunction<BlockBehaviour.Properties, Consumer<BlockBehaviour.Properties>, T> blockSupplier;
            public BlockBehaviour.Properties properties;
            public Consumer<BlockBehaviour.Properties> applier = properties1 -> {};
            public final TFBlockType<T> blockType;

            public TFBlockBuildEntry(TFBlockType<T> blockType, BlockBehaviour.Properties defaultProp, BiFunction<BlockBehaviour.Properties, Consumer<BlockBehaviour.Properties>, T> blockSupplier) {
                this.blockType = blockType;
                this.properties = defaultProp;
                this.blockSupplier = blockSupplier;
            }

            public Supplier<T> getEntryResult() {
                /* Copy to instance-like */
                BlockBehaviour.Properties propertiesFinal = this.properties;
                Consumer<BlockBehaviour.Properties> applierFinal = applier;
                BiFunction<BlockBehaviour.Properties, Consumer<BlockBehaviour.Properties>, T> blockSupplierFinal = blockSupplier;

                return () -> blockSupplierFinal.apply(propertiesFinal, applierFinal);
            }
        }

        protected final TFBlockSetType materialType;
        protected final boolean fullCopyProp;
        private final Block propSourceBlock;
        private int buttonPressedTick = 30;
        private float chairSitHeight = 0.5f;
        private float sofaSitHeight = 0.55f;
        private float toiletSitHeight = 11.0f / 16;
        private int candleBlockLight = 14;
        private int lanternBlockLight = 14;
        private int lamp, candelabras, chandelier;

        private final Map<TFBlockType<?>, TFBlockBuildEntry<?>> entries = new Object2ObjectArrayMap<>();

        public Builder(TFBlockSetType materialType, Block propSourceBlock, boolean fullCopyProp) {
            this.materialType = materialType;
            this.fullCopyProp = fullCopyProp;
            this.propSourceBlock = propSourceBlock;
            putEntry(TFBlockType.BUTTON, (p, a) -> new ButtonBlock(p, materialType.getType(), this.buttonPressedTick, true));
            putEntry(TFBlockType.PRESSURE_PLATE, (p, a) -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p, materialType.getType()));
            putEntry(TFBlockType.SLAB, (p, a) -> new SlabBlock(p));
            putEntry(TFBlockType.STAIRS, (p, a) -> new StairBlock(propSourceBlock::defaultBlockState, p));
            putEntry(TFBlockType.TRAPDOOR, (p, a) -> new TrapDoorBlock(p, materialType.getType()));
            putEntry(TFBlockType.DOOR, (p, a) -> new TFDoorBlock(materialType, p));
            putEntry(TFBlockType.TABLE, (p, a) -> new TableBlock(materialType, p));
            putEntry(TFBlockType.CHAIR, (p, a) -> new ChairBlock(materialType, propSourceBlock.defaultBlockState(), a, this.chairSitHeight));
            putEntry(TFBlockType.SOFA, (p, a) -> new SofaBlock(materialType, propSourceBlock.defaultBlockState(), a, this.sofaSitHeight));
            putEntry(TFBlockType.TOILET, (p, a) -> new ToiletBlock(materialType, propSourceBlock.defaultBlockState(), a, this.toiletSitHeight));
            putEntry(TFBlockType.BED, (p, a) -> new TFBedBlock(materialType, p));
            putEntry(TFBlockType.BATHTUB, (p, a) -> new BathtubBlock(materialType, p));
            putEntry(TFBlockType.SINK, (p, a) -> new SinkBlock(materialType, propSourceBlock.defaultBlockState(), p));
            putEntry(TFBlockType.LARGE_CHANDELIER, (p, a) -> new LargeChandelierBlock(p));
            putEntry(TFBlockType.CANDLE, (p, a) -> new SwitchableLightBlock(materialType, p, BlockShapeType.CANDLE));
            putEntry(TFBlockType.LANTERN, (p, a) -> new SwitchableLightBlock(materialType, p, BlockShapeType.LANTERN));
            putEntry(TFBlockType.LAMP, (p, a) -> new SwitchableLightBlock(materialType, p, BlockShapeType.LAMP));
            putEntry(TFBlockType.CHANDELIER, (p, a) -> new SwitchableLightBlock(materialType, p, BlockShapeType.CHANDELIER));
            putEntry(TFBlockType.CLOCK, (p, a) -> new ClockBlock(p));
            putEntry(TFBlockType.CANDELABRAS, (p, a) -> new CandelabraBlock(materialType, p));
        }

        protected <T extends Block> void putEntry(TFBlockType<T> type, BiFunction<BlockBehaviour.Properties, Consumer<BlockBehaviour.Properties>, T> blockSupplier) {
            entries.put(type, new TFBlockBuildEntry<>(type, BlockBehaviour.Properties.copy(propSourceBlock), blockSupplier));
        }

        @SuppressWarnings("unchecked")
        public <T extends Block> TFBlockBuildEntry<T> getEntry(TFBlockType<T> type) {
            return (TFBlockBuildEntry<T>) entries.get(type);
        }

        public Builder disableAll() {
            entries.values().forEach(entry -> entry.available = false);
            return this;
        }

        public Builder disableVanilla() {
            entries.get(TFBlockType.BUTTON).available = false;
            entries.get(TFBlockType.PRESSURE_PLATE).available = false;
            entries.get(TFBlockType.SLAB).available = false;
            entries.get(TFBlockType.STAIRS).available = false;
            entries.get(TFBlockType.TRAPDOOR).available = false;
            return this;
        }

        public <T extends Block> Builder setAvailabilityFor(TFBlockType<T> key, boolean availability) {
            entries.get(key).available = availability;
            return this;
        }

        @SuppressWarnings("all")
        public <T extends Block> Builder setGetterFor(TFBlockType<T> key, BiFunction<BlockBehaviour.Properties, Consumer<BlockBehaviour.Properties>, T> instanceGetter) {
            ((TFBlockBuildEntry<T>) entries.get(key)).blockSupplier = instanceGetter;
            return this;
        }

        public <T extends Block> Builder setSpecialIdFor(TFBlockType<T> key, String id) {
            entries.get(key).specialId = id;
            return this;
        }

        public <T extends Block> Builder setPropertyApplierFor(TFBlockType<T> key, Consumer<BlockBehaviour.Properties> applier) {
            entries.get(key).applier = applier;
            return this;
        }

        public <T extends Block> Builder setPropertyFor(TFBlockType<T> key, Function<BlockBehaviour.Properties, BlockBehaviour.Properties> properties) {
            BlockBehaviour.Properties old = entries.get(key).properties;
            entries.get(key).properties = properties.apply(old);
            return this;
        }

        public Builder doLightSetup(int candle, int lantern, int lamp, int candelabras, int chandelier) {
            this.candleBlockLight = candle;
            this.lanternBlockLight = lantern;
            this.lamp = lamp;
            this.candelabras = candelabras;
            this.chandelier = chandelier;
            return this;
        }

        public Builder buttonPressedTick(int buttonPressedTick) {
            this.buttonPressedTick = buttonPressedTick;
            return this;
        }

        public Builder chairSitHeight(float chairSitHeight) {
            this.chairSitHeight = chairSitHeight;
            return this;
        }

        public Builder sofaSitHeight(float sofaSitHeight) {
            this.sofaSitHeight = sofaSitHeight;
            return this;
        }

        public Builder toiletSitHeight(float toiletSitHeight) {
            this.toiletSitHeight = toiletSitHeight;
            return this;
        }

        public TFBlockSet build() {
            /* Light init */
            setPropertyFor(TFBlockType.CHANDELIER, properties -> properties.lightLevel(TFBlocks.litBlockEmission(chandelier)));
            setPropertyFor(TFBlockType.CANDLE, properties -> properties.lightLevel(TFBlocks.litBlockEmission(candleBlockLight)));
            setPropertyFor(TFBlockType.LANTERN, properties -> properties.lightLevel(TFBlocks.litBlockEmission(lanternBlockLight)));
            setPropertyFor(TFBlockType.LAMP, properties -> properties.lightLevel(TFBlocks.litBlockEmission(lamp)));
            setPropertyFor(TFBlockType.CANDELABRAS, properties -> properties.lightLevel(TFBlocks.litBlockEmission(candelabras)));
            return new TFBlockSet(this);
        }
    }
}
