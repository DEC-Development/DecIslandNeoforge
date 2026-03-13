package com.dec.decisland.entity;

import com.dec.decisland.DecIsland;
import com.dec.decisland.entity.projectile.BlizzardEnergy;
import com.dec.decisland.entity.projectile.EnergyRay;
import com.dec.decisland.entity.projectile.ThrownAshPufferfish;
import com.dec.decisland.item.custom.AshPufferfish;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static EntityType<BlizzardEnergy> TYPE = null;
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(DecIsland.MOD_ID);

    public static final Supplier<EntityType<BlizzardEnergy>> BLIZZARD_ENERGY =
            ENTITY_TYPES.registerEntityType("blizzard_energy", BlizzardEnergy::new, MobCategory.MISC,
                    builder -> builder.sized(0.5f, 0.5f));

    public static final Supplier<EntityType<ThrownAshPufferfish>> THROWN_ASH_PUFFERFISH =
            ENTITY_TYPES.registerEntityType("thrown_ash_pufferfish", ThrownAshPufferfish::new, MobCategory.MISC,
                    builder -> builder.sized(0.1f, 0.1f));

    public static final Supplier<EntityType<EnergyRay>> ENERGY_RAY =
            ENTITY_TYPES.registerEntityType("energy_ray", EnergyRay::new, MobCategory.MISC,
                    builder -> builder.sized(0.5f, 0.5f));

    public static final void registry(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
