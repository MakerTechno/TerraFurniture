package org.confluence.terra_furniture.client.generators;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.lib.common.LibTags;
import org.confluence.terra_furniture.common.block.func.BlockSetGetter;
import org.confluence.terra_furniture.common.datagen.empowered.BlockDataGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 目标行为:
 * <ol>
 *     <li>准备方块模型</li>
 *     <li>
 *         <ol>
 *             <li>尝试从 <b>block/[blockTypeSet-name]/[template-name](/part_abc).json</b> 取模型</li>
 *             <li>失败, 尝试从 <b>block/templates/[template-name](/part_abc).json</b> 取模型</li>
 *             <li>失败, 发出严重提醒, 取消对当前方块的生成</li>
 *         </ol>
 *     </li>
 *     <li>准备贴图</li>
 *     <li>
 *         <ol>
 *             <li>通过hasParticle确认是否存在粒子贴图</li>
 *             <li>从 <b>block/[blockTypeSet-name]/[block-id](/pic_abc).png</b> 取图片</li>
 *             <ol>
 *                 <li>若hasParticle为true, 材质填入#0</li>
 *                 <li>若hasParticle为false, 材质填入#particle</li>
 *             </ol>
 *             <li>失败, 发出警告并尝试取 <b>block/templates/[template-name](/pic_abc).png</b></li>
 *             <ol>
 *                 <li>若hasParticle为true, 材质填入#0</li>
 *                 <li>若hasParticle为false, 材质填入#particle</li>
 *             </ol>
 *             <li>失败, 发出严重提醒, 取消对当前方块的生成</li>
 *         </ol>
 *     </li>
 *     <li>生成 <b>block/[blockTypeSet-name]/[block-id](/part_abc).json</b></li>
 *     <li>生成 <b>blockstates/[block-id].json</b></li>
 *     </br>
 *     <li>准备物品模型</li>
 *     <li>
 *         <ol>
 *             <li>尝试从 <b>item/[blockTypeSet-name]/[template-name](/part_abc).json</b> 取模型</li>
 *             <li>失败, 尝试从 <b>item/templates/[template-name](/part_abc).json</b> 取模型</li>
 *             <li>失败, 检查是否存在item/[block-id].json</li>
 *             <li>不存在, 使用方块作为父模型; 存在, 取消对当前物品的生成</li>
 *         </ol>
 *     </li>
 *     <li>当目标为定义可填入时, 准备贴图; 否则, 不进行贴图 </li>
 *     <li>
 *         <ol>
 *             <li>从 <b>item/[blockTypeSet-name]/[block-id](/pic_abc).png</b> 取图片</li>
 *             <li>失败, 发出警告并尝试取 <b>item/templates/[template-name](/pic_abc).png</b></li>
 *             <li>失败, 发出严重提醒, 停止对当前物品的生成</li>
 *         </ol>
 *     </li>
 *     <li>生成 <b>item/[block-id].json</b></li>
 * </ol>
 */
public abstract class DefaultBlockDataGenerator<T extends Block & BlockSetGetter<T>> implements BlockDataGenerator<T> {
    public static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    public static final ExistingFileHelper.ResourceType MODEL = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");

    public boolean isBlockValid = false;
    public boolean isItemValid = true;

    public String getOutputLoc(T block) {
        return block.getType().name() + "/" + getBlockPath(block);
    }

    public String typeName(T block) {
        return block.getType().name();
    }

    public String withTypeName(T block, String loc) {
        return typeName(block) + "/" + loc;
    }

    public <B extends ModelBuilder<B>, P extends ModelProvider<B>> Pair<ResourceState, B> processModel(T block, P provider, ExistingFileHelper helper, @Nullable String part, AccessType type) {
        part = part != null && !part.isBlank() ? "/" + part : "";
        String blockPath = getBlockPath(block) + part;
        B mb = provider.getBuilder(type.equals(AccessType.ITEM) ? prefix(blockPath, type) : prefix(getOutputLoc(block), type) + part);

        // 存在特制变体模型
        ResourceLocation location = toResourceLocation(prefix(withTypeName(block, getTemplateType(block) + part), type));
        if (helper.exists(location, MODEL)) {
            return Pair.of(ResourceState.EXIST, mb.parent(provider.getExistingFile(location)));
        }

        // 尝试返回默认的模板模型
        location = toResourceLocation(prefix(getTemplateLoc(block) + part, type));
        if (helper.exists(location, MODEL)) {
            return Pair.of(ResourceState.TEMPLATE, mb.parent(provider.getExistingFile(location)));
        }

        // 对物品额外进行默认检测或方块父获取
        if (type.equals(AccessType.ITEM)) {
            location = toResourceLocation(prefix(getOutputLoc(block) + part, type));
            if (helper.exists(location, MODEL)) return Pair.of(ResourceState.NOT_EXIST, null);
            location = toResourceLocation(prefix(getOutputLoc(block) + part, AccessType.BLOCK));
            if (helper.exists(location, MODEL)) {
                return Pair.of(ResourceState.NOT_EXIST, mb.parent(provider.getExistingFile(location)));
            }
        }

        // 错误: 不存在对应的模板模型或变体, 可能存在错误的纳入范围, 是否应该考虑从自动生成器中将其剔除?
        reportModel(prefix(getTemplateLoc(block) + part, type), blockPath, typeName(block));
        return Pair.of(ResourceState.NOT_EXIST, null);
    }

