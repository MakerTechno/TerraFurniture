package nowebsite.makertechno.terra_furniture.common.init;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.block.*;
import nowebsite.makertechno.terra_furniture.common.block.chair.ChairBlock;
import nowebsite.makertechno.terra_furniture.common.block.chair.PlasticChairBlock;
import nowebsite.makertechno.terra_furniture.common.block.chair.SofaBlock;
import nowebsite.makertechno.terra_furniture.common.block.chair.ToiletBlock;
import nowebsite.makertechno.terra_furniture.common.block.light.*;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public final class TFBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TerraFurniture.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TerraFurniture.MODID);

    public static final DeferredBlock<PlasticChairBlock> PLASTIC_CHAIR = registerWithItem("plastic_chair", () -> new PlasticChairBlock(BlockBehaviour.Properties.of().lightLevel(BlockState -> 1).explosionResistance(3600000.8F)), PlasticChairBlock.Item::new);
    public static final Supplier<BlockEntityType<PlasticChairBlock.Entity>> PLASTIC_CHAIR_ENTITY = BLOCK_ENTITIES.register("plastic_chair_entity", () -> BlockEntityType.Builder.of(PlasticChairBlock.Entity::new, PLASTIC_CHAIR.get()).build(null));

    // Glass
    public static final BlockSetType GLASS = new BlockSetType(
            "glass", true, true, true,
            BlockSetType.PressurePlateSensitivity.MOBS, SoundType.GLASS,
            SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
            SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);
    public static final DeferredBlock<DoorBlock> GLASS_DOOR = registerWithItem("glass_door", () -> new DoorBlock(GLASS, BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS)));
    public static final DeferredBlock<ChairBlock> GLASS_CHAIR = registerWithItem("glass_chair", () -> new ChairBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS)));
    public static final DeferredBlock<ToiletBlock> GLASS_TOILET = registerWithItem("glass_toilet", () -> new ToiletBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS)));
    public static final DeferredBlock<TableBlock> GLASS_TABLE = registerWithItem("glass_table", () -> new TableBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS)));
    public static final DeferredBlock<TFCandleBlock> GLASS_CANDLE = registerWithItem("glass_candle", () -> new TFCandleBlock(ParticleTypes.END_ROD,BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<ChandelierBlock> GLASS_CHANDELIER = registerWithItem("glass_chandelier", () -> new ChandelierBlock(ParticleTypes.SOUL_FIRE_FLAME,BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<TFLanternBlock> GLASS_LANTERN = registerWithItem("glass_lantern", () -> new TFLanternBlock(ParticleTypes.END_ROD,BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<SofaBlock> GLASS_SOFA = registerWithItem("glass_sofa", () -> new SofaBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS)));
    public static final DeferredBlock<LampBlock> GLASS_LAMP = registerWithItem("glass_lamp", () -> new LampBlock(ParticleTypes.END_ROD,BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));
    public static final DeferredBlock<CandelairasBlock> GLASS_CANDELABRAS = registerWithItem("glass_candelabras", () -> new CandelairasBlock(ParticleTypes.END_ROD,BlockBehaviour.Properties.ofLegacyCopy(Blocks.GLASS).lightLevel(litBlockEmission(15))));

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

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return p_50763_ -> p_50763_.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
