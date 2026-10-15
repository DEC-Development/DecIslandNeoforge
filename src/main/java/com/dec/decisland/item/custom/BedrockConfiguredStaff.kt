package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.EnergyBall
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

/** Generic Bedrock-style staff adapter used when no dedicated projectile exists yet. */
class BedrockConfiguredStaff(
    properties: Properties,
    override val manaCost: Float,
    private val velocity: Float = 1.0f,
    private val inaccuracy: Float = 2.0f,
    private val cooldownTicks: Int = 0,
) : ProjectileStaff(properties) {
    override fun getCooldownTicks(): Int = cooldownTicks
    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player && spawnProjectile(serverLevel, source, stack, ::EnergyBall, velocity, inaccuracy)) {
            ManaManager.reduceMana(source, manaCost)
        }
    }
}
