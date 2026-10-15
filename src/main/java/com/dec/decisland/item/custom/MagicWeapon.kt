package com.dec.decisland.item.custom

import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource

open class MagicWeapon(properties: Properties) : Item(properties) {
    protected open fun getMaxAttackCount(): Int = 7

    protected open fun getResetTimeMs(): Long = 5000

    protected open fun getCastSound(): SoundEvent = SoundEvents.AMETHYST_BLOCK_BREAK

    protected open fun getCastSoundVolume(): Float = 1.0f

    protected open fun getCastSoundPitch(): Float = 1.0f

    protected open fun getCooldownTicks(): Int = 0

    open fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
    }

    open fun judge(level: Level, player: Player): Boolean = true

    fun shootTrigger(stack: ItemStack, serverLevel: ServerLevel, player: Player) {
        val customData = stack.get(DataComponents.CUSTOM_DATA)
        val tag = customData?.copyTag() ?: CompoundTag()
        val currentTime = System.currentTimeMillis()

        val shouldReset = if (tag.contains(LAST_ATTACK_TIME_KEY)) {
            currentTime - tag.getLong(LAST_ATTACK_TIME_KEY) > getResetTimeMs()
        } else {
            false
        }

        var attackCount = if (shouldReset) 0 else if (tag.contains(ATTACK_COUNTER_KEY)) {
            tag.getInt(ATTACK_COUNTER_KEY)
        } else {
            0
        }

        attackCount++
        tag.putInt(ATTACK_COUNTER_KEY, attackCount)
        tag.putLong(LAST_ATTACK_TIME_KEY, currentTime)
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag))

        shoot(attackCount, serverLevel, player, stack)

        if (attackCount >= getMaxAttackCount()) {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
        }
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (player.cooldowns.isOnCooldown(stack.item)) {
            return InteractionResultHolder.fail(stack)
        }
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack)
        }

        val serverLevel = level as? ServerLevel ?: return InteractionResultHolder.pass(stack)
        if (!judge(serverLevel, player)) {
            return InteractionResultHolder.fail(stack)
        }

        shootTrigger(player.getItemInHand(hand), serverLevel, player)
        getCooldownTicks().takeIf { it > 0 }?.let { player.cooldowns.addCooldown(stack.item, it) }
        serverLevel.playSound(
            null,
            player.x,
            player.y,
            player.z,
            getCastSound(),
            SoundSource.PLAYERS,
            getCastSoundVolume(),
            getCastSoundPitch(),
        )
        player.swing(hand, true)
        return InteractionResultHolder.consume(stack)
    }

    companion object {
        protected const val ATTACK_COUNTER_KEY: String = "AttackCounter"
        protected const val LAST_ATTACK_TIME_KEY: String = "LastAttackTime"
    }
}
