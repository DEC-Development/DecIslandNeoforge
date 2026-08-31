package com.dec.decisland.item.custom

import com.dec.decisland.network.Networking
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.sqrt

// Rapier: right click dashes the player forward (Bedrock dec:sprint). Thunder and dead
// wood variants additionally trail collision pulses behind the dash (Bedrock
// dec:sustain_damage), and melee hits build a combo counter that re-triggers the dash.
class RapierItem(
    properties: Properties,
    private val config: RapierConfig,
) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.getItemInHand(hand)
        if (player.cooldowns.isOnCooldown(stack)) {
            return InteractionResult.FAIL
        }

        if (!level.isClientSide) {
            performLunge(level as ServerLevel, player, stack, combo = false)
            player.awardStat(Stats.ITEM_USED.get(this))
        }

        player.swing(hand, true)
        return InteractionResult.SUCCESS_SERVER
    }

    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val player = attacker as? Player
        if (player != null && player.level() is ServerLevel) {
            val tag = readTag(stack)
            val count = tag.getIntOr(TAG_SKILL_COUNT, 0)
            if (count > config.comboThreshold) {
                performLunge(player.level() as ServerLevel, player, stack, combo = true)
            } else {
                tag.putInt(TAG_SKILL_COUNT, count + 1)
                writeTag(stack, tag)
            }
        }
        super.hurtEnemy(stack, target, attacker)
    }

    // Drives the dash window: collision pulses every 4 ticks and the trailing particles.
    override fun inventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        super.inventoryTick(stack, level, entity, slot)
        val player = entity as? Player ?: return
        val tag = readTag(stack)
        if (!tag.contains(TAG_LUNGE_START)) {
            return
        }

        val elapsed = level.gameTime - tag.getLongOr(TAG_LUNGE_START, 0L)
        if (elapsed >= LUNGE_DURATION_TICKS) {
            tag.remove(TAG_LUNGE_START)
            tag.remove(TAG_LUNGE_COMBO)
            writeTag(stack, tag)
            return
        }
        if (elapsed <= 0L) {
            return
        }

        val combo = tag.getBooleanOr(TAG_LUNGE_COMBO, false)
        if (config.pulseDamage > 0.0f && elapsed % PULSE_INTERVAL_TICKS == 0L) {
            dealPulseDamage(level, player, if (combo) config.comboPulseDamage else config.pulseDamage)
        }

        val trailParticleId = config.trailParticleId
        if (trailParticleId != null && elapsed < config.trailDurationTicks) {
            Networking.sendBedrockEmitterToNearby(level, trailParticleId, player.position(), 64.0, 1)
        }
    }

    private fun performLunge(serverLevel: ServerLevel, player: Player, stack: ItemStack, combo: Boolean) {
        val tag = readTag(stack)
        tag.putInt(TAG_SKILL_COUNT, 0)
        tag.putLong(TAG_LUNGE_START, serverLevel.gameTime)
        tag.putBoolean(TAG_LUNGE_COMBO, combo)
        writeTag(stack, tag)

        // Bedrock dec:sprint: horizontal impulse along the view direction.
        val view = player.getViewVector(0.0f)
        val horizontalLength = sqrt(view.x * view.x + view.z * view.z) + 1.0E-6
        val power = (if (combo) config.comboDashPower else config.dashPower) * DASH_SCALE
        player.push(view.x / horizontalLength * power, 0.0, view.z / horizontalLength * power)

        val auraEffects = if (combo) config.comboAuraEffects else config.auraEffects
        applyAura(serverLevel, player, auraEffects)

        // Bedrock sustain_damage fires the first pulse immediately.
        if (config.pulseDamage > 0.0f) {
            dealPulseDamage(serverLevel, player, if (combo) config.comboPulseDamage else config.pulseDamage)
        }
    }

    // Bedrock aura: radius 3 ring with a 1 block inner exclusion around the caster.
    private fun applyAura(serverLevel: ServerLevel, player: Player, effects: List<SickleItem.EffectConfig>) {
        if (effects.isEmpty()) {
            return
        }

        val center = player.position()
        val box = AABB(center, center).inflate(AURA_RADIUS)
        serverLevel.getEntitiesOfClass(LivingEntity::class.java, box)
            .filter { target ->
                target !== player &&
                    target.isAlive &&
                    target !is ArmorStand &&
                    target.distanceTo(player) > AURA_INNER_RADIUS
            }
            .forEach { target -> effects.forEach { effect -> target.addEffect(effect.toMobEffectInstance()) } }
    }

    // Bedrock sustain_damage: collision pulse in radius 2 around the player's current position.
    private fun dealPulseDamage(serverLevel: ServerLevel, player: Player, damage: Float) {
        if (damage <= 0.0f) {
            return
        }

        val source = if (config.magicPulse) {
            serverLevel.damageSources().indirectMagic(player, player)
        } else {
            serverLevel.damageSources().playerAttack(player)
        }
        val center = player.position()
        val box = AABB(center, center).inflate(PULSE_RADIUS)
        serverLevel.getEntitiesOfClass(LivingEntity::class.java, box)
            .filter { target -> target !== player && target.isAlive && target !is ArmorStand }
            .forEach { target ->
                val originalInvulnerableTime = target.invulnerableTime
                target.invulnerableTime = 0
                val hurt = target.hurtServer(serverLevel, source, damage)
                if (!hurt) {
                    target.invulnerableTime = originalInvulnerableTime
                }
            }
    }

    private fun readTag(stack: ItemStack): CompoundTag = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()

    private fun writeTag(stack: ItemStack, tag: CompoundTag) {
        stack.set(DataComponents.CUSTOM_DATA, if (tag.isEmpty) CustomData.EMPTY else CustomData.of(tag))
    }

    class RapierConfig private constructor(builder: Builder) {
        @JvmField
        val dashPower: Float = builder.dashPower

        @JvmField
        val comboDashPower: Float = builder.comboDashPower

        @JvmField
        val pulseDamage: Float = builder.pulseDamage

        @JvmField
        val comboPulseDamage: Float = builder.comboPulseDamage

        @JvmField
        val magicPulse: Boolean = builder.magicPulse

        @JvmField
        val trailParticleId: Identifier? = builder.trailParticleId

        @JvmField
        val trailDurationTicks: Int = builder.trailDurationTicks

        @JvmField
        val auraEffects: List<SickleItem.EffectConfig> = builder.auraEffects.toList()

        @JvmField
        val comboAuraEffects: List<SickleItem.EffectConfig> = builder.comboAuraEffects.toList()

        @JvmField
        val comboThreshold: Int = builder.comboThreshold

        class Builder(
            @JvmField val dashPower: Float,
            @JvmField val comboDashPower: Float,
        ) {
            internal var pulseDamage: Float = 0.0f
            internal var comboPulseDamage: Float = 0.0f
            internal var magicPulse: Boolean = false
            internal var trailParticleId: Identifier? = null
            internal var trailDurationTicks: Int = 0
            internal val auraEffects = mutableListOf<SickleItem.EffectConfig>()
            internal val comboAuraEffects = mutableListOf<SickleItem.EffectConfig>()
            internal var comboThreshold: Int = 4

            fun pulse(skill: Float, combo: Float, magic: Boolean): Builder = apply {
                pulseDamage = skill
                comboPulseDamage = combo
                magicPulse = magic
            }

            fun trailParticle(trailParticleId: Identifier?, durationTicks: Int): Builder = apply {
                this.trailParticleId = trailParticleId
                trailDurationTicks = durationTicks
            }

            fun auraEffect(effect: SickleItem.EffectConfig): Builder = apply {
                auraEffects += effect
            }

            fun comboAuraEffect(effect: SickleItem.EffectConfig): Builder = apply {
                comboAuraEffects += effect
            }

            fun comboThreshold(comboThreshold: Int): Builder = apply {
                this.comboThreshold = comboThreshold
            }

            fun build(): RapierConfig = RapierConfig(this)
        }
    }

    companion object {
        private const val TAG_SKILL_COUNT = "skill_count"
        private const val TAG_LUNGE_START = "lunge_start"
        private const val TAG_LUNGE_COMBO = "lunge_combo"
        private const val LUNGE_DURATION_TICKS = 20L
        private const val PULSE_INTERVAL_TICKS = 4L
        private const val PULSE_RADIUS = 2.0
        private const val AURA_RADIUS = 3.0
        private const val AURA_INNER_RADIUS = 1.0
        private const val DASH_SCALE = 0.35f
    }
}
