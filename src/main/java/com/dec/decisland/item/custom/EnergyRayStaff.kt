package com.dec.decisland.item.custom

import com.dec.decisland.attachment.ModAttachments
import com.dec.decisland.entity.projectile.EnergyRay
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level

class EnergyRayStaff(properties: Properties) : MagicWeapon(properties) {
    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity) {
        Projectile.spawnProjectileUsingShoot(
            ::EnergyRay,
            serverLevel,
            source.useItem,
            source,
            source.getViewVector(0.0f).x,
            source.getViewVector(0.0f).y,
            source.getViewVector(0.0f).z,
            1.4f,
            2.0f,
        )
    }

    override fun judge(level: Level, player: Player): Boolean {
        val currentMana = player.getData(ModAttachments.CURRENT_MANA.get())
        return if (currentMana >= 3.0f) {
            ManaManager.reduceMana(player, 3.0f)
            true
        } else {
            false
        }
    }
}
