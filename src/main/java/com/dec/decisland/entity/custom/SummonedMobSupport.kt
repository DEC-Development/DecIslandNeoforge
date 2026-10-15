package com.dec.decisland.entity.custom

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/** Runtime owner/lifetime state for Bedrock summon projectiles. */
object SummonedMobSupport {
    private data class State(val owner: UUID, val expiresAt: Long)

    private val states: MutableMap<UUID, State> = ConcurrentHashMap()

    fun mark(mob: Mob, owner: LivingEntity, lifetimeTicks: Int) {
        states[mob.uuid] = State(owner.uuid, owner.level().gameTime + lifetimeTicks)
        mob.addTag(SUMMON_TAG)
        mob.addTag("${OWNER_TAG_PREFIX}${owner.uuid}")
        mob.target = null
    }

    fun tick(mob: Mob) {
        val state = states[mob.uuid] ?: return
        val level = mob.level() as? ServerLevel ?: return
        if (!mob.isAlive || level.gameTime >= state.expiresAt) {
            states.remove(mob.uuid)
            mob.discard()
            return
        }
        val target = mob.target
        if (target != null && (target.uuid == state.owner || target is Player || isOwnedBy(target, state.owner))) {
            mob.target = null
            mob.navigation.stop()
        }
    }

    fun isOwnedBy(entity: net.minecraft.world.entity.Entity, owner: UUID): Boolean =
        entity.tags.contains("${OWNER_TAG_PREFIX}${owner}")

    fun owner(mob: Mob): Player? {
        val state = states[mob.uuid] ?: return null
        val level = mob.level() as? ServerLevel ?: return null
        return level.getPlayerByUUID(state.owner)
    }

    private const val SUMMON_TAG = "decisland_summoned"
    private const val OWNER_TAG_PREFIX = "decisland_owner_"
}
