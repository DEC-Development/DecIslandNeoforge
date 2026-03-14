package com.dec.decisland.network

import com.dec.decisland.client.gui.ClientManaOverlay
import net.minecraft.network.chat.Component
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
}
