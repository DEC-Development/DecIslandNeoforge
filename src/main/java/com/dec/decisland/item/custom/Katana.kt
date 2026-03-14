package com.dec.decisland.item.custom

import com.dec.decisland.api.ModItemEventTrigger
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.common.extensions.IItemExtension
import kotlin.math.cos
import kotlin.math.sin

abstract class Katana(properties: Properties) : Item(properties), IItemExtension, ModItemEventTrigger {
    protected open fun getMaxAttackCount(): Int = 7

    protected open fun getResetTimeMs(): Long = 5000

    open fun getUseSkillRadius(): Float = 2.0f

    open fun getUseSkillBreakAmount(): Int = 5

    open fun getUseSkillKnockback(): Float = 0.4f

    open fun useSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
    }

    open fun useServer(serverLevel: ServerLevel, source: LivingEntity) {
    }

    open fun getAttackSkillRadius(): Float = 1.1f

    open fun getAttackSkillBreakAmount(): Int = 3

    open fun getAttackSkillKnockback(): Float = 0.4f

    open fun attackSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
    }

    open fun attackServer(serverLevel: ServerLevel, source: LivingEntity) {
    }

    open fun onAttackTriggerSweep(stack: ItemStack): Boolean = true

    open fun getBaseDamage(entity: LivingEntity): Float = entity.getAttributeValue(Attributes.ATTACK_DAMAGE).toFloat()

    open fun getUseSkillBonusDamage(): Float = 0.0f

    open fun getAttackSkillBonusDamage(): Float = 0.0f

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        sweepAttack(
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
        return InteractionResult.SUCCESS
    }

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
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

        if (!onAttackTriggerSweep(stack)) {
            return
        }

        val hand = if (attacker is Player) attacker.usedItemHand else null
        sweepAttack(
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

        if (attackCount >= getMaxAttackCount()) {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
        }
    }

    fun sweepAttack(
        level: Level,
        source: LivingEntity,
        hand: InteractionHand?,
        radius: Float,
        knockbackStrength: Float,
        breakAmount: Int,
        baseDamage: Float,
        bonusDamage: Float,
        particleAction: ParticleAction?,
        serverAction: ServerAction?,
    ) {
        if (level.isClientSide) {
            source.playSound(SoundEvents.PLAYER_ATTACK_SWEEP)
            return
        }

        if (source is Player && hand != null) {
            val itemStack = source.getItemInHand(hand)
            itemStack.hurtAndBreak(breakAmount, source, hand.asEquipmentSlot())
        }

        val view = source.getViewVector(0.0f)
        val x = source.x + view.x
        val y = source.eyeY + view.y
        val z = source.z + view.z

        if (particleAction != null && level is ServerLevel) {
            particleAction.spawn(level, x, y, z)
        }

        if (level is ServerLevel) {
            serverAction?.execute(level, source)

            val entityReachSq = if (source is Player) {
                Mth.square(source.entityInteractionRange())
            } else {
                Mth.square(4.0)
            }
            val a = radius
            val pointX = source.x + a * view.x
            val pointY = source.y + a * view.y
            val pointZ = source.z + a * view.z
            val aabb = AABB(pointX, pointY, pointZ, pointX, pointY, pointZ).inflate(a.toDouble())
            val entities = level.getEntitiesOfClass(LivingEntity::class.java, aabb)
            val center = Vec3(pointX, pointY, pointZ)

            entities.forEach { livingEntity ->
                if (livingEntity.position().distanceToSqr(center) <= a * a &&
                    livingEntity !== source &&
                    !source.isAlliedTo(livingEntity) &&
                    !(livingEntity is ArmorStand && livingEntity.isMarker) &&
                    source.distanceToSqr(livingEntity) < entityReachSq
                ) {
                    val sweepingDamageRatio = source.getAttribute(Attributes.SWEEPING_DAMAGE_RATIO)?.value?.toFloat() ?: 0.0f
                    val finalDamage = 1.0f + sweepingDamageRatio * baseDamage + bonusDamage
                    var damageSource: DamageSource = level.damageSources().mobAttack(source)
                    if (source is Player) {
                        damageSource = level.damageSources().playerAttack(source)
                    }

                    if (livingEntity.hurtServer(level, damageSource, finalDamage)) {
                        val radians = Math.toRadians(source.yRot.toDouble())
                        livingEntity.knockback(
                            knockbackStrength.toDouble(),
                            sin(radians),
                            -cos(radians),
                        )
                        EnchantmentHelper.doPostAttackEffects(level, livingEntity, damageSource)
                    }
                }
            }
        }
    }

    fun interface ParticleAction {
        fun spawn(serverLevel: ServerLevel, x: Double, y: Double, z: Double)
    }

    fun interface ServerAction {
        fun execute(serverLevel: ServerLevel, source: LivingEntity)
    }

    companion object {
        protected const val ATTACK_COUNTER_KEY: String = "AttackCounter"
        protected const val LAST_ATTACK_TIME_KEY: String = "LastAttackTime"
    }
}
