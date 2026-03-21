package com.dec.decisland.item.custom

import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class ProjectileStaff(properties: Properties) : MagicWeapon(properties) {
    protected abstract val manaCost: Float

    protected fun <T : Projectile> spawnProjectile(
        serverLevel: ServerLevel,
        source: LivingEntity,
        stack: ItemStack,
        projectileFactory: (Level, LivingEntity, ItemStack) -> T,
        velocity: Float,
        inaccuracy: Float,
    ): Boolean {
        val projectile = projectileFactory(serverLevel, source, stack)
        projectile.shootFromRotation(source, source.xRot, source.yRot, 0.0f, velocity, inaccuracy)
        val added = serverLevel.addFreshEntity(projectile)
        if (added) {
            projectile.applyOnProjectileSpawned(serverLevel, stack)
        }
        return added
    }

    override fun judge(level: Level, player: Player): Boolean {
        if (ManaManager.getCurrentMana(player) <= manaCost) {
            return false
        }

        ManaManager.reduceMana(player, manaCost)
        return true
    }
}
