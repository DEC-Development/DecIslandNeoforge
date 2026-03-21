package com.dec.decisland.item.custom

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class SwordFish(properties: Properties) : Item(properties) {
    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val damageSource = if (attacker is Player) {
            attacker.damageSources().playerAttack(attacker)
        } else {
            attacker.damageSources().mobAttack(attacker)
        }
        target.hurt(damageSource, 1.0f)
    }
}
