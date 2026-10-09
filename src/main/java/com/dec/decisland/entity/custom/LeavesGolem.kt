package com.dec.decisland.entity.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.network.Networking
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerBossEvent
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.BossEvent
import net.minecraft.world.Difficulty
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.animal.IronGolem
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.npc.AbstractVillager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import kotlin.math.min

/**
 * 绿叶精华 Boss（移植自基岩版 dec:leaves_golem）。
 *
 * 战斗循环（对应基岩版 component_groups 事件链）：
 * 1. spawn 阶段 5 秒：无敌 + 极度缓慢（播放 spawn 破土动画）
 * 2. melee 阶段 10 秒：近战追击玩家/村民/铁傀儡
 * 3. roar 阶段 1.2 秒：0.5 秒时咆哮击退周围 4 格内非怪物生物并召唤 1 只绿叶精灵
 *    然后回到 melee 阶段循环
 *
 * - 难度伤害：简单 5 / 普通 7 / 困难 9（基岩版 attack range 5~9）
 * - 咆哮击退伤害：简单 4 / 普通 6 / 困难 8（基岩版固定 6）
 * - 难度血量：简单 150 / 普通 200 / 困难 300
 * - 免疫火焰与摔落伤害，完全抗击退，Boss 血条，玩家击杀经验 100
 */
class LeavesGolem(entityType: EntityType<out IronGolem>, level: Level) : IronGolem(entityType, level) {

    enum class Phase { SPAWN, MELEE, ROAR }

    private val bossEvent = ServerBossEvent(
        Component.translatable("entity.decisland.leaves_golem"),
        BossEvent.BossBarColor.GREEN,
        BossEvent.BossBarOverlay.PROGRESS,
    ).apply {
        setDarkenScreen(false)
        setPlayBossMusic(false)
    }

    var phase: Phase
        get() = Phase.entries[entityData.get(DATA_PHASE).coerceIn(0, Phase.entries.lastIndex)]
        private set(value) {
            entityData.set(DATA_PHASE, value.ordinal)
            entityData.set(DATA_STATE_TICK, tickCount)
        }

    /** 当前阶段开始的 tick（双端同步，用于客户端动画计时） */
    val stateStartTick: Int get() = entityData.get(DATA_STATE_TICK)

