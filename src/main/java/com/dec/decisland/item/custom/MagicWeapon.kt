package com.dec.decisland.item.custom

import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level

open class MagicWeapon(properties: Properties) : Item(properties) {
    protected open fun getMaxAttackCount(): Int = 7

    protected open fun getResetTimeMs(): Long = 5000

    open fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity) {
    }

    open fun judge(level: Level, player: Player): Boolean = true

    fun shootTrigger(stack: ItemStack, serverLevel: ServerLevel, player: Player) {
        val customData = stack.get(DataComponents.CUSTOM_DATA)
        val tag = customData?.copyTag() ?: CompoundTag()
        val currentTime = System.currentTimeMillis()

        val shouldReset = if (tag.contains(LAST_ATTACK_TIME_KEY)) {
            currentTime - tag.getLong(LAST_ATTACK_TIME_KEY).get() > getResetTimeMs()
        } else {
            false
        }

        var attackCount = if (shouldReset) 0 else if (tag.contains(ATTACK_COUNTER_KEY)) {
            tag.getInt(ATTACK_COUNTER_KEY).get()
        } else {
            0
        }

        attackCount++
        tag.putInt(ATTACK_COUNTER_KEY, attackCount)
        tag.putLong(LAST_ATTACK_TIME_KEY, currentTime)
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag))

        shoot(attackCount, serverLevel, player)

        if (attackCount >= getMaxAttackCount()) {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
        }
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (level is ServerLevel && judge(level, player)) {
            shootTrigger(player.getItemInHand(hand), level, player)
            return InteractionResult.SUCCESS
        }
        return InteractionResult.PASS
    }

    companion object {
        protected const val ATTACK_COUNTER_KEY: String = "AttackCounter"
        protected const val LAST_ATTACK_TIME_KEY: String = "LastAttackTime"
    }
}
