package com.dec.decisland.item.custom

import com.dec.decisland.entity.ModEntities
import com.dec.decisland.entity.projectile.BedrockSpecialProjectile
import com.dec.decisland.item.compat.asEquipmentSlot
import com.dec.decisland.mana.ManaManager
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level

/** Bedrock star_sword: twelve uses of charge, then one six-projectile release. */
class BedrockChargedStarSword(properties: Properties) : MagicWeapon(properties) {
    override fun getMaxAttackCount(): Int = 12

    override fun judge(level: Level, player: Player): Boolean = true

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        val player = source as? Player ?: return
        if (ManaManager.getCurrentMana(player) < REQUIRED_MANA) {
            writeCharge(stack, 0)
            return
        }

        val charge = (readCharge(stack) + 1).coerceAtMost(MAX_CHARGE)
        writeCharge(stack, charge)
        when (charge) {
            4, 8 -> serverLevel.sendParticles(
                net.minecraft.core.particles.ParticleTypes.FLAME,
                player.x,
                player.y + 0.5,
                player.z,
                12,
                0.25,
                0.4,
                0.25,
                0.03,
            )
            MAX_CHARGE -> {
                val powers = floatArrayOf(0.2f, 0.4f, 0.5f, 0.9f, 1.4f, 2.0f)
                powers.forEach { power ->
                    val projectile = BedrockSpecialProjectile.create(
                        serverLevel,
                        player,
                        stack,
                        ModEntities.SPOTS_BY_SWORD.get(),
                    )
                    projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0f, power, 1.7f)
                    serverLevel.addFreshEntity(projectile)
                }
                ManaManager.reduceMana(player, REQUIRED_MANA.toFloat())
                stack.hurtAndBreak(1, player, player.usedItemHand?.asEquipmentSlot()
                    ?: net.minecraft.world.entity.EquipmentSlot.MAINHAND)
                writeCharge(stack, 0)
            }
        }
    }

    private fun readCharge(stack: ItemStack): Int =
        stack.get(DataComponents.CUSTOM_DATA)?.copyTag()?.getInt(CHARGE_KEY) ?: 0

    private fun writeCharge(stack: ItemStack, value: Int) {
        val tag = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()
        if (value <= 0) tag.remove(CHARGE_KEY) else tag.putInt(CHARGE_KEY, value)
        stack.set(DataComponents.CUSTOM_DATA, if (tag.isEmpty) CustomData.EMPTY else CustomData.of(tag))
    }

    companion object {
        private const val CHARGE_KEY = "BedrockStarSwordCharge"
        private const val MAX_CHARGE = 12
        private const val REQUIRED_MANA = 15
    }
}
