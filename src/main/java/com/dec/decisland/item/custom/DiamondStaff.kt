package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.StreamEnergyBall
import com.dec.decisland.mana.ManaManager
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level

class DiamondStaff(properties: Properties) : ProjectileStaff(properties) {
    override val manaCost: Float = 6.0f

    override fun judge(level: Level, player: Player): Boolean = ManaManager.getCurrentMana(player) >= manaCost

    override fun shoot(attackCounter: Int, serverLevel: ServerLevel, source: LivingEntity, stack: ItemStack) {
        if (source is Player && spawnProjectile(serverLevel, source, stack, ::StreamEnergyBall, 1.45f, 1.3f)) {
            ManaManager.reduceMana(source, manaCost)
            scheduleFollowUpShot(stack, serverLevel.gameTime + BURST_SHOT_DELAY_TICKS)
        }
    }

    override fun inventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        super.inventoryTick(stack, level, entity, slot)

        if (entity !is Player || !hasPendingFollowUpShot(stack)) {
            return
        }

        if (!isHeldBy(entity, stack) || level.gameTime < getFollowUpShotTime(stack)) {
            if (!isHeldBy(entity, stack)) {
                clearFollowUpShot(stack)
            }
            return
        }

        clearFollowUpShot(stack)
        if (spawnProjectile(level, entity, stack, ::StreamEnergyBall, 1.45f, 1.3f)) {
            level.playSound(
                null,
                entity.x,
                entity.y,
                entity.z,
                getCastSound(),
                SoundSource.PLAYERS,
                getCastSoundVolume(),
                getCastSoundPitch(),
            )
        }
    }

    private fun isHeldBy(player: Player, stack: ItemStack): Boolean = player.mainHandItem === stack || player.offhandItem === stack

    private fun hasPendingFollowUpShot(stack: ItemStack): Boolean = readTag(stack).contains(FOLLOW_UP_SHOT_TIME_KEY)

    private fun getFollowUpShotTime(stack: ItemStack): Long = readTag(stack).getLong(FOLLOW_UP_SHOT_TIME_KEY).get()

    private fun scheduleFollowUpShot(stack: ItemStack, gameTime: Long) {
        val tag = readTag(stack)
        tag.putLong(FOLLOW_UP_SHOT_TIME_KEY, gameTime)
        writeTag(stack, tag)
    }

    private fun clearFollowUpShot(stack: ItemStack) {
        val tag = readTag(stack)
        tag.remove(FOLLOW_UP_SHOT_TIME_KEY)
        writeTag(stack, tag)
    }

    private fun readTag(stack: ItemStack): CompoundTag = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()

    private fun writeTag(stack: ItemStack, tag: CompoundTag) {
        stack.set(DataComponents.CUSTOM_DATA, if (tag.isEmpty) CustomData.EMPTY else CustomData.of(tag))
    }

    companion object {
        private const val BURST_SHOT_DELAY_TICKS: Long = 6L
        private const val FOLLOW_UP_SHOT_TIME_KEY: String = "DiamondFollowUpShotTime"
    }
}
