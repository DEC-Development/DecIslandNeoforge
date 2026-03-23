package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.FrozenRay
import com.dec.decisland.mana.ManaManager
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack

class FrozenRayStaff(properties: Properties) : BedrockProjectileStaff(properties) {
    override val manaCost: Float = 4.0f

    override fun getCastSound(): SoundEvent = SoundEvents.SNOW_BREAK

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player) {
            val view = source.getViewVector(0.0f)
            Projectile.spawnProjectileUsingShoot(
                ::FrozenRay,
                serverLevel,
                stack,
                source,
                view.x,
                view.y,
                view.z,
                0.3f,
                2.0f,
            )
            ManaManager.reduceMana(source, manaCost)
        }
    }
}
