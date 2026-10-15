package com.dec.decisland.item.custom

import com.dec.decisland.item.compat.asEquipmentSlot
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/** Shared adapter for Bedrock items whose projectile already exists in DecIsland. */
class BedrockConfiguredProjectileStaff(
    properties: Properties,
    override val manaCost: Float,
    private val projectileFactory: (Level, LivingEntity, ItemStack) -> Projectile,
    private val launchPowers: List<Float> = listOf(1.0f),
    private val inaccuracy: Float = 2.0f,
    private val durabilityCost: Int = 1,
    private val cooldownTicks: Int = 0,
) : ProjectileStaff(properties) {
    override fun getCooldownTicks(): Int = cooldownTicks
    override fun judge(level: Level, player: Player): Boolean =
        manaCost <= 0.0f || ManaManager.getCurrentMana(player) > manaCost

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source !is Player) return
        launchPowers.forEach { launchPower ->
            val projectile = projectileFactory(serverLevel, source, stack)
            projectile.shootFromRotation(source, source.xRot, source.yRot, 0.0f, launchPower, inaccuracy)
            serverLevel.addFreshEntity(projectile)
        }
        ManaManager.reduceMana(source, manaCost)
        stack.hurtAndBreak(durabilityCost, source, source.usedItemHand?.asEquipmentSlot() ?: net.minecraft.world.entity.EquipmentSlot.MAINHAND)
    }
}
