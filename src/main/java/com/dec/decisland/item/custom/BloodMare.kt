package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level
import kotlin.math.min

class BloodMare(properties: Properties) : Katana(properties) {
    override fun getMaxAttackCount(): Int = 4

    override fun getUseSkillRadius(): Float = 1.3f

    override fun getUseSkillBreakAmount(): Int = 1

    override fun getUseSkillBonusDamage(): Float = 7.0f

    override fun getAttackSkillRadius(): Float = 1.3f

    override fun getAttackSkillBreakAmount(): Int = 1

    override fun getAttackSkillBonusDamage(): Float = 7.0f

    override fun useSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, BLOOD_MARE_PARTICLE_ID, x, y, z)
    }

    override fun attackSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, BLOOD_MARE_PARTICLE_ID, x, y, z)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val hitCount = sweepAttack(
            level,
            player,
            hand,
            getUseSkillRadius(),
            getUseSkillKnockback(),
            getUseSkillBreakAmount(),
            getBaseDamage(player),
            getUseSkillBonusDamage(),
            { lvl, x, y, z -> useSpawnParticle(lvl, x, y, z) },
            { server, source -> useServer(server, source) },
        )
        if (!level.isClientSide) {
            healFromHits(player, hitCount)
        }
        return InteractionResult.SUCCESS
    }

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if (attacker.level().isClientSide) {
            return
        }

        if (attacker.health <= LOW_HEALTH_THRESHOLD) {
            if (attacker.random.nextInt(4) == 0) {
                return
            }

            val hand = if (attacker is Player) attacker.usedItemHand else null
            val hitCount = sweepAttack(
                attacker.level(),
                attacker,
                hand,
                getAttackSkillRadius(),
                getAttackSkillKnockback(),
                getAttackSkillBreakAmount(),
                getBaseDamage(attacker),
                LOW_HEALTH_ATTACK_BONUS_DAMAGE,
                { lvl, x, y, z -> attackSpawnParticle(lvl, x, y, z) },
                { server, source -> attackServer(server, source) },
            )
            healFromHits(attacker, hitCount)
            return
        }

        val customData = stack.get(DataComponents.CUSTOM_DATA)
        val tag = customData?.copyTag() ?: CompoundTag()
        val currentTime = System.currentTimeMillis()

        val shouldReset = if (tag.contains(LAST_ATTACK_TIME_KEY)) {
            currentTime - tag.getLong(LAST_ATTACK_TIME_KEY).get() > getResetTimeMs()
        } else {
            false
        }

        var attackCount = if (shouldReset) 0 else if (tag.contains(ATTACK_COUNTER_KEY)) {
            tag.getInt(ATTACK_COUNTER_KEY).get()
        } else {
            0
        }

        attackCount++
        tag.putInt(ATTACK_COUNTER_KEY, attackCount)
        tag.putLong(LAST_ATTACK_TIME_KEY, currentTime)
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag))

        if (attackCount < getMaxAttackCount()) {
            return
        }

        val hand = if (attacker is Player) attacker.usedItemHand else null
        val hitCount = sweepAttack(
            attacker.level(),
            attacker,
            hand,
            getAttackSkillRadius(),
            getAttackSkillKnockback(),
            getAttackSkillBreakAmount(),
            getBaseDamage(attacker),
            getAttackSkillBonusDamage(),
            { lvl, x, y, z -> attackSpawnParticle(lvl, x, y, z) },
            { server, source -> attackServer(server, source) },
        )
        healFromHits(attacker, hitCount)
        stack.set(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
    }

    private fun healFromHits(source: LivingEntity, hitCount: Int) {
        if (hitCount <= 0) {
            return
        }

        val healAmount = min(hitCount.toFloat(), MAX_HEAL_PER_SWEEP)
        source.heal(healAmount)
    }

    companion object {
        private val BLOOD_MARE_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "blood_mare_particle")
        private const val LOW_HEALTH_THRESHOLD: Float = 2.0f
        private const val LOW_HEALTH_ATTACK_BONUS_DAMAGE: Float = 6.0f
        private const val MAX_HEAL_PER_SWEEP: Float = 4.0f
    }
}
