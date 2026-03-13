package com.dec.decisland.network;

import com.dec.decisland.DecIsland;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = DecIsland.MOD_ID)
public class Networking {

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(DecIsland.MOD_ID)
                .versioned("1.0.0"); // 可选版本号

        // 注册客户端方向的play阶段payload
        registrar.playToClient(
                AttackPayload.TYPE,
                AttackPayload.STREAM_CODEC,
                ClientPayloadHandler.getInstance()::handleAttack
        );

        // 注册mana同步包
        registrar.playToClient(
                ManaSyncPayload.TYPE,
                ManaSyncPayload.STREAM_CODEC,
                ClientPayloadHandler.getInstance()::handleManaSync
        );
    }
}