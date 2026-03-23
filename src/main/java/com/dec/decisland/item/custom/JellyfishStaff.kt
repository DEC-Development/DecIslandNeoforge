package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.JellyfishStaffProjectile
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class JellyfishStaff(properties: Properties) : BedrockProjectileStaff(properties) {
    override val manaCost: Float = 2.0f

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player && spawnProjectile(serverLevel, source, stack, ::JellyfishStaffProjectile, 0.6f, 20.0f)) {
            ManaManager.reduceMana(source, manaCost)
        }
    }
}
