package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.ModEntities
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class SpotsByBook(entityType: EntityType<SpotsByBook>, level: Level) : VisibleParticleRayProjectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.SPOTS_BY_BOOK.get(), level) {
        setSpawnPositionFromOwner(owner)
    }

    override val baseDamage: Float = 10.0f
    override val maxLifetimeTicks: Int = 60
    override val airInertia: Double = 1.05
    override val waterInertia: Double = 0.95
    override val trailDurationTicks: Int = 4
    override val trailParticleId: Identifier =
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "fire_wake_particle")

    override fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
        target.igniteForSeconds(5.0f)
    }
}
