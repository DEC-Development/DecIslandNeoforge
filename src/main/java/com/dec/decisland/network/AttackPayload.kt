package com.dec.decisland.network

import com.dec.decisland.DecIsland
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.minecraft.world.InteractionHand

class AttackPayload(
    val attackerId: Int,
    val targetId: Int,
    val weaponTag: CompoundTag?,
    val hand: InteractionHand,
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    companion object {
        @JvmField
        val ID: Identifier = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "attack_payload")

        @JvmField
        val TYPE: CustomPacketPayload.Type<AttackPayload> = CustomPacketPayload.Type(ID)

        @JvmField
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, AttackPayload> = StreamCodec.of(
            { buf, payload ->
                buf.writeVarInt(payload.attackerId)
                buf.writeVarInt(payload.targetId)
                buf.writeNbt(payload.weaponTag)
                buf.writeEnum(payload.hand)
            },
            { buf ->
                AttackPayload(
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readNbt(),
                    buf.readEnum(InteractionHand::class.java),
                )
            },
        )
    }
}
