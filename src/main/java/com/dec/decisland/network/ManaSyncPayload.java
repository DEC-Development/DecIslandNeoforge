package com.dec.decisland.network;

import com.dec.decisland.DecIsland;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ManaSyncPayload(float currentMana, float maxMana) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "mana_sync_payload");
    public static final CustomPacketPayload.Type<ManaSyncPayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, ManaSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ManaSyncPayload::currentMana,
            ByteBufCodecs.FLOAT, ManaSyncPayload::maxMana,
            ManaSyncPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}