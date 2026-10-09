package com.dec.decisland.entity.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.network.Networking
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Difficulty
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Vex
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor

/**
 * 绿叶精灵（移植自基岩版 dec:elf_of_leaves）。
 *
 * 绿叶精华 Boss 咆哮阶段结束时召唤的辅助单位：
 * - 无重力漂浮，俯冲攻击（复用 Vex 的 swoop_attack AI）
 * - 仅攻击玩家，跟随范围 64 格
 * - 存在 20 秒后自爆消失（无伤害，仅粒子效果）
 * - 难度伤害：简单 2 / 普通 4 / 困难 6（基岩版固定 4）
 */
class ElfOfLeaves(entityType: EntityType<out Vex>, level: Level) : Vex(entityType, level) {

    override fun tick() {
        super.tick()
        if (level().isClientSide) return

        // 拖尾粒子（对应基岩版 client 实体的 leaves_essence 发射器）
        if (tickCount % TRAIL_PARTICLE_INTERVAL == 0) {
            Networking.sendBedrockEmitterToTracking(
                this,
                LEAVES_ESSENCE_PARTICLE,
                position().add(0.0, 0.4, 0.0),
                2,
            )
        }

        // 20 秒后自爆消失（对应基岩版 minecraft:explode power=0 + 事件移除）
        if (tickCount >= SELF_DESTRUCT_TICKS) {
            val serverLevel = level() as? ServerLevel ?: return
            Networking.sendBedrockEmitterToNearby(
                serverLevel,
                LEAVES_ESSENCE_PARTICLE,
                position().add(0.0, 0.4, 0.0),
                32.0,
                6,
            )
            discard()
        }
    }

    override fun doHurtTarget(target: Entity): Boolean {
        // 攻击前按当前世界难度动态刷新攻击伤害
        getAttribute(Attributes.ATTACK_DAMAGE)?.baseValue = when (level().difficulty) {
            Difficulty.HARD -> HARD_DAMAGE
            Difficulty.NORMAL -> NORMAL_DAMAGE
            else -> EASY_DAMAGE
        }
        return super.doHurtTarget(target)
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnType: MobSpawnType,
        spawnData: SpawnGroupData?,
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnType, spawnData)
        health = maxHealth
        return data
    }

    override fun removeWhenFarAway(distanceToClosestPlayer: Double): Boolean = false

    companion object {
        private const val SELF_DESTRUCT_TICKS = 400  // 20 秒
        private const val TRAIL_PARTICLE_INTERVAL = 4

        private const val EASY_DAMAGE = 2.0
        private const val NORMAL_DAMAGE = 4.0
        private const val HARD_DAMAGE = 6.0

        @JvmField
        val LEAVES_ESSENCE_PARTICLE: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "leaves_essence_particle")

        @JvmStatic
        fun createElfAttributes(): AttributeSupplier.Builder = Vex.createAttributes()
            .add(Attributes.MAX_HEALTH, 5.0)
            .add(Attributes.MOVEMENT_SPEED, 0.7)
            .add(Attributes.ATTACK_DAMAGE, NORMAL_DAMAGE)
            .add(Attributes.FOLLOW_RANGE, 64.0)
    }
}
