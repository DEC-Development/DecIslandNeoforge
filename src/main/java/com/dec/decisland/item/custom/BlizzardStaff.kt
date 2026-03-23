package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.BlizzardEnergy
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack

class BlizzardStaff(properties: Properties) : BedrockProjectileStaff(properties) {
    override val manaCost: Float = 7.0f

    override fun getCastSound(): SoundEvent = SoundEvents.SNOW_BREAK

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source !is Player) {
            return
        }

        val view = source.getViewVector(0.0f)
        repeat(PROJECTILE_COUNT) {
            Projectile.spawnProjectileUsingShoot(
                ::BlizzardEnergy,
                serverLevel,
                stack,
                source,
                view.x,
                view.y,
                view.z,
                0.02f,
                30.0f,
            )
        }

        ManaManager.reduceMana(source, manaCost)
    }

    companion object {
        private const val PROJECTILE_COUNT: Int = 7
    }
}
