package com.dec.decisland.entity

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.projectile.AmethystEnergyBall
import com.dec.decisland.entity.projectile.AmethystEnergyRay
import com.dec.decisland.entity.projectile.BlizzardEnergy
import com.dec.decisland.entity.projectile.DeepEnergy
import com.dec.decisland.entity.projectile.EnergyBall
import com.dec.decisland.entity.projectile.EnergyRay
import com.dec.decisland.entity.projectile.FireflyBottleProjectile
import com.dec.decisland.entity.projectile.FlintlockBulletEntity
import com.dec.decisland.entity.projectile.FrozenBallProjectile
import com.dec.decisland.entity.projectile.FrozenEnergyBall
import com.dec.decisland.entity.projectile.FrozenRay
import com.dec.decisland.entity.projectile.GasBombProjectile
import com.dec.decisland.entity.projectile.GoldenEnergyBall
import com.dec.decisland.entity.projectile.GrowingEnergyRay
import com.dec.decisland.entity.projectile.MindControllerProjectile
import com.dec.decisland.entity.projectile.MuddyBallProjectile
import com.dec.decisland.entity.projectile.NightmareRay
import com.dec.decisland.entity.projectile.NightmareSpore
import com.dec.decisland.entity.projectile.PureEnergyBall
import com.dec.decisland.entity.projectile.SnowEnergy
import com.dec.decisland.entity.projectile.SmokeBombProjectile
import com.dec.decisland.entity.projectile.SpotsByBook
import com.dec.decisland.entity.projectile.SpotsOverflow
import com.dec.decisland.entity.projectile.StreamEnergyBall
import com.dec.decisland.entity.projectile.StormFuse
import com.dec.decisland.entity.projectile.ThunderBall
import com.dec.decisland.entity.projectile.ThrowableBombProjectile
import com.dec.decisland.entity.projectile.ThrownAshPufferfish
import com.dec.decisland.entity.projectile.ThrownStickyAsh
import com.dec.decisland.entity.projectile.WaveEnergy
import com.dec.decisland.entity.projectile.WinterEnergy
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
    val STICKY_ASH: Supplier<EntityType<ThrownStickyAsh>> =
        ENTITY_TYPES.registerEntityType("sticky_ash", ::ThrownStickyAsh, MobCategory.MISC) { builder ->
            builder.sized(0.25f, 0.25f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val FROZEN_BALL: Supplier<EntityType<FrozenBallProjectile>> =
        ENTITY_TYPES.registerEntityType("frozen_ball", ::FrozenBallProjectile, MobCategory.MISC) { builder ->
            builder.sized(0.31f, 0.31f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val MIND_CONTROLLER: Supplier<EntityType<MindControllerProjectile>> =
        ENTITY_TYPES.registerEntityType("mind_controller", ::MindControllerProjectile, MobCategory.MISC) { builder ->
            builder.sized(0.25f, 0.25f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val MUDDY_BALL: Supplier<EntityType<MuddyBallProjectile>> =
        ENTITY_TYPES.registerEntityType("muddy_ball", ::MuddyBallProjectile, MobCategory.MISC) { builder ->
            builder.sized(0.25f, 0.25f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val FIREFLY_BOTTLE: Supplier<EntityType<FireflyBottleProjectile>> =
        ENTITY_TYPES.registerEntityType("firefly_bottle", ::FireflyBottleProjectile, MobCategory.MISC) { builder ->
            builder.sized(0.1f, 0.1f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val SMOKE_BOMB: Supplier<EntityType<SmokeBombProjectile>> =
        ENTITY_TYPES.registerEntityType("smoke_bomb", ::SmokeBombProjectile, MobCategory.MISC) { builder ->
            builder.sized(0.25f, 0.25f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val GAS_BOMB: Supplier<EntityType<GasBombProjectile>> =
        ENTITY_TYPES.registerEntityType("gas_bomb", ::GasBombProjectile, MobCategory.MISC) { builder ->
            builder.sized(0.25f, 0.25f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val THROWABLE_BOMB: Supplier<EntityType<ThrowableBombProjectile>> =
        ENTITY_TYPES.registerEntityType("throwable_bomb", ::ThrowableBombProjectile, MobCategory.MISC) { builder ->
            builder.sized(0.1f, 0.1f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val ENERGY_BALL: Supplier<EntityType<EnergyBall>> =
        ENTITY_TYPES.registerEntityType("energy_ball", ::EnergyBall, MobCategory.MISC) { builder ->
            builder.sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val AMETHYST_ENERGY_BALL: Supplier<EntityType<AmethystEnergyBall>> =
        ENTITY_TYPES.registerEntityType("amethyst_energy_ball", ::AmethystEnergyBall, MobCategory.MISC) { builder ->
            builder.sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val GOLDEN_ENERGY_BALL: Supplier<EntityType<GoldenEnergyBall>> =
        ENTITY_TYPES.registerEntityType("golden_energy_ball", ::GoldenEnergyBall, MobCategory.MISC) { builder ->
            builder.sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val STREAM_ENERGY_BALL: Supplier<EntityType<StreamEnergyBall>> =
        ENTITY_TYPES.registerEntityType("stream_energy_ball", ::StreamEnergyBall, MobCategory.MISC) { builder ->
            builder.sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val PURE_ENERGY_BALL: Supplier<EntityType<PureEnergyBall>> =
        ENTITY_TYPES.registerEntityType("pure_energy_ball", ::PureEnergyBall, MobCategory.MISC) { builder ->
            builder.sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val SPOTS_BY_BOOK: Supplier<EntityType<SpotsByBook>> =
        ENTITY_TYPES.registerEntityType("spots_by_book", ::SpotsByBook, MobCategory.MISC) { builder ->
            builder.sized(0.31f, 0.31f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val SPOTS_OVERFLOW: Supplier<EntityType<SpotsOverflow>> =
        ENTITY_TYPES.registerEntityType("spots_overflow", ::SpotsOverflow, MobCategory.MISC) { builder ->
            builder.sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val DEEP_ENERGY: Supplier<EntityType<DeepEnergy>> =
        ENTITY_TYPES.registerEntityType("deep_energy", ::DeepEnergy, MobCategory.MISC) { builder ->
            builder.sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val FROZEN_ENERGY_BALL: Supplier<EntityType<FrozenEnergyBall>> =
        ENTITY_TYPES.registerEntityType("frozen_energy_ball", ::FrozenEnergyBall, MobCategory.MISC) { builder ->
            builder.sized(0.6f, 0.6f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val WINTER_ENERGY: Supplier<EntityType<WinterEnergy>> =
        ENTITY_TYPES.registerEntityType("winter_energy", ::WinterEnergy, MobCategory.MISC) { builder ->
            builder.sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val THUNDER_BALL: Supplier<EntityType<ThunderBall>> =
        ENTITY_TYPES.registerEntityType("thunder_ball", ::ThunderBall, MobCategory.MISC) { builder ->
            builder.sized(0.9f, 0.9f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val ENERGY_RAY: Supplier<EntityType<EnergyRay>> =
        ENTITY_TYPES.registerEntityType("energy_ray", ::EnergyRay, MobCategory.MISC) { builder ->
            builder.sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val AMETHYST_ENERGY_RAY: Supplier<EntityType<AmethystEnergyRay>> =
        ENTITY_TYPES.registerEntityType("amethyst_energy_ray", ::AmethystEnergyRay, MobCategory.MISC) { builder ->
            builder.sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val FROZEN_RAY: Supplier<EntityType<FrozenRay>> =
        ENTITY_TYPES.registerEntityType("frozen_ray", ::FrozenRay, MobCategory.MISC) { builder ->
            builder.sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val GROWING_ENERGY_RAY: Supplier<EntityType<GrowingEnergyRay>> =
        ENTITY_TYPES.registerEntityType("growing_energy_ray", ::GrowingEnergyRay, MobCategory.MISC) { builder ->
            builder.sized(0.31f, 0.31f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val NIGHTMARE_SPORE: Supplier<EntityType<NightmareSpore>> =
        ENTITY_TYPES.registerEntityType("nightmare_spore", ::NightmareSpore, MobCategory.MISC) { builder ->
            builder.sized(0.4f, 0.4f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val NIGHTMARE_RAY: Supplier<EntityType<NightmareRay>> =
        ENTITY_TYPES.registerEntityType("nightmare_ray", ::NightmareRay, MobCategory.MISC) { builder ->
            builder.sized(0.31f, 0.31f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val WAVE_ENERGY: Supplier<EntityType<WaveEnergy>> =
        ENTITY_TYPES.registerEntityType("wave_energy", ::WaveEnergy, MobCategory.MISC) { builder ->
            builder.sized(1.0f, 1.0f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val STORM_FUSE: Supplier<EntityType<StormFuse>> =
        ENTITY_TYPES.registerEntityType("storm_fuse", ::StormFuse, MobCategory.MISC) { builder ->
            builder.sized(0.1f, 0.1f).clientTrackingRange(64).updateInterval(1)
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
