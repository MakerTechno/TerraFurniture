package org.confluence.terra_furniture.common.block.func.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TFStateProperties {
    public static final EnumProperty<HorizontalSixPart> HORIZONTAL_SIX_PART = EnumProperty.create("horizontal_six_part", HorizontalSixPart.class);
    public static final EnumProperty<HorizontalTwelvePart> HORIZONTAL_TWELVE_PART = EnumProperty.create("horizontal_twelve_part", HorizontalTwelvePart.class);

    public enum HorizontalSixPart implements StringRepresentable {
        LEFT_FRONT("down_left_front"),
        LEFT_CENTER("down_left_center"),// base
        LEFT_BACK("down_left_back"),
        RIGHT_FRONT("down_right_front"),
        RIGHT_CENTER("down_right_center"),
        RIGHT_BACK("down_right_back"),;
        private final String name;
        HorizontalSixPart(String name) {
            this.name = name;
        }
        @Contract(pure = true)
        @Override
        public @NotNull String getSerializedName() {
            return name;
        }

        public BlockPos toBase(BlockPos now, Direction facing, boolean reverseRelative) {
            if (reverseRelative) facing = facing.getOpposite();
            return switch (this){
                case LEFT_FRONT -> now.relative(facing.getOpposite());
                case LEFT_CENTER -> now;
                case LEFT_BACK -> now.relative(facing);
                case RIGHT_FRONT -> now.relative(facing.getCounterClockWise()).relative(facing.getOpposite());
                case RIGHT_CENTER -> now.relative(facing.getCounterClockWise());
                case RIGHT_BACK -> now.relative(facing.getCounterClockWise()).relative(facing);
            };
        }

        public static @NotNull Map<HorizontalSixPart, BlockPos> getAllExcept(Direction facing, BlockPos center, @Nullable HorizontalSixPart ign) {
            Map<HorizontalSixPart, BlockPos> allEx = new HashMap<>();
            for (HorizontalSixPart part : HorizontalSixPart.values()) {
                if (part == ign) continue;
                allEx.put(part, part.toBase(center, facing, true));
            }
            return allEx;
        }

        public boolean isCenter() {
            return this.equals(LEFT_CENTER);
        }

        public static @Nullable HorizontalSixPart infer(BlockPos center, BlockPos current, Direction facing) {
            for (Map.Entry<HorizontalSixPart, BlockPos> entry : getAllExcept(facing, center ,null).entrySet()) {
                if (entry.getValue().equals(current)) return entry.getKey();
            }
            return null;
        }
    }


    public enum HorizontalTwelvePart implements StringRepresentable {
        UP_LEFT_FRONT("up_left_front"),
        UP_LEFT_CENTER("up_left_center"),
        UP_LEFT_BACK("up_left_back"),
        UP_RIGHT_FRONT("up_right_front"),
        UP_RIGHT_CENTER("up_right_center"),
        UP_RIGHT_BACK("up_right_back"),
        DOWN_LEFT_FRONT("down_left_front"),
        DOWN_LEFT_CENTER("down_left_center"),
        DOWN_LEFT_BACK("down_left_back"),
        DOWN_RIGHT_FRONT("down_right_front"),
        DOWN_RIGHT_CENTER("down_right_center"),
        DOWN_RIGHT_BACK("down_right_back"),;
        private final String name;
        HorizontalTwelvePart(String name) {
            this.name = name;
        }
        @Contract(pure = true)
        @Override
        public @NotNull String getSerializedName() {
            return name;
        }

        public BlockPos toBase(BlockPos now, Direction facing, boolean reverseRelative) {
            if (reverseRelative) facing = facing.getOpposite();
            int verticalReverse = reverseRelative? -1 : 1;
            return switch (this){
                case UP_LEFT_FRONT -> now.relative(facing.getOpposite()).above(verticalReverse);
                case UP_LEFT_CENTER -> now.above(verticalReverse);
                case UP_LEFT_BACK -> now.relative(facing).above(verticalReverse);
                case UP_RIGHT_FRONT -> now.relative(facing.getCounterClockWise()).relative(facing.getOpposite()).above(verticalReverse);
                case UP_RIGHT_CENTER -> now.relative(facing.getCounterClockWise()).above(verticalReverse);
                case UP_RIGHT_BACK -> now.relative(facing.getCounterClockWise()).relative(facing).above(verticalReverse);
                case DOWN_LEFT_FRONT -> now.relative(facing.getOpposite());
                case DOWN_LEFT_CENTER -> now;
                case DOWN_LEFT_BACK -> now.relative(facing);
                case DOWN_RIGHT_FRONT -> now.relative(facing.getCounterClockWise()).relative(facing.getOpposite());
                case DOWN_RIGHT_CENTER -> now.relative(facing.getCounterClockWise());
                case DOWN_RIGHT_BACK -> now.relative(facing.getCounterClockWise()).relative(facing);
            };
        }

        public static @NotNull Map<HorizontalTwelvePart, BlockPos> getAllExcept(Direction facing, BlockPos center, @Nullable HorizontalTwelvePart ign) {
            Map<HorizontalTwelvePart, BlockPos> allEx = new HashMap<>();
            for (HorizontalTwelvePart part : HorizontalTwelvePart.values()) {
                if (part == ign) continue;
                allEx.put(part, part.toBase(center, facing, true));
            }
            return allEx;
        }

        public boolean isCenter() {
            return this.equals(DOWN_LEFT_CENTER);
        }

        public static @Nullable HorizontalTwelvePart infer(BlockPos center, BlockPos current, Direction facing) {
            for (Map.Entry<HorizontalTwelvePart, BlockPos> entry : getAllExcept(facing, center ,null).entrySet()) {
                if (entry.getValue().equals(current)) return entry.getKey();
            }
            return null;
        }
    }
}
