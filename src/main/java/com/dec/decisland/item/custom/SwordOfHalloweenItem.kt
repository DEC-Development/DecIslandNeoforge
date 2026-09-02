package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.custom.PumpkinBombEntity
import com.dec.decisland.network.Networking
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class SwordOfHalloweenItem(properties: Properties) : Item(properties) {
    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        val serverLevel = attacker.level() as? ServerLevel ?: return
        if (serverLevel.random.nextInt(PROC_CHANCE) == 0) {
            serverLevel.addFreshEntity(PumpkinBombEntity(serverLevel, target.x, target.y, target.z))
            Networking.sendBedrockEmitterToNearby(
                serverLevel,
                BAT_SPURT_PARTICLE_ID,
                target.position().add(0.0, 0.5, 0.0),
            )
        }
    }

    companion object {
        private const val PROC_CHANCE = 5
        private val BAT_SPURT_PARTICLE_ID: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bat_spurt_particle")
    }
}