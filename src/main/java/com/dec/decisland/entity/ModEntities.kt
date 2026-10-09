package com.dec.decisland.entity

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.custom.ElfOfLeaves
import com.dec.decisland.entity.custom.LeavesGolem
import com.dec.decisland.entity.custom.PumpkinBombEntity
import com.dec.decisland.entity.custom.WitherCloudEntity
import com.dec.decisland.entity.custom.ZombieWarrior
import com.dec.decisland.entity.projectile.AmethystEnergyBall
import com.dec.decisland.entity.projectile.AmethystEnergyRay
import com.dec.decisland.entity.projectile.BlizzardEnergy
import com.dec.decisland.entity.projectile.ConcentratedSoulBullet
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
import com.dec.decisland.entity.projectile.JellyfishStaffProjectile
import com.dec.decisland.entity.projectile.LapisBullet
import com.dec.decisland.entity.projectile.MindControllerProjectile
import com.dec.decisland.entity.projectile.MuddyBallProjectile
import com.dec.decisland.entity.projectile.NightmareRay
import com.dec.decisland.entity.projectile.NightmareSpore
import com.dec.decisland.entity.projectile.PureEnergyBall
import com.dec.decisland.entity.projectile.SoulWakeBullet
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
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.Level
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

data class BulletEntityConfig(
    val hitParticleIds: List<ResourceLocation> = emptyList(),
    val hitParticleChance: Double = 1.0,
)

object ModEntities {
    @JvmField
    var TYPE: EntityType<BlizzardEnergy>? = null

    @JvmField
    val ENTITY_TYPES: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(Registries.ENTITY_TYPE, DecIsland.MOD_ID)

    private fun <T : Entity> registerEntity(
        name: String,
        factory: (EntityType<T>, Level) -> T,
        category: MobCategory,
        configure: EntityType.Builder<T>.() -> Unit = {},
    ): DeferredHolder<EntityType<*>, EntityType<T>> =
        ENTITY_TYPES.register(
            name,
            Supplier { EntityType.Builder.of(factory, category).apply(configure).build(name) },
        )

    @JvmField
    val BLIZZARD_ENERGY: Supplier<EntityType<BlizzardEnergy>> =
        registerEntity("blizzard_energy", { type, level -> BlizzardEnergy(type, level) }, MobCategory.MISC) {
            sized(0.5f, 0.5f)
        }

