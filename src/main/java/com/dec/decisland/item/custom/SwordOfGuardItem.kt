package com.dec.decisland.item.custom

import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class SwordOfGuardItem(properties: Properties) : MagicWeapon(properties) {
    override fun judge(level: Level, player: Player): Boolean {
        if (ManaManager.getCurrentMana(player) <= MANA_COST) {
            return false
        }
        ManaManager.reduceMana(player, MANA_COST)
        return true
    }

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        source.addEffect(MobEffectInstance(MobEffects.RESISTANCE, RESISTANCE_DURATION_TICKS, 0))
    }

    companion object {
        private const val MANA_COST: Float = 1.0f
        private const val RESISTANCE_DURATION_TICKS = 100
    }
}