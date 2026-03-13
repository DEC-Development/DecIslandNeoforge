package com.dec.decisland.network;

import com.dec.decisland.client.gui.ClientManaOverlay;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleAttack(AttackPayload payload, IPayloadContext context) {
        // 原有的攻击处理逻辑
    }

    // 处理mana同步包
    public void handleManaSync(ManaSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            // 在主线程中更新mana UI
            ClientManaOverlay.updateManaValues(payload.currentMana(), payload.maxMana());
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("decisland.networking.failed", e.getMessage()));
            return null;
        });
    }
}