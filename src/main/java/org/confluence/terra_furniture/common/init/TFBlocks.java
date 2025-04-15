package org.confluence.terra_furniture.common.init;

import com.mojang.datafixers.DSL;
import net.minecraft.core.particles.ParticleTypes;
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
import org.confluence.terra_furniture.common.block.BathtubBlock;
import org.confluence.terra_furniture.common.block.GrandfatherClockBlock;
import org.confluence.terra_furniture.common.block.SinkBlock;
import org.confluence.terra_furniture.common.block.TableBlock;
import org.confluence.terra_furniture.common.block.crafting.GlassKilnBlock;
import org.confluence.terra_furniture.common.block.crafting.IceMachineBlock;
import org.confluence.terra_furniture.common.block.crafting.LivingLoomBlock;
import org.confluence.terra_furniture.common.block.light.*;
import org.confluence.terra_furniture.common.block.sittable.ChairBlock;
import org.confluence.terra_furniture.common.block.sittable.PlasticChairBlock;
import org.confluence.terra_furniture.common.block.sittable.SofaBlock;
import org.confluence.terra_furniture.common.block.sittable.ToiletBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public final class TFBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TerraFurniture.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TerraFurniture.MODID);

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

    public static final DeferredBlock<DoorBlock> GLASS_DOOR = registerWithItem("glass_door", () -> new DoorBlock(GLASS, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<TableBlock> GLASS_TABLE = registerWithItem("glass_table", () -> new TableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<TFCandleBlock> GLASS_CANDLE = registerWithItem("glass_candle", () -> new TFCandleBlock(ParticleTypes.END_ROD, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<ChandelierBlock> GLASS_CHANDELIER = registerWithItem("glass_chandelier", () -> new ChandelierBlock(ParticleTypes.SOUL_FIRE_FLAME, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<TFLanternBlock> GLASS_LANTERN = registerWithItem("glass_lantern", () -> new TFLanternBlock(ParticleTypes.END_ROD, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<LampBlock> GLASS_LAMP = registerWithItem("glass_lamp", () -> new LampBlock(ParticleTypes.END_ROD, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<CandelabraBlock> GLASS_CANDELABRAS = registerWithItem("glass_candelabras", () -> new CandelabraBlock(ParticleTypes.END_ROD, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<GrandfatherClockBlock> GLASS_CLOCK = registerWithItem("glass_clock", () -> new GrandfatherClockBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final DeferredBlock<BathtubBlock> GLASS_BATHTUB = registerWithItem("glass_bathtub", () -> new BathtubBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChairBlock.Entity>> CHAIR_ENTITY = BLOCK_ENTITIES.register("chair_entity", () -> BlockEntityType.Builder.of(ChairBlock.Entity::new,
            GLASS_CHAIR.get(), GLASS_SOFA.get(), GLASS_TOILET.get()
    ).build(DSL.remainderType()));

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

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return p_50763_ -> p_50763_.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
