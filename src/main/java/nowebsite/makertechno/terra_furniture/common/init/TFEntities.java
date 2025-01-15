package nowebsite.makertechno.terra_furniture.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import nowebsite.makertechno.terra_furniture.TerraFurniture;
import nowebsite.makertechno.terra_furniture.common.entity.chair.ChairEntity;

import java.util.function.Supplier;

public final class TFEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, TerraFurniture.MODID);

    public static final Supplier<EntityType<ChairEntity>> CHAIR = ENTITIES.register("chair", () -> EntityType.Builder.of(ChairEntity::new, MobCategory.MISC).sized(0.05F, 0.02F).build("terra_furniture:chair"));
}
