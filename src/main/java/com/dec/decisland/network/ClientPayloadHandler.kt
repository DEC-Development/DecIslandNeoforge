package com.dec.decisland.network

import com.dec.decisland.client.RecoilClient
import com.dec.decisland.client.bedrock.BedrockEmitterManager
import com.dec.decisland.client.gui.ClientManaOverlay
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.handling.IPayloadContext

object ClientPayloadHandler {
    fun handleAttack(payload: AttackPayload, context: IPayloadContext) {
    }

    fun handleManaSync(payload: ManaSyncPayload, context: IPayloadContext) {
        context.enqueueWork {
            ClientManaOverlay.updateManaValues(payload.currentMana, payload.maxMana)
        }.exceptionally { error ->
            context.disconnect(Component.translatable("decisland.networking.failed", error.message ?: "unknown"))
            null
        }
    }

    fun handleRecoil(payload: RecoilPayload, context: IPayloadContext) {
        context.enqueueWork {
            RecoilClient.kick(payload.pitchUpDegrees, payload.yawDegrees)
        }.exceptionally { error ->
            context.disconnect(Component.translatable("decisland.networking.failed", error.message ?: "unknown"))
            null
        }
    }

    fun handleSpawnBedrockEmitter(payload: SpawnBedrockEmitterPayload, context: IPayloadContext) {
        context.enqueueWork {
            val client = Minecraft.getInstance()
            if (client.level != null) {
                BedrockEmitterManager.spawnAt(payload.id, Vec3(payload.x, payload.y, payload.z), payload.durationTicks)
            }
        }.exceptionally { error ->
            context.disconnect(Component.translatable("decisland.networking.failed", error.message ?: "unknown"))
            null
        }
    }
}
