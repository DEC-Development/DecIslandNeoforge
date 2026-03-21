package com.dec.decisland.entity.projectile

import com.dec.decisland.DecIsland
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.entity.ModEntities
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class GrowingEnergyRay(entityType: EntityType<GrowingEnergyRay>, level: Level) : ParticleRayProjectile(entityType, level) {
    constructor(level: Level, owner: LivingEntity, spawnedFrom: ItemStack) : this(ModEntities.GROWING_ENERGY_RAY.get(), level) {
        setOwner(owner)
        setPos(
            owner.x + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).x,
            owner.eyeY + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).y,
            owner.z + 0.5 * owner.bbWidth * owner.getViewVector(0.0f).z,
        )
    }

    override val baseDamage: Float = 6.0f
    override val airInertia: Double = 1.0
    override val waterInertia: Double = 1.0
    override val trailParticleId: Identifier =
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "flower_ghost_block_particle")
    override val hitParticleIds: List<Identifier> =
        listOf(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "flower_ghost_block_particle"))

    override fun onEntityDamaged(serverLevel: ServerLevel, target: Entity) {
        placeFlowerGhostBlock(serverLevel, target.position())
    }

    override fun onBlockHit(serverLevel: ServerLevel, pos: Vec3) {
        placeFlowerGhostBlock(serverLevel, pos)
    }

    private fun placeFlowerGhostBlock(serverLevel: ServerLevel, pos: Vec3) {
        val blockPos = BlockPos.containing(pos)
        if (serverLevel.getBlockState(blockPos).isAir) {
            serverLevel.setBlockAndUpdate(blockPos, ModBlocks.FLOWER_GHOST_BLOCK.get().defaultBlockState())
        }
    }
}
