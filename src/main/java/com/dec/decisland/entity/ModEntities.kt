package com.dec.decisland.entity

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.projectile.BlizzardEnergy
import com.dec.decisland.entity.projectile.EnergyRay
import com.dec.decisland.entity.projectile.ThrownAshPufferfish
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModEntities {
    @JvmField
    var TYPE: EntityType<BlizzardEnergy>? = null

    @JvmField
    val ENTITY_TYPES: DeferredRegister.Entities = DeferredRegister.createEntities(DecIsland.MOD_ID)

    @JvmField
    val BLIZZARD_ENERGY: Supplier<EntityType<BlizzardEnergy>> =
        ENTITY_TYPES.registerEntityType("blizzard_energy", ::BlizzardEnergy, MobCategory.MISC) { builder ->
            builder.sized(0.5f, 0.5f)
        }

    @JvmField
    val THROWN_ASH_PUFFERFISH: Supplier<EntityType<ThrownAshPufferfish>> =
        ENTITY_TYPES.registerEntityType("thrown_ash_pufferfish", ::ThrownAshPufferfish, MobCategory.MISC) { builder ->
            builder.sized(0.1f, 0.1f)
        }

    @JvmField
    val ENERGY_RAY: Supplier<EntityType<EnergyRay>> =
        ENTITY_TYPES.registerEntityType("energy_ray", ::EnergyRay, MobCategory.MISC) { builder ->
            builder.sized(0.5f, 0.5f)
        }

    @JvmStatic
    fun registry(eventBus: IEventBus) {
        ENTITY_TYPES.register(eventBus)
    }
}
