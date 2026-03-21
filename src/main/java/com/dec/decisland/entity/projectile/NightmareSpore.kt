package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.ModEntities
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class NightmareSpore(entityType: EntityType<NightmareSpore>, level: Level) : NightmareProjectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.NIGHTMARE_SPORE.get(), level) {
        setOwner(owner)
        setPos(
            owner.x + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).x,
            owner.eyeY + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).y,
            owner.z + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).z,
        )
    }

    override val baseDamage: Float = 13.0f
    override val maxLifetimeTicks: Int = 160
    override val airInertia: Double = 0.15
    override val waterInertia: Double = 0.10
    override val blockExtent: Double = 0.3
    override val trailParticleId: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "nightmare_spore_particle")
    override val spawnParticleId: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "nightmare_block_spawn_particle")

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    }
}
