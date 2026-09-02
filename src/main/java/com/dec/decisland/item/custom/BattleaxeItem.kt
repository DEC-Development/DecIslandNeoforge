package com.dec.decisland.item.custom

import com.dec.decisland.network.Networking
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

// Battleaxe: right click performs the Bedrock ring AOE skill; melee hits build a combo
// counter that re-triggers a weaker ring once enough hits have landed.
class BattleaxeItem(
    properties: Properties,
    private val config: BattleaxeConfig,
) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.getItemInHand(hand)
        if (player.cooldowns.isOnCooldown(stack)) {
            return InteractionResult.FAIL
        }

        if (!level.isClientSide) {
            performSkill(level as ServerLevel, player, stack, combo = false)
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
                performSkill(player.level() as ServerLevel, player, stack, combo = true)
            } else {
                tag.putInt(TAG_SKILL_COUNT, count + 1)
                writeTag(stack, tag)
            }
        }
        super.hurtEnemy(stack, target, attacker)
    }

    // Bedrock's attack_more event: mining with a battleaxe costs 2 extra durability
    // on top of the regular tool wear applied by the base mineBlock.
    override fun mineBlock(stack: ItemStack, level: Level, state: BlockState, pos: BlockPos, entity: LivingEntity): Boolean {
        val worn = super.mineBlock(stack, level, state, pos, entity)
        if (worn && !level.isClientSide && state.getDestroySpeed(level, pos) != 0.0f) {
            stack.hurtAndBreak(EXTRA_MINING_DURABILITY_COST, entity, EquipmentSlot.MAINHAND)
        }
        return worn
    }

    private fun performSkill(serverLevel: ServerLevel, player: Player, stack: ItemStack, combo: Boolean) {
        val tag = readTag(stack)
        tag.putInt(TAG_SKILL_COUNT, 0)
        writeTag(stack, tag)

        val bursts = if (combo) config.comboParticleBursts else config.skillParticleBursts
        repeat(bursts) {
            Networking.sendBedrockEmitterToNearby(serverLevel, config.ringParticleId, player.position(), 64.0, RING_PARTICLE_DURATION_TICKS)
        }

        val damage = if (combo) config.comboSkillDamage else config.skillDamage
        findSkillTargets(serverLevel, player).forEach { target -> dealSkillDamage(serverLevel, player, target, damage) }
    }

    // Samples the Bedrock offset circles (e.g. x=~1,z=~,r=1.3 selectors) around the player.
    private fun findSkillTargets(serverLevel: ServerLevel, player: Player): List<LivingEntity> {
        val targets = LinkedHashSet<LivingEntity>()
        for (offset in config.aoeOffsets) {
            val center = player.position().add(offset)
            val box = AABB(center, center).inflate(config.aoeRadius)
            serverLevel.getEntitiesOfClass(LivingEntity::class.java, box)
                .filter { target -> target !== player && target.isAlive && target !is ArmorStand }
                .forEach(targets::add)
        }
        return targets.toList()
    }

    // Deals skill damage while bypassing the target's current hurt cooldown.
    private fun dealSkillDamage(serverLevel: ServerLevel, attacker: Player, target: LivingEntity, damage: Float): Boolean {
        if (damage <= 0.0f) {
            return false
        }

        val originalInvulnerableTime = target.invulnerableTime
        target.invulnerableTime = 0
        val hurt = target.hurtServer(serverLevel, serverLevel.damageSources().playerAttack(attacker), damage)
        if (!hurt) {
            target.invulnerableTime = originalInvulnerableTime
        }
        return hurt
    }

    private fun readTag(stack: ItemStack): CompoundTag = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()

    private fun writeTag(stack: ItemStack, tag: CompoundTag) {
        stack.set(DataComponents.CUSTOM_DATA, if (tag.isEmpty) CustomData.EMPTY else CustomData.of(tag))
    }

    class BattleaxeConfig private constructor(builder: Builder) {
        @JvmField
        val skillDamage: Float = builder.skillDamage

        @JvmField
        val comboSkillDamage: Float = builder.comboSkillDamage

        @JvmField
        val aoeRadius: Double = builder.aoeRadius

        @JvmField
        val aoeOffsets: List<Vec3> = builder.aoeOffsets.toList()

        @JvmField
        val ringParticleId: Identifier = builder.ringParticleId

        @JvmField
        val skillParticleBursts: Int = builder.skillParticleBursts

        @JvmField
        val comboParticleBursts: Int = builder.comboParticleBursts

        @JvmField
        val comboThreshold: Int = builder.comboThreshold

        class Builder(
            @JvmField val skillDamage: Float,
            @JvmField val comboSkillDamage: Float,
            @JvmField val ringParticleId: Identifier,
        ) {
            internal var aoeRadius: Double = 1.3
            internal var aoeOffsets: List<Vec3> = CROSS_OFFSETS
            internal var skillParticleBursts: Int = 3
            internal var comboParticleBursts: Int = 2
            internal var comboThreshold: Int = 4

            fun aoe(offsets: List<Vec3>, radius: Double): Builder = apply {
                aoeOffsets = offsets
                aoeRadius = radius
            }

            fun particleBursts(skill: Int, combo: Int): Builder = apply {
                skillParticleBursts = skill
                comboParticleBursts = combo
            }

            fun comboThreshold(comboThreshold: Int): Builder = apply {
                this.comboThreshold = comboThreshold
            }

            fun build(): BattleaxeConfig = BattleaxeConfig(this)
        }
    }

    companion object {
        private const val TAG_SKILL_COUNT = "skill_count"
        private const val EXTRA_MINING_DURABILITY_COST = 2
        private const val RING_PARTICLE_DURATION_TICKS = 20

        @JvmField
        val CROSS_OFFSETS: List<Vec3> = listOf(
            Vec3(0.0, 0.0, 1.0),
            Vec3(0.0, 0.0, -1.0),
            Vec3(1.0, 0.0, 0.0),
            Vec3(-1.0, 0.0, 0.0),
        )

        @JvmField
        val GRID_OFFSETS: List<Vec3> = listOf(
            Vec3(0.0, 0.0, 2.0),
            Vec3(0.0, 0.0, -2.0),
            Vec3(2.0, 0.0, 0.0),
            Vec3(-2.0, 0.0, 0.0),
            Vec3(1.0, 0.0, 1.0),
            Vec3(-1.0, 0.0, 1.0),
            Vec3(1.0, 0.0, -1.0),
            Vec3(-1.0, 0.0, -1.0),
        )
    }
}
