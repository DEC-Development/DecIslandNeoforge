package com.dec.decisland.item.custom

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class GuideBookItem(properties: Properties) : Item(properties) {
    override fun isFoil(stack: ItemStack): Boolean = true

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        MESSAGE_KEYS.forEach { key ->
            player.displayClientMessage(Component.translatable(key), false)
        }

        return InteractionResult.SUCCESS_SERVER
    }

    companion object {
        private val MESSAGE_KEYS: List<String> = listOf(
            "text.dec:guide_book_0.name",
            "text.dec:guide_book_1.name",
            "text.dec:guide_book_2.name",
            "text.dec:guide_book_3.name",
        )
    }
}
