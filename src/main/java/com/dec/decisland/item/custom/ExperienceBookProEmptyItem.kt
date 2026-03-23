package com.dec.decisland.item.custom

import com.dec.decisland.item.ModItems
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ExperienceBookProEmptyItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        if (player.experienceLevel < MIN_LEVEL_REQUIRED) {
            return InteractionResult.FAIL
        }

        val stack = player.getItemInHand(hand)
        stack.consume(1, player)
        giveOrDrop(player, ItemStack(ModItems.EXPERIENCE_BOOK.get()))
        player.giveExperienceLevels(-LEVEL_COST)
        return InteractionResult.SUCCESS_SERVER
    }

    private fun giveOrDrop(player: Player, stack: ItemStack) {
        if (!player.addItem(stack)) {
            player.drop(stack, false)
        }
    }

    companion object {
        private const val MIN_LEVEL_REQUIRED: Int = 2
        private const val LEVEL_COST: Int = 2
    }
}
