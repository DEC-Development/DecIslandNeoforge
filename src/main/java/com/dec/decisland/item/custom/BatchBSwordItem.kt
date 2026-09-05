package com.dec.decisland.item.custom

import com.dec.decisland.mana.ManaManager
import com.dec.decisland.network.Networking
import com.dec.decisland.DecIsland
import net.minecraft.resources.Identifier
import net.minecraft.core.component.DataComponents
import com.dec.decisland.particles.ModParticles
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUseAnimation
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB

class BatchBSwordItem(properties: Properties, private val kind: Kind) : MagicWeapon(properties) {
    enum class Kind { WINTER, DEEP, VORTEX, SCALE, WARDEN, SHADOW, IMPERIAL }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (kind == Kind.WARDEN) { player.startUsingItem(hand); return InteractionResult.CONSUME }
        if (kind == Kind.SHADOW) return startShadow(level, player, hand)
        if (level.isClientSide) return InteractionResult.SUCCESS
        val server = level as ServerLevel
        if (kind == Kind.IMPERIAL) return imperial(server, player, hand)
        val cost = when (kind) { Kind.WINTER -> 1f; Kind.DEEP -> 4f; Kind.VORTEX -> if (player.isInWaterOrRain) 7f else 11f; Kind.SCALE -> if (player.isInWaterOrRain) 6f else 10f; else -> 0f }
        if (!spend(player, cost)) return InteractionResult.FAIL
        when (kind) {
            Kind.WINTER -> { emitter(server, player, "everlasting_winter_seep_particle"); nearby(server, player, 4.0, 2.0).forEach { it.addEffect(MobEffectInstance(MobEffects.SLOWNESS, 60, 1)) } }
            Kind.DEEP -> { emitter(server, player, "deep_range_particle"); nearby(server, player, 4.0).forEach { it.addEffect(MobEffectInstance(MobEffects.WITHER, 200, 1)) } }
            Kind.VORTEX -> { emitter(server, player, "bubble_vortex_particle"); if (player.isInWaterOrRain) { player.addEffect(MobEffectInstance(MobEffects.CONDUIT_POWER, 100, 0)); areaDamage(server, player, 100, 10f) } else areaDamage(server, player, 40, 7f) }
            Kind.SCALE -> { emitter(server, player, "bubble_vortex_particle"); if (player.isInWaterOrRain) { player.addEffect(MobEffectInstance(MobEffects.CONDUIT_POWER, 200, 0)); areaDamage(server, player, 140, 12f) } else areaDamage(server, player, 60, 8f) }
            else -> Unit
        }
        player.swing(hand, true)
        return InteractionResult.SUCCESS_SERVER
    }

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val player = attacker as? Player ?: return
        val server = attacker.level() as? ServerLevel ?: return
        if (kind == Kind.SCALE && player.isInWaterOrRain && player.random.nextBoolean()) {
            emitter(server, target, "bubble_spurt_middle_particle")
            nearby(server, player, 3.0).forEach { it.hurtServer(server, server.damageSources().drown(), 10f) }
            player.addEffect(MobEffectInstance(MobEffects.CONDUIT_POWER, 200, 0))
        }
    }

    override fun releaseUsing(stack: ItemStack, level: Level, livingEntity: LivingEntity, timeCharged: Int): Boolean {
        val player = livingEntity as? Player ?: return super.releaseUsing(stack, level, livingEntity, timeCharged)
        val server = level as? ServerLevel ?: return super.releaseUsing(stack, level, livingEntity, timeCharged)
        val usedTicks = getUseDuration(stack, player) - timeCharged
        if (kind == Kind.WARDEN && usedTicks >= 30) activateWarden(server, player)
        if (kind == Kind.SHADOW) activateShadow(server, player)
        return super.releaseUsing(stack, level, livingEntity, timeCharged)
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val player = livingEntity as? Player ?: return stack
        val server = level as? ServerLevel ?: return stack
        when (kind) {
            Kind.WARDEN -> activateWarden(server, player)
            Kind.SHADOW -> activateShadow(server, player)
            else -> Unit
        }
        return stack
    }

    override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration)
        if (kind == Kind.SHADOW && !level.isClientSide && remainingUseDuration <= 1) {
            val player = livingEntity as? Player ?: return
            activateShadow(level as ServerLevel, player)
            player.stopUsingItem()
        }
    }

    private fun activateWarden(server: ServerLevel, player: Player) {
        if (!spend(player, 20f)) return
        player.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 200, 0))
        player.addEffect(MobEffectInstance(MobEffects.DARKNESS, 600, 0))
        player.addEffect(MobEffectInstance(MobEffects.STRENGTH, 160, 0))
        player.addEffect(MobEffectInstance(MobEffects.SPEED, 160, 1))
        player.addEffect(MobEffectInstance(MobEffects.RESISTANCE, 160, 1))
        emitter(server, player, "deep_range_particle")
    }

    private fun activateShadow(server: ServerLevel, player: Player) {
        player.removeEffect(MobEffects.INVISIBILITY)
        player.getCooldowns().addCooldown(player.getItemInHand(player.usedItemHand), 80)
        emitter(server, player, "ender_bomb_particle")
        nearby(server, player, 4.0).forEach { it.hurtServer(server, server.damageSources().magic(), 11f) }
    }

    override fun getUseAnimation(stack: ItemStack): ItemUseAnimation = ItemUseAnimation.NONE
    // Keep channeling open so releasing the button always reaches releaseUsing;
    // the required charge time is measured from the remaining duration.
    override fun getUseDuration(stack: ItemStack, entity: LivingEntity) =
        if (kind == Kind.WARDEN) WARDEN_USE_DURATION else if (kind == Kind.SHADOW) SHADOW_USE_DURATION else super.getUseDuration(stack, entity)

    private fun startShadow(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (player.getCooldowns().isOnCooldown(player.getItemInHand(hand))) return InteractionResult.FAIL
        if (level.isClientSide) { player.startUsingItem(hand); return InteractionResult.CONSUME }
        if (!spend(player, 10f)) return InteractionResult.FAIL
        player.addEffect(MobEffectInstance(MobEffects.INVISIBILITY, 60, 0)); player.addEffect(MobEffectInstance(MobEffects.SPEED, 60, 2)); emitter(level as ServerLevel, player, "ender_spurt_particle"); player.startUsingItem(hand)
        return InteractionResult.CONSUME
    }

    private fun imperial(server: ServerLevel, player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.getItemInHand(hand)
        var count = readCount(stack) + 1
        if (count == 8 || count == 16) {
            if (!spend(player, 15f)) return InteractionResult.FAIL
            player.addEffect(MobEffectInstance(MobEffects.STRENGTH, 100, if (count == 8) 1 else 2)); player.addEffect(MobEffectInstance(MobEffects.RESISTANCE, 40, if (count == 8) 2 else 3))
            nearby(server, player, 5.0, 2.0).forEach { it.addEffect(MobEffectInstance(MobEffects.SLOWNESS, if (count == 8) 20 else 40, 255)) }
            server.sendParticles(
                ModParticles.IMPERIAL_TOTEM_PARTICLE.get(),
                player.x, player.y + 1.0, player.z,
                if (count == 8) 30 else 60,
                0.35, 0.6, 0.35, 0.25,
            )
            if (count == 16) count = 0
        }
        writeCount(stack, count); player.swing(hand, true); return InteractionResult.SUCCESS_SERVER
    }

    private fun areaDamage(server: ServerLevel, player: Player, poisonTicks: Int, damage: Float) { nearby(server, player, 3.0).forEach { it.addEffect(MobEffectInstance(MobEffects.POISON, poisonTicks, 0)); it.hurtServer(server, server.damageSources().drown(), damage) } }
    private fun nearby(level: ServerLevel, player: Player, radius: Double, minRadius: Double = 0.0): List<LivingEntity> = level.getEntitiesOfClass(LivingEntity::class.java, AABB.ofSize(player.position(), radius * 2, radius * 2, radius * 2)) { it !== player && it.isAlive && it.distanceToSqr(player) >= minRadius * minRadius }
    private fun spend(player: Player, amount: Float): Boolean { if (ManaManager.getCurrentMana(player) <= amount) return false; ManaManager.reduceMana(player, amount); return true }
    private fun readCount(stack: ItemStack): Int = stack.get(DataComponents.CUSTOM_DATA)?.copyTag()?.getInt("ImperialCount")?.orElse(0) ?: 0
    private fun writeCount(stack: ItemStack, value: Int) { val tag = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag(); if (value == 0) tag.remove("ImperialCount") else tag.putInt("ImperialCount", value); stack.set(DataComponents.CUSTOM_DATA, if (tag.isEmpty) CustomData.EMPTY else CustomData.of(tag)) }
    private fun emitter(level: ServerLevel, entity: LivingEntity, name: String, copies: Int = 1) { repeat(copies) { Networking.sendBedrockEmitterToNearby(level, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, name), entity.position(), 64.0, 6) } }

    companion object {
        private const val WARDEN_USE_DURATION = 30
        private const val SHADOW_USE_DURATION = 60
    }
}
