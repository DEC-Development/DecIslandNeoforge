package com.dec.decisland.entity.projectile.dart

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.projectile.SnowEnergy
import com.dec.decisland.item.custom.dart.DartItemSettings
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity

object ModDarts {
    private const val LAVA_DART_EXPLODE_WEIGHT = 3
    private const val LAVA_DART_NO_EXPLODE_WEIGHT = 3
    private const val EVERLASTING_WINTER_DART_NO_EXTRA_WEIGHT = 3
    @JvmField
    val AMETHYST_DART: DartDefinition = DartDefinition(
        path = "amethyst_dart",
        entitySettings = DartSettings(
            baseDamage = 5.5f,
        ),
        itemSettings = DartItemSettings(
            power = 2.5f,
            uncertainty = 2.0f,
        ),
    )

    @JvmField
    val COPPER_DART: DartDefinition = basicDart(
        path = "copper_dart",
        baseDamage = 5.5f,
        power = 2.05f,
        uncertainty = 6.0f,
        cooldownTicks = 10,
        gravity = 0.032,
        bedrockInertia = 1.05,
    )

    @JvmField
    val CORAL_DART: DartDefinition = basicDart(
        path = "coral_dart",
        baseDamage = 5.0f,
        power = 2.1f,
        uncertainty = 4.0f,
        cooldownTicks = 10,
        gravity = 0.03,
        bedrockInertia = 1.1,
        waterInertia = 1.1f,
    )

    @JvmField
    val DIAMOND_DART: DartDefinition = basicDart(
        path = "diamond_dart",
        baseDamage = 7.0f,
        power = 2.1f,
        uncertainty = 5.0f,
        cooldownTicks = 10,
        gravity = 0.03,
        bedrockInertia = 1.1,
    )

    @JvmField
    val EMERALD_DART: DartDefinition = basicDart(
        path = "emerald_dart",
        baseDamage = 6.5f,
        power = 2.51f,
        uncertainty = 1.0f,
        cooldownTicks = 4,
        gravity = 0.021,
        bedrockInertia = 1.24,
    )

    @JvmField
    val EVERLASTING_WINTER_DART: DartDefinition = DartDefinition(
        path = "everlasting_winter_dart",
        entitySettings = DartSettings(
            baseDamage = 7.0f,
            gravity = 0.03,
            bedrockInertia = 1.1,
            effectOnHit = { MobEffectInstance(MobEffects.SLOWNESS, 200, 1) },
        ),
        itemSettings = DartItemSettings(
            power = 2.1f,
            uncertainty = 6.0f,
            cooldownTicks = 20,
        ),
        onHitServer = {
            sendSnowballPoof(6)

            when (random.nextInt(6)) {
                in 0 until EVERLASTING_WINTER_DART_NO_EXTRA_WEIGHT -> {}
                3 -> spawnSnowEnergyPattern(this, SNOW_PATTERN_1)
                4 -> spawnSnowEnergyPattern(this, SNOW_PATTERN_2)
                else -> spawnSnowEnergyPattern(this, SNOW_PATTERN_4)
            }
        },
    )

    @JvmField
    val FROZEN_DART: DartDefinition = basicDart(
        path = "frozen_dart",
        baseDamage = 6.0f,
        power = 2.1f,
        uncertainty = 6.0f,
        cooldownTicks = 10,
        gravity = 0.03,
        bedrockInertia = 1.1,
        effectOnHit = { MobEffectInstance(MobEffects.SLOWNESS, 100, 0) },
    )

    @JvmField
    val GOLD_DART: DartDefinition = basicDart(
        path = "gold_dart",
        baseDamage = 5.0f,
        power = 2.4f,
        uncertainty = 3.0f,
        cooldownTicks = 6,
        gravity = 0.025,
        bedrockInertia = 1.2,
    )

    @JvmField
    val IRON_DART: DartDefinition = basicDart(
        path = "iron_dart",
        baseDamage = 6.0f,
        power = 2.1f,
        uncertainty = 6.0f,
        cooldownTicks = 10,
        gravity = 0.03,
        bedrockInertia = 1.1,
    )

    @JvmField
    val LAVA_DART: DartDefinition = DartDefinition(
        path = "lava_dart",
        entitySettings = DartSettings(
            baseDamage = 8.0f,
            gravity = 0.03,
            bedrockInertia = 1.1,
            igniteSecondsOnHit = 5.0f,
        ),
        itemSettings = DartItemSettings(
            power = 2.1f,
            uncertainty = 6.0f,
            cooldownTicks = 12,
        ),
        onHitServer = {
            val totalWeight = LAVA_DART_EXPLODE_WEIGHT + LAVA_DART_NO_EXPLODE_WEIGHT
            val explode = random.nextInt(totalWeight) < LAVA_DART_EXPLODE_WEIGHT
            if (!explode) {
                return@DartDefinition
            }

            sendHitEmitters(listOf(id("lava_spurt_particle")))
            playHitSound(SoundEvents.GENERIC_EXPLODE.value())
            explodeDamage(radius = 3.0, damage = 12.0f)
        },
    )

    @JvmField
    val NETHERITE_DART: DartDefinition = basicDart(
        path = "netherite_dart",
        baseDamage = 8.0f,
        power = 2.1f,
        uncertainty = 5.0f,
        cooldownTicks = 10,
        gravity = 0.03,
        bedrockInertia = 1.1,
    )

