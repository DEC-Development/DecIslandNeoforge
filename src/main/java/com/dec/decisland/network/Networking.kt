package com.dec.decisland.network

import com.dec.decisland.DecIsland
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
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
    }
}
