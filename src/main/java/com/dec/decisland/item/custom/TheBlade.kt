package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel

class TheBlade(properties: Properties) : Katana(properties) {
    override fun getMaxAttackCount(): Int = 4

    override fun getUseSkillRadius(): Float = 1.3f

    override fun getUseSkillBreakAmount(): Int = 1

    override fun getUseSkillBonusDamage(): Float = 2.0f

    override fun getAttackSkillRadius(): Float = 1.3f

    override fun getAttackSkillBreakAmount(): Int = 1

    override fun getAttackSkillBonusDamage(): Float = 2.0f

    override fun useSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, THE_BLADE_PARTICLE_ID, x, y, z)
    }

    override fun attackSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, THE_BLADE_PARTICLE_ID, x, y, z)
    }

    companion object {
        private val THE_BLADE_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "the_blade_particle")
    }
}
