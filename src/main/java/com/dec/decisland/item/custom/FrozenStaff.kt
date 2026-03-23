package com.dec.decisland.item.custom

import com.dec.decisland.mana.ManaManager
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level

class FrozenStaff(properties: Properties) : MagicWeapon(properties) {
    override fun shouldCauseReequipAnimation(oldStack: ItemStack, newStack: ItemStack, slotChanged: Boolean): Boolean =
        slotChanged || !newStack.`is`(oldStack.item)

    override fun getCastSound(): SoundEvent = SoundEvents.AMETHYST_BLOCK_BREAK

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.getItemInHand(hand)
        if (level.isClientSide) {
            return if (willFireNextShot(stack)) InteractionResult.CONSUME else InteractionResult.PASS
        }

        val serverLevel = level as? ServerLevel ?: return InteractionResult.PASS
        val currentMana = ManaManager.getCurrentMana(player)
        if (currentMana < CHARGE_MANA_COST) {
            setSkillCount(stack, 0)
            return InteractionResult.FAIL
        }

        var skillCount = getSkillCount(stack)
        if (skillCount >= RESET_THRESHOLD) {
            skillCount = 0
        }

        val nextSkillCount = skillCount + 1
        val shouldFire = nextSkillCount >= FIRE_THRESHOLD

        if (!shouldFire) {
            ManaManager.reduceMana(player, CHARGE_MANA_COST)
            setSkillCount(stack, nextSkillCount)
            return InteractionResult.CONSUME
        }

        if (currentMana < SHOT_MANA_COST) {
            return InteractionResult.FAIL
        }

        if (!spawnSnowball(serverLevel, player)) {
            return InteractionResult.FAIL
        }

        ManaManager.reduceMana(player, SHOT_MANA_COST)
        stack.hurtAndBreak(1, player, hand.asEquipmentSlot())
        playSound(serverLevel, player, getCastSound(), getCastSoundVolume(), getCastSoundPitch())
        player.swing(hand, true)
        setSkillCount(stack, if (nextSkillCount >= RESET_THRESHOLD) 0 else nextSkillCount)
        return InteractionResult.SUCCESS_SERVER
    }

    private fun willFireNextShot(stack: ItemStack): Boolean {
        var skillCount = getSkillCount(stack)
        if (skillCount >= RESET_THRESHOLD) {
            skillCount = 0
        }
        return skillCount + 1 >= FIRE_THRESHOLD
    }

    private fun spawnSnowball(serverLevel: ServerLevel, player: Player): Boolean {
        val projectile = EntityType.SNOWBALL.create(serverLevel, EntitySpawnReason.TRIGGERED) ?: return false
        projectile.setOwner(player)
        val view = player.getViewVector(0.0f)
        val spawnPos = player.eyePosition.add(view.scale(SPAWN_FORWARD_OFFSET))
        projectile.setPos(spawnPos.x, spawnPos.y, spawnPos.z)
        projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0f, SNOWBALL_VELOCITY, SNOWBALL_INACCURACY)
        return serverLevel.addFreshEntity(projectile)
    }

    private fun playSound(serverLevel: ServerLevel, player: Player, sound: SoundEvent, volume: Float, pitch: Float) {
        serverLevel.playSound(
            null,
            player.x,
            player.y,
            player.z,
            sound,
            SoundSource.PLAYERS,
            volume,
            pitch,
        )
    }

    private fun getSkillCount(stack: ItemStack): Int {
        val tag = readTag(stack)
        return if (tag.contains(SKILL_COUNT_KEY)) tag.getInt(SKILL_COUNT_KEY).get() else 0
    }

    private fun setSkillCount(stack: ItemStack, value: Int) {
        val tag = readTag(stack)
        if (value <= 0) {
            tag.remove(SKILL_COUNT_KEY)
        } else {
            tag.putInt(SKILL_COUNT_KEY, value)
        }
        writeTag(stack, tag)
    }

    private fun readTag(stack: ItemStack): CompoundTag = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()

    private fun writeTag(stack: ItemStack, tag: CompoundTag) {
        stack.set(DataComponents.CUSTOM_DATA, if (tag.isEmpty) CustomData.EMPTY else CustomData.of(tag))
    }

    companion object {
        private const val CHARGE_MANA_COST: Float = 1.0f
        private const val SHOT_MANA_COST: Float = 3.0f
        private const val FIRE_THRESHOLD: Int = 4
        private const val RESET_THRESHOLD: Int = 6
        private const val SNOWBALL_VELOCITY: Float = 1.0f
        private const val SNOWBALL_INACCURACY: Float = 0.0f
        private const val SPAWN_FORWARD_OFFSET: Double = 0.8
        private const val SKILL_COUNT_KEY: String = "FrozenStaffSkillCount"
    }
}
