package com.dec.decisland.item.custom

import com.dec.decisland.entity.ModEntities
import com.dec.decisland.entity.projectile.BedrockSpecialProjectile
import com.dec.decisland.item.compat.asEquipmentSlot
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/** Four Bedrock storm_energy component-group variants in one cast. */
class BedrockStormGiantSword(properties: Properties) : MagicWeapon(properties) {
    override fun getCooldownTicks(): Int = 40
    override fun judge(level: Level, player: Player): Boolean = ManaManager.getCurrentMana(player) > 3.0f

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        val player = source as? Player ?: return
        val variants = arrayOf("a" to 0.08f, "b" to 0.1f, "c" to 0.2f, "d" to 0.3f)
        variants.forEach { (variant, speed) ->
            val projectile = BedrockSpecialProjectile.create(
                serverLevel,
                player,
                stack,
                ModEntities.STORM_ENERGY.get(),
                variant,
            )
            projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0f, speed, 0.0f)
            serverLevel.addFreshEntity(projectile)
        }
        ManaManager.reduceMana(player, 3.0f)
        stack.hurtAndBreak(1, player, player.usedItemHand?.asEquipmentSlot() ?: net.minecraft.world.entity.EquipmentSlot.MAINHAND)
    }
}
