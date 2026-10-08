package org.confluence.terra_furniture.common.block.func.set;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.confluence.terra_furniture.common.block.light.BlockShapeType;
import org.confluence.terra_furniture.common.block.light.CandelabraBlock;
import org.confluence.terra_furniture.common.block.light.LargeChandelierBlock;
import org.confluence.terra_furniture.common.block.light.SwitchableLightBlock;
import org.confluence.terra_furniture.common.block.misc.ClockBlock;
import org.confluence.terra_furniture.common.block.misc.OneLegTableBlock;
import org.confluence.terra_furniture.common.block.misc.SinkBlock;
import org.confluence.terra_furniture.common.block.misc.TFChestBlock;
import org.confluence.terra_furniture.common.block.misc.TFDoorBlock;
import org.confluence.terra_furniture.common.block.misc.TableBlock;
import org.confluence.terra_furniture.common.block.sittable.ChairBlock;
import org.confluence.terra_furniture.common.block.sittable.SofaBlock;
import org.confluence.terra_furniture.common.block.sittable.ToiletBlock;
import org.confluence.terra_furniture.common.block.sleep.BathtubBlock;
import org.confluence.terra_furniture.common.block.sleep.TFBedBlock;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TFBlockSet {
    /* Vanilla support */
    public final @Nullable DeferredBlock<ButtonBlock> BUTTON;
    public final @Nullable DeferredBlock<PressurePlateBlock> PRESSURE_PLATE;
    public final @Nullable DeferredBlock<SlabBlock> SLAB;
    public final @Nullable DeferredBlock<StairBlock> STAIRS;
    public final @Nullable DeferredBlock<TrapDoorBlock> TRAPDOOR;

    /* Special furniture */
    public final @Nullable DeferredBlock<TFDoorBlock> DOOR; // This is a bit different from vanilla
    public final @Nullable DeferredBlock<TableBlock> TABLE;
    public final @Nullable DeferredBlock<ChairBlock> CHAIR;
    public final @Nullable DeferredBlock<SofaBlock> SOFA;
    public final @Nullable DeferredBlock<ToiletBlock> TOILET;
    public final @Nullable DeferredBlock<TFBedBlock> BED;
    public final @Nullable DeferredBlock<BathtubBlock> BATHTUB;
    public final @Nullable DeferredBlock<SinkBlock> SINK;

    /* 可选家具，需要套装显式启用并配置后才注册 */
    public final @Nullable DeferredBlock<TFChestBlock> CHEST;
    public final @Nullable DeferredBlock<OneLegTableBlock> ONE_LEG_TABLE;
    public final List<DeferredBlock<SwitchableLightBlock>> CANDLESTICKS;

    /* Not completed */
    public final @Nullable DeferredBlock<ClockBlock> CLOCK;
    public final @Nullable DeferredBlock<LargeChandelierBlock> LARGE_CHANDELIER; // This one uses GeoBER model
    public final @Nullable DeferredBlock<SwitchableLightBlock> CANDLE;
    public final @Nullable DeferredBlock<SwitchableLightBlock> LANTERN;
    public final @Nullable DeferredBlock<SwitchableLightBlock> LAMP;
    public final @Nullable DeferredBlock<SwitchableLightBlock> CHANDELIER;
    public final @Nullable DeferredBlock<CandelabraBlock> CANDELABRAS;

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
        CHANDELIER = init(builder, TFBlockType.CHANDELIER);
        CLOCK = init(builder, TFBlockType.CLOCK);
        CANDELABRAS = init(builder, TFBlockType.CANDELABRAS);
        CHEST = init(builder, TFBlockType.CHEST);
        ONE_LEG_TABLE = init(builder, TFBlockType.ONE_LEG_TABLE);
        CANDLESTICKS = initVariants(builder, TFBlockType.CANDLESTICK);
    }

    @Nullable
    public static <T extends Block> DeferredBlock<T> init(Builder builder, TFBlockType<T> type) {
        Builder.TFBlockBuildEntry<T> entry = builder.getEntry(type);
        if (!entry.available) {
            return null;
        }
        String id = entry.specialId != null ? entry.specialId : builder.materialType.name() + "_" + type.name();
        DeferredBlock<T> block = entry.itemFactory == null
                ? TFBlocks.registerWithItem(id, entry.getEntryResult())
                : TFBlocks.registerWithItem(id, entry.getEntryResult(), entry.itemFactory);
        type.register(block);
        return block;
    }

    /**
     * 注册多方块类型的全部变体，例如云杉木烛台。
     * 注册顺序由传入的 variants map 的迭代顺序决定。
     */
    public static <T extends Block> List<DeferredBlock<T>> initVariants(Builder builder, TFBlockType<T> type) {
        Builder.TFBlockBuildEntry<T> entry = builder.getEntry(type);
        if (!entry.available) {
            return List.of();
        }
        if (entry.variants.isEmpty()) {
            throw new IllegalStateException("Furniture type " + type.name() + " is enabled without any variant");
        }
        BlockBehaviour.Properties properties = entry.properties;
        Consumer<BlockBehaviour.Properties> applier = entry.applier;
        List<DeferredBlock<T>> blocks = new ArrayList<>(entry.variants.size());
        int index = 0;
        Function<T, BlockItem> itemFactory = entry.itemFactory;
        for (Map.Entry<String, Builder.Variant<T>> variant : entry.variants.entrySet()) {
            Builder.Variant<T> factory = variant.getValue();
            Supplier<T> blockSupplier = () -> factory.create(properties, applier);
            index++;
            String id = variant.getKey() != null && !variant.getKey().isBlank()
                    ? variant.getKey()
                    : builder.materialType.name() + "_" + type.name() + "_" + index;
            DeferredBlock<T> block = itemFactory == null
                    ? TFBlocks.registerWithItem(id, blockSupplier)
                    : TFBlocks.registerWithItem(id, blockSupplier, itemFactory);
            type.register(block);
            blocks.add(block);
        }
        return List.copyOf(blocks);
    }

    @Nullable
    public static DeferredBlock<LargeChandelierBlock> initLargeChandelier(Builder builder) {
        Builder.TFBlockBuildEntry<LargeChandelierBlock> entry = builder.getEntry(TFBlockType.LARGE_CHANDELIER);
        if (!entry.available) {
            return null;
        }
        DeferredBlock<LargeChandelierBlock> block = TFBlocks.registerLargeChandelier(entry.specialId != null ? entry.specialId : builder.materialType.name() + "_" + TFBlockType.LARGE_CHANDELIER.name(), entry.getEntryResult());
        TFBlockType.LARGE_CHANDELIER.register(block);
        return block;
    }

    public static class Builder {
        /**
         * 多方块类型中的单个变体，由该类型配置好的方块属性创建。
         */
        @FunctionalInterface
        public interface Variant<T extends Block> {
            T create(BlockBehaviour.Properties properties, Consumer<BlockBehaviour.Properties> applier);
        }

        public static class TFBlockBuildEntry<T extends Block> {
            public boolean available = true;
            public @Nullable String specialId;
            public @Nullable BiFunction<BlockBehaviour.Properties, Consumer<BlockBehaviour.Properties>, T> blockSupplier;
            public BlockBehaviour.Properties properties;
            public Consumer<BlockBehaviour.Properties> applier = properties -> {};
            public @Nullable Function<T, BlockItem> itemFactory;
            public final Map<String, Variant<T>> variants = new LinkedHashMap<>();
            public final TFBlockType<T> blockType;

            public TFBlockBuildEntry(TFBlockType<T> blockType, BlockBehaviour.Properties defaultProp, @Nullable BiFunction<BlockBehaviour.Properties, Consumer<BlockBehaviour.Properties>, T> blockSupplier) {
                this.blockType = blockType;
                this.properties = defaultProp;
                this.blockSupplier = blockSupplier;
            }

            public Supplier<T> getEntryResult() {
                if (blockSupplier == null) {
                    throw new IllegalStateException("Furniture type " + blockType.name() + " has no block getter, call setGetterFor(...) first");
                }
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
            putEntry(TFBlockType.BUTTON, (p, a) -> new ButtonBlock(materialType.getType(), this.buttonPressedTick, p));
            putEntry(TFBlockType.PRESSURE_PLATE, (p, a) -> new PressurePlateBlock(materialType.getType(), p));
            putEntry(TFBlockType.SLAB, (p, a) -> new SlabBlock(p));
            putEntry(TFBlockType.STAIRS, (p, a) -> new StairBlock(propSourceBlock.defaultBlockState(), p));
            putEntry(TFBlockType.TRAPDOOR, (p, a) -> new TrapDoorBlock(materialType.getType(), p));
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
            putOptionalEntry(TFBlockType.CHEST);
            putOptionalEntry(TFBlockType.ONE_LEG_TABLE);
            putOptionalEntry(TFBlockType.CANDLESTICK);
        }

        protected <T extends Block> void putEntry(TFBlockType<T> type, BiFunction<BlockBehaviour.Properties, Consumer<BlockBehaviour.Properties>, T> blockSupplier) {
            entries.put(type, new TFBlockBuildEntry<>(type, fullCopyProp ? BlockBehaviour.Properties.ofFullCopy(propSourceBlock) : BlockBehaviour.Properties.ofLegacyCopy(propSourceBlock), blockSupplier));
        }

        /**
         * 声明一个需要套装通过 {@link #setAvailabilityFor} 启用、并通过 {@link #setGetterFor}
         * （多方块类型则用 {@link #setVariantsFor}）提供方块获取器的类型。
         */
        protected <T extends Block> void putOptionalEntry(TFBlockType<T> type) {
            TFBlockBuildEntry<T> entry = new TFBlockBuildEntry<>(type, fullCopyProp ? BlockBehaviour.Properties.ofFullCopy(propSourceBlock) : BlockBehaviour.Properties.ofLegacyCopy(propSourceBlock), null);
            entry.available = false;
            entries.put(type, entry);
        }

        @SuppressWarnings("unchecked")
        public <T extends Block> TFBlockBuildEntry<T> getEntry(TFBlockType<T> type) {
            TFBlockBuildEntry<?> entry = entries.get(Objects.requireNonNull(type, "Furniture type"));
            if (entry == null) {
                throw new IllegalArgumentException("Unsupported furniture type: " + type.name());
            }
            return (TFBlockBuildEntry<T>) entry;
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
            getEntry(key).available = availability;
            return this;
        }

        public <T extends Block> Builder setGetterFor(TFBlockType<T> key, BiFunction<BlockBehaviour.Properties, Consumer<BlockBehaviour.Properties>, T> instanceGetter) {
            getEntry(key).blockSupplier = Objects.requireNonNull(instanceGetter, "Block supplier");
            return this;
        }

        public <T extends Block> Builder setSpecialIdFor(TFBlockType<T> key, String id) {
            getEntry(key).specialId = id;
            return this;
        }

        /**
         * 为该类型使用自定义物品，而非默认的 BlockItem，例如使用 Geo 渲染的樱花木箱物品。
         */
        public <T extends Block> Builder setItemFactoryFor(TFBlockType<T> key, Function<T, BlockItem> itemFactory) {
            getEntry(key).itemFactory = itemFactory;
            return this;
        }

        /**
         * 声明多方块类型的全部方块，例如云杉木单/双/三烛台。
         * map 的键为方块 id，键为空白时回退为 {@code <材质>_<类型>_<序号>}。
         */
        public <T extends Block> Builder setVariantsFor(TFBlockType<T> key, Map<String, Variant<T>> variants) {
            TFBlockBuildEntry<T> entry = getEntry(key);
            Map<String, Variant<T>> copy = new LinkedHashMap<>(variants);
            copy.values().forEach(variant -> Objects.requireNonNull(variant, "Variant supplier"));
            entry.variants.clear();
            entry.variants.putAll(copy);
            return this;
        }

        /**
         * 以其他方块的属性作为该类型的基础属性，而非套装材质的属性。
         */
        public <T extends Block> Builder setPropertySourceFor(TFBlockType<T> key, Block source) {
            getEntry(key).properties = BlockBehaviour.Properties.ofFullCopy(source);
            return this;
        }

        public <T extends Block> Builder setPropertyApplierFor(TFBlockType<T> key, Consumer<BlockBehaviour.Properties> applier) {
            getEntry(key).applier = Objects.requireNonNull(applier, "Property applier");
            return this;
        }

        public <T extends Block> Builder setPropertyFor(TFBlockType<T> key, Function<BlockBehaviour.Properties, BlockBehaviour.Properties> properties) {
            BlockBehaviour.Properties old = getEntry(key).properties;
            getEntry(key).properties = Objects.requireNonNull(properties.apply(old), "Block properties");
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
