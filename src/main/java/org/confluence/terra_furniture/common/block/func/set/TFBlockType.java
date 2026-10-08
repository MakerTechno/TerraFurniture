package org.confluence.terra_furniture.common.block.func.set;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TFBlockType<T extends Block> {

    private final String name;
    private final Set<DeferredBlock<T>> registered = new LinkedHashSet<>();

    private static final Map<String, TFBlockType<?>> REGISTRY = new ConcurrentHashMap<>();

    private TFBlockType(String name) {
        this.name = name;
    }

    public static <T extends Block> TFBlockType<T> create(String id) {
        return create(id, id);
    }

    private static <T extends Block> TFBlockType<T> create(String id, String name) {
        TFBlockType<T> type = new TFBlockType<>(Objects.requireNonNull(name, "Furniture type name"));
        if (REGISTRY.putIfAbsent(id, type) != null) {
            throw new IllegalArgumentException("Duplicate furniture type: " + id);
        }
        return type;
    }

    public String name() {
        return name;
    }

    public synchronized void register(DeferredBlock<T> block) {
        registered.add(Objects.requireNonNull(block, "Registered block"));
    }

    public synchronized Set<DeferredBlock<T>> getAll() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(registered));
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
    public static final TFBlockType<LargeChandelierBlock> LARGE_CHANDELIER = create("large_chandelier", "chandelier");
    public static final TFBlockType<SwitchableLightBlock> CANDLE = create("candle");
    public static final TFBlockType<SwitchableLightBlock> LANTERN = create("lantern");
    public static final TFBlockType<SwitchableLightBlock> LAMP = create("lamp");
    public static final TFBlockType<SwitchableLightBlock> CHANDELIER = create("chandelier");
    public static final TFBlockType<CandelabraBlock> CANDELABRAS = create("candelabras");

    /* 可选类型，除非套装显式启用，否则始终不可用 */
    public static final TFBlockType<TFChestBlock> CHEST = create("chest");
    public static final TFBlockType<OneLegTableBlock> ONE_LEG_TABLE = create("one_leg_table");
    public static final TFBlockType<SwitchableLightBlock> CANDLESTICK = create("candlestick");
}
