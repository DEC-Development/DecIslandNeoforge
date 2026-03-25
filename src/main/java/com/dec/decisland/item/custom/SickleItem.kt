package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.events.AccessoryCombatEffects
import com.dec.decisland.mana.ManaManager
import com.dec.decisland.network.Networking
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUseAnimation
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

open class SickleItem(
    properties: Properties,
    protected val config: SickleConfig,
) : Item(properties) {
    private val movementSpeedModifierId: Identifier =
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "movement_speed/${config.name}")

    // Handles right click for both instant skills and channeled skills.
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val activeSkill = config.activeSkill ?: return InteractionResult.PASS
        if (activeSkill.channelIntervalTicks > 0) {
            if (!canUseActiveSkill(player, activeSkill.manaCost)) {
                return InteractionResult.FAIL
            }

            if (!level.isClientSide) {
                initializeChannelState(
                    player.getItemInHand(hand),
                    level.gameTime,
                    activeSkill.channelIntervalTicks,
                    activeSkill.particleIntervalTicks,
                )
                player.awardStat(Stats.ITEM_USED.get(this))
                AccessoryCombatEffects.onSuccessfulWeaponUse(player, player.getItemInHand(hand))
            }
            player.startUsingItem(hand)
            return InteractionResult.CONSUME
        }

        val serverLevel = level as? ServerLevel ?: return InteractionResult.FAIL
        if (!canUseActiveSkill(player, activeSkill.manaCost)) {
            return InteractionResult.FAIL
        }

        val stack = player.getItemInHand(hand)
        spawnParticle(serverLevel, activeSkill.casterParticleId, player.position(), activeSkill.particleDurationTicks)
        val hitCount = applyActiveSkill(serverLevel, player, stack, activeSkill, selectRandomTarget = false)
        ManaManager.reduceMana(player, activeSkill.manaCost)
        if (hitCount > 0) {
            activeSkill.selfEffectsOnHit.forEach { player.addEffect(it.toMobEffectInstance()) }
        }

        player.awardStat(Stats.ITEM_USED.get(this))
        AccessoryCombatEffects.onSuccessfulWeaponUse(player, stack)
        player.swing(hand, true)
        return InteractionResult.SUCCESS_SERVER
    }

    // Ticks a channeled active skill using server game time so pulse spacing stays stable.
    override fun onUseTick(level: Level, livingEntity: LivingEntity, stack: ItemStack, remainingUseDuration: Int) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration)
        val activeSkill = config.activeSkill ?: return
        if (activeSkill.channelIntervalTicks <= 0 || level !is ServerLevel) {
            return
        }

        val player = livingEntity as? Player ?: return
        val gameTime = level.gameTime

        if (gameTime >= getNextParticleTick(stack, gameTime)) {
            spawnParticle(level, activeSkill.casterParticleId, player.position(), activeSkill.particleDurationTicks)
            setNextParticleTick(stack, gameTime + activeSkill.particleIntervalTicks)
        }

        if (gameTime < getNextPulseTick(stack, gameTime + activeSkill.channelIntervalTicks)) {
            return
        }
        if (!canUseActiveSkill(player, activeSkill.manaCost)) {
            clearChannelState(stack)
            player.stopUsingItem()
            return
        }

        val hitCount = applyActiveSkill(level, player, stack, activeSkill, selectRandomTarget = activeSkill.randomTargetPerPulse)
        ManaManager.reduceMana(player, activeSkill.manaCost)
        if (hitCount > 0) {
            val healAmount = (hitCount * activeSkill.healPerTarget).coerceAtMost(activeSkill.maxHealAmount)
            if (healAmount > 0.0f) {
                player.heal(healAmount)
            }
            activeSkill.selfEffectsOnHit.forEach { player.addEffect(it.toMobEffectInstance()) }
        }
        setNextPulseTick(stack, gameTime + activeSkill.channelIntervalTicks)
    }

    // Clears channel timers when the player releases right click.
    override fun releaseUsing(stack: ItemStack, level: Level, livingEntity: LivingEntity, timeCharged: Int): Boolean {
        clearChannelState(stack)
        return super.releaseUsing(stack, level, livingEntity, timeCharged)
    }

    // Gives channeled sickles a long use duration so holding right click can continue.
    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int =
        if ((config.activeSkill?.channelIntervalTicks ?: 0) > 0) CHANNELED_USE_DURATION else super.getUseDuration(stack, entity)

    // Uses a blocking pose for channeled sickles so the player has visible feedback while channeling.
    override fun getUseAnimation(stack: ItemStack): ItemUseAnimation =
        if ((config.activeSkill?.channelIntervalTicks ?: 0) > 0) ItemUseAnimation.BLOCK else super.getUseAnimation(stack)

    // Applies the passive extra damage and proc effects on melee hit.
    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if (attacker.level().isClientSide) {
            return
        }

        val serverLevel = attacker.level() as? ServerLevel ?: return
        applyPassiveEffect(serverLevel, attacker, target, config.passiveSkill.baseExtraDamage, emptyList())
        config.passiveSkill.procs.forEach { proc ->
            if (attacker.random.nextInt(proc.chanceDenominator) == 0) {
                applyPassiveProc(serverLevel, attacker, target, proc)
            }
        }
    }

    // Keeps hand-held movement modifiers in sync while the sickle is equipped.
    override fun inventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        super.inventoryTick(stack, level, entity, slot)
        updateMovementSpeedModifier(entity, slot, config.movementSpeedAddition, movementSpeedModifierId)
    }

    // Finds active-skill targets and applies effects either to all targets or one random target.
    // Finds active-skill targets and applies effects either to all targets or one random target.
    protected fun applyActiveSkill(
        serverLevel: ServerLevel,
        source: Player,
        stack: ItemStack,
        activeSkill: ActiveSkillConfig,
        selectRandomTarget: Boolean,
    ): Int {
        val candidates = findActiveSkillCandidates(serverLevel, source, activeSkill.minRadius, activeSkill.maxRadius)

        val targets = if (selectRandomTarget && candidates.isNotEmpty()) {
            listOf(selectRandomTarget(candidates, source, stack))
        } else {
            candidates
        }

        var hitCount = 0
        targets.forEach { target ->
            if (activeSkill.extraDamage > 0.0f) {
                dealExtraDamage(serverLevel, source, target, activeSkill.extraDamage)
            }
            activeSkill.targetEffects.forEach { target.addEffect(it.toMobEffectInstance()) }
            spawnParticle(serverLevel, activeSkill.targetParticleId, target.position(), activeSkill.particleDurationTicks)
            hitCount++
        }
        return hitCount
    }

    // Finds all valid living targets inside the supplied annulus around the player.
    protected fun findActiveSkillCandidates(
        serverLevel: ServerLevel,
        source: Player,
        minRadius: Double,
        maxRadius: Double,
    ): List<LivingEntity> {
        val center = source.position()
        val maxDistanceSquared = maxRadius * maxRadius
        val minDistanceSquared = minRadius * minRadius
        val searchBox = AABB(center, center).inflate(maxRadius)
        return serverLevel.getEntitiesOfClass(LivingEntity::class.java, searchBox)
            .filter { target ->
                val distanceSquared = target.position().distanceToSqr(center)
                distanceSquared >= minDistanceSquared &&
                    distanceSquared <= maxDistanceSquared &&
                    target !== source &&
                    !source.isAlliedTo(target) &&
                    !(target is ArmorStand && target.isMarker)
            }
    }

    // Picks one random target for a pulse and avoids repeating the previous target when alternatives exist.
    protected fun selectRandomTarget(candidates: List<LivingEntity>, source: Player, stack: ItemStack): LivingEntity {
        if (candidates.size == 1) {
            rememberLastRandomTarget(stack, candidates.first())
            return candidates.first()
        }

        val lastTargetId = getLastRandomTargetId(stack)
        val pool = candidates.filterNot { it.uuid.toString() == lastTargetId }
        val chosenPool = if (pool.isNotEmpty()) pool else candidates
        val chosen = chosenPool[source.random.nextInt(chosenPool.size)]
        rememberLastRandomTarget(stack, chosen)
        return chosen
    }

    // Applies unconditional passive damage and debuffs.
    private fun applyPassiveEffect(
        serverLevel: ServerLevel,
        attacker: LivingEntity,
        target: LivingEntity,
        extraDamage: Float,
        effects: List<EffectConfig>,
    ) {
        if (extraDamage > 0.0f) {
            dealExtraDamage(serverLevel, attacker, target, extraDamage)
        }
        effects.forEach { target.addEffect(it.toMobEffectInstance()) }
    }

    // Applies a passive proc and emits the corresponding holder/target particles.
    private fun applyPassiveProc(
        serverLevel: ServerLevel,
        attacker: LivingEntity,
        target: LivingEntity,
        proc: PassiveProcConfig,
    ) {
        applyPassiveEffect(serverLevel, attacker, target, proc.extraDamage, proc.effects)
        spawnHolderFrontParticle(serverLevel, proc.holderParticleId, attacker, proc.particleDurationTicks)
        spawnParticle(serverLevel, proc.targetParticleId, target.position(), proc.particleDurationTicks)
    }

    // Deals bonus damage while bypassing the target's current hurt cooldown.
    protected fun dealExtraDamage(serverLevel: ServerLevel, attacker: LivingEntity, target: LivingEntity, damage: Float): Boolean {
        if (damage <= 0.0f) {
            return false
        }

        val originalInvulnerableTime = target.invulnerableTime
        target.invulnerableTime = 0
        val damageSource = if (attacker is Player) {
            serverLevel.damageSources().playerAttack(attacker)
        } else {
            serverLevel.damageSources().mobAttack(attacker)
        }
        val hurt = target.hurtServer(serverLevel, damageSource, damage)
        if (!hurt) {
            target.invulnerableTime = originalInvulnerableTime
        }
        return hurt
    }

    // Spawns a bedrock emitter at the supplied world position.
    protected fun spawnParticle(serverLevel: ServerLevel, particleId: Identifier?, position: Vec3, durationTicks: Int) {
        if (particleId == null) {
            return
        }
        Networking.sendBedrockEmitterToNearby(serverLevel, particleId, position, 64.0, durationTicks)
    }

    // Spawns swing particles one block in front of the attacker, matching Bedrock's `^^^1` behavior.
    protected fun spawnHolderFrontParticle(
        serverLevel: ServerLevel,
        particleId: Identifier?,
        attacker: LivingEntity,
        durationTicks: Int,
    ) {
        if (particleId == null) {
            return
        }

        val view = attacker.getViewVector(0.0f)
        val position = Vec3(
            attacker.x + view.x,
            attacker.eyeY + view.y - HELD_SWING_PARTICLE_Y_OFFSET,
            attacker.z + view.z,
        )
        Networking.sendBedrockEmitterToNearby(serverLevel, particleId, position, 64.0, durationTicks)
    }

    // Checks whether the player has enough mana to spend one active-skill pulse.
    protected fun canUseActiveSkill(player: Player, manaCost: Float): Boolean =
        ManaManager.getCurrentMana(player) > manaCost

    // Initializes the first channel pulse and particle timestamps when a player starts holding right click.
    protected fun initializeChannelState(
        stack: ItemStack,
        gameTime: Long,
        pulseIntervalTicks: Int,
        particleIntervalTicks: Int,
    ) {
        val tag = readTag(stack)
        tag.putLong(NEXT_PULSE_TICK_KEY, gameTime + pulseIntervalTicks)
        tag.putLong(NEXT_PARTICLE_TICK_KEY, gameTime)
        tag.putInt(PARTICLE_INTERVAL_TICKS_KEY, particleIntervalTicks)
        writeTag(stack, tag)
    }

    // Reads the next pulse timestamp, or falls back to the supplied default if the stack has no state yet.
    protected fun getNextPulseTick(stack: ItemStack, defaultValue: Long): Long {
        val tag = readTag(stack)
        return if (tag.contains(NEXT_PULSE_TICK_KEY)) tag.getLong(NEXT_PULSE_TICK_KEY).get() else defaultValue
    }

    // Reads the next particle timestamp, or falls back to the supplied default if the stack has no state yet.
    protected fun getNextParticleTick(stack: ItemStack, defaultValue: Long): Long {
        val tag = readTag(stack)
        return if (tag.contains(NEXT_PARTICLE_TICK_KEY)) tag.getLong(NEXT_PARTICLE_TICK_KEY).get() else defaultValue
    }

    // Stores the next server tick when the channeled skill may deal damage again.
    protected fun setNextPulseTick(stack: ItemStack, nextTick: Long) {
        val tag = readTag(stack)
        tag.putLong(NEXT_PULSE_TICK_KEY, nextTick)
        writeTag(stack, tag)
    }

    // Stores the next server tick when the channeling particle should be emitted again.
    protected fun setNextParticleTick(stack: ItemStack, nextTick: Long) {
        val tag = readTag(stack)
        tag.putLong(NEXT_PARTICLE_TICK_KEY, nextTick)
        writeTag(stack, tag)
    }

    // Reads the configured particle interval for the current channel session.
    protected fun getParticleIntervalTicks(stack: ItemStack, defaultValue: Int): Int {
        val tag = readTag(stack)
        return if (tag.contains(PARTICLE_INTERVAL_TICKS_KEY)) tag.getInt(PARTICLE_INTERVAL_TICKS_KEY).get() else defaultValue
    }

    // Clears all temporary channeling state from the stack.
    protected fun clearChannelState(stack: ItemStack) {
        val tag = readTag(stack)
        tag.remove(NEXT_PULSE_TICK_KEY)
        tag.remove(NEXT_PARTICLE_TICK_KEY)
        tag.remove(PARTICLE_INTERVAL_TICKS_KEY)
        tag.remove(LAST_RANDOM_TARGET_KEY)
        writeTag(stack, tag)
    }

    // Reads the UUID of the previous random target, if one was stored on the stack.
    protected fun getLastRandomTargetId(stack: ItemStack): String? {
        val tag = readTag(stack)
        return if (tag.contains(LAST_RANDOM_TARGET_KEY)) tag.getString(LAST_RANDOM_TARGET_KEY).orElse(null) else null
    }

    // Stores the UUID of the most recent random target so the next pulse can avoid repeating it.
    protected fun rememberLastRandomTarget(stack: ItemStack, target: LivingEntity) {
        val tag = readTag(stack)
        tag.putString(LAST_RANDOM_TARGET_KEY, target.uuid.toString())
        writeTag(stack, tag)
    }

    // Reads the stack's custom data into a mutable tag.
    protected fun readTag(stack: ItemStack): CompoundTag = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()

    // Writes the stack's custom data back, removing the component entirely if the tag is empty.
    protected fun writeTag(stack: ItemStack, tag: CompoundTag) {
        stack.set(DataComponents.CUSTOM_DATA, if (tag.isEmpty) CustomData.EMPTY else CustomData.of(tag))
    }

    // Applies or removes the movement-speed modifier depending on whether the sickle is currently held.
    private fun updateMovementSpeedModifier(
        entity: Entity,
        slot: EquipmentSlot?,
        amount: Double,
        modifierId: Identifier,
    ) {
        val player = entity as? Player ?: return
        if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
            removeMovementSpeedModifier(player, modifierId)
            return
        }
        if (amount == 0.0) {
            removeMovementSpeedModifier(player, modifierId)
            return
        }

        val attribute = player.getAttribute(Attributes.MOVEMENT_SPEED) ?: return
        val existing = attribute.getModifier(modifierId)
        if (existing != null && existing.amount == amount) {
            return
        }
        if (existing != null) {
            attribute.removeModifier(modifierId)
        }

        attribute.addTransientModifier(
            AttributeModifier(
                modifierId,
                amount,
                AttributeModifier.Operation.ADD_VALUE,
            ),
        )
    }

    // Removes the transient movement-speed modifier if it is present.
    private fun removeMovementSpeedModifier(player: Player, modifierId: Identifier) {
        val attribute = player.getAttribute(Attributes.MOVEMENT_SPEED) ?: return
        if (attribute.getModifier(modifierId) != null) {
            attribute.removeModifier(modifierId)
        }
    }

    class SickleConfig private constructor(builder: Builder) {
        @JvmField
        val name: String = builder.name

        @JvmField
        val passiveSkill: PassiveSkillConfig = builder.passiveSkill

        @JvmField
        val activeSkill: ActiveSkillConfig? = builder.activeSkill

        @JvmField
        val movementSpeedAddition: Double = builder.movementSpeedAddition

        class Builder(@JvmField val name: String) {
            internal var passiveSkill: PassiveSkillConfig = PassiveSkillConfig.Builder().build()
            internal var activeSkill: ActiveSkillConfig? = null
            internal var movementSpeedAddition: Double = 0.0

            // Sets the passive behavior for this sickle.
            fun passiveSkill(passiveSkill: PassiveSkillConfig): Builder = apply {
                this.passiveSkill = passiveSkill
            }

            // Sets the optional active skill for this sickle.
            fun activeSkill(activeSkill: ActiveSkillConfig?): Builder = apply {
                this.activeSkill = activeSkill
            }

            // Sets the movement-speed modifier while the sickle is held.
            fun movementSpeedAddition(movementSpeedAddition: Double): Builder = apply {
                this.movementSpeedAddition = movementSpeedAddition
            }

            // Builds an immutable sickle config.
            fun build(): SickleConfig = SickleConfig(this)
        }
    }

    class PassiveSkillConfig private constructor(builder: Builder) {
        @JvmField
        val baseExtraDamage: Float = builder.baseExtraDamage

        @JvmField
        val procs: List<PassiveProcConfig> = builder.procs.toList()

        class Builder {
            internal var baseExtraDamage: Float = 0.0f
            internal val procs: MutableList<PassiveProcConfig> = mutableListOf()

            // Sets the unconditional extra damage on every hit.
            fun baseExtraDamage(baseExtraDamage: Float): Builder = apply {
                this.baseExtraDamage = baseExtraDamage
            }

            // Adds one probabilistic passive proc.
            fun proc(proc: PassiveProcConfig): Builder = apply {
                procs += proc
            }

            // Builds an immutable passive-skill config.
            fun build(): PassiveSkillConfig = PassiveSkillConfig(this)
        }
    }

    class PassiveProcConfig private constructor(builder: Builder) {
        @JvmField
        val chanceDenominator: Int = builder.chanceDenominator

        @JvmField
        val extraDamage: Float = builder.extraDamage

        @JvmField
        val effects: List<EffectConfig> = builder.effects.toList()

        @JvmField
        val holderParticleId: Identifier? = builder.holderParticleId

        @JvmField
        val targetParticleId: Identifier? = builder.targetParticleId

        @JvmField
        val particleDurationTicks: Int = builder.particleDurationTicks

        class Builder(@JvmField val chanceDenominator: Int) {
            internal var extraDamage: Float = 0.0f
            internal val effects: MutableList<EffectConfig> = mutableListOf()
            internal var holderParticleId: Identifier? = null
            internal var targetParticleId: Identifier? = null
            internal var particleDurationTicks: Int = 6

            // Sets the extra damage dealt when this proc succeeds.
            fun extraDamage(extraDamage: Float): Builder = apply {
                this.extraDamage = extraDamage
            }

            // Adds a status effect applied when this proc succeeds.
            fun effect(effect: EffectConfig): Builder = apply {
                effects += effect
            }

            // Sets the particle emitted in front of the attacker when this proc succeeds.
            fun holderParticleId(holderParticleId: Identifier?): Builder = apply {
                this.holderParticleId = holderParticleId
            }

            // Sets the particle emitted on the target when this proc succeeds.
            fun targetParticleId(targetParticleId: Identifier?): Builder = apply {
                this.targetParticleId = targetParticleId
            }

            // Sets how long the proc particles should stay alive.
            fun particleDurationTicks(particleDurationTicks: Int): Builder = apply {
                this.particleDurationTicks = particleDurationTicks
            }

            // Builds an immutable passive proc config.
            fun build(): PassiveProcConfig = PassiveProcConfig(this)
        }
    }

    class ActiveSkillConfig private constructor(builder: Builder) {
        @JvmField
        val manaCost: Float = builder.manaCost

        @JvmField
        val minRadius: Double = builder.minRadius

        @JvmField
        val maxRadius: Double = builder.maxRadius

        @JvmField
        val extraDamage: Float = builder.extraDamage

        @JvmField
        val targetEffects: List<EffectConfig> = builder.targetEffects.toList()

        @JvmField
        val selfEffectsOnHit: List<EffectConfig> = builder.selfEffectsOnHit.toList()

        @JvmField
        val casterParticleId: Identifier? = builder.casterParticleId

        @JvmField
        val targetParticleId: Identifier? = builder.targetParticleId

        @JvmField
        val particleDurationTicks: Int = builder.particleDurationTicks

        @JvmField
        val channelIntervalTicks: Int = builder.channelIntervalTicks

        @JvmField
        val particleIntervalTicks: Int = builder.particleIntervalTicks

        @JvmField
        val randomTargetPerPulse: Boolean = builder.randomTargetPerPulse

        @JvmField
        val healPerTarget: Float = builder.healPerTarget

        @JvmField
        val maxHealAmount: Float = builder.maxHealAmount

        class Builder {
            internal var manaCost: Float = 0.0f
            internal var minRadius: Double = 0.0
            internal var maxRadius: Double = 0.0
            internal var extraDamage: Float = 0.0f
            internal val targetEffects: MutableList<EffectConfig> = mutableListOf()
            internal val selfEffectsOnHit: MutableList<EffectConfig> = mutableListOf()
            internal var casterParticleId: Identifier? = null
            internal var targetParticleId: Identifier? = null
            internal var particleDurationTicks: Int = 6
            internal var channelIntervalTicks: Int = 0
            internal var particleIntervalTicks: Int = 1
            internal var randomTargetPerPulse: Boolean = false
            internal var healPerTarget: Float = 0.0f
            internal var maxHealAmount: Float = Float.MAX_VALUE

            // Sets the mana consumed by one skill activation or one channel pulse.
            fun manaCost(manaCost: Float): Builder = apply {
                this.manaCost = manaCost
            }

            // Sets the minimum radius of the active skill.
            fun minRadius(minRadius: Double): Builder = apply {
                this.minRadius = minRadius
            }

            // Sets the maximum radius of the active skill.
            fun maxRadius(maxRadius: Double): Builder = apply {
                this.maxRadius = maxRadius
            }

            // Sets the active skill's bonus damage.
            fun extraDamage(extraDamage: Float): Builder = apply {
                this.extraDamage = extraDamage
            }

            // Adds a status effect to targets hit by the active skill.
            fun targetEffect(effect: EffectConfig): Builder = apply {
                targetEffects += effect
            }

            // Adds a self-applied effect that triggers after the active skill hits something.
            fun selfEffectOnHit(effect: EffectConfig): Builder = apply {
                selfEffectsOnHit += effect
            }

            // Sets the particle emitted on the user while the active skill fires.
            fun casterParticleId(casterParticleId: Identifier?): Builder = apply {
                this.casterParticleId = casterParticleId
            }

            // Sets the particle emitted on each target hit by the active skill.
            fun targetParticleId(targetParticleId: Identifier?): Builder = apply {
                this.targetParticleId = targetParticleId
            }

            // Sets how long active-skill particles should stay alive.
            fun particleDurationTicks(particleDurationTicks: Int): Builder = apply {
                this.particleDurationTicks = particleDurationTicks
            }

            // Enables channel mode and defines how often the channel should pulse.
            fun channelIntervalTicks(channelIntervalTicks: Int): Builder = apply {
                this.channelIntervalTicks = channelIntervalTicks
            }

            // Defines how often the channeling particle should be emitted.
            fun particleIntervalTicks(particleIntervalTicks: Int): Builder = apply {
                this.particleIntervalTicks = particleIntervalTicks
            }

            // Makes each channel pulse pick a single random target instead of all valid targets.
            fun randomTargetPerPulse(randomTargetPerPulse: Boolean): Builder = apply {
                this.randomTargetPerPulse = randomTargetPerPulse
            }

            // Sets how much health is restored for each target hit by the active skill.
            fun healPerTarget(healPerTarget: Float): Builder = apply {
                this.healPerTarget = healPerTarget
            }

            // Caps the total healing returned by one active-skill activation or one channel pulse.
            fun maxHealAmount(maxHealAmount: Float): Builder = apply {
                this.maxHealAmount = maxHealAmount
            }

            // Builds an immutable active-skill config.
            fun build(): ActiveSkillConfig = ActiveSkillConfig(this)
        }
    }

    class EffectConfig private constructor(builder: Builder) {
        @JvmField
        val effect: Holder<MobEffect> = builder.effect

        @JvmField
        val durationTicks: Int = builder.durationTicks

        @JvmField
        val amplifier: Int = builder.amplifier

        // Materializes the stored effect config into a vanilla effect instance.
        fun toMobEffectInstance(): MobEffectInstance = MobEffectInstance(effect, durationTicks, amplifier)

        class Builder(@JvmField val effect: Holder<MobEffect>) {
            internal var durationTicks: Int = 0
            internal var amplifier: Int = 0

            // Sets the effect duration directly in ticks.
            fun durationTicks(durationTicks: Int): Builder = apply {
                this.durationTicks = durationTicks
            }

            // Sets the effect duration in seconds.
            fun durationSeconds(durationSeconds: Int): Builder = apply {
                durationTicks = durationSeconds * 20
            }

            // Sets the effect amplifier.
            fun amplifier(amplifier: Int): Builder = apply {
                this.amplifier = amplifier
            }

            // Builds an immutable effect config.
            fun build(): EffectConfig = EffectConfig(this)
        }
    }

    companion object {
        private const val HELD_SWING_PARTICLE_Y_OFFSET: Double = 1.7
        private const val CHANNELED_USE_DURATION: Int = 72000
        private const val NEXT_PULSE_TICK_KEY: String = "SickleNextPulseTick"
        private const val NEXT_PARTICLE_TICK_KEY: String = "SickleNextParticleTick"
        private const val PARTICLE_INTERVAL_TICKS_KEY: String = "SickleParticleIntervalTicks"
        private const val LAST_RANDOM_TARGET_KEY: String = "SickleLastRandomTarget"
    }
}
