package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.GrowingEnergyRay
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class GrowthItem(properties: Properties) : MagicWeapon(properties) {
    override fun judge(level: Level, player: Player): Boolean {
        if (ManaManager.getCurrentMana(player) <= USE_MANA_COST) {
            return false
        }
        ManaManager.reduceMana(player, USE_MANA_COST)
        return true
    }

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        spawnRay(serverLevel, source, stack, USE_LAUNCH_POWER)
    }

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val serverLevel = attacker.level() as? ServerLevel ?: return
        val player = attacker as? Player ?: return
        if (ManaManager.getCurrentMana(player) <= ATTACK_MANA_COST) {
            return
        }
        ManaManager.reduceMana(player, ATTACK_MANA_COST)
        serverLevel.playSound(null, attacker.x, attacker.y, attacker.z, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS)
        spawnRay(serverLevel, attacker, stack, ATTACK_LAUNCH_POWER)
    }

    private fun spawnRay(serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack, velocity: Float) {
        val ray = GrowingEnergyRay(serverLevel, source, stack)
        ray.shootFromRotation(source, source.xRot, source.yRot, 0.0f, velocity, 0.0f)
        serverLevel.addFreshEntity(ray)
    }

    companion object {
        private const val USE_MANA_COST: Float = 5.0f
        private const val ATTACK_MANA_COST: Float = 4.0f
        private const val USE_LAUNCH_POWER: Float = 0.7f
        private const val ATTACK_LAUNCH_POWER: Float = 0.2f
    }
}