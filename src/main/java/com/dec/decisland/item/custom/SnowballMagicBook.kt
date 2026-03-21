package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.FrozenEnergyBall
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class SnowballMagicBook(properties: Properties) : ProjectileStaff(properties) {
    override val manaCost: Float = 2.0f

    override fun getCastSound(): SoundEvent = SoundEvents.SNOW_BREAK

    override fun judge(level: Level, player: Player): Boolean = ManaManager.getCurrentMana(player) > manaCost

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player && spawnProjectile(serverLevel, source, stack, ::FrozenEnergyBall, 0.8f, 6.0f)) {
            ManaManager.reduceMana(source, manaCost)
        }
    }
}
