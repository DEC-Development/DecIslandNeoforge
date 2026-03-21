package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.projectile.BlizzardEnergy
import com.dec.decisland.particles.ModParticles
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import java.util.Random

class AbsoluteZero(properties: Properties) : Katana(properties) {
    override fun getUseSkillRadius(): Float = 2.0f

    override fun getUseSkillBreakAmount(): Int = 5

    override fun getUseSkillKnockback(): Float = 0.4f

    override fun useSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, ABSOLUTE_ZERO_PARTICLE_ID, x, y, z)
        serverLevel.sendParticles(ModParticles.ABSOLUTE_ZERO_SMOKE_BIG_PARTICLE.get(), x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
    }

    override fun useServer(serverLevel: ServerLevel, source: LivingEntity) {
        repeat(5) {
            Projectile.spawnProjectileUsingShoot(
                ::BlizzardEnergy,
                serverLevel,
                source.useItem,
                source,
                source.getViewVector(0.0f).x,
                source.getViewVector(0.0f).y,
                source.getViewVector(0.0f).z,
                0.02f,
                30.0f,
            )
        }
    }

    override fun getAttackSkillRadius(): Float = 1.1f

    override fun getAttackSkillBreakAmount(): Int = 3

    override fun getAttackSkillKnockback(): Float = 0.4f

    override fun getUseSkillBonusDamage(): Float = 11.0f

    override fun getAttackSkillBonusDamage(): Float = 5.0f

    override fun attackSpawnParticle(serverLevel: ServerLevel, x: Double, y: Double, z: Double) {
        sendBedrockKatanaEmitter(serverLevel, ABSOLUTE_ZERO_PARTICLE_ID, x, y, z)
        serverLevel.sendParticles(ModParticles.ABSOLUTE_ZERO_SMOKE_SMALL_PARTICLE.get(), x, y, z, 1, 0.0, 0.0, 0.0, 0.0)
    }

    override fun attackServer(serverLevel: ServerLevel, source: LivingEntity) {
        repeat(3) {
            Projectile.spawnProjectileUsingShoot(
                ::BlizzardEnergy,
                serverLevel,
                source.useItem,
                source,
                source.getViewVector(0.0f).x,
                source.getViewVector(0.0f).y,
                source.getViewVector(0.0f).z,
                0.02f,
                30.0f,
            )
        }
    }

    override fun onAttackTriggerSweep(stack: ItemStack): Boolean = Random().nextInt(3) == 0

    override fun inventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        super.inventoryTick(stack, level, entity, slot)
        updateMovementSpeedModifier(entity, slot, MOVEMENT_SPEED_ADDITION, MOVEMENT_SPEED_MODIFIER_ID)
    }

    companion object {
        private val ABSOLUTE_ZERO_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "absolute_zero_particle")
        private val MOVEMENT_SPEED_MODIFIER_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "movement_speed/absolute_zero")
        private const val MOVEMENT_SPEED_ADDITION: Double = 0.03
    }
}
