package com.dec.decisland.network

import com.dec.decisland.DecIsland
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class SpawnBedrockEmitterPayload(
    val id: Identifier,
    val x: Double,
    val y: Double,
    val z: Double,
    val durationTicks: Int,
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    companion object {
        @JvmField
        val ID: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "spawn_bedrock_emitter_s2c")

        @JvmField
        val TYPE: CustomPacketPayload.Type<SpawnBedrockEmitterPayload> = CustomPacketPayload.Type(ID)

        @JvmField
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, SpawnBedrockEmitterPayload> = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            SpawnBedrockEmitterPayload::id,
            ByteBufCodecs.DOUBLE,
            SpawnBedrockEmitterPayload::x,
            ByteBufCodecs.DOUBLE,
            SpawnBedrockEmitterPayload::y,
            ByteBufCodecs.DOUBLE,
            SpawnBedrockEmitterPayload::z,
            ByteBufCodecs.VAR_INT,
            SpawnBedrockEmitterPayload::durationTicks,
            ::SpawnBedrockEmitterPayload,
        )
    }
}
