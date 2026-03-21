package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.mana.ManaManager
import com.dec.decisland.network.Networking
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class ManaRestoreItem(properties: Properties) : Item(properties) {
    protected abstract val manaRestoreAmount: Float
    protected abstract val useThreshold: Float
    protected open val particleBursts: Int = 1
    protected open val consumeOnSuccessfulUse: Boolean = false
    protected open val damageOnEveryUse: Boolean = false
    protected open val glint: Boolean = false

    override fun isFoil(stack: ItemStack): Boolean = glint || super.isFoil(stack)

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        val serverLevel = level as? ServerLevel ?: return InteractionResult.PASS
        val stack = player.getItemInHand(hand)
        val currentMana = ManaManager.getCurrentMana(player)
        var restored = false

        if (currentMana <= useThreshold) {
            restored = ManaManager.addManaIgnoringMax(player, manaRestoreAmount) > 0.0f
            if (restored) {
                repeat(particleBursts) {
                    Networking.sendBedrockEmitterToNearby(
                        serverLevel,
                        WHITE_STAR_PARTICLE_ID,
                        player.position().add(0.0, 1.0, 0.0),
                        64.0,
                        2,
                    )
                }
                serverLevel.playSound(
                    null,
                    player.x,
                    player.y,
                    player.z,
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.PLAYERS,
                    0.8f,
                    1.15f,
                )
            }
        }

        if (consumeOnSuccessfulUse && restored) {
            stack.shrink(1)
        }

        if (damageOnEveryUse) {
            stack.hurtAndBreak(1, player, hand.asEquipmentSlot())
        }

        if (restored || damageOnEveryUse) {
            player.swing(hand, true)
            return InteractionResult.SUCCESS_SERVER
        }

        return InteractionResult.FAIL
    }

    companion object {
        private val WHITE_STAR_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "white_star_particle")
    }
}
