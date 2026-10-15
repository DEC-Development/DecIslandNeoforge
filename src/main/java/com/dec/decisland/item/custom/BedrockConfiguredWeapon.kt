package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.EnergyBall
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

/** Generic active weapon adapter for Bedrock weapons with a right-click skill. */
class BedrockConfiguredWeapon(
    properties: Properties,
    private val manaCost: Float,
    private val velocity: Float = 1.0f,
    private val inaccuracy: Float = 2.0f,
) : MagicWeapon(properties) {
    override fun judge(level: net.minecraft.world.level.Level, player: Player): Boolean =
        ManaManager.getCurrentMana(player) > manaCost

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player) {
            val projectile = EnergyBall(serverLevel, source, stack)
            projectile.shootFromRotation(source, source.xRot, source.yRot, 0.0f, velocity, inaccuracy)
            if (serverLevel.addFreshEntity(projectile)) {
                ManaManager.reduceMana(source, manaCost)
            }
        }
    }
}
