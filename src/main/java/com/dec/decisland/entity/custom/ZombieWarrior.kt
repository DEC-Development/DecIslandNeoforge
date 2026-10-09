package com.dec.decisland.entity.custom

import net.minecraft.core.Holder
import net.minecraft.util.RandomSource
import net.minecraft.world.Difficulty
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor

/**
 * 僵尸战士（移植自基岩版 dec:zombie_warrior）。
 *
 * 与原版僵尸的差异：
 * - 难度感知攻击伤害：简单 3 / 普通 4 / 困难 6（基岩版固定 3，这里按难度梯度增强）
 * - 随机最大生命值 18~30（基岩版 range_min/max）
 * - 生成时随机获得「跳跃扑击」或「冲刺扑击」AI
 * - 手持斧头（石/木/铁/金/钻，权重递减），10% 附魔
 * - 手持武器掉率 2%，盔甲掉率 0.05%
 */
class ZombieWarrior(entityType: EntityType<out Zombie>, level: Level) : Zombie(entityType, level) {

    override fun doHurtTarget(target: Entity): Boolean {
        // 攻击前按当前世界难度动态刷新攻击伤害
        getAttribute(Attributes.ATTACK_DAMAGE)?.baseValue = currentDifficultyDamage()
        return super.doHurtTarget(target)
    }

    private fun currentDifficultyDamage(): Double = when (level().difficulty) {
        Difficulty.HARD -> HARD_DAMAGE
        Difficulty.NORMAL -> NORMAL_DAMAGE
        else -> EASY_DAMAGE
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnType: MobSpawnType,
        spawnData: SpawnGroupData?,
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnType, spawnData)
        val random = level.random

        // 随机最大生命值（18~30），基岩版 range_min/range_max
        getAttribute(Attributes.MAX_HEALTH)?.baseValue = random.nextInt(MIN_HEALTH, MAX_HEALTH + 1).toDouble()
        health = maxHealth

        // 随机「冲刺扑击」或「跳跃扑击」AI（基岩版 randomize 组件组）
        val leapHeight = if (random.nextBoolean()) SPRINT_LEAP_HEIGHT else JUMP_LEAP_HEIGHT
        goalSelector.addGoal(LEAP_PRIORITY, LeapAtTargetGoal(this, leapHeight))

        // 10% 概率给手持武器附魔（5~20 级，基岩版 enchant_with_level）
        val weapon = mainHandItem
        if (!weapon.isEmpty && random.nextFloat() < WEAPON_ENCHANT_CHANCE) {
            enchantWeapon(random, weapon)
        }
        return data
    }

    private fun enchantWeapon(random: RandomSource, weapon: ItemStack) {
        val registryAccess = level().registryAccess()
        val enchantments = registryAccess.lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
        val candidates = enchantments.listElements().toList()
        if (candidates.isEmpty()) return
        val enchantment: Holder<Enchantment> = candidates[random.nextInt(candidates.size)]
        val enchantmentLevel = random.nextInt(1, 4)
        weapon.enchant(enchantment, enchantmentLevel)
    }

    override fun populateDefaultEquipmentSlots(random: RandomSource, difficulty: DifficultyInstance) {
        super.populateDefaultEquipmentSlots(random, difficulty)
        setItemSlot(EquipmentSlot.MAINHAND, pickWeapon(random))
    }

    override fun getEquipmentDropChance(slot: EquipmentSlot): Float =
        if (slot == EquipmentSlot.MAINHAND) WEAPON_DROP_CHANCE else ARMOR_DROP_CHANCE

    override fun removeWhenFarAway(distanceToClosestPlayer: Double): Boolean = true

    companion object {
        private const val MIN_HEALTH = 18
        private const val MAX_HEALTH = 30
        private const val EASY_DAMAGE = 3.0
        private const val NORMAL_DAMAGE = 4.0
        private const val HARD_DAMAGE = 6.0
        private const val LEAP_PRIORITY = 4
        private const val SPRINT_LEAP_HEIGHT = 0.5f
        private const val JUMP_LEAP_HEIGHT = 0.0f
        private const val WEAPON_ENCHANT_CHANCE = 0.10f
        private const val WEAPON_DROP_CHANCE = 0.02f
        private const val ARMOR_DROP_CHANCE = 0.0005f

        // 基岩版 zombie_warrior_gear.json 的权重表
        private val WEAPONS: List<Pair<Item, Int>> = listOf(
            Items.STONE_AXE to 15,
            Items.WOODEN_AXE to 10,
            Items.IRON_AXE to 8,
            Items.GOLDEN_AXE to 2,
            Items.DIAMOND_AXE to 2,
        )

        private fun pickWeapon(random: RandomSource): ItemStack {
            val total = WEAPONS.sumOf { it.second }
            var roll = random.nextInt(total)
            for ((item, weight) in WEAPONS) {
                roll -= weight
                if (roll < 0) return ItemStack(item)
            }
            return ItemStack(Items.STONE_AXE)
        }

        @JvmStatic
        fun createWarriorAttributes(): AttributeSupplier.Builder = Zombie.createAttributes()
    }
}
