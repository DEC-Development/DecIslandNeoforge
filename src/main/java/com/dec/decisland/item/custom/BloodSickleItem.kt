package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.events.AccessoryCombatEffects
import com.dec.decisland.mana.ManaManager
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUseAnimation
import net.minecraft.world.level.Level
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class BloodSickleItem(
    properties: Properties,
    config: SickleConfig,
) : SickleItem(properties, config) {
    // Prevents channel state changes from looking like a re-equip and interrupting the use animation.
    override fun shouldCauseReequipAnimation(oldStack: ItemStack, newStack: ItemStack, slotChanged: Boolean): Boolean =
        slotChanged || !newStack.`is`(oldStack.item)

    // Starts the blood-drain channel when the player right clicks with enough mana.
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        // Let the client enter the using state immediately so minor mana sync delays do not
        // cause the hand animation to pop in and out while the server validates the channel.
        if (level.isClientSide) {
            player.startUsingItem(hand)
            return InteractionResult.CONSUME
        }

        if (!canUseActiveSkill(player, MANA_COST)) {
            return InteractionResult.FAIL
        }

        val stack = player.getItemInHand(hand)
        channelStates[player.uuid] = ChannelState(
            nextPulseTick = level.gameTime + CHANNEL_INTERVAL_TICKS,
            nextParticleTick = level.gameTime,
            lastTargetId = null,
        )
        player.awardStat(Stats.ITEM_USED.get(this))
        AccessoryCombatEffects.onSuccessfulWeaponUse(player, stack)

        player.startUsingItem(hand)
        return InteractionResult.CONSUME
    }

    // Pulses the life-drain effect every fixed interval while also keeping the particle loop alive.
    override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration)
        val player = livingEntity as? Player ?: return
        val serverLevel = level as? ServerLevel ?: return
        val gameTime = serverLevel.gameTime
        val state = channelStates[player.uuid] ?: ChannelState(
            nextPulseTick = gameTime + CHANNEL_INTERVAL_TICKS,
            nextParticleTick = gameTime,
            lastTargetId = null,
        ).also { channelStates[player.uuid] = it }

        if (gameTime >= state.nextParticleTick) {
            spawnParticle(serverLevel, BLOOD_BALL_PARTICLE_ID, player.position(), PARTICLE_DURATION_TICKS)
            state.nextParticleTick = gameTime + PARTICLE_INTERVAL_TICKS
        }

        if (gameTime < state.nextPulseTick) {
            return
        }
        if (!canUseActiveSkill(player, MANA_COST)) {
            channelStates.remove(player.uuid)
            player.stopUsingItem()
            return
        }

        val candidates = findActiveSkillCandidates(serverLevel, player, MIN_RADIUS, MAX_RADIUS)
        if (candidates.isNotEmpty()) {
            val pool = if (candidates.size > 1) {
                candidates.filterNot { it.uuid == state.lastTargetId }.ifEmpty { candidates }
            } else {
                candidates
            }
            val target = pool[player.random.nextInt(pool.size)]
            state.lastTargetId = target.uuid
            dealExtraDamage(serverLevel, player, target, DAMAGE_PER_PULSE)
            spawnParticle(serverLevel, BLOOD_SEEP_PARTICLE_ID, target.position(), PARTICLE_DURATION_TICKS)
            player.heal(HEAL_PER_PULSE)
            if (player.random.nextBoolean()) {
                stack.hurtAndBreak(1, player, player.usedItemHand.asEquipmentSlot())
            }
        }

        ManaManager.reduceMana(player, MANA_COST)
        state.nextPulseTick = gameTime + CHANNEL_INTERVAL_TICKS
    }

    // Clears the blood-drain channel state when the player releases right click.
    override fun releaseUsing(stack: ItemStack, level: Level, livingEntity: LivingEntity, timeCharged: Int): Boolean {
        if (livingEntity is Player) {
            channelStates.remove(livingEntity.uuid)
        }
        return super.releaseUsing(stack, level, livingEntity, timeCharged)
    }

    // Keeps the blood-drain channel alive while the player holds right click.
    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = CHANNELED_USE_DURATION

    // Shows a holding pose during the blood-drain channel.
    override fun getUseAnimation(stack: ItemStack): ItemUseAnimation = ItemUseAnimation.BLOCK

    companion object {
        private const val CHANNELED_USE_DURATION: Int = 72000
        private const val CHANNEL_INTERVAL_TICKS: Int = 10
        private const val PARTICLE_INTERVAL_TICKS: Int = 5
        private const val PARTICLE_DURATION_TICKS: Int = 12
        private const val MANA_COST: Float = 1.0f
        private const val DAMAGE_PER_PULSE: Float = 2.0f
        private const val HEAL_PER_PULSE: Float = 1.0f
        private const val MIN_RADIUS: Double = 2.0
        private const val MAX_RADIUS: Double = 4.0

        private data class ChannelState(
            var nextPulseTick: Long,
            var nextParticleTick: Long,
            var lastTargetId: UUID?,
        )

        private val channelStates: MutableMap<UUID, ChannelState> = ConcurrentHashMap()

        private val BLOOD_BALL_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "blood_spore_ball_particle")
        private val BLOOD_SEEP_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "blood_spore_seep_particle")
    }
}
