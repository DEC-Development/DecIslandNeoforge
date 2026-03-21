package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.ModEntities
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class FrozenRay(entityType: EntityType<FrozenRay>, level: Level) : ParticleRayProjectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.FROZEN_RAY.get(), level) {
        setOwner(owner)
        setPos(
            owner.x + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).x,
            owner.eyeY + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).y,
            owner.z + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).z,
        )
    }

    override val baseDamage: Float = 8.0f
    override val airInertia: Double = 1.3
    override val waterInertia: Double = 0.7
    override val trailParticleId: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "frozen_wake_particle")

    override fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
        if (target is LivingEntity) {
            target.addEffect(MobEffectInstance(MobEffects.SLOWNESS, 200, 0))
        }
    }
}
