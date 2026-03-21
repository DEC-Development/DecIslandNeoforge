package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.projectile.NightmareSpore
import com.dec.decisland.mana.ManaManager
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class NightSword(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        val serverLevel = level as? ServerLevel ?: return InteractionResult.FAIL
        val isDay = isDaytime(level)
        val manaCost = if (isDay) DAY_MANA_COST else NIGHT_MANA_COST
        if (ManaManager.getCurrentMana(player) <= manaCost) {
            return InteractionResult.FAIL
        }

        ManaManager.reduceMana(player, manaCost)
        serverLevel.playSound(null, player.x, player.y, player.z, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f)
        if (!isDay) {
            player.addEffect(MobEffectInstance(MobEffects.INVISIBILITY, 3 * 20, 0))
        }

        val powers = if (isDay) DAY_SPORE_POWERS else NIGHT_SPORE_POWERS
        powers.forEach { launchPower ->
            val projectile = NightmareSpore(serverLevel, player, player.getItemInHand(hand))
            projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0f, launchPower * SPORE_BASE_SPEED, SPORE_INACCURACY)
            serverLevel.addFreshEntity(projectile)
        }

        player.getItemInHand(hand).hurtAndBreak(if (isDay) 2 else 1, player, hand.asEquipmentSlot())
        player.swing(hand, true)
        return InteractionResult.SUCCESS_SERVER
    }

    override fun inventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        super.inventoryTick(stack, level, entity, slot)
        val player = entity as? Player ?: return
        if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
            removeMovementSpeedModifier(player)
            return
        }

        val attribute = player.getAttribute(Attributes.MOVEMENT_SPEED) ?: return
        val existing = attribute.getModifier(MOVEMENT_SPEED_MODIFIER_ID)
        if (existing != null && existing.amount == MOVEMENT_SPEED_ADDITION) {
            return
        }
        if (existing != null) {
            attribute.removeModifier(MOVEMENT_SPEED_MODIFIER_ID)
        }
        attribute.addTransientModifier(
            AttributeModifier(
                MOVEMENT_SPEED_MODIFIER_ID,
                MOVEMENT_SPEED_ADDITION,
                AttributeModifier.Operation.ADD_VALUE,
            ),
        )
    }

    private fun removeMovementSpeedModifier(player: Player) {
        val attribute = player.getAttribute(Attributes.MOVEMENT_SPEED) ?: return
        if (attribute.getModifier(MOVEMENT_SPEED_MODIFIER_ID) != null) {
            attribute.removeModifier(MOVEMENT_SPEED_MODIFIER_ID)
        }
    }

    private fun isDaytime(level: Level): Boolean {
        val time = level.dayTime % 24000L
        return time > 6000L && time < 18000L
    }

    companion object {
        private val MOVEMENT_SPEED_MODIFIER_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "movement_speed/night_sword")
        private const val MOVEMENT_SPEED_ADDITION: Double = 0.01
        private const val SPORE_BASE_SPEED: Float = 4.0f
        private const val SPORE_INACCURACY: Float = 70.0f
        private const val DAY_MANA_COST: Float = 25.0f
        private const val NIGHT_MANA_COST: Float = 14.0f
        private val DAY_SPORE_POWERS: List<Float> = listOf(
            0.8f, 0.8f, 0.8f,
            1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            1.2f, 1.2f, 1.2f, 1.2f, 1.2f,
        )
        private val NIGHT_SPORE_POWERS: List<Float> = listOf(
            0.1f, 0.1f, 0.1f,
            0.2f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f,
            0.3f, 0.3f, 0.3f, 0.3f, 0.3f,
        )
    }
}
