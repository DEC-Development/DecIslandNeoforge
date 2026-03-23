package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.ConcentratedSoulBullet
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class SoulStaff(properties: Properties) : BedrockProjectileStaff(properties) {
    override val manaCost: Float = 3.0f

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player && spawnProjectile(serverLevel, source, stack, ::ConcentratedSoulBullet, 0.9f, 1.3f)) {
            ManaManager.reduceMana(source, manaCost)
        }
    }
}
