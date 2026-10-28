package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.entity.projectile.BedrockSpecialProjectile
import com.dec.decisland.item.compat.asEquipmentSlot
import com.dec.decisland.mana.ManaManager
import com.dec.decisland.network.Networking
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level

/** Bedrock star_sword: twelve uses of charge, then one six-projectile release. */
class BedrockChargedStarSword(properties: Properties) : MagicWeapon(properties) {
    override fun getMaxAttackCount(): Int = 12

    override fun judge(level: Level, player: Player): Boolean = true

    override fun use(
        level: Level,
        player: Player,
        hand: InteractionHand,
    ): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (player.cooldowns.isOnCooldown(stack.item)) {
            return InteractionResultHolder.fail(stack)
        }

        // Do not let a held right mouse button animate the hand on every
        // charge step.  The client still sends the use packet for PASS; the
        // server processes it and the only swing is the explicit one on the
        // release step below.
        if (level.isClientSide) {
            return InteractionResultHolder.pass(stack)
        }

        val serverLevel = level as? ServerLevel ?: return InteractionResultHolder.pass(stack)
        if (ManaManager.getCurrentMana(player) < REQUIRED_MANA) {
            resetCharge(stack)
            return InteractionResultHolder.fail(stack)
        }

        val releases = readAttackCounter(stack) + 1 >= MAX_CHARGE
        shootTrigger(stack, serverLevel, player)

        if (releases) {
            serverLevel.playSound(
                null,
                player.x,
                player.y,
                player.z,
                SoundEvents.BLAZE_SHOOT,
                SoundSource.PLAYERS,
                1.0f,
                1.0f,
            )
            player.swing(hand, true)
        }

        // Keep the result non-swinging.  The explicit swing above is the
        // single release animation; returning SUCCESS would make the server
        // packet handler swing a second time as well.
        return InteractionResultHolder.consume(stack)
    }

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        val player = source as? Player ?: return
        if (ManaManager.getCurrentMana(player) < REQUIRED_MANA) {
            resetCharge(stack)
            return
        }

        // MagicWeapon has already incremented and stored this counter before
        // calling shoot.  Use that authoritative counter for the stage
        // effects; maintaining a second counter on the stack could drift when
        // the server synchronizes the item between repeated uses.
        val charge = attackCounter.coerceIn(1, MAX_CHARGE)
        when (charge) {
            4, 8 -> emitFirePowering(serverLevel, player)
            MAX_CHARGE -> {
                // Bedrock's star_sword_shoot function emits the same charge
                // burst again immediately before releasing the projectiles.
                emitFirePowering(serverLevel, player)
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
            }
        }
    }

    private fun emitFirePowering(serverLevel: ServerLevel, player: Player) {
        // The Bedrock command runs at the player's feet; fire_powering's own
        // emitter shape supplies the +0.5 Y offset.
        Networking.sendBedrockEmitterToNearby(
            serverLevel,
            FIRE_POWERING_PARTICLE_ID,
            player.position().add(0.0, 0.5, 0.0),
            64.0,
            4,
        )
    }

    private fun readAttackCounter(stack: ItemStack): Int =
        stack.get(DataComponents.CUSTOM_DATA)?.copyTag()?.getInt(ATTACK_COUNTER_KEY) ?: 0

    private fun resetCharge(stack: ItemStack) {
        val tag = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()
        tag.remove(ATTACK_COUNTER_KEY)
        tag.remove(LAST_ATTACK_TIME_KEY)
        stack.set(DataComponents.CUSTOM_DATA, if (tag.isEmpty) CustomData.EMPTY else CustomData.of(tag))
    }

    companion object {
        private const val MAX_CHARGE = 12
        private const val REQUIRED_MANA = 15
        private val FIRE_POWERING_PARTICLE_ID: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "fire_powering_particle")
    }
}
