package org.confluence.terra_furniture.common.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.init.TFBlocks;

public class TFChineseProvider extends LanguageProvider {
    public TFChineseProvider(PackOutput output) {
        super(output, TerraFurniture.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("creativetab.terra_furniture", "泰拉家具");

        add("msg.terra_furniture.sit", "出现某些错误导致乘坐方块失败。");

        add("container.terra_furniture.glass_kiln", "玻璃窑");
        add("container.terra_furniture.living_loom", "生命木织机");
        add("container.terra_furniture.ice_machine", "冰雪机");
        add("container.terra_furniture.trash_can", "垃圾桶");

        add("title.terra_furniture.glass_kiln", "玻璃窑");
        add("title.terra_furniture.living_loom", "生命木织机");
        add("title.terra_furniture.ice_machine", "冰雪机");

        add("info.terra_furniture.time", "时间: [%s:%s]");

        add(TFBlocks.GLASS_KILN.get(), "玻璃窑");
        add(TFBlocks.LIVING_LOOM.get(), "生命木织机");
        add(TFBlocks.ICE_MACHINE.get(), "冰雪机");

        add(TFBlocks.FISH_BOWL.get(), "鱼缸");
        add(TFBlocks.GOLD_FISH_BOWL.get(), "金鱼缸");
        add(TFBlocks.PUPFISH_BOWL.get(), "鳉鱼缸");
        add(TFBlocks.LAVA_SERPENT_BOWL.get(), "熔岩蛇缸");
        add(TFBlocks.TRASH_CAN.get(), "垃圾桶");
        add(TFBlocks.HANGING_POT.get(), "吊挂盆");

        add(TFBlocks.PLASTIC_CHAIR.get(), "塑料椅");
        add(TFBlocks.GLASS_SET.DOOR.get(), "玻璃门");
        add(TFBlocks.GLASS_SET.CHAIR.get(), "玻璃椅");
        add(TFBlocks.GLASS_SET.TOILET.get(), "玻璃马桶");
        add(TFBlocks.GLASS_SET.TABLE.get(), "玻璃桌");
        add(TFBlocks.GLASS_SET.CANDLE.get(), "玻璃蜡烛");
        add(TFBlocks.GLASS_SET.LANTERN.get(), "玻璃灯笼");
        add(TFBlocks.GLASS_SET.SOFA.get(), "玻璃沙发");
        add(TFBlocks.GLASS_SET.LAMP.get(), "玻璃灯");
        add(TFBlocks.GLASS_SET.CANDELABRAS.get(), "玻璃烛台");
        add(TFBlocks.GLASS_SET.SINK.get(), "玻璃水槽");
        add(TFBlocks.GLASS_SET.CLOCK.get(), "玻璃时钟");
        add(TFBlocks.GLASS_SET.BATHTUB.get(), "玻璃浴缸");

        add(TFBlocks.BLUE_DUNGEON_SET.DOOR.get(), "蓝地牢门");
        add(TFBlocks.BLUE_DUNGEON_SET.CHAIR.get(), "蓝地牢椅");
        add(TFBlocks.BLUE_DUNGEON_SET.TOILET.get(), "蓝地牢马桶");
        add(TFBlocks.BLUE_DUNGEON_SET.TABLE.get(), "蓝地牢桌");
        add(TFBlocks.BLUE_DUNGEON_SET.CANDLE.get(), "蓝地牢蜡烛");
        add(TFBlocks.BLUE_DUNGEON_SET.LANTERN.get(), "蓝地牢灯笼");
        add(TFBlocks.BLUE_DUNGEON_SET.SOFA.get(), "蓝地牢沙发");
        add(TFBlocks.BLUE_DUNGEON_SET.LAMP.get(), "蓝地牢灯");
        add(TFBlocks.BLUE_DUNGEON_SET.CANDELABRAS.get(), "蓝地牢烛台");
        add(TFBlocks.BLUE_DUNGEON_SET.SINK.get(), "蓝地牢水槽");
        add(TFBlocks.BLUE_DUNGEON_SET.CLOCK.get(), "蓝地牢时钟");
        add(TFBlocks.BLUE_DUNGEON_SET.BATHTUB.get(), "蓝地牢浴缸");

        add(TFBlocks.ACACIA_SET.TABLE.get(), "金合欢木桌");
        add(TFBlocks.BAMBOO_SET.TABLE.get(), "竹桌");
        add(TFBlocks.BIRCH_SET.TABLE.get(), "白桦木桌");
        add(TFBlocks.CHERRY_SET.TABLE.get(), "樱花木桌");
        add(TFBlocks.CRIMSON_SET.TABLE.get(), "绯红木桌");
        add(TFBlocks.DARK_OAK_SET.TABLE.get(), "深色橡木桌");
        add(TFBlocks.GOLD_SET.TABLE.get(), "金桌");
        add(TFBlocks.JUNGLE_SET.TABLE.get(), "丛林木桌");
        add(TFBlocks.MANGROVE_SET.TABLE.get(), "红木桌");
        add(TFBlocks.OBSIDIAN_SET.TABLE.get(), "黑曜石桌");
        add(TFBlocks.PINE_SET.TABLE.get(), "松木桌");
        add(TFBlocks.ASH_WOOD_SET.TABLE.get(), "灰烬木桌");
        add(TFBlocks.BAOBAB_SET.TABLE.get(), "猴面包木桌");
        add(TFBlocks.DYNASTY_SET.TABLE.get(), "王朝木桌");
        add(TFBlocks.EBONWOOD_SET.TABLE.get(), "乌木桌");
        add(TFBlocks.FEYWOOD_SET.TABLE.get(), "仙灵木桌");
        add(TFBlocks.MUSHROOM_SET.TABLE.get(), "蘑菇桌");
        add(TFBlocks.PEARLWOOD_SET.TABLE.get(), "珍珠木桌");
        add(TFBlocks.SHADEWOOD_SET.TABLE.get(), "暗影木桌");
        add(TFBlocks.SKYWARE_SET.TABLE.get(), "天域桌");
        add(TFBlocks.DUSKWARE_SET.TABLE.get(), "暮色桌");
        add(TFBlocks.SPOOKY_SET.TABLE.get(), "阴森木桌");
        add(TFBlocks.POLISHED_BLACKSTONE_SET.TABLE.get(), "磨制黑石桌");
        add(TFBlocks.SPRUCE_SET.TABLE.get(), "云杉木桌");
        add(TFBlocks.STONE_SET.TABLE.get(), "石桌");
        add(TFBlocks.WARPED_SET.TABLE.get(), "诡异木桌");
        add(TFBlocks.MARBLE_SET.TABLE.get(), "大理石桌");
        add(TFBlocks.BALLOON_SET.TABLE.get(), "气球桌");
        add(TFBlocks.FLINX_FUR_SET.BED.get(), "小雪怪皮毛床");
        
        add(TFBlocks.CLOUD_SET.BED.get(), "云床");

        add(TFBlocks.COPPER_SET.TABLE.get(), "铜桌");

        add(TFBlocks.IRON_SET.TABLE.get(), "铁桌");

        add(TFBlocks.OAK_SET.CHAIR.get(), "木椅");
        add(TFBlocks.OAK_SET.TABLE.get(), "木桌");
        add(TFBlocks.OAK_SET.BED.get(), "木床");
        add(TFBlocks.OAK_SET.CANDLE.get(), "木烛台");

        add(TFBlocks.PIN_WHEEL.get(), "风车");

        add(TFBlocks.BLUE_DUNGEON_SET.LARGE_CHANDELIER.get(), "蓝地牢大型吊灯");

        // 椅子
        add(TFBlocks.ACACIA_SET.CHAIR.get(), "金合欢椅子");
        add(TFBlocks.SPRUCE_SET.CHAIR.get(), "云杉木椅子");
        add(TFBlocks.DARK_OAK_SET.CHAIR.get(), "深色橡木椅子");
        add(TFBlocks.JUNGLE_SET.CHAIR.get(), "丛林木椅子");

    }
}