    /** 最近一次近战命中 tick（用于客户端攻击动画） */
    val attackStartTick: Int get() = entityData.get(DATA_ATTACK_TICK)

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(DATA_PHASE, Phase.SPAWN.ordinal)
        builder.define(DATA_STATE_TICK, 0)
        builder.define(DATA_ATTACK_TICK, -1000)
    }

    override fun registerGoals() {
        // 完全重写目标：不沿用铁傀儡的「攻击怪物」逻辑（精灵是己方召唤物）
        goalSelector.addGoal(1, GolemMeleeGoal(this, 1.0, true))
        goalSelector.addGoal(2, MoveTowardsTargetGoal(this, 0.9, 32.0f))
        goalSelector.addGoal(6, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(7, LookAtPlayerGoal(this, Player::class.java, 6.0f))
        goalSelector.addGoal(8, RandomLookAroundGoal(this))
        targetSelector.addGoal(1, HurtByTargetGoal(this))
        targetSelector.addGoal(
            2,
            NearestAttackableTargetGoal(
                this, LivingEntity::class.java, 16, true, false,
            ) { entity: LivingEntity ->
                entity is Player || entity is AbstractVillager ||
                    (entity is IronGolem && entity !is LeavesGolem)
            },
        )
    }

    /** 近战仅允许在 MELEE 阶段出手（spawn/roar 阶段站桩） */
    private class GolemMeleeGoal(mob: LeavesGolem, speedModifier: Double, followingTargetEvenIfNotSeen: Boolean) :
        MeleeAttackGoal(mob, speedModifier, followingTargetEvenIfNotSeen) {
        private val golem: LeavesGolem = mob
        override fun canUse(): Boolean = golem.phase == Phase.MELEE && super.canUse()
        override fun canContinueToUse(): Boolean = golem.phase == Phase.MELEE && super.canContinueToUse()
    }

    override fun tick() {
        super.tick()
        if (level().isClientSide) return

        // 基岩版 experience_reward 条件式：仅玩家击杀时掉落经验
        xpReward = if (lastHurtByPlayerTime > 0) 100 else 0

        bossEvent.progress = Mth.clamp(health / maxHealth, 0.0f, 1.0f)

        val elapsed = tickCount - entityData.get(DATA_STATE_TICK)
        when (phase) {
            Phase.SPAWN -> if (elapsed >= SPAWN_TICKS) phase = Phase.MELEE
            Phase.MELEE -> {
                // 有目标时每 10 秒进入咆哮阶段
                if (elapsed >= MELEE_TICKS && target != null) {
                    phase = Phase.ROAR
                    navigation.stop()
                    playSound(SoundEvents.RAVAGER_ROAR, 1.0f, 1.0f)
                }
            }
            Phase.ROAR -> {
                navigation.stop()
                if (elapsed == ROAR_HIT_TICKS) performRoarAttack()
                if (elapsed >= ROAR_TICKS) {
                    summonElf()
                    phase = Phase.MELEE
                }
            }
        }
    }

    /** 咆哮：4 格内所有非怪物生物受到伤害并击退（对应基岩版 knockback_roar） */
    private fun performRoarAttack() {
        val level = level() as? ServerLevel ?: return
        val damage = when (level.difficulty) {
            Difficulty.HARD -> ROAR_DAMAGE_HARD
            Difficulty.NORMAL -> ROAR_DAMAGE_NORMAL
            else -> ROAR_DAMAGE_EASY
        }
        level.getEntities(this, boundingBox.inflate(ROAR_RANGE)) { it is LivingEntity && it !is Monster }
            .forEach { entity ->
                val living = entity as LivingEntity
                living.hurt(damageSources().mobAttack(this), damage)
                val dx = living.x - x
                val dz = living.z - z
                val dist = kotlin.math.sqrt(dx * dx + dz * dz).coerceAtLeast(0.01)
                living.push(
                    dx / dist * ROAR_KNOCKBACK,
                    ROAR_KNOCKBACK_UP,
                    dz / dist * ROAR_KNOCKBACK,
                )
                living.hurtMarked = true
            }
        Networking.sendBedrockEmitterToNearby(
            level,
            LEAVES_ESSENCE_PARTICLE,
            position().add(0.0, 1.5, 0.0),
            64.0,
            10,
        )
    }

    /** 召唤 1 只绿叶精灵（对应基岩版 minecraft:spawn_entity dec:elf_of_leaves） */
    private fun summonElf() {
        val level = level() as? ServerLevel ?: return
        val elf = ModEntities.ELF_OF_LEAVES.get().create(level) ?: return
        val offsetX = (random.nextDouble() - 0.5) * 4.0
        val offsetZ = (random.nextDouble() - 0.5) * 4.0
        elf.moveTo(x + offsetX, y + 1.0, z + offsetZ, random.nextFloat() * 360.0f, 0.0f)
        elf.finalizeSpawn(level, level.getCurrentDifficultyAt(BlockPos.containing(elf.position())), MobSpawnType.MOB_SUMMONED, null)
        level.addFreshEntity(elf)
        Networking.sendBedrockEmitterToNearby(level, LEAVES_ESSENCE_PARTICLE, elf.position(), 64.0, 6)
    }

    override fun doHurtTarget(target: Entity): Boolean {
        // 攻击前按当前世界难度动态刷新攻击伤害
        getAttribute(Attributes.ATTACK_DAMAGE)?.baseValue = when (level().difficulty) {
            Difficulty.HARD -> HARD_DAMAGE
            Difficulty.NORMAL -> NORMAL_DAMAGE
            else -> EASY_DAMAGE
        }
        val result = super.doHurtTarget(target)
        if (result) entityData.set(DATA_ATTACK_TICK, tickCount)
        return result
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        // spawn 阶段无敌（对应基岩版 damage_sensor deals_damage=false）
        if (phase == Phase.SPAWN && !source.`is`(DamageTypes.GENERIC_KILL) && !source.`is`(DamageTypes.FELL_OUT_OF_WORLD)) {
            return false
        }
        return super.hurt(source, amount)
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnType: MobSpawnType,
        spawnData: SpawnGroupData?,
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnType, spawnData)

        // 难度血量差异
        getAttribute(Attributes.MAX_HEALTH)?.baseValue = when (level.difficulty) {
            Difficulty.HARD -> HARD_HEALTH
            Difficulty.NORMAL -> NORMAL_HEALTH
            else -> EASY_HEALTH
        }
        health = maxHealth

        // spawn 阶段：5 秒极度缓慢（播放破土动画期间站桩）
        addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, SPAWN_TICKS, 50, false, false))
        phase = Phase.SPAWN
        return data
    }

    override fun removeWhenFarAway(distanceToClosestPlayer: Double): Boolean = false

    // ---- Boss 血条 ----
    override fun startSeenByPlayer(player: ServerPlayer) {
        super.startSeenByPlayer(player)
        bossEvent.addPlayer(player)
    }

    override fun stopSeenByPlayer(player: ServerPlayer) {
        super.stopSeenByPlayer(player)
        bossEvent.removePlayer(player)
    }

    override fun remove(reason: RemovalReason) {
        super.remove(reason)
        bossEvent.removeAllPlayers()
    }

    // ---- 音效：沿用铁傀儡 ----
    override fun getHurtSound(source: DamageSource): SoundEvent = SoundEvents.IRON_GOLEM_HURT
    override fun getDeathSound(): SoundEvent = SoundEvents.IRON_GOLEM_DEATH

    companion object {
        private val DATA_PHASE: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(LeavesGolem::class.java, EntityDataSerializers.INT)
        private val DATA_STATE_TICK: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(LeavesGolem::class.java, EntityDataSerializers.INT)
        private val DATA_ATTACK_TICK: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(LeavesGolem::class.java, EntityDataSerializers.INT)

        private const val SPAWN_TICKS = 100      // 5 秒
        private const val MELEE_TICKS = 200      // 10 秒
        private const val ROAR_TICKS = 24        // 1.2 秒
        private const val ROAR_HIT_TICKS = 10    // 0.5 秒
        private const val ROAR_RANGE = 4.0
        private const val ROAR_KNOCKBACK = 3.0
        private const val ROAR_KNOCKBACK_UP = 0.6

        private const val EASY_DAMAGE = 5.0
        private const val NORMAL_DAMAGE = 7.0
        private const val HARD_DAMAGE = 9.0
        private const val ROAR_DAMAGE_EASY = 4.0f
        private const val ROAR_DAMAGE_NORMAL = 6.0f
        private const val ROAR_DAMAGE_HARD = 8.0f

        private const val EASY_HEALTH = 150.0
        private const val NORMAL_HEALTH = 200.0
        private const val HARD_HEALTH = 300.0

        @JvmField
        val LEAVES_ESSENCE_PARTICLE: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "leaves_essence_particle")

        @JvmStatic
        fun createGolemAttributes(): AttributeSupplier.Builder = IronGolem.createAttributes()
            .add(Attributes.MAX_HEALTH, NORMAL_HEALTH)
            .add(Attributes.MOVEMENT_SPEED, 0.30)
            .add(Attributes.ATTACK_DAMAGE, NORMAL_DAMAGE)
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
            .add(Attributes.FOLLOW_RANGE, 64.0)
    }
}
