package com.dec.decisland.item.custom

import com.dec.decisland.entity.custom.SummonedMobSupport
import com.dec.decisland.item.compat.asEquipmentSlot
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/** Shared Bedrock-style item for items that launch a tameable summon entity. */
class BedrockSummonWeapon(
    properties: Properties,
    private val manaCost: Float,
    private val entityFactory: (ServerLevel) -> Mob,
    private val lifetimeTicks: Int = 200,
    private val cooldownTicks: Int = 0,
) : MagicWeapon(properties) {
    override fun getCooldownTicks(): Int = cooldownTicks
    override fun judge(level: Level, player: Player): Boolean = ManaManager.getCurrentMana(player) > manaCost

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source !is Player) return
        val summon = entityFactory(serverLevel)
        val view = source.getViewVector(0.0f)
        val position = source.eyePosition.add(view.scale(0.9))
        summon.setPos(position.x, position.y, position.z)
        summon.yRot = source.yRot
        summon.yHeadRot = source.yRot
        summon.xRot = source.xRot
        summon.setDeltaMovement(view.scale(0.18))
        SummonedMobSupport.mark(summon, source, lifetimeTicks)
        if (serverLevel.addFreshEntity(summon)) {
            ManaManager.reduceMana(source, manaCost)
            stack.hurtAndBreak(1, source, source.usedItemHand?.asEquipmentSlot() ?: net.minecraft.world.entity.EquipmentSlot.MAINHAND)
        }
    }
}
