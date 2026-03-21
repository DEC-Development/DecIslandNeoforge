package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.WinterEnergy
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class EverlastingWinterStaff(properties: Properties) : BedrockProjectileStaff(properties) {
    override val manaCost: Float = 10.0f

    override fun getCastSound(): SoundEvent = SoundEvents.SNOW_BREAK

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player && spawnProjectile(serverLevel, source, stack, ::WinterEnergy, 1.6f, 2.0f)) {
            ManaManager.reduceMana(source, manaCost)
        }
    }
}
