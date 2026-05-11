package org.confluence.terra_furniture.common.init;

import com.mojang.datafixers.DSL;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.block.crafting.GlassKilnBlock;
import org.confluence.terra_furniture.common.block.crafting.IceMachineBlock;
import org.confluence.terra_furniture.common.block.crafting.LivingLoomBlock;
import org.confluence.terra_furniture.common.block.func.set.TFBlockSet;
import org.confluence.terra_furniture.common.block.func.set.TFBlockType;
import org.confluence.terra_furniture.common.block.light.LargeChandelierBlock;
import org.confluence.terra_furniture.common.block.misc.*;
import org.confluence.terra_furniture.common.block.sittable.ChairBlock;
import org.confluence.terra_furniture.common.block.sittable.PlasticChairBlock;
import org.confluence.terra_furniture.common.block.sittable.ToiletBlock;
import org.confluence.terra_furniture.common.item.FishBowlItem;
import org.confluence.terra_furniture.common.item.SimpleGeoRenderedItem;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static org.confluence.terra_furniture.common.init.TFBlockSetTypes.*;

@SuppressWarnings("unused")
public final class TFBlocks {
    /* The Registers */
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TerraFurniture.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TerraFurniture.MODID);


    /* Working machines for crafting furniture */
    public static final DeferredBlock<GlassKilnBlock> GLASS_KILN = registerWithItem("glass_kiln", () -> new GlassKilnBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FURNACE)));
    public static final Supplier<BlockEntityType<GlassKilnBlock.Entity>> GLASS_KILN_ENTITY = BLOCK_ENTITIES.register("glass_kiln_entity", () -> BlockEntityType.Builder.of(GlassKilnBlock.Entity::new, GLASS_KILN.get()).build(DSL.remainderType()));
    public static final DeferredBlock<LivingLoomBlock> LIVING_LOOM = registerWithItem("living_loom", () -> new LivingLoomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LOOM)));
    public static final DeferredBlock<IceMachineBlock> ICE_MACHINE = registerWithItem("ice_machine", () -> new IceMachineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRINDSTONE)));

    /* I AM THE STORM THAT IS APPROACHING!! */
    public static final DeferredBlock<PlasticChairBlock> PLASTIC_CHAIR = registerWithItem("plastic_chair", () -> new PlasticChairBlock(property -> property.lightLevel(BlockState -> 1).explosionResistance(3600000.8F)), PlasticChairBlock.Item::new);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlasticChairBlock.PlasticChairBE>> PLASTIC_CHAIR_ENTITY = BLOCK_ENTITIES.register("plastic_chair_entity", () -> BlockEntityType.Builder.of(PlasticChairBlock.PlasticChairBE::new, PLASTIC_CHAIR.get()).build(DSL.remainderType()));

    /* Special furniture, not belongs to any furniture set */
    public static final DeferredBlock<Block> FISH_BOWL = registerWithItem("fish_bowl", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)), block -> new FishBowlItem(block, new Item.Properties()));
    public static final DeferredBlock<Block> GOLD_FISH_BOWL = registerWithItem("gold_fish_bowl", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)), block -> new FishBowlItem(block, new Item.Properties()));
    public static final DeferredBlock<Block> PUPFISH_BOWL = registerWithItem("pupfish_bowl", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)), block -> new FishBowlItem(block, new Item.Properties()));
    public static final DeferredBlock<Block> LAVA_SERPENT_BOWL = registerWithItem("lava_serpent_bowl", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)), block -> new FishBowlItem(block, new Item.Properties()));
    public static final DeferredBlock<TrashCanBlock> TRASH_CAN = registerWithItem("trash_can", () -> new TrashCanBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)));
    public static final Supplier<BlockEntityType<TrashCanBlock.Entity>> TRASH_CAN_ENTITY = BLOCK_ENTITIES.register("trash_can_entity", () -> BlockEntityType.Builder.of(TrashCanBlock.Entity::new, TRASH_CAN.get()).build(DSL.remainderType()));

    /*
        Furniture sets.
        Use Simplified Chinese to label each type.
        It is strongly recommended to sort these in the same order as TFBlockSetTypes.
        TODO: Sort all these sets
    */

    // 玻璃
    public static final TFBlockSet GLASS_SET = new TFBlockSet.Builder(GLASS, Blocks.GLASS, true)
            .disableVanilla()
            .setAvailabilityFor(TFBlockType.BED, false)
            .setAvailabilityFor(TFBlockType.LARGE_CHANDELIER, false)
            .doLightSetup(14, 14, 15, 15, 15)
            .build();

    // 橡木
    public static final TFBlockSet OAK_SET = new TFBlockSet.Builder(OAK, Blocks.OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.CHAIR, true)
            .setSpecialIdFor(TFBlockType.CHAIR, "wooden_chair")
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .setSpecialIdFor(TFBlockType.TABLE, "wooden_table")
            .build();

    // 蓝地牢
    public static final TFBlockSet BLUE_DUNGEON_SET = new TFBlockSet.Builder(BLUE_DUNGEON, Blocks.STONE, true)
            .disableVanilla()
            .setAvailabilityFor(TFBlockType.BED, false)
            .setAvailabilityFor(TFBlockType.CHANDELIER, false) // Only Large Chandelier, this two has the same id
            .doLightSetup(14, 14, 15, 15, 15)
            .build();

    public static final TFBlockSet ACACIA_SET = new TFBlockSet.Builder(ACACIA, Blocks.ACACIA_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .setAvailabilityFor(TFBlockType.CHAIR, true)
            .build();

    public static final TFBlockSet CACTUS_SET = new TFBlockSet.Builder(CACTUS, Blocks.CACTUS, false)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet BAMBOO_SET = new TFBlockSet.Builder(BAMBOO, Blocks.BAMBOO_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet BIRCH_SET = new TFBlockSet.Builder(BIRCH, Blocks.BIRCH_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet CHERRY_SET = new TFBlockSet.Builder(CHERRY, Blocks.CHERRY_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet CRIMSON_SET = new TFBlockSet.Builder(CRIMSON, Blocks.CRIMSON_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet DARK_OAK_SET = new TFBlockSet.Builder(DARK_OAK, Blocks.DARK_OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .setAvailabilityFor(TFBlockType.CHAIR, true)
            .build();
    public static final TFBlockSet GOLD_SET = new TFBlockSet.Builder(GOLD, Blocks.GOLD_BLOCK, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet JUNGLE_SET = new TFBlockSet.Builder(JUNGLE, Blocks.JUNGLE_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .setAvailabilityFor(TFBlockType.CHAIR, true)
            .build();
    public static final TFBlockSet MANGROVE_SET = new TFBlockSet.Builder(MANGROVE, Blocks.MANGROVE_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet OBSIDIAN_SET = new TFBlockSet.Builder(OBSIDIAN, Blocks.OBSIDIAN, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet PINE_SET = new TFBlockSet.Builder(PINE, Blocks.SPRUCE_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet POLISHED_BLACKSTONE_SET = new TFBlockSet.Builder(POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet SPRUCE_SET = new TFBlockSet.Builder(SPRUCE, Blocks.SPRUCE_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .setAvailabilityFor(TFBlockType.CHAIR, true)
            .build();
    public static final TFBlockSet STONE_SET = new TFBlockSet.Builder(STONE, Blocks.STONE, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet WARPED_SET = new TFBlockSet.Builder(WARPED, Blocks.WARPED_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet BONE_SET = new TFBlockSet.Builder(BONE, Blocks.BONE_BLOCK, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet FLINX_FUR_SET = new TFBlockSet.Builder(FLINX_FUR, Blocks.WHITE_WOOL, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.BED, true)
            .build();
    public static final TFBlockSet MARBLE_SET = new TFBlockSet.Builder(MARBLE, Blocks.GRANITE, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet BALLOON_SET = new TFBlockSet.Builder(BALLOON, Blocks.DIRT, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet ASH_WOOD_SET = new TFBlockSet.Builder(ASH_WOOD, Blocks.OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet BAOBAB_SET = new TFBlockSet.Builder(BAOBAB, Blocks.OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet DYNASTY_SET = new TFBlockSet.Builder(DYNASTY, Blocks.OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet EBONWOOD_SET = new TFBlockSet.Builder(EBONWOOD, Blocks.OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet FEYWOOD_SET = new TFBlockSet.Builder(FEYWOOD, Blocks.OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet MUSHROOM_SET = new TFBlockSet.Builder(MUSHROOM, Blocks.OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet PEARLWOOD_SET = new TFBlockSet.Builder(PEARLWOOD, Blocks.OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet SHADEWOOD_SET = new TFBlockSet.Builder(SHADEWOOD, Blocks.OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet SKYWARE_SET = new TFBlockSet.Builder(SKYWARE, Blocks.STONE, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet CLOUD_SET = new TFBlockSet.Builder(CLOUD, Blocks.WHITE_WOOL, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.BED, true)
            .build();
    public static final TFBlockSet COPPER_SET = new TFBlockSet.Builder(COPPER, Blocks.COPPER_BLOCK, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet IRON_SET = new TFBlockSet.Builder(IRON, Blocks.IRON_BLOCK, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet DUSKWARE_SET = new TFBlockSet.Builder(DUSKWARE, Blocks.STONE, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();
    public static final TFBlockSet SPOOKY_SET = new TFBlockSet.Builder(SPOOKY, Blocks.OAK_PLANKS, true)
            .disableAll()
            .setAvailabilityFor(TFBlockType.TABLE, true)
            .build();

    public static final DeferredBlock<PinWheel> PIN_WHEEL = registerWithoutItem("pin_wheel", () -> new PinWheel(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO).noCollission()));
    public static final DeferredItem<SimpleGeoRenderedItem> PIN_WHEEL_ITEM = TFItems.BLOCK_ITEMS.register("pin_wheel", () -> new SimpleGeoRenderedItem(PIN_WHEEL.get(), new Item.Properties(), false));

    public static final DeferredBlock<HangingPotBlock> HANGING_POT = registerWithoutItem("hanging_pot", () -> new HangingPotBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FLOWER_POT).noCollission().lightLevel(litBlockEmission(15))));
    public static final DeferredItem<HangingPotBlock.BItem> HANGING_POT_ITEM = TFItems.BLOCK_ITEMS.register("hanging_pot", () -> new HangingPotBlock.BItem(HANGING_POT.get(), new Item.Properties()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HangingPotBlock.BEntity>> HANGING_POT_ENTITY = BLOCK_ENTITIES.register(
            "hanging_pot_entity",
            () -> BlockEntityType.Builder.of(HangingPotBlock.BEntity::new, HANGING_POT.get()).build(DSL.remainderType())
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PinWheel.BEntity>> PIN_WHEEL_ENTITY = BLOCK_ENTITIES.register(
            "pin_wheel_entity",
            () -> BlockEntityType.Builder.of(PinWheel.BEntity::new, PIN_WHEEL.get()).build(DSL.remainderType())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LargeChandelierBlock.BEntity>> LARGE_CHANDELIER_ENTITY = BLOCK_ENTITIES.register(
            "large_chandelier_entity",
            () -> BlockEntityType.Builder.of(
                    LargeChandelierBlock.BEntity::new,
                    TFBlockType.LARGE_CHANDELIER.getAll().stream().map(DeferredBlock::get).toArray(Block[]::new)
            ).build(DSL.remainderType())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChairBlock.ChairBE>> CHAIR_ENTITY = BLOCK_ENTITIES.register(
            "chair_entity",
            () -> BlockEntityType.Builder.of(
                    ChairBlock.ChairBE::new,
                    Stream.concat(TFBlockType.CHAIR.getAll().stream(), TFBlockType.SOFA.getAll().stream()).map(DeferredBlock::get).toArray(Block[]::new)
            ).build(DSL.remainderType())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ToiletBlock.ToiletBE>> TOILET_ENTITY = BLOCK_ENTITIES.register(
            "toilet_entity",
            () -> BlockEntityType.Builder.of(
                    ToiletBlock.ToiletBE::new,
                    TFBlockType.TOILET.getAll().stream().map(DeferredBlock::get).toArray(Block[]::new)
            ).build(DSL.remainderType())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ClockBlock.Entity>> CLOCK_ENTITY = BLOCK_ENTITIES.register(
            "clock_entity",
            () -> BlockEntityType.Builder.of(
                    ClockBlock.Entity::new,
                    TFBlockType.CLOCK.getAll().stream().map(DeferredBlock::get).toArray(Block[]::new)
            ).build(DSL.remainderType())
    );



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

    public static DeferredBlock<LargeChandelierBlock> registerLargeChandelier(String id, Supplier<LargeChandelierBlock> block) {
        DeferredBlock<LargeChandelierBlock> deferredBlock = BLOCKS.register(id, block);
        TFItems.BLOCK_ITEMS.register(id, () -> new LargeChandelierBlock.BItem(deferredBlock.get(), new Item.Properties()));
        return deferredBlock;
    }


    public static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return blockState -> blockState.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}