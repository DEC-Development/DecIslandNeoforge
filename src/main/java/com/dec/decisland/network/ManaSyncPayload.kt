package com.dec.decisland.network

import com.dec.decisland.DecIsland
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class ManaSyncPayload(
    val currentMana: Float,
    val maxMana: Float,
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    companion object {
        @JvmField
        val ID: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "mana_sync_payload")

        @JvmField
        val TYPE: CustomPacketPayload.Type<ManaSyncPayload> = CustomPacketPayload.Type(ID)

        @JvmField
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ManaSyncPayload> = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            ManaSyncPayload::currentMana,
            ByteBufCodecs.FLOAT,
            ManaSyncPayload::maxMana,
            ::ManaSyncPayload,
        )
    }
}
