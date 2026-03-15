package com.dec.decisland.network

import com.dec.decisland.DecIsland
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

@EventBusSubscriber(modid = DecIsland.MOD_ID)
object Networking {
    @SubscribeEvent
    @JvmStatic
    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar(DecIsland.MOD_ID).versioned("1.0.0")

        registrar.playToClient(
            AttackPayload.TYPE,
            AttackPayload.STREAM_CODEC,
            ClientPayloadHandler::handleAttack,
        )

        registrar.playToClient(
            ManaSyncPayload.TYPE,
            ManaSyncPayload.STREAM_CODEC,
            ClientPayloadHandler::handleManaSync,
        )

        registrar.playToClient(
            RecoilPayload.TYPE,
            RecoilPayload.STREAM_CODEC,
            ClientPayloadHandler::handleRecoil,
        )

        registrar.playToClient(
            SpawnBedrockEmitterPayload.TYPE,
            SpawnBedrockEmitterPayload.STREAM_CODEC,
            ClientPayloadHandler::handleSpawnBedrockEmitter,
        )
    }

    @JvmStatic
    fun sendRecoil(player: ServerPlayer, pitchUpDegrees: Float, yawDegrees: Float) {
        PacketDistributor.sendToPlayer(player, RecoilPayload(pitchUpDegrees, yawDegrees))
    }

    @JvmStatic
    fun sendBedrockEmitter(player: ServerPlayer, particleId: Identifier, position: Vec3, durationTicks: Int = 40) {
        PacketDistributor.sendToPlayer(
            player,
            SpawnBedrockEmitterPayload(particleId, position.x, position.y, position.z, durationTicks),
        )
    }

    @JvmStatic
    fun sendBedrockEmitterToTracking(entity: Entity, particleId: Identifier, position: Vec3, durationTicks: Int = 2) {
        PacketDistributor.sendToPlayersTrackingEntity(
            entity,
            SpawnBedrockEmitterPayload(particleId, position.x, position.y, position.z, durationTicks),
        )
    }

    @JvmStatic
    fun sendBedrockEmitterToNearby(
        level: ServerLevel,
        particleId: Identifier,
        position: Vec3,
        radius: Double = 64.0,
        durationTicks: Int = 2,
    ) {
        val payload = SpawnBedrockEmitterPayload(particleId, position.x, position.y, position.z, durationTicks)
        val radiusSquared = radius * radius
        level.players().forEach { player ->
            if (player.position().distanceToSqr(position) <= radiusSquared) {
                PacketDistributor.sendToPlayer(player, payload)
            }
        }
    }
}