    @JvmField
    val POISON_DART: DartDefinition = basicDart(
        path = "poison_dart",
        baseDamage = 6.0f,
        power = 2.1f,
        uncertainty = 6.0f,
        cooldownTicks = 10,
        gravity = 0.03,
        bedrockInertia = 1.1,
        effectOnHit = { MobEffectInstance(MobEffects.POISON, 200, 0) },
    )

    @JvmField
    val STEEL_DART: DartDefinition = basicDart(
        path = "steel_dart",
        baseDamage = 6.5f,
        power = 2.1f,
        uncertainty = 4.0f,
        cooldownTicks = 10,
        gravity = 0.03,
        bedrockInertia = 1.1,
    )

    @JvmField
    val STONE_DART: DartDefinition = basicDart(
        path = "stone_dart",
        baseDamage = 5.0f,
        power = 2.0f,
        uncertainty = 9.0f,
        cooldownTicks = 12,
        gravity = 0.035,
        bedrockInertia = 1.1,
    )

    @JvmField
    val STREAM_DART: DartDefinition = basicDart(
        path = "stream_dart",
        baseDamage = 10.0f,
        power = 2.3f,
        uncertainty = 6.0f,
        cooldownTicks = 22,
        gravity = 0.03,
        bedrockInertia = 1.1,
        applyKnockback = false,
        effectOnHit = { MobEffectInstance(MobEffects.INVISIBILITY, 200, 0) },
    )

    @JvmField
    val WOOD_DART: DartDefinition = basicDart(
        path = "wood_dart",
        baseDamage = 4.0f,
        power = 1.8f,
        uncertainty = 11.0f,
        cooldownTicks = 12,
        gravity = 0.035,
        bedrockInertia = 1.0,
    )

    @JvmField
    val ALL: List<DartDefinition> = listOf(
        AMETHYST_DART,
        COPPER_DART,
        CORAL_DART,
        DIAMOND_DART,
        EMERALD_DART,
        EVERLASTING_WINTER_DART,
        FROZEN_DART,
        GOLD_DART,
        IRON_DART,
        LAVA_DART,
        NETHERITE_DART,
        POISON_DART,
        STEEL_DART,
        STONE_DART,
        STREAM_DART,
        WOOD_DART,
    )

    private fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, path)

    private fun basicDart(
        path: String,
        baseDamage: Float,
        power: Float,
        uncertainty: Float,
        cooldownTicks: Int,
        gravity: Double,
        bedrockInertia: Double,
        waterInertia: Float = 0.8f,
        applyKnockback: Boolean = true,
        effectOnHit: (() -> MobEffectInstance)? = null,
    ): DartDefinition = DartDefinition(
        path = path,
        entitySettings = DartSettings(
            baseDamage = baseDamage,
            gravity = gravity,
            bedrockInertia = bedrockInertia,
            waterInertia = waterInertia,
            applyKnockback = applyKnockback,
            effectOnHit = effectOnHit,
        ),
        itemSettings = DartItemSettings(
            power = power,
            uncertainty = uncertainty,
            cooldownTicks = cooldownTicks,
        ),
    )

    private fun spawnSnowEnergyPattern(dart: DartEntity, offsets: List<Pair<Int, Int>>) {
        val level = dart.level() as? ServerLevel ?: return
        val owner = dart.owner as? LivingEntity
        offsets.forEach { (offsetX, offsetZ) ->
            val spawnY = dart.y + weightedSnowEnergyHeight(dart.random)
            level.addFreshEntity(
                SnowEnergy(
                    level = level,
                    owner = owner,
                    x = dart.x + offsetX,
                    y = spawnY,
                    z = dart.z + offsetZ,
                ),
            )
        }
    }

    private fun weightedSnowEnergyHeight(random: net.minecraft.util.RandomSource): Double =
        when (random.nextInt(19)) {
            in 0..3 -> 3.0
            in 4..8 -> 4.0
            in 9..12 -> 5.0
            in 13..16 -> 6.0
            else -> 7.0
        }

    private val SNOW_PATTERN_1: List<Pair<Int, Int>> = listOf(
        0 to 0,
        1 to 0,
        0 to 1,
        -1 to 0,
        0 to -1,
        1 to 1,
        -1 to -1,
        1 to -1,
        -1 to 1,
        2 to 2,
        3 to 3,
        -2 to 2,
        -3 to 3,
        2 to -2,
        3 to -3,
        -2 to -2,
        -3 to -3,
        3 to 0,
        5 to 0,
        -3 to 0,
        -5 to 0,
        0 to 3,
        0 to 5,
        0 to -3,
        0 to -5,
    )

    private val SNOW_PATTERN_2: List<Pair<Int, Int>> = listOf(
        1 to 0,
        2 to 0,
        4 to 0,
        -1 to 0,
        -2 to 0,
        -4 to 0,
        0 to 1,
        0 to 2,
        0 to 4,
        0 to -1,
        0 to -2,
        0 to -4,
        3 to 1,
        3 to -1,
        -3 to 1,
        -3 to -1,
        1 to 3,
        -1 to 3,
        1 to -3,
        -1 to -3,
        3 to 3,
        -3 to 3,
        3 to -3,
        -3 to -3,
    )

    private val SNOW_PATTERN_4: List<Pair<Int, Int>> = listOf(
        0 to 0,
        1 to 1,
        -1 to 1,
        1 to -1,
        -1 to -1,
        2 to 2,
        2 to -2,
        -2 to 2,
        -2 to -2,
        3 to 0,
        -3 to 0,
        0 to 3,
        0 to -3,
    )
}
