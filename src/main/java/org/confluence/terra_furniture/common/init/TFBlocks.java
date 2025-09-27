package org.confluence.terra_furniture.common.init;

import com.mojang.datafixers.DSL;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.crafting.GlassKilnBlock;
import org.confluence.terra_furniture.common.block.crafting.IceMachineBlock;
import org.confluence.terra_furniture.common.block.crafting.LivingLoomBlock;
import org.confluence.terra_furniture.common.block.light.*;
import org.confluence.terra_furniture.common.block.misc.*;
import org.confluence.terra_furniture.common.block.sittable.ChairBlock;
import org.confluence.terra_furniture.common.block.sittable.PlasticChairBlock;
import org.confluence.terra_furniture.common.block.sittable.SofaBlock;
import org.confluence.terra_furniture.common.block.sittable.ToiletBlock;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static net.minecraft.world.level.block.state.properties.BlockSetType.STONE;

public final class TFBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TerraFurniture.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TerraFurniture.MODID);
    private static List<DeferredBlock<?>> chairBlocks = new LinkedList<>();
    private static List<DeferredBlock<ClockBlock>> clockBlocks = new LinkedList<>();
    private static List<DeferredBlock<LargeChandelierBlock>> largeChandelierBlocks = new LinkedList<>();

    public static final DeferredBlock<PlasticChairBlock> PLASTIC_CHAIR = registerWithItem("plastic_chair", () -> new PlasticChairBlock(BlockBehaviour.Properties.of().lightLevel(BlockState -> 1).explosionResistance(3600000.8F)), PlasticChairBlock.Item::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlasticChairBlock.Entity>> PLASTIC_CHAIR_ENTITY = BLOCK_ENTITIES.register("plastic_chair_entity", () -> BlockEntityType.Builder.of(PlasticChairBlock.Entity::new, PLASTIC_CHAIR.get()).build(DSL.remainderType()));

    public static final DeferredBlock<GlassKilnBlock> GLASS_KILN = registerWithItem("glass_kiln", () -> new GlassKilnBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
    public static final Supplier<BlockEntityType<GlassKilnBlock.Entity>> GLASS_KILN_ENTITY = BLOCK_ENTITIES.register("glass_kiln_entity", () -> BlockEntityType.Builder.of(GlassKilnBlock.Entity::new, GLASS_KILN.get()).build(null));
    public static final DeferredBlock<LivingLoomBlock> LIVING_LOOM = registerWithItem("living_loom", () -> new LivingLoomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LOOM)));
    public static final DeferredBlock<IceMachineBlock> ICE_MACHINE = registerWithItem("ice_machine", () -> new IceMachineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRINDSTONE)));

    // Glass
    public static final BlockSetType GLASS = new BlockSetType(
            "glass", true, true, true,
            BlockSetType.PressurePlateSensitivity.MOBS, SoundType.GLASS,
            SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
            SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);

    public static final DeferredBlock<ChairBlock> GLASS_CHAIR = registerChairDiscardItem("glass_chair", Blocks.GLASS, properties -> {});
    public static final DeferredBlock<SofaBlock> GLASS_SOFA = registerSofaDiscardItem("glass_sofa", Blocks.GLASS, properties -> {});
    public static final DeferredBlock<ToiletBlock> GLASS_TOILET = registerToiletDiscardItem("glass_toilet", Blocks.GLASS, properties -> {});
    public static final DeferredBlock<SinkBlock> GLASS_SINK = registerSinkDiscardItem("glass_sink", Blocks.GLASS, properties -> {});

    public static final DeferredBlock<FishBowlBlock> FISH_BOWL = registerWithItem("fish_bowl", () -> new FishBowlBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)), block -> new FishBowlBlock.Item(block, new Item.Properties()));
    public static final DeferredBlock<FishBowlBlock> GOLD_FISH_BOWL = registerWithItem("gold_fish_bowl", () -> new FishBowlBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)), block -> new FishBowlBlock.Item(block, new Item.Properties()));
    public static final DeferredBlock<FishBowlBlock> PUPFISH_BOWL = registerWithItem("pupfish_bowl", () -> new FishBowlBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)), block -> new FishBowlBlock.Item(block, new Item.Properties()));
    public static final DeferredBlock<FishBowlBlock> LAVA_SERPENT_BOWL = registerWithItem("lava_serpent_bowl", () -> new FishBowlBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)), block -> new FishBowlBlock.Item(block, new Item.Properties()));
    public static final DeferredBlock<TrashCanBlock> TRASH_CAN = registerWithItem("trash_can", () -> new TrashCanBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)));
    public static final Supplier<BlockEntityType<TrashCanBlock.Entity>> TRASH_CAN_ENTITY = BLOCK_ENTITIES.register("trash_can_entity", () -> BlockEntityType.Builder.of(TrashCanBlock.Entity::new, TRASH_CAN.get()).build(DSL.remainderType()));

    public static final DeferredBlock<DoorBlock> GLASS_DOOR = registerWithItem("glass_door", () -> new DoorBlock(GLASS, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<TableBlock> GLASS_TABLE = registerWithItem("glass_table", () -> new TableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<TFCandleBlock> GLASS_CANDLE = registerWithItem("glass_candle", () -> new TFCandleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<ChandelierBlock> GLASS_CHANDELIER = registerWithItem("glass_chandelier", () -> new ChandelierBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<TFLanternBlock> GLASS_LANTERN = registerWithItem("glass_lantern", () -> new TFLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<LampBlock> GLASS_LAMP = registerWithItem("glass_lamp", () -> new LampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<CandelabraBlock> GLASS_CANDELABRAS = registerWithItem("glass_candelabras", () -> new CandelabraBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<ClockBlock> GLASS_CLOCK = registerClock("glass_clock", () -> new ClockBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<BathtubBlock> GLASS_BATHTUB = registerWithItem("glass_bathtub", () -> new BathtubBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));

    //地牢
    public static final DeferredBlock<ChairBlock> BLUE_BRICK_CHAIR = registerChairDiscardItem("blue_brick_chair", Blocks.STONE, properties -> {});
    public static final DeferredBlock<SofaBlock> BLUE_BRICK_SOFA = registerSofaDiscardItem("blue_brick_sofa", Blocks.STONE, properties -> {});
    public static final DeferredBlock<ToiletBlock> BLUE_BRICK_TOILET = registerToiletDiscardItem("blue_brick_toilet", Blocks.STONE, properties -> {});
    public static final DeferredBlock<SinkBlock> BLUE_BRICK_SINK = registerSinkDiscardItem("blue_brick_sink", Blocks.STONE, properties -> {});
    public static final DeferredBlock<DoorBlock> BLUE_BRICK_DOOR = registerWithItem("blue_brick_door", () -> new DoorBlock(STONE, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<TableBlock> BLUE_BRICK_TABLE = registerWithItem("blue_brick_table", () -> new TableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<TFCandleBlock> BLUE_BRICK_CANDLE = registerWithItem("blue_brick_candle", () -> new TFCandleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<ChandelierBlock> BLUE_BRICK_CHANDELIER = registerWithItem("blue_brick_chandelier", () -> new ChandelierBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<TFLanternBlock> BLUE_BRICK_LANTERN = registerWithItem("blue_brick_lantern", () -> new TFLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<LampBlock> BLUE_BRICK_LAMP = registerWithItem("blue_brick_lamp", () -> new LampBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<CandelabraBlock> BLUE_BRICK_CANDELABRAS = registerWithItem("blue_brick_candelabras", () -> new CandelabraBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<ClockBlock> BLUE_BRICK_CLOCK = registerClock("blue_brick_clock", () -> new ClockBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<BathtubBlock> BLUE_BRICK_BATHTUB = registerWithItem("blue_brick_bathtub", () -> new BathtubBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

    public static final DeferredBlock<LargeChandelierBlock> BLUE_DUNGEON_CHANDELIER = registerLargeChandelier("blue_dungeon_chandeliers", () -> new LargeChandelierBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollission().lightLevel(litBlockEmission(15))));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeChandelierBlock.BEntity>> LARGE_CHANDELIER_ENTITY = BLOCK_ENTITIES.register("large_chandelier_entity", () -> {
        BlockEntityType<LargeChandelierBlock.BEntity> entityType = BlockEntityType.Builder.of(LargeChandelierBlock.BEntity::new, largeChandelierBlocks.stream().map(DeferredBlock::get).toArray(Block[]::new)).build(DSL.remainderType());
        largeChandelierBlocks = null;
        return entityType;
    });

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChairBlock.Entity>> CHAIR_ENTITY = BLOCK_ENTITIES.register("chair_entity", () -> {
        BlockEntityType<ChairBlock.Entity> entityType = BlockEntityType.Builder.of(ChairBlock.Entity::new, chairBlocks.stream().map(DeferredBlock::get).toArray(Block[]::new)).build(DSL.remainderType());
        chairBlocks = null;
        return entityType;
    });
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ClockBlock.Entity>> CLOCK_ENTITY = BLOCK_ENTITIES.register("clock_entity", () -> {
        BlockEntityType<ClockBlock.Entity> entityType = BlockEntityType.Builder.of(ClockBlock.Entity::new, clockBlocks.stream().map(DeferredBlock::get).toArray(Block[]::new)).build(DSL.remainderType());
        clockBlocks = null;
        return entityType;
    });

    public static <B extends Block> DeferredBlock<B> registerWithItem(String id, Supplier<B> block) {
        return registerWithItem(id, block, new Item.Properties());
    }

    public static <B extends Block> DeferredBlock<B> registerWithItem(String id, Supplier<B> block, Function<B, BlockItem> item) {
        DeferredBlock<B> object = BLOCKS.register(id, block);
        TFItems.BLOCK_ITEMS.register(id, () -> item.apply(object.get()));
        return object;
    }

    public static <B extends Block> DeferredBlock<B> registerWithItem(String id, Supplier<B> block, Item.Properties properties) {
        DeferredBlock<B> object = BLOCKS.register(id, block);
        TFItems.BLOCK_ITEMS.registerSimpleBlockItem(object, properties);
        return object;
    }

    public static <B extends Block> DeferredBlock<B> registerWithoutItem(String id, Supplier<B> block) {
        return BLOCKS.register(id, block);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }

    public static @NotNull DeferredBlock<ChairBlock> registerChair(String id, Block asBlock, @NotNull Consumer<BlockBehaviour.Properties> extraProp) {
        BlockBehaviour.Properties properties1 = BlockBehaviour.Properties.ofFullCopy(asBlock);
        extraProp.accept(properties1);
        return BLOCKS.register(id, () -> new ChairBlock(asBlock.defaultBlockState(), properties1));
    }

    public static @NotNull DeferredBlock<ChairBlock> registerChairDiscardItem(String id, Block asBlock, @NotNull Consumer<BlockBehaviour.Properties> extraProp) {
        DeferredBlock<ChairBlock> block = registerChair(id, asBlock, extraProp);
        chairBlocks.add(block);
        TFItems.BLOCK_ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    public static @NotNull DeferredBlock<SofaBlock> registerSofa(String id, Block asBlock, @NotNull Consumer<BlockBehaviour.Properties> extraProp) {
        BlockBehaviour.Properties properties1 = BlockBehaviour.Properties.ofFullCopy(asBlock);
        extraProp.accept(properties1);
        return BLOCKS.register(id, () -> new SofaBlock(asBlock.defaultBlockState(), properties1));
    }

    public static @NotNull DeferredBlock<SofaBlock> registerSofaDiscardItem(String id, Block asBlock, @NotNull Consumer<BlockBehaviour.Properties> extraProp) {
        DeferredBlock<SofaBlock> block = registerSofa(id, asBlock, extraProp);
        chairBlocks.add(block);
        TFItems.BLOCK_ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    public static @NotNull DeferredBlock<ToiletBlock> registerToilet(String id, Block asBlock, @NotNull Consumer<BlockBehaviour.Properties> extraProp) {
        BlockBehaviour.Properties properties1 = BlockBehaviour.Properties.ofFullCopy(asBlock);
        extraProp.accept(properties1);
        return BLOCKS.register(id, () -> new ToiletBlock(asBlock.defaultBlockState(), properties1));
    }

    public static @NotNull DeferredBlock<ToiletBlock> registerToiletDiscardItem(String id, Block asBlock, @NotNull Consumer<BlockBehaviour.Properties> extraProp) {
        DeferredBlock<ToiletBlock> block = registerToilet(id, asBlock, extraProp);
        chairBlocks.add(block);
        TFItems.BLOCK_ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    public static @NotNull DeferredBlock<SinkBlock> registerSink(String id, Block asBlock, @NotNull Consumer<BlockBehaviour.Properties> extraProp) {
        BlockBehaviour.Properties properties1 = BlockBehaviour.Properties.ofFullCopy(asBlock);
        extraProp.accept(properties1);
        return BLOCKS.register(id, () -> new SinkBlock(asBlock.defaultBlockState(), properties1));
    }

    public static @NotNull DeferredBlock<SinkBlock> registerSinkDiscardItem(String id, Block asBlock, @NotNull Consumer<BlockBehaviour.Properties> extraProp) {
        DeferredBlock<SinkBlock> block = registerSink(id, asBlock, extraProp);
        TFItems.BLOCK_ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    public static DeferredBlock<ClockBlock> registerClock(String id, Supplier<ClockBlock> block) {
        DeferredBlock<ClockBlock> deferredBlock = BLOCKS.register(id, block);
        clockBlocks.add(deferredBlock);
        TFItems.BLOCK_ITEMS.register(id, () -> new ClockBlock.Item(deferredBlock.get(), new Item.Properties()));
        return deferredBlock;
    }

    public static DeferredBlock<LargeChandelierBlock> registerLargeChandelier(String id, Supplier<LargeChandelierBlock> block) {
        DeferredBlock<LargeChandelierBlock> deferredBlock = BLOCKS.register(id, block);
        largeChandelierBlocks.add(deferredBlock);
        TFItems.BLOCK_ITEMS.register(id, () -> new LargeChandelierBlock.Item(deferredBlock.get(), new Item.Properties()));
        return deferredBlock;
    }

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
