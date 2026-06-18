package org.confluence.terra_furniture.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_furniture.TerraFurniture;
import org.confluence.terra_furniture.common.entity.RideableEntityNull;

public final class TFEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, TerraFurniture.MODID);

    public static final RegistryObject<EntityType<RideableEntityNull>> NULL_RIDE = ENTITIES.register(
        "null_ride",
        ()-> EntityType.Builder.<RideableEntityNull>of(
                RideableEntityNull::new,
                MobCategory.MISC
            ).sized(0,0)
            .clientTrackingRange(10)
            .noSave()
            .build("null_ride")
    );
}
