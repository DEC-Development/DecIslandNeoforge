package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.WaveEnergy
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class WaveMagicBook(properties: Properties) : ProjectileStaff(properties) {
    override val manaCost: Float = 9.0f

    override fun getCastSound(): SoundEvent = SoundEvents.CONDUIT_ACTIVATE

    override fun judge(level: Level, player: Player): Boolean = ManaManager.getCurrentMana(player) > manaCost

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source !is Player) {
            return
        }

        var spawned = 0
        repeat(3) {
            if (spawnProjectile(serverLevel, source, stack, ::WaveEnergy, 0.6f, 2.0f)) {
                spawned++
            }
        }

        if (spawned > 0) {
            ManaManager.reduceMana(source, manaCost)
        }
    }
}
