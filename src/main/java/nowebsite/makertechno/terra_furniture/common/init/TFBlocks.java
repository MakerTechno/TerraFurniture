package nowebsite.makertechno.terra_furniture.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.block.chair.PlasticChairBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public final class TFBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(TerraFurniture.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TerraFurniture.MODID);

    public static final DeferredBlock<PlasticChairBlock> PLASTIC_CHAIR = registerWithItem("plastic_chair", () -> new PlasticChairBlock(BlockBehaviour.Properties.of().lightLevel(BlockState -> 1).explosionResistance(3600000.8F)), PlasticChairBlock.Item::new);
    public static final Supplier<BlockEntityType<PlasticChairBlock.Entity>> PLASTIC_CHAIR_ENTITY = BLOCK_ENTITIES.register("plastic_chair_entity", () -> BlockEntityType.Builder.of(PlasticChairBlock.Entity::new, PLASTIC_CHAIR.get()).build(null));

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
}
