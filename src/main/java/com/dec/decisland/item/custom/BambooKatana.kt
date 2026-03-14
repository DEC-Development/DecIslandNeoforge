package com.dec.decisland.item.custom

import com.dec.decisland.particles.ModParticles
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData

open class BambooKatana(properties: Properties) : Katana(properties) {
    override fun getMaxAttackCount(): Int = 7

    override fun getResetTimeMs(): Long = 5000

    override fun getUseSkillRadius(): Float = 1.2f

    override fun getUseSkillBreakAmount(): Int = 1

    override fun useSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        serverLevel.sendParticles(ModParticles.BAMBOO_KATANA_PARTICLE.get(), x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
    }

    override fun getAttackSkillRadius(): Float = 1.2f

    override fun getAttackSkillBreakAmount(): Int = 1

    override fun getUseSkillBonusDamage(): Float = 3.0f

    override fun getAttackSkillBonusDamage(): Float = 3.0f

    override fun attackSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        serverLevel.sendParticles(ModParticles.BAMBOO_KATANA_PARTICLE.get(), x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
    }

    override fun onAttackTriggerSweep(stack: ItemStack): Boolean {
        val customData = stack.get(DataComponents.CUSTOM_DATA)
        val tag = customData?.copyTag() ?: CompoundTag()
        val attackCount = if (tag.contains(ATTACK_COUNTER_KEY)) {
            tag.getInt(ATTACK_COUNTER_KEY).get()
        } else {
            0
        }
        return attackCount == getMaxAttackCount()
    }
}
