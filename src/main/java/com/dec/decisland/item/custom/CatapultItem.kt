package com.dec.decisland.item.custom

import com.dec.decisland.entity.ModEntities
import com.dec.decisland.entity.projectile.BedrockSpecialProjectile
import com.dec.decisland.item.category.Material
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/** Bedrock catapult: exploding pellets have priority over small stones. */
class CatapultItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(usedHand)
        if (player.cooldowns.isOnCooldown(stack.item)) return InteractionResultHolder.fail(stack)
        if (level.isClientSide) return InteractionResultHolder.success(stack)
        val serverLevel = level as? ServerLevel ?: return InteractionResultHolder.pass(stack)
        val ammo = findAmmo(player) ?: return InteractionResultHolder.fail(stack)
        val entityType = if (ammo.first == Material.EXPLODING_PELLETS.get()) {
            ModEntities.BULLET_BY_CATAPULT_EXPLODE.get()
        } else {
            ModEntities.BULLET_BY_CATAPULT_NORMAL.get()
        }
        val projectile = BedrockSpecialProjectile.create(serverLevel, player, stack, entityType)
        projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0f, 0.9f, 3.0f)
        serverLevel.addFreshEntity(projectile)
        if (!player.abilities.instabuild) ammo.second.shrink(1)
        stack.hurtAndBreak(1, player, if (usedHand == InteractionHand.MAIN_HAND) net.minecraft.world.entity.EquipmentSlot.MAINHAND else net.minecraft.world.entity.EquipmentSlot.OFFHAND)
        player.cooldowns.addCooldown(stack.item, 30)
        player.swing(usedHand, true)
        return InteractionResultHolder.consume(stack)
    }

    private fun findAmmo(player: Player): Pair<Item, ItemStack>? {
        val exploding = Material.EXPLODING_PELLETS.get()
        player.inventory.items.firstOrNull { !it.isEmpty && it.`is`(exploding) }?.let { return exploding to it }
        val offhand = player.offhandItem
        if (!offhand.isEmpty && offhand.`is`(exploding)) return exploding to offhand
        val smallStone = Material.SMALL_STONE.get()
        player.inventory.items.firstOrNull { !it.isEmpty && it.`is`(smallStone) }?.let { return smallStone to it }
        if (!offhand.isEmpty && offhand.`is`(smallStone)) return smallStone to offhand
        return null
    }
}
