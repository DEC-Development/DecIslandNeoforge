package com.dec.decisland.entity.projectile

import com.dec.decisland.entity.ModEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class WinterEnergy(entityType: EntityType<WinterEnergy>, level: Level) :
    VisibleMagicBallProjectile(
        entityType,
        level,
        "everlasting_winter_wake_particle",
        "winter_energy_hit_particle",
    ) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.WINTER_ENERGY.get(), level) {
        setSpawnPositionFromOwner(owner)
    }

    override val baseDamage: Float = 12.0f
    override val projectileGravity: Double = 0.03
    override val airInertia: Double = 1.01
    override val waterInertia: Double = 1.0

    override fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
        spawnSnowPattern(serverLevel, target.position())
    }

    override fun onBlockHit(serverLevel: ServerLevel, pos: Vec3) {
        spawnSnowPattern(serverLevel, pos)
    }

    private fun spawnSnowPattern(serverLevel: ServerLevel, center: Vec3) {
        val random = serverLevel.random
        val pattern = choosePattern(random)
        pattern.forEach { offset ->
            serverLevel.addFreshEntity(
                SnowEnergy(
                    serverLevel,
                    owner as? LivingEntity,
                    center.x + offset.x,
                    center.y + randomSpawnHeight(random),
                    center.z + offset.z,
                ),
            )
        }
    }

    companion object {
        private val SNOW_PATTERNS: List<WeightedPattern> = listOf(
            WeightedPattern(2, pattern1()),
            WeightedPattern(2, pattern2()),
            WeightedPattern(1, pattern3()),
            WeightedPattern(2, pattern4()),
            WeightedPattern(1, pattern5()),
            WeightedPattern(1, pattern6()),
        )

        private fun choosePattern(random: RandomSource): List<Vec3> {
            val totalWeight = SNOW_PATTERNS.sumOf { it.weight }
            var roll = random.nextInt(totalWeight)
            for (pattern in SNOW_PATTERNS) {
                roll -= pattern.weight
                if (roll < 0) {
                    return pattern.offsets
                }
            }
            return SNOW_PATTERNS.first().offsets
        }

        private fun randomSpawnHeight(random: RandomSource): Double =
            when (random.nextInt(19)) {
                in 0..3 -> 3.0
                in 4..8 -> 4.0
                in 9..12 -> 5.0
                in 13..16 -> 6.0
                else -> 7.0
            }

        private fun pattern1(): List<Vec3> = listOf(
            Vec3(0.0, 0.0, 0.0),
            Vec3(1.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 1.0),
            Vec3(-1.0, 0.0, 0.0),
            Vec3(0.0, 0.0, -1.0),
            Vec3(1.0, 0.0, 1.0),
            Vec3(-1.0, 0.0, -1.0),
            Vec3(1.0, 0.0, -1.0),
            Vec3(-1.0, 0.0, 1.0),
            Vec3(2.0, 0.0, 2.0),
            Vec3(3.0, 0.0, 3.0),
            Vec3(-2.0, 0.0, 2.0),
            Vec3(-3.0, 0.0, 3.0),
            Vec3(2.0, 0.0, -2.0),
            Vec3(3.0, 0.0, -3.0),
            Vec3(-2.0, 0.0, -2.0),
            Vec3(-3.0, 0.0, -3.0),
            Vec3(3.0, 0.0, 0.0),
            Vec3(5.0, 0.0, 0.0),
            Vec3(-3.0, 0.0, 0.0),
            Vec3(-5.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 3.0),
            Vec3(0.0, 0.0, 5.0),
            Vec3(0.0, 0.0, -3.0),
            Vec3(0.0, 0.0, -5.0),
        )

        private fun pattern2(): List<Vec3> = listOf(
            Vec3(1.0, 0.0, 0.0),
            Vec3(2.0, 0.0, 0.0),
            Vec3(4.0, 0.0, 0.0),
            Vec3(-1.0, 0.0, 0.0),
            Vec3(-2.0, 0.0, 0.0),
            Vec3(-4.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 1.0),
            Vec3(0.0, 0.0, 2.0),
            Vec3(0.0, 0.0, 4.0),
            Vec3(0.0, 0.0, -1.0),
            Vec3(0.0, 0.0, -2.0),
            Vec3(0.0, 0.0, -4.0),
            Vec3(3.0, 0.0, 1.0),
            Vec3(3.0, 0.0, -1.0),
            Vec3(-3.0, 0.0, 1.0),
            Vec3(-3.0, 0.0, -1.0),
            Vec3(1.0, 0.0, 3.0),
            Vec3(-1.0, 0.0, 3.0),
            Vec3(1.0, 0.0, -3.0),
            Vec3(-1.0, 0.0, -3.0),
            Vec3(3.0, 0.0, 3.0),
            Vec3(-3.0, 0.0, 3.0),
            Vec3(3.0, 0.0, -3.0),
            Vec3(-3.0, 0.0, -3.0),
        )

        private fun pattern3(): List<Vec3> = listOf(
            Vec3(0.0, 0.0, 0.0),
            Vec3(3.0, 0.0, 0.0),
            Vec3(-3.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 3.0),
            Vec3(0.0, 0.0, -3.0),
            Vec3(2.0, 0.0, 1.0),
            Vec3(2.0, 0.0, -1.0),
            Vec3(-2.0, 0.0, 1.0),
            Vec3(-2.0, 0.0, -1.0),
            Vec3(1.0, 0.0, 2.0),
            Vec3(1.0, 0.0, -2.0),
            Vec3(-1.0, 0.0, 2.0),
            Vec3(-1.0, 0.0, -2.0),
            Vec3(2.0, 0.0, 3.0),
            Vec3(2.0, 0.0, -3.0),
            Vec3(-2.0, 0.0, 3.0),
            Vec3(-2.0, 0.0, -3.0),
            Vec3(3.0, 0.0, 2.0),
            Vec3(3.0, 0.0, -2.0),
            Vec3(-3.0, 0.0, 2.0),
            Vec3(-3.0, 0.0, -2.0),
            Vec3(3.0, 0.0, 3.0),
            Vec3(-3.0, 0.0, 3.0),
            Vec3(3.0, 0.0, -3.0),
            Vec3(-3.0, 0.0, -3.0),
            Vec3(4.0, 0.0, 4.0),
            Vec3(-4.0, 0.0, 4.0),
            Vec3(4.0, 0.0, -4.0),
            Vec3(-4.0, 0.0, -4.0),
        )

        private fun pattern4(): List<Vec3> = listOf(
            Vec3(0.0, 0.0, 0.0),
            Vec3(1.0, 0.0, 1.0),
            Vec3(-1.0, 0.0, 1.0),
            Vec3(1.0, 0.0, -1.0),
            Vec3(-1.0, 0.0, -1.0),
            Vec3(2.0, 0.0, 2.0),
            Vec3(2.0, 0.0, -2.0),
            Vec3(-2.0, 0.0, 2.0),
            Vec3(-2.0, 0.0, -2.0),
            Vec3(3.0, 0.0, 0.0),
            Vec3(-3.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 3.0),
            Vec3(0.0, 0.0, -3.0),
        )

        private fun pattern5(): List<Vec3> = listOf(
            Vec3(1.0, 0.0, 0.0),
            Vec3(-1.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 1.0),
            Vec3(0.0, 0.0, -1.0),
            Vec3(3.0, 0.0, 0.0),
            Vec3(-3.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 3.0),
            Vec3(0.0, 0.0, -3.0),
            Vec3(2.0, 0.0, 2.0),
            Vec3(-2.0, 0.0, 2.0),
            Vec3(2.0, 0.0, -2.0),
            Vec3(-2.0, 0.0, -2.0),
            Vec3(5.0, 0.0, 1.0),
            Vec3(5.0, 0.0, -1.0),
            Vec3(-5.0, 0.0, 1.0),
            Vec3(-5.0, 0.0, -1.0),
            Vec3(1.0, 0.0, 5.0),
            Vec3(1.0, 0.0, -5.0),
            Vec3(-1.0, 0.0, 5.0),
            Vec3(-1.0, 0.0, -5.0),
            Vec3(3.0, 0.0, 4.0),
            Vec3(3.0, 0.0, -4.0),
            Vec3(-3.0, 0.0, 4.0),
            Vec3(-3.0, 0.0, -4.0),
            Vec3(4.0, 0.0, 3.0),
            Vec3(4.0, 0.0, -3.0),
            Vec3(-4.0, 0.0, 3.0),
            Vec3(-4.0, 0.0, -3.0),
        )

        private fun pattern6(): List<Vec3> = listOf(
            Vec3(0.0, 0.0, 0.0),
            Vec3(1.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 1.0),
            Vec3(-1.0, 0.0, 0.0),
            Vec3(0.0, 0.0, -1.0),
            Vec3(1.0, 0.0, 1.0),
            Vec3(-1.0, 0.0, -1.0),
            Vec3(1.0, 0.0, -1.0),
            Vec3(-1.0, 0.0, 1.0),
            Vec3(2.0, 0.0, 0.0),
            Vec3(-2.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 2.0),
            Vec3(0.0, 0.0, -2.0),
            Vec3(5.0, 0.0, 0.0),
            Vec3(-5.0, 0.0, 0.0),
            Vec3(0.0, 0.0, 5.0),
            Vec3(0.0, 0.0, -5.0),
            Vec3(4.0, 0.0, 1.0),
            Vec3(4.0, 0.0, -1.0),
            Vec3(-4.0, 0.0, 1.0),
            Vec3(-4.0, 0.0, -1.0),
            Vec3(1.0, 0.0, 4.0),
            Vec3(1.0, 0.0, -4.0),
            Vec3(-1.0, 0.0, 4.0),
            Vec3(-1.0, 0.0, -4.0),
            Vec3(3.0, 0.0, 2.0),
            Vec3(3.0, 0.0, -2.0),
            Vec3(-3.0, 0.0, 2.0),
            Vec3(-3.0, 0.0, -2.0),
            Vec3(2.0, 0.0, 3.0),
            Vec3(2.0, 0.0, -3.0),
            Vec3(-2.0, 0.0, 3.0),
            Vec3(-2.0, 0.0, -3.0),
            Vec3(4.0, 0.0, 3.0),
            Vec3(4.0, 0.0, -3.0),
            Vec3(-4.0, 0.0, 3.0),
            Vec3(-4.0, 0.0, -3.0),
            Vec3(3.0, 0.0, 4.0),
            Vec3(3.0, 0.0, -4.0),
            Vec3(-3.0, 0.0, 4.0),
            Vec3(-3.0, 0.0, -4.0),
            Vec3(4.0, 0.0, 4.0),
            Vec3(4.0, 0.0, -4.0),
            Vec3(-4.0, 0.0, 4.0),
            Vec3(-4.0, 0.0, -4.0),
        )

        private data class WeightedPattern(val weight: Int, val offsets: List<Vec3>)
    }
}
