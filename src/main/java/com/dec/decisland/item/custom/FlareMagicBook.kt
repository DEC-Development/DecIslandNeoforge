package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.projectile.SpotsByBook
import com.dec.decisland.entity.projectile.SpotsOverflow
import com.dec.decisland.mana.ManaManager
import com.dec.decisland.network.Networking
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level

class FlareMagicBook(properties: Properties) : MagicWeapon(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS
        }

        val serverLevel = level as? ServerLevel ?: return InteractionResult.PASS
        val stack = player.getItemInHand(hand)
        var skillCount = getSkillCount(stack)
        var firedMainProjectile = false

        if (skillCount > MAIN_SHOT_THRESHOLD && ManaManager.getCurrentMana(player) > MANA_COST) {
            if (spawnProjectile(serverLevel, player, stack, ::SpotsByBook, 1.5f, 1.3f)) {
                firedMainProjectile = true
                ManaManager.reduceMana(player, MANA_COST)
                spawnPoweringParticle(serverLevel, player)
                playSound(serverLevel, player, SoundEvents.FIRE_AMBIENT, 0.8f, 1.0f)
                playSound(serverLevel, player, SoundEvents.BLAZE_SHOOT, 0.9f, 1.0f)
                stack.hurtAndBreak(1, player, hand.asEquipmentSlot())
            }
        }

        if (skillCount >= RESET_THRESHOLD) {
            skillCount = 0
        }

        if (skillCount <= MAX_TRACKED_SKILL_COUNT) {
            skillCount += 1
            scheduleOverflowShots(serverLevel, player, stack)
        }

        if (skillCount == CHARGE_STAGE_ONE || skillCount == CHARGE_STAGE_TWO) {
            spawnPoweringParticle(serverLevel, player)
            playSound(serverLevel, player, SoundEvents.FIRE_AMBIENT, 0.8f, 1.0f)
        }

        setSkillCount(stack, skillCount)
        if (firedMainProjectile) {
            player.swing(hand, true)
        }
        return InteractionResult.SUCCESS_SERVER
    }

    override fun inventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        super.inventoryTick(stack, level, entity, slot)
        val player = entity as? Player ?: return
        val pendingShots = getPendingOverflowShots(stack)
        if (pendingShots <= 0 || level.gameTime < getNextOverflowShotTime(stack)) {
            return
        }

        spawnProjectile(level, player, stack, ::SpotsOverflow, 0.7f, 30.0f)
        if (pendingShots == 1) {
            clearPendingOverflowShots(stack)
        } else {
            setPendingOverflowShots(stack, pendingShots - 1, level.gameTime + OVERFLOW_DELAY_TICKS)
        }
    }

    private fun scheduleOverflowShots(serverLevel: ServerLevel, player: Player, stack: ItemStack) {
        when (player.random.nextInt(16)) {
            in 0..3 -> spawnProjectile(serverLevel, player, stack, ::SpotsOverflow, 0.7f, 30.0f)
            in 4..5 -> {
                spawnProjectile(serverLevel, player, stack, ::SpotsOverflow, 0.7f, 30.0f)
                setPendingOverflowShots(stack, 1, serverLevel.gameTime + OVERFLOW_DELAY_TICKS)
            }
            6 -> {
                spawnProjectile(serverLevel, player, stack, ::SpotsOverflow, 0.7f, 30.0f)
                setPendingOverflowShots(stack, 2, serverLevel.gameTime + OVERFLOW_DELAY_TICKS)
            }
        }
    }

    private fun spawnPoweringParticle(serverLevel: ServerLevel, player: Player) {
        Networking.sendBedrockEmitterToNearby(
            serverLevel,
            FIRE_POWERING_PARTICLE_ID,
            player.position().add(0.0, 0.5, 0.0),
            64.0,
            4,
        )
    }

    private fun playSound(serverLevel: ServerLevel, player: Player, sound: net.minecraft.sounds.SoundEvent, volume: Float, pitch: Float) {
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

    private fun <T : Projectile> spawnProjectile(
        serverLevel: ServerLevel,
        source: LivingEntity,
        stack: ItemStack,
        projectileFactory: (Level, LivingEntity, ItemStack) -> T,
        velocity: Float,
        inaccuracy: Float,
    ): Boolean {
        val projectile = projectileFactory(serverLevel, source, stack)
        projectile.shootFromRotation(source, source.xRot, source.yRot, 0.0f, velocity, inaccuracy)
        val added = serverLevel.addFreshEntity(projectile)
        if (added) {
            projectile.applyOnProjectileSpawned(serverLevel, stack)
        }
        return added
    }

    private fun getSkillCount(stack: ItemStack): Int {
        val tag = readTag(stack)
        return if (tag.contains(SKILL_COUNT_KEY)) tag.getInt(SKILL_COUNT_KEY).get() else 0
    }

    private fun setSkillCount(stack: ItemStack, value: Int) {
        val tag = readTag(stack)
        tag.putInt(SKILL_COUNT_KEY, value)
        writeTag(stack, tag)
    }

    private fun getPendingOverflowShots(stack: ItemStack): Int {
        val tag = readTag(stack)
        return if (tag.contains(PENDING_OVERFLOW_SHOTS_KEY)) tag.getInt(PENDING_OVERFLOW_SHOTS_KEY).get() else 0
    }

    private fun getNextOverflowShotTime(stack: ItemStack): Long {
        val tag = readTag(stack)
        return if (tag.contains(NEXT_OVERFLOW_SHOT_TIME_KEY)) tag.getLong(NEXT_OVERFLOW_SHOT_TIME_KEY).get() else 0L
    }

    private fun setPendingOverflowShots(stack: ItemStack, count: Int, nextShotTime: Long) {
        val tag = readTag(stack)
        tag.putInt(PENDING_OVERFLOW_SHOTS_KEY, count)
        tag.putLong(NEXT_OVERFLOW_SHOT_TIME_KEY, nextShotTime)
        writeTag(stack, tag)
    }

    private fun clearPendingOverflowShots(stack: ItemStack) {
        val tag = readTag(stack)
        tag.remove(PENDING_OVERFLOW_SHOTS_KEY)
        tag.remove(NEXT_OVERFLOW_SHOT_TIME_KEY)
        writeTag(stack, tag)
    }

    private fun readTag(stack: ItemStack): CompoundTag = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()

    private fun writeTag(stack: ItemStack, tag: CompoundTag) {
        stack.set(DataComponents.CUSTOM_DATA, if (tag.isEmpty) CustomData.EMPTY else CustomData.of(tag))
    }

    companion object {
        private const val MANA_COST: Float = 6.0f
        private const val MAIN_SHOT_THRESHOLD: Int = 4
        private const val RESET_THRESHOLD: Int = 10
        private const val MAX_TRACKED_SKILL_COUNT: Int = 12
        private const val CHARGE_STAGE_ONE: Int = 2
        private const val CHARGE_STAGE_TWO: Int = 4
        private const val OVERFLOW_DELAY_TICKS: Long = 10L
        private const val SKILL_COUNT_KEY: String = "FlareSkillCount"
        private const val PENDING_OVERFLOW_SHOTS_KEY: String = "FlarePendingOverflowShots"
        private const val NEXT_OVERFLOW_SHOT_TIME_KEY: String = "FlareNextOverflowShotTime"

        private val FIRE_POWERING_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "fire_powering_particle")
    }
}
