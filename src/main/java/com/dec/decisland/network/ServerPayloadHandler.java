package com.dec.decisland.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

    public static ServerPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleAttack(final AttackPayload payload, final IPayloadContext context) {
        // 服务器不会收到此包，无需实现
    }
}