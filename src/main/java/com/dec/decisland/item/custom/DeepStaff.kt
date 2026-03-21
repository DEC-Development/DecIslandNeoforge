package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.DeepEnergy
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class DeepStaff(properties: Properties) : BedrockProjectileStaff(properties) {
    override val manaCost: Float = 7.0f

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player && spawnProjectile(serverLevel, source, stack, ::DeepEnergy, 1.9f, 10.0f)) {
            ManaManager.reduceMana(source, manaCost)
        }
    }
}
