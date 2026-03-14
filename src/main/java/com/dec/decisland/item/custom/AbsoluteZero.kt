package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.BlizzardEnergy
import com.dec.decisland.particles.ModParticles
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import java.util.Random

class AbsoluteZero(properties: Properties) : Katana(properties) {
    override fun getUseSkillRadius(): Float = 2.0f

    override fun getUseSkillBreakAmount(): Int = 5

    override fun getUseSkillKnockback(): Float = 0.4f

    override fun useSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        serverLevel.sendParticles(ModParticles.ABSOLUTE_ZERO_PARTICLE.get(), x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
        serverLevel.sendParticles(ModParticles.ABSOLUTE_ZERO_SMOKE_BIG_PARTICLE.get(), x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
    }

    override fun useServer(serverLevel: ServerLevel, source: LivingEntity) {
        repeat(5) {
            Projectile.spawnProjectileUsingShoot(
                ::BlizzardEnergy,
                serverLevel,
                source.useItem,
                source,
                source.getViewVector(0.0f).x,
                source.getViewVector(0.0f).y,
                source.getViewVector(0.0f).z,
                0.02f,
                30.0f,
            )
        }
    }

    override fun getAttackSkillRadius(): Float = 1.1f

    override fun getAttackSkillBreakAmount(): Int = 3

    override fun getAttackSkillKnockback(): Float = 0.4f

    override fun getUseSkillBonusDamage(): Float = 11.0f

    override fun getAttackSkillBonusDamage(): Float = 5.0f

    override fun attackSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        serverLevel.sendParticles(ModParticles.ABSOLUTE_ZERO_PARTICLE.get(), x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
        serverLevel.sendParticles(ModParticles.ABSOLUTE_ZERO_SMOKE_SMALL_PARTICLE.get(), x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
    }

    override fun attackServer(serverLevel: ServerLevel, source: LivingEntity) {
        repeat(3) {
            Projectile.spawnProjectileUsingShoot(
                ::BlizzardEnergy,
                serverLevel,
                source.useItem,
                source,
                source.getViewVector(0.0f).x,
                source.getViewVector(0.0f).y,
                source.getViewVector(0.0f).z,
                0.02f,
                30.0f,
            )
        }
    }

    override fun onAttackTriggerSweep(stack: ItemStack): Boolean = Random().nextInt(3) == 0
}