    @JvmField
    val SNOW_ENERGY: Supplier<EntityType<SnowEnergy>> =
        registerEntity("snow_energy", { type, level -> SnowEnergy(type, level) }, MobCategory.MISC) {
            sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
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
    val VOID_WHISPERING_DAGGER: Supplier<EntityType<DartEntity>> =
        registerDartEntity(ModDarts.VOID_WHISPERING_DAGGER)

    @JvmField
    val THROWN_ASH_PUFFERFISH: Supplier<EntityType<ThrownAshPufferfish>> =
        registerEntity("thrown_ash_pufferfish", { type, level -> ThrownAshPufferfish(type, level) }, MobCategory.MISC) {
            sized(0.1f, 0.1f)
        }

    @JvmField
    val STICKY_ASH: Supplier<EntityType<ThrownStickyAsh>> =
        registerEntity("sticky_ash", { type, level -> ThrownStickyAsh(type, level) }, MobCategory.MISC) {
            sized(0.25f, 0.25f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val FROZEN_BALL: Supplier<EntityType<FrozenBallProjectile>> =
        registerEntity("frozen_ball", { type, level -> FrozenBallProjectile(type, level) }, MobCategory.MISC) {
            sized(0.31f, 0.31f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val MIND_CONTROLLER: Supplier<EntityType<MindControllerProjectile>> =
        registerEntity("mind_controller", { type, level -> MindControllerProjectile(type, level) }, MobCategory.MISC) {
            sized(0.25f, 0.25f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val MUDDY_BALL: Supplier<EntityType<MuddyBallProjectile>> =
        registerEntity("muddy_ball", { type, level -> MuddyBallProjectile(type, level) }, MobCategory.MISC) {
            sized(0.25f, 0.25f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val FIREFLY_BOTTLE: Supplier<EntityType<FireflyBottleProjectile>> =
        registerEntity("firefly_bottle", { type, level -> FireflyBottleProjectile(type, level) }, MobCategory.MISC) {
            sized(0.1f, 0.1f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val SMOKE_BOMB: Supplier<EntityType<SmokeBombProjectile>> =
        registerEntity("smoke_bomb", { type, level -> SmokeBombProjectile(type, level) }, MobCategory.MISC) {
            sized(0.25f, 0.25f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val GAS_BOMB: Supplier<EntityType<GasBombProjectile>> =
        registerEntity("gas_bomb", { type, level -> GasBombProjectile(type, level) }, MobCategory.MISC) {
            sized(0.25f, 0.25f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val THROWABLE_BOMB: Supplier<EntityType<ThrowableBombProjectile>> =
        registerEntity("throwable_bomb", { type, level -> ThrowableBombProjectile(type, level) }, MobCategory.MISC) {
            sized(0.1f, 0.1f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val ENERGY_BALL: Supplier<EntityType<EnergyBall>> =
        registerEntity("energy_ball", { type, level -> EnergyBall(type, level) }, MobCategory.MISC) {
            sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val AMETHYST_ENERGY_BALL: Supplier<EntityType<AmethystEnergyBall>> =
        registerEntity("amethyst_energy_ball", { type, level -> AmethystEnergyBall(type, level) }, MobCategory.MISC) {
            sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val GOLDEN_ENERGY_BALL: Supplier<EntityType<GoldenEnergyBall>> =
        registerEntity("golden_energy_ball", { type, level -> GoldenEnergyBall(type, level) }, MobCategory.MISC) {
            sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val LAPIS_BULLET: Supplier<EntityType<LapisBullet>> =
        registerEntity("lapis_bullet", { type, level -> LapisBullet(type, level) }, MobCategory.MISC) {
            sized(0.31f, 0.31f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val CONCENTRATED_SOUL_BULLET: Supplier<EntityType<ConcentratedSoulBullet>> =
        registerEntity("concentrated_soul_bullet", { type, level -> ConcentratedSoulBullet(type, level) }, MobCategory.MISC) {
            sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val JELLYFISH_BY_JELLYFISH_STAFF: Supplier<EntityType<JellyfishStaffProjectile>> =
        registerEntity("jellyfish_by_jellyfish_staff", { type, level -> JellyfishStaffProjectile(type, level) }, MobCategory.MISC) {
            sized(0.31f, 0.31f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val SOUL_WAKE_BULLET: Supplier<EntityType<SoulWakeBullet>> =
        registerEntity("soul_wake_bullet", { type, level -> SoulWakeBullet(type, level) }, MobCategory.MISC) {
            sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val STREAM_ENERGY_BALL: Supplier<EntityType<StreamEnergyBall>> =
        registerEntity("stream_energy_ball", { type, level -> StreamEnergyBall(type, level) }, MobCategory.MISC) {
            sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val PURE_ENERGY_BALL: Supplier<EntityType<PureEnergyBall>> =
        registerEntity("pure_energy_ball", { type, level -> PureEnergyBall(type, level) }, MobCategory.MISC) {
            sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val SPOTS_BY_BOOK: Supplier<EntityType<SpotsByBook>> =
        registerEntity("spots_by_book", { type, level -> SpotsByBook(type, level) }, MobCategory.MISC) {
            sized(0.31f, 0.31f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val SPOTS_OVERFLOW: Supplier<EntityType<SpotsOverflow>> =
        registerEntity("spots_overflow", { type, level -> SpotsOverflow(type, level) }, MobCategory.MISC) {
            sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val DEEP_ENERGY: Supplier<EntityType<DeepEnergy>> =
        registerEntity("deep_energy", { type, level -> DeepEnergy(type, level) }, MobCategory.MISC) {
            sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val FROZEN_ENERGY_BALL: Supplier<EntityType<FrozenEnergyBall>> =
        registerEntity("frozen_energy_ball", { type, level -> FrozenEnergyBall(type, level) }, MobCategory.MISC) {
            sized(0.6f, 0.6f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val WINTER_ENERGY: Supplier<EntityType<WinterEnergy>> =
        registerEntity("winter_energy", { type, level -> WinterEnergy(type, level) }, MobCategory.MISC) {
            sized(0.8f, 0.8f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val THUNDER_BALL: Supplier<EntityType<ThunderBall>> =
        registerEntity("thunder_ball", { type, level -> ThunderBall(type, level) }, MobCategory.MISC) {
            sized(0.9f, 0.9f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val ENERGY_RAY: Supplier<EntityType<EnergyRay>> =
        registerEntity("energy_ray", { type, level -> EnergyRay(type, level) }, MobCategory.MISC) {
            sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val AMETHYST_ENERGY_RAY: Supplier<EntityType<AmethystEnergyRay>> =
        registerEntity("amethyst_energy_ray", { type, level -> AmethystEnergyRay(type, level) }, MobCategory.MISC) {
            sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val FROZEN_RAY: Supplier<EntityType<FrozenRay>> =
        registerEntity("frozen_ray", { type, level -> FrozenRay(type, level) }, MobCategory.MISC) {
            sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val GROWING_ENERGY_RAY: Supplier<EntityType<GrowingEnergyRay>> =
        registerEntity("growing_energy_ray", { type, level -> GrowingEnergyRay(type, level) }, MobCategory.MISC) {
            sized(0.31f, 0.31f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val NIGHTMARE_SPORE: Supplier<EntityType<NightmareSpore>> =
        registerEntity("nightmare_spore", { type, level -> NightmareSpore(type, level) }, MobCategory.MISC) {
            sized(0.4f, 0.4f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val NIGHTMARE_RAY: Supplier<EntityType<NightmareRay>> =
        registerEntity("nightmare_ray", { type, level -> NightmareRay(type, level) }, MobCategory.MISC) {
            sized(0.31f, 0.31f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val WAVE_ENERGY: Supplier<EntityType<WaveEnergy>> =
        registerEntity("wave_energy", { type, level -> WaveEnergy(type, level) }, MobCategory.MISC) {
            sized(1.0f, 1.0f).clientTrackingRange(64).updateInterval(1)
        }

    @JvmField
    val STORM_FUSE: Supplier<EntityType<StormFuse>> =
        registerEntity("storm_fuse", { type, level -> StormFuse(type, level) }, MobCategory.MISC) {
            sized(0.1f, 0.1f).clientTrackingRange(64).updateInterval(1)
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

    @JvmField
    val PUMPKIN_BOMB: Supplier<EntityType<PumpkinBombEntity>> =
        registerEntity("pumpkin_bomb", { type, level -> PumpkinBombEntity(type, level) }, MobCategory.MISC) {
            sized(0.5f, 0.5f).clientTrackingRange(8).updateInterval(10)
        }

    @JvmField
    val WITHER_CLOUD: Supplier<EntityType<WitherCloudEntity>> =
        registerEntity("wither_cloud", { type, level -> WitherCloudEntity(type, level) }, MobCategory.MISC) {
            sized(0.5f, 0.5f).clientTrackingRange(8).updateInterval(10)
        }

    @JvmField
    val ZOMBIE_WARRIOR: Supplier<EntityType<ZombieWarrior>> =
        registerEntity("zombie_warrior", { type, level -> ZombieWarrior(type, level) }, MobCategory.MONSTER) {
            sized(0.6f, 1.95f).clientTrackingRange(10).updateInterval(2)
        }

    @JvmField
    val LEAVES_GOLEM: Supplier<EntityType<LeavesGolem>> =
        registerEntity("leaves_golem", { type, level -> LeavesGolem(type, level) }, MobCategory.MONSTER) {
            sized(1.4f, 2.9f).fireImmune().clientTrackingRange(10).updateInterval(2)
        }

    @JvmField
    val ELF_OF_LEAVES: Supplier<EntityType<ElfOfLeaves>> =
        registerEntity("elf_of_leaves", { type, level -> ElfOfLeaves(type, level) }, MobCategory.MONSTER) {
            sized(0.4f, 0.8f).clientTrackingRange(10).updateInterval(2)
        }

    @JvmStatic
    fun registry(eventBus: IEventBus) {
        ENTITY_TYPES.register(eventBus)
    }

    private fun registerFlintlockBullet(path: String): Supplier<EntityType<FlintlockBulletEntity>> =
        registerEntity(path, { type, level -> FlintlockBulletEntity(type, level) }, MobCategory.MISC) {
            sized(0.2f, 0.2f).clientTrackingRange(64).updateInterval(10)
        }

    private fun registerDartEntity(
        definition: DartDefinition,
    ): Supplier<EntityType<DartEntity>> {
        DartEntity.registerDefinition(definition)
        return registerEntity(
            definition.path,
            { type, level -> DartEntity(type, level) },
            MobCategory.MISC,
        ) {
            sized(definition.entitySettings.entityWidth, definition.entitySettings.entityHeight)
                .clientTrackingRange(definition.entitySettings.clientTrackingRange)
                .updateInterval(definition.entitySettings.updateInterval)
        }.also(definition::bindEntityTypeSupplier)
    }

    private fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, path)
}
