package com.dec.decisland.item.custom

import com.dec.decisland.particles.ModParticles
import net.minecraft.server.level.ServerLevel

class HardBambooKatana(properties: Properties) : BambooKatana(properties) {
    override fun getMaxAttackCount(): Int = 6

    override fun useSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        serverLevel.sendParticles(ModParticles.HARD_BAMBOO_KATANA_PARTICLE.get(), x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
    }

    override fun attackSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        serverLevel.sendParticles(ModParticles.HARD_BAMBOO_KATANA_PARTICLE.get(), x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
    }
}
