package com.dec.decisland.entity.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.GeneratedMobs
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.network.Networking
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.animal.IronGolem
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.level.Level

/** Visual 34-tick portal used by the Bedrock Village Guardian skill. */
class VillagePortalEntity(entityType: EntityType<VillagePortalEntity>, level: Level) : Entity(entityType, level) {
    override fun defineSynchedData(builder: net.minecraft.network.syncher.SynchedEntityData.Builder) {
    }

    override fun tick() {
        super.tick()
        if (level().isClientSide) return
        val serverLevel = level() as? ServerLevel ?: return
        if (tickCount in PARTICLE_TICKS) {
            Networking.sendBedrockEmitterToNearby(serverLevel, PORTAL_PARTICLE, position(), 64.0, 4)
        }
        when (tickCount) {
            12 -> spawnFirstWave(serverLevel)
            21, 30 -> if (serverLevel.random.nextInt(17) == 16) {
                discard()
                return
            } else {
                spawnWave(serverLevel)
            }
            33 -> discard()
        }
    }

    private fun spawnFirstWave(serverLevel: ServerLevel) {
        when (serverLevel.random.nextInt(11)) {
            in 0..4 -> spawn(serverLevel, GeneratedMobs.SOLDIER.get())
            in 5..9 -> spawn(serverLevel, GeneratedMobs.ARCHER.get())
            else -> spawn(serverLevel, BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.withDefaultNamespace("iron_golem")))
        }
    }

    private fun spawnWave(serverLevel: ServerLevel) {
        when (serverLevel.random.nextInt(17)) {
            in 0..4 -> spawn(serverLevel, GeneratedMobs.SOLDIER.get())
            in 5..9 -> spawn(serverLevel, GeneratedMobs.ARCHER.get())
            10 -> spawn(serverLevel, BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.withDefaultNamespace("iron_golem")))
            in 11..15 -> spawn(serverLevel, BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.withDefaultNamespace("villager")))
        }
    }

    private fun spawn(serverLevel: ServerLevel, type: EntityType<*>) {
        val entity = type.create(serverLevel) ?: return
        entity.setPos(x, y, z)
        if (entity is Mob) {
            entity.yRot = yRot
            entity.yHeadRot = yRot
        }
        serverLevel.addFreshEntity(entity)
    }

    override fun addAdditionalSaveData(compound: net.minecraft.nbt.CompoundTag) {
    }

    override fun readAdditionalSaveData(compound: net.minecraft.nbt.CompoundTag) {
    }

    override fun isPickable(): Boolean = false

    companion object {
        private val PORTAL_PARTICLE = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "village_portal_spurt_particle")
        private val PARTICLE_TICKS = setOf(3, 9, 12, 15, 18, 21, 24, 27, 30, 33)
    }
}
