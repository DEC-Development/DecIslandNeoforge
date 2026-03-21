package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.GoldenEnergyBall
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class GoldenStaff(properties: Properties) : BedrockProjectileStaff(properties) {
    override val manaCost: Float = 6.0f

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player && spawnProjectile(serverLevel, source, stack, ::GoldenEnergyBall, 1.3f, 2.0f)) {
            ManaManager.reduceMana(source, manaCost)
        }
    }
}
