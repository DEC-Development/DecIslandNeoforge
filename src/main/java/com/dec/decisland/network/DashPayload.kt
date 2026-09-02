package com.dec.decisland.network

import com.dec.decisland.DecIsland
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class DashPayload(
    val power: Float,
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    companion object {
        @JvmField
        val ID: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "dash_s2c")

        @JvmField
        val TYPE: CustomPacketPayload.Type<DashPayload> = CustomPacketPayload.Type(ID)

        @JvmField
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, DashPayload> = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            DashPayload::power,
            ::DashPayload,
        )
    }
}
