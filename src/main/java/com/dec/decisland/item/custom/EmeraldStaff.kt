package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.PureEnergyBall
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class EmeraldStaff(properties: Properties) : BedrockProjectileStaff(properties) {
    override val manaCost: Float = 8.0f

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player && spawnProjectile(serverLevel, source, stack, ::PureEnergyBall, 1.7f, 2.0f)) {
            ManaManager.reduceMana(source, manaCost)
        }
    }
}
