package com.dec.decisland.entity

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.projectile.BlizzardEnergy
import com.dec.decisland.entity.projectile.EnergyRay
import com.dec.decisland.entity.projectile.FlintlockBulletEntity
import com.dec.decisland.entity.projectile.SnowEnergy
import com.dec.decisland.entity.projectile.ThrownAshPufferfish
import com.dec.decisland.entity.projectile.dart.DartDefinition
import com.dec.decisland.entity.projectile.dart.DartEntity
import com.dec.decisland.entity.projectile.dart.ModDarts
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

data class BulletEntityConfig(
    val hitParticleIds: List<Identifier> = emptyList(),
    val hitParticleChance: Double = 1.0,
)

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
    val SNOW_ENERGY: Supplier<EntityType<SnowEnergy>> =
        ENTITY_TYPES.registerEntityType("snow_energy", ::SnowEnergy, MobCategory.MISC) { builder ->
            builder.sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val AMETHYST_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.AMETHYST_DART)

    @JvmField
    val COPPER_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.COPPER_DART)

    @JvmField
    val CORAL_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.CORAL_DART)

    @JvmField
    val DIAMOND_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.DIAMOND_DART)

    @JvmField
    val EMERALD_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.EMERALD_DART)

    @JvmField
    val EVERLASTING_WINTER_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.EVERLASTING_WINTER_DART)

    @JvmField
    val FROZEN_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.FROZEN_DART)

    @JvmField
    val GOLD_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.GOLD_DART)

    @JvmField
    val IRON_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.IRON_DART)

    @JvmField
    val LAVA_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.LAVA_DART)

    @JvmField
    val NETHERITE_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.NETHERITE_DART)

    @JvmField
    val POISON_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.POISON_DART)

    @JvmField
    val STEEL_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.STEEL_DART)

    @JvmField
    val STONE_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.STONE_DART)

    @JvmField
    val STREAM_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.STREAM_DART)

    @JvmField
    val WOOD_DART: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.WOOD_DART)

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

    @JvmField
    val BULLET_BY_FLINTLOCK: Supplier<EntityType<FlintlockBulletEntity>> = registerFlintlockBullet("bullet_by_flintlock")

    @JvmField
    val BULLET_BY_FLINTLOCK_PRO: Supplier<EntityType<FlintlockBulletEntity>> = registerFlintlockBullet("bullet_by_flintlock_pro")

    @JvmField
    val BULLET_BY_SHORT_FLINTLOCK: Supplier<EntityType<FlintlockBulletEntity>> = registerFlintlockBullet("bullet_by_short_flintlock")

    @JvmField
    val BULLET_BY_EVERLASTING_WINTER_FLINTLOCK: Supplier<EntityType<FlintlockBulletEntity>> =
        registerFlintlockBullet("bullet_by_everlasting_winter_flintlock")

    @JvmField
    val BULLET_BY_GHOST_FLINTLOCK: Supplier<EntityType<FlintlockBulletEntity>> = registerFlintlockBullet("bullet_by_ghost_flintlock")

    @JvmField
    val BULLET_BY_LAVA_FLINTLOCK: Supplier<EntityType<FlintlockBulletEntity>> = registerFlintlockBullet("bullet_by_lava_flintlock")

    @JvmField
    val BULLET_BY_STAR_FLINTLOCK: Supplier<EntityType<FlintlockBulletEntity>> = registerFlintlockBullet("bullet_by_star_flintlock")

    @JvmField
    val BULLET_BY_STORM_FLINTLOCK: Supplier<EntityType<FlintlockBulletEntity>> = registerFlintlockBullet("bullet_by_storm_flintlock")

    @JvmField
    val BULLET_CONFIG_BY_TYPE: Map<Supplier<EntityType<FlintlockBulletEntity>>, BulletEntityConfig> = linkedMapOf(
        BULLET_BY_FLINTLOCK to BulletEntityConfig(),
        BULLET_BY_FLINTLOCK_PRO to BulletEntityConfig(),
        BULLET_BY_SHORT_FLINTLOCK to BulletEntityConfig(),
        BULLET_BY_EVERLASTING_WINTER_FLINTLOCK to BulletEntityConfig(
            hitParticleIds = listOf(
                id("everlasting_winter_wake_particle"),
                id("everlasting_winter_spurt_particle"),
                id("snowflake_particle"),
            ),
        ),
        BULLET_BY_GHOST_FLINTLOCK to BulletEntityConfig(
            hitParticleIds = listOf(
                id("ghost_spurt_particle"),
                id("soul_particle"),
            ),
            hitParticleChance = 0.30,
        ),
        BULLET_BY_LAVA_FLINTLOCK to BulletEntityConfig(
            hitParticleIds = listOf(
                id("lava_spurt_particle"),
                id("lava_particle"),
                id("lava_explode_particle"),
            ),
            hitParticleChance = 0.30,
        ),
        BULLET_BY_STAR_FLINTLOCK to BulletEntityConfig(
            hitParticleIds = listOf(
                id("lava_spurt_particle"),
                id("star_spurt_particle"),
                id("star_particle"),
                id("star_explode_particle"),
            ),
            hitParticleChance = 0.40,
        ),
        BULLET_BY_STORM_FLINTLOCK to BulletEntityConfig(
            hitParticleIds = listOf(
                id("bubble_spurt_small_particle"),
                id("bubble_particle"),
            ),
        ),
    )

    @JvmStatic
    fun registry(eventBus: IEventBus) {
        ENTITY_TYPES.register(eventBus)
    }

    private fun registerFlintlockBullet(path: String): Supplier<EntityType<FlintlockBulletEntity>> =
        ENTITY_TYPES.registerEntityType(path, ::FlintlockBulletEntity, MobCategory.MISC) { builder ->
            builder.sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(10)
        }

    private fun registerDartEntity(
        definition: DartDefinition,
    ): Supplier<EntityType<DartEntity>> {
        DartEntity.registerDefinition(definition)
        return ENTITY_TYPES.registerEntityType(definition.path, ::DartEntity, MobCategory.MISC) { builder ->
            builder
                .sized(definition.entitySettings.entityWidth, definition.entitySettings.entityHeight)
                .clientTrackingRange(definition.entitySettings.clientTrackingRange)
                .updateInterval(definition.entitySettings.updateInterval)
        }.also(definition::bindEntityTypeSupplier)
    }

    private fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, path)
}
