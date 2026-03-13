package com.dec.decisland.network;

import com.dec.decisland.DecIsland;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.Nullable;

public record AttackPayload(int attackerId, int targetId, @Nullable CompoundTag weaponTag, InteractionHand hand) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "attack_payload");
    public static final CustomPacketPayload.Type<AttackPayload> TYPE = new CustomPacketPayload.Type<>(ID);

    // 编解码器：使用 ByteBufCodecs.VAR_INT 和手写的枚举/标签编解码
    public static final StreamCodec<FriendlyByteBuf, AttackPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, AttackPayload::attackerId,
            ByteBufCodecs.VAR_INT, AttackPayload::targetId,
            ByteBufCodecs.COMPOUND_TAG, AttackPayload::weaponTag,          // ✅ 使用内置的 NBT 编解码器
            StreamCodec.of(FriendlyByteBuf::writeEnum, buf -> buf.readEnum(InteractionHand.class)), AttackPayload::hand,
            AttackPayload::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}