    public <B extends ModelBuilder<B>> Pair<ResourceState, B> processTexture(T block, B mb, @Nullable String name, AccessType type) {
        name = name != null && !name.isBlank() ? "/" + name : "";
        String blockPath = getBlockPath(block) + name;
        boolean hasParticle = block.hasParticle(block) && !type.equals(AccessType.ITEM);
        try {
            // 如果存在变体图片，添加该图片
            return Pair.of(ResourceState.EXIST, mb.texture(hasParticle ? "0" : "particle", toResourceLocation(prefix(withTypeName(block, blockPath), type))));
        } catch (IllegalArgumentException ignore) {
            warnTexture(prefix(withTypeName(block, blockPath), type), blockPath); // 警告: 变体图片在实际环境中是必须的
            try {
                return Pair.of(ResourceState.TEMPLATE, mb.texture(hasParticle ? "0" : "particle", toResourceLocation(prefix(getTemplateLoc(block) + name, type))));
            } catch (IllegalArgumentException exception) {
                reportTexture(blockPath, exception); // 错误: 不存在这个模板的WIP贴图
                return Pair.of(ResourceState.NOT_EXIST, null);
            }
        }
    }

    public <B extends ModelBuilder<B>, P extends ModelProvider<B>> @Nullable B processTogether(T block, P provider, ExistingFileHelper helper, @Nullable String part, boolean singleTexture) {
        Pair<ResourceState, B> mb = processModel(block, provider, helper, part, AccessType.BLOCK);
        if (mb.getFirst().equals(ResourceState.NOT_EXIST)) return null;
        mb = processTexture(block, mb.getSecond(), singleTexture ? null : part, AccessType.BLOCK);
        if (!mb.getFirst().equals(ResourceState.EXIST)) isBlockValid = false;
        return mb.getSecond();
    }

    @Override
    public void buildBlockWithTemplate(T block, BlockStateProvider builderProvider, ExistingFileHelper helper) {
        Pair<ResourceState, BlockModelBuilder> buildResult = processModel(block, builderProvider.models(), helper,null, AccessType.BLOCK);
        if (buildResult.getFirst().equals(ResourceState.NOT_EXIST)) return;
        isBlockValid = true;
        buildResult = processTexture(block, buildResult.getSecond(), null, AccessType.BLOCK);
        if (!buildResult.getFirst().equals(ResourceState.NOT_EXIST)) builderProvider.simpleBlock(block, buildResult.getSecond());
        if (!buildResult.getFirst().equals(ResourceState.EXIST)) isBlockValid = false;
    }

    @Override
    public void buildItemWithTemplate(T block, ItemModelProvider provider, ExistingFileHelper helper) {
        Pair<ResourceState, ItemModelBuilder> buildResult = processModel(block, provider, helper, null, AccessType.ITEM);
        if (buildResult.getFirst().equals(ResourceState.NOT_EXIST)) return;
        buildResult = processTexture(block, buildResult.getSecond(), null, AccessType.BLOCK);
        if (!buildResult.getFirst().equals(ResourceState.EXIST)) isItemValid = false;
    }

    @Override
    public List<TagKey<Block>> getRegBlockTags(T block, BlockTagsProvider provider) {
        return block.getType().getTagKeys().get();
    }

    @Override
    public List<TagKey<Item>> getRegItemTags(ItemTagsProvider provider) {
        return isBlockValid && isItemValid ? List.of() : List.of(LibTags.Items.WIP);
    }

    public enum ResourceState {
        NOT_EXIST, TEMPLATE, EXIST
    }
}
