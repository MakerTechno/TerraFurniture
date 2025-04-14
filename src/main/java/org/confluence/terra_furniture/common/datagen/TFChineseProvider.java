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

        add("title.terra_furniture.glass_kiln", "玻璃窑");
        add("title.terra_furniture.living_loom", "生命木织机");

        add(TFBlocks.GLASS_KILN.get(), "玻璃窑");
        add(TFBlocks.LIVING_LOOM.get(), "生命木织机");

        add(TFBlocks.PLASTIC_CHAIR.get(), "塑料椅");
        add(TFBlocks.GLASS_DOOR.get(), "玻璃门");
        add(TFBlocks.GLASS_CHAIR.get(), "玻璃椅");
        add(TFBlocks.GLASS_TOILET.get(), "玻璃马桶");
        add(TFBlocks.GLASS_TABLE.get(), "玻璃桌");
        add(TFBlocks.GLASS_CANDLE.get(), "玻璃蜡烛");
        add(TFBlocks.GLASS_CHANDELIER.get(), "玻璃吊灯");
        add(TFBlocks.GLASS_LANTERN.get(), "玻璃灯笼");
        add(TFBlocks.GLASS_SOFA.get(), "玻璃沙发");
        add(TFBlocks.GLASS_LAMP.get(), "玻璃灯");
        add(TFBlocks.GLASS_CANDELABRAS.get(), "玻璃烛台");
        add(TFBlocks.GLASS_SINK.get(), "玻璃水槽");
    }
}
