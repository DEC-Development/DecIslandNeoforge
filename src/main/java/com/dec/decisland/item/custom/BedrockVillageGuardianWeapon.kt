package com.dec.decisland.item.custom

import com.dec.decisland.entity.ModEntities
import com.dec.decisland.entity.custom.LeavesGolem
import com.dec.decisland.entity.custom.VillagePortalEntity
import com.dec.decisland.item.compat.asEquipmentSlot
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.npc.AbstractVillager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB

/** Bedrock village_guardian skill and its melee village regeneration effect. */
class BedrockVillageGuardianWeapon(properties: Properties) : MagicWeapon(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (player.cooldowns.isOnCooldown(stack.item)) return InteractionResultHolder.fail(stack)
        if (level.isClientSide) return InteractionResultHolder.success(stack)
        val serverLevel = level as? ServerLevel ?: return InteractionResultHolder.pass(stack)
        val remainingDurability = stack.maxDamage - stack.damageValue
        val validView = player.xRot in -90.0f..-80.0f
        val hasBoss = serverLevel.getEntitiesOfClass(
            LeavesGolem::class.java,
            AABB.ofSize(player.position(), 128.0, 128.0, 128.0),
        ) { it.isAlive && it.distanceToSqr(player) <= 64.0 * 64.0 }.isNotEmpty()
        if (remainingDurability <= 100 || !validView || hasBoss || ManaManager.getCurrentMana(player) <= 19.0f) {
            return InteractionResultHolder.fail(stack)
        }

        val portal = VillagePortalEntity(ModEntities.VILLAGE_PORTAL.get(), serverLevel)
        portal.setPos(player.x, player.y, player.z)
        portal.yRot = player.yRot
        serverLevel.addFreshEntity(portal)
        ManaManager.reduceMana(player, 19.0f)
        stack.hurtAndBreak(70, player, hand.asEquipmentSlot())
        player.cooldowns.addCooldown(stack.item, 1200)
        player.swing(hand, true)
        return InteractionResultHolder.consume(stack)
    }

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        val serverLevel = attacker.level() as? ServerLevel ?: return true
        serverLevel.getEntitiesOfClass(
            AbstractVillager::class.java,
            AABB.ofSize(target.position(), 8.0, 8.0, 8.0),
        ) { it.isAlive && it.distanceToSqr(target) <= 16.0 }.forEach {
            it.addEffect(MobEffectInstance(MobEffects.REGENERATION, 200, 0))
        }
        return true
    }
}
