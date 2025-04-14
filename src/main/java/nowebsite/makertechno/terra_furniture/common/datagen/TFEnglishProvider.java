package nowebsite.makertechno.terra_furniture.common.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.init.TFBlocks;
import org.confluence.lib.util.LibUtils;

public class TFEnglishProvider extends LanguageProvider {
    public TFEnglishProvider(PackOutput output) {
        super(output, TerraFurniture.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("creativetab.terra_furniture", "Terra Furniture");

        add("msg.terra_furniture.sit", "Something wrong happened, it's the reason why you can't sit on this block.");

        add("container.terra_furniture.glass_kiln", "Glass Kiln");
        add("container.terra_furniture.living_loom", "Living Loom");

        add("title.terra_furniture.glass_kiln", "Glass Kiln");
        add("title.terra_furniture.living_loom", "Living Loom");

        TFBlocks.BLOCKS.getEntries().forEach(block -> add(block.get(), LibUtils.toTitleCase(block.getId().getPath())));
    }
}
