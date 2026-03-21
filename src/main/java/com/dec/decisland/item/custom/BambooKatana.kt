package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData

open class BambooKatana(properties: Properties) : Katana(properties) {
    override fun getMaxAttackCount(): Int = 7

    override fun getResetTimeMs(): Long = 5000

    override fun getUseSkillRadius(): Float = 1.2f

    override fun getUseSkillBreakAmount(): Int = 1

    override fun useSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, BAMBOO_KATANA_PARTICLE_ID, x, y, z)
    }

    override fun getAttackSkillRadius(): Float = 1.2f

    override fun getAttackSkillBreakAmount(): Int = 1

    override fun getUseSkillBonusDamage(): Float = 3.0f

    override fun getAttackSkillBonusDamage(): Float = 3.0f

    override fun attackSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, BAMBOO_KATANA_PARTICLE_ID, x, y, z)
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

    companion object {
        private val BAMBOO_KATANA_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bamboo_katana_particle")
    }
}
