package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.ModEntities
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class NightmareRay(entityType: EntityType<NightmareRay>, level: Level) : NightmareProjectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.NIGHTMARE_RAY.get(), level) {
        setOwner(owner)
        setPos(
            owner.x + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).x,
            owner.eyeY + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).y,
            owner.z + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).z,
        )
    }

    override val baseDamage: Float = 15.0f
    override val maxLifetimeTicks: Int = 60
    override val airInertia: Double = 1.0
    override val waterInertia: Double = 1.0
    override val blockExtent: Double = 0.7
    override val trailParticleId: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "nightmare_ray_particle")
    override val spawnParticleId: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "nightmare_block_spawn_particle")

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    }
}
