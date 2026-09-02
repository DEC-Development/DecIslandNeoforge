package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.network.Networking
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class GhostSwordItem(properties: Properties) : Item(properties) {
    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val serverLevel = attacker.level() as? ServerLevel ?: return
        if (serverLevel.random.nextInt(PROC_CHANCE) == 0) {
            Networking.sendBedrockEmitterToNearby(serverLevel, GHOST_SICKLE_PARTICLE_ID, target.position())
            target.addEffect(MobEffectInstance(MobEffects.INVISIBILITY, INVISIBILITY_DURATION_TICKS, 0))
        }
    }

    companion object {
        private const val PROC_CHANCE = 6
        private const val INVISIBILITY_DURATION_TICKS = 60
        private val GHOST_SICKLE_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "ghost_sickle_particle")
    }
}