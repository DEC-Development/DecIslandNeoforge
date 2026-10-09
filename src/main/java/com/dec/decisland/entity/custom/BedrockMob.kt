package com.dec.decisland.entity.custom

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.PanicGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal
import net.minecraft.world.entity.ai.goal.RangedAttackGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation
import net.minecraft.world.entity.animal.IronGolem
import net.minecraft.world.entity.animal.SnowGolem
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.monster.RangedAttackMob
import net.minecraft.world.entity.npc.AbstractVillager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level
import net.minecraft.world.level.pathfinder.PathType
import java.util.function.Supplier
import kotlin.math.sqrt

/**
 * 基岩版生物的通用数据驱动基类（移植自基岩版行为包）。
 *
 * 属性与 AI 均由 [MobConfig] 数据驱动：
 * - 近战：MeleeAttackGoal
 * - 远程：RangedAttackGoal + 已移植的模组弹射物
 * - 目标：玩家 / 村民 / 铁傀儡 / 雪傀儡
 * - 水生：WaterBoundPathNavigation + RandomSwimmingGoal
 * - 火焰免疫：覆写 fireImmune()
 * - 水下呼吸 / 亡灵分类：由 entity_type 标签数据驱动（can_breathe_under_water / undead 等）
 */
open class BedrockMob(
    entityType: EntityType<out BedrockMob>,
    level: Level,
    private val cfg: MobConfig,
) : Monster(entityType, level), RangedAttackMob {

    init {
        xpReward = cfg.xp
        if (cfg.water) {
            setPathfindingMalus(PathType.WATER, 0.0f)
            moveControl = SmoothSwimmingMoveControl(this, 85, 10, 0.5f, 0.1f, true)
            navigation = WaterBoundPathNavigation(this, level)
        }
    }

    override fun fireImmune(): Boolean = cfg.fireImmune || super.fireImmune()

    override fun registerGoals() {
        cfg.buildGoals(this)
    }

    /** 远程攻击：发射配置的模组弹射物（未配置时静默忽略） */
    override fun performRangedAttack(target: LivingEntity, velocity: Float) {
        val entityType = cfg.rangedProjectile?.get() ?: return
        val projectile = entityType.create(level()) as? Projectile ?: return
        val eyeX = target.x - x
        val eyeY = target.getBoundingBox().minY + target.getBbHeight() / 2.0 - (y + getEyeHeight())
        val eyeZ = target.z - z
        val horizontal = sqrt(eyeX * eyeX + eyeZ * eyeZ)
        val lift = if (horizontal > 2.0) horizontal.toFloat() / 24.0f else 0.0f
        projectile.setPos(x, y + getEyeHeight(), z)
        projectile.owner = this
        projectile.shoot(eyeX, eyeY + lift, eyeZ, 1.1f, 2.0f)
        level().addFreshEntity(projectile)
    }

    companion object {
        @JvmStatic
        fun createAttributes(cfg: MobConfig): AttributeSupplier.Builder =
            Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, cfg.health)
                .add(Attributes.MOVEMENT_SPEED, cfg.speed)
                .add(Attributes.ATTACK_DAMAGE, cfg.attackDamage)
                .add(Attributes.FOLLOW_RANGE, cfg.followRange)
                .add(Attributes.KNOCKBACK_RESISTANCE, cfg.knockbackResistance)
                .add(Attributes.ARMOR, cfg.armor)
    }
}

/**
 * 基岩版生物 AI/属性配置（由生成器从行为包 JSON 数据化）。
 */
class MobConfig(
    val name: String,
    val health: Double = 20.0,
    val speed: Double = 0.25,
    val attackDamage: Double = 3.0,
    val followRange: Double = 32.0,
    val knockbackResistance: Double = 0.0,
    val armor: Double = 0.0,
    val xp: Int = 5,
    val fireImmune: Boolean = false,
    val undead: Boolean = false,
    val water: Boolean = false,
    val friendly: Boolean = false,
    val melee: Boolean = true,
    val rangedProjectile: Supplier<EntityType<*>>? = null,
    val rangedInterval: Int = 40,
    val targets: List<String> = emptyList(),
) {
    fun buildGoals(mob: Monster) {
        val goals = mob.goalSelector
        goals.addGoal(0, FloatGoal(mob))
        if (friendly) {
            goals.addGoal(1, PanicGoal(mob, 1.3))
        }
        val attackSpeed = if (water) 1.0 else 1.15
        if (rangedProjectile != null) {
            goals.addGoal(2, RangedAttackGoal(mob as RangedAttackMob, attackSpeed, rangedInterval, 15.0f))
        } else if (!friendly && melee) {
            goals.addGoal(2, MeleeAttackGoal(mob, attackSpeed, true))
        }
        if (water) {
            goals.addGoal(5, RandomSwimmingGoal(mob, 1.0, 40))
        } else {
            goals.addGoal(5, WaterAvoidingRandomStrollGoal(mob, 1.0))
        }
        goals.addGoal(6, LookAtPlayerGoal(mob, Player::class.java, 8.0f))
        goals.addGoal(7, RandomLookAroundGoal(mob))

        if (friendly) return
        val targetGoals = mob.targetSelector
        targetGoals.addGoal(1, HurtByTargetGoal(mob))
        if ("player" in targets) {
            targetGoals.addGoal(2, NearestAttackableTargetGoal(mob, Player::class.java, true))
        }
        if ("villager" in targets) {
            targetGoals.addGoal(3, NearestAttackableTargetGoal(mob, AbstractVillager::class.java, false))
        }
        if ("iron_golem" in targets) {
            targetGoals.addGoal(3, NearestAttackableTargetGoal(mob, IronGolem::class.java, false))
        }
        if ("snow_golem" in targets) {
            targetGoals.addGoal(3, NearestAttackableTargetGoal(mob, SnowGolem::class.java, false))
        }
    }
}
