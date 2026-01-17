package org.confluence.terra_furniture.common.init;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

/**
 * 仅静态存储。
 */
@SuppressWarnings("unused")
public class TFBlockSetTypes {

    /*
     * 已在原版定义的, 可以直接使用该名称(无需写BlockSetType.XXX, 直接写XXX可以识别):
     * OAK 橡木(就是普通木头)
     * SPRUCE 云杉木(可能是针叶木, 如果单做了再说的)
     * BIRCH 白桦木
     * ACACIA 金合欢木
     * CHERRY 樱花木
     * JUNGLE 丛林木
     * DARK_OAK 深色橡木
     * CRIMSON 绯红木
     * WARPED 诡异木
     * MANGROVE 红树木
     * BAMBOO 竹木(就当竹子用的)
     *
     * IRON 铁
     * COPPER 铜
     * GOLD 金(泰拉中无法制作，只能通过海盗事件获得的系列)
     * STONE 石头(注意不是圆石)
     * POLISHED_BLACKSTONE 磨制黑石
     */



    /* 下面是不可通过制作获得的材质 */
    // 黑曜石(废墟建筑获得家具)
    public static final BlockSetType OBSIDIAN = BlockSetType.register(new BlockSetType("obsidian"));
    // 蓝地牢砖()
    public static final BlockSetType BLUE_DUNGEON = BlockSetType.register(new BlockSetType(
            "blue_dungeon", true, true, true,
            BlockSetType.PressurePlateSensitivity.MOBS, SoundType.BONE_BLOCK,
            SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
            SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON
    ));
    // 绿地牢砖
    public static final BlockSetType GREEN_DUNGEON = BlockSetType.register(new BlockSetType(
            "green_dungeon", true, true, true,
            BlockSetType.PressurePlateSensitivity.MOBS, SoundType.BONE_BLOCK,
            SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
            SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON
    ));
    // 粉地牢砖
    public static final BlockSetType PINK_DUNGEON = BlockSetType.register(new BlockSetType(
            "pink_dungeon", true, true, true,
            BlockSetType.PressurePlateSensitivity.MOBS, SoundType.BONE_BLOCK,
            SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
            SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON
    ));
    // 哥特
    public static final BlockSetType GOTHIC = BlockSetType.register(new BlockSetType(
            "gothic", true, true, true,
            BlockSetType.PressurePlateSensitivity.MOBS, SoundType.BONE_BLOCK,
            SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
            SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON
    ));



    /* 下面是可以通过制作获得的材质 */
    // 骨头
    public static final BlockSetType BONE = BlockSetType.register(new BlockSetType(
            "bone", true, true, true,
            BlockSetType.PressurePlateSensitivity.MOBS, SoundType.BONE_BLOCK,
            SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
            SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON
    ));
    // 病变
    public static final BlockSetType LESION = BlockSetType.register(new BlockSetType("lesion"));
    // 血肉
    public static final BlockSetType FLESH = BlockSetType.register(new BlockSetType("flesh"));
    // 玻璃
    public static final BlockSetType GLASS = BlockSetType.register(new BlockSetType(
            "glass", true, true, true,
            BlockSetType.PressurePlateSensitivity.MOBS, SoundType.GLASS,
            SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
            SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON
    ));
    // 蜂蜜
    public static final BlockSetType HONEY = BlockSetType.register(new BlockSetType("honey"));
    // 冰冻(冰雪)
    public static final BlockSetType FROZEN = BlockSetType.register(new BlockSetType(
            "frozen", true, true, true,
            BlockSetType.PressurePlateSensitivity.MOBS, SoundType.GLASS,
            SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN,
            SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN,
            SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON
    ));
    // 丛林蜥蜴砖(官方维基就这么拼的)
    public static final BlockSetType LIHZAHRD = BlockSetType.register(new BlockSetType("lihzahrd"));
    // 生命木
    public static final BlockSetType LIVING_WOOD = BlockSetType.register(new BlockSetType("living_wood"));
    // 天域(日盘块)
    public static final BlockSetType SKYWARE = BlockSetType.register(new BlockSetType("skyware"));
    // 史莱姆
    public static final BlockSetType SLIME = BlockSetType.register(new BlockSetType("slime"));
    // 蒸汽朋克(齿轮)
    public static final BlockSetType STEAMPUNK = BlockSetType.register(new BlockSetType("steampunk"));
    // 灰烬木
    public static final BlockSetType ASH_WOOD = BlockSetType.register(new BlockSetType("ash_wood"));
    // 气球
    public static final BlockSetType BALLOON = BlockSetType.register(new BlockSetType("balloon"));
    // 仙人掌
    public static final BlockSetType CACTUS = BlockSetType.register(new BlockSetType("cactus"));
    // 水晶(水晶块)
    public static final BlockSetType CRYSTAL = BlockSetType.register(new BlockSetType("crystal"));
    // 王朝木
    public static final BlockSetType DYNASTY = BlockSetType.register(new BlockSetType("dynasty"));
    // 乌木
    public static final BlockSetType EBONWOOD = BlockSetType.register(new BlockSetType("ebonwood"));
    // 花岗岩
    public static final BlockSetType GRANITE = BlockSetType.register(new BlockSetType("granite"));
    // 大理石
    public static final BlockSetType MARBLE = BlockSetType.register(new BlockSetType("marble"));
    // 火星(火星管道护板)
    public static final BlockSetType MARTIAN = BlockSetType.register(new BlockSetType("martian"));
    // 陨石
    public static final BlockSetType METEORITE = BlockSetType.register(new BlockSetType("meteorite"));
    // 蘑菇
    public static final BlockSetType MUSHROOM = BlockSetType.register(new BlockSetType("mushroom"));
    // 棕榈木
    public static final BlockSetType PALM_WOOD = BlockSetType.register(new BlockSetType("palm_wood"));
    // 珍珠木
    public static final BlockSetType PEARLWOOD = BlockSetType.register(new BlockSetType("pearlwood"));
    // 松木
    public static final BlockSetType PINE = BlockSetType.register(new BlockSetType("pine"));
    // 南瓜
    public static final BlockSetType PUMPKIN = BlockSetType.register(new BlockSetType("pumpkin"));
    // 珊瑚礁
    public static final BlockSetType REEF = BlockSetType.register(new BlockSetType("reef"));
    // 砂岩
    public static final BlockSetType SANDSTONE = BlockSetType.register(new BlockSetType("sandstone"));
    // 暗影木
    public static final BlockSetType SHADEWOOD = BlockSetType.register(new BlockSetType("shadewood"));
    // 蜘蛛(蜘蛛窝块)
    public static final BlockSetType SPIDER = BlockSetType.register(new BlockSetType("spider"));
    // 阴森木
    public static final BlockSetType SPOOKY = BlockSetType.register(new BlockSetType("spooky"));


    /* 四柱的 */
    // 星云
    public static final BlockSetType NEBULA = BlockSetType.register(new BlockSetType("nebula"));
    // 日耀
    public static final BlockSetType SOLAR = BlockSetType.register(new BlockSetType("solar"));
    // 星尘
    public static final BlockSetType STARDUST = BlockSetType.register(new BlockSetType("stardust"));
    // 星旋(这个旋是维基的翻译字)
    public static final BlockSetType VORTEX = BlockSetType.register(new BlockSetType("vortex"));
}
