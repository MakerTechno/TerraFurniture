package org.confluence.terra_furniture.common.block.func.set;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.registries.DeferredBlock;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TFBlockType<T extends Block> {

    private final String name;
    private final Set<DeferredBlock<T>> registered = new ObjectArraySet<>();

    private static final Map<String, TFBlockType<?>> REGISTRY = new ConcurrentHashMap<>();

    private TFBlockType(String name) {
        this.name = name;
    }

    public static <T extends Block> TFBlockType<T> create(String id) {
        TFBlockType<T> type = new TFBlockType<>(id);
        REGISTRY.put(id, type);
        return type;
    }

    public String name() {
        return name;
    }

    public void register(DeferredBlock<T> block) {
        registered.add(block);
    }

    public Set<DeferredBlock<T>> getAll() {
        return Collections.unmodifiableSet(registered);
    }

    @Contract(pure = true)
    public static @UnmodifiableView Map<String, TFBlockType<?>> registry() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static final TFBlockType<ButtonBlock> BUTTON = create("button");
    public static final TFBlockType<PressurePlateBlock> PRESSURE_PLATE = create("pressure_plate");
    public static final TFBlockType<SlabBlock> SLAB = create("slab");
    public static final TFBlockType<StairBlock> STAIRS = create("stairs");
    public static final TFBlockType<TrapDoorBlock> TRAPDOOR = create("trapdoor");
    public static final TFBlockType<TFDoorBlock> DOOR = create("door");
    public static final TFBlockType<TableBlock> TABLE = create("table");
    public static final TFBlockType<ChairBlock> CHAIR = create("chair");
    public static final TFBlockType<SofaBlock> SOFA = create("sofa");
    public static final TFBlockType<ToiletBlock> TOILET = create("toilet");
    public static final TFBlockType<TFBedBlock> BED = create("bed");
    public static final TFBlockType<BathtubBlock> BATHTUB = create("bathtub");
    public static final TFBlockType<SinkBlock> SINK = create("sink");
    public static final TFBlockType<ClockBlock> CLOCK = create("clock");
    public static final TFBlockType<LargeChandelierBlock> LARGE_CHANDELIER = create("chandelier");
    public static final TFBlockType<SwitchableLightBlock> CANDLE = create("candle");
    public static final TFBlockType<SwitchableLightBlock> LANTERN = create("lantern");
    public static final TFBlockType<SwitchableLightBlock> LAMP = create("lamp");
    public static final TFBlockType<SwitchableLightBlock> CHANDELIER = create("chandelier");
    public static final TFBlockType<CandelabraBlock> CANDELABRAS = create("candelabras");
}
