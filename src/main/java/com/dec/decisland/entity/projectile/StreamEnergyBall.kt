package com.dec.decisland.entity.projectile

import com.dec.decisland.entity.ModEntities
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class StreamEnergyBall(entityType: EntityType<StreamEnergyBall>, level: Level) :
    VisibleMagicBallProjectile(
        entityType,
        level,
        "stream_energy_ball_wake_particle",
        "stream_energy_ball_hit_particle",
    ) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.STREAM_ENERGY_BALL.get(), level) {
        setSpawnPositionFromOwner(owner)
    }

    override val baseDamage: Float = 8.0f
}
