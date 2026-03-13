package com.dec.decisland.attachment;

import com.dec.decisland.DecIsland;
import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DecIsland.MOD_ID);

    // 存储整数等级，默认值为0，会自动保存到玩家NBT
    public static final Supplier<AttachmentType<Integer>> MAX_HEALTH_LEVEL =
            ATTACHMENT_TYPES.register("max_health_level",
                    () -> AttachmentType.builder(() -> 0)
                            .serialize(Codec.INT.fieldOf("value"))   // 关键！添加序列化
                            .build());



    // 最大魔法值（浮点数，默认 20.0）
    public static final Supplier<AttachmentType<Float>> MAX_MANA =
            ATTACHMENT_TYPES.register("max_mana",
                    () -> AttachmentType.builder(() -> 20.0f)
                            .serialize(Codec.FLOAT.fieldOf("value"))   // 关键！添加序列化
                            .build());

    // 魔法等级（整数，默认 0）
    public static final Supplier<AttachmentType<Float>> MANA_GAIN_LEVEL =
            ATTACHMENT_TYPES.register("mana_level",
                    () -> AttachmentType.builder(() -> 0.0f)
                            .serialize(Codec.FLOAT.fieldOf("value"))   // 关键！添加序列化
                            .build());

    // 魔法恢复等级（整数，默认 0）
    public static final Supplier<AttachmentType<Integer>> MANA_RECKON =
            ATTACHMENT_TYPES.register("mana_regen_level",
                    () -> AttachmentType.builder(() -> 0)
                            .serialize(Codec.INT.fieldOf("value"))   // 关键！添加序列化
                            .build());

    // 当前魔法值（浮点数，默认 20.0，即初始满蓝）
    public static final Supplier<AttachmentType<Float>> CURRENT_MANA =
            ATTACHMENT_TYPES.register("current_mana",
                    () -> AttachmentType.builder(() -> 20.0f)
                            .serialize(Codec.FLOAT.fieldOf("value"))   // 关键！添加序列化
                            .build());

    // 恢复间隔计数器（原 magic_gap），默认 60
    public static final Supplier<AttachmentType<Integer>> MAGIC_GAP =
            ATTACHMENT_TYPES.register("magic_gap",
                    () -> AttachmentType.builder(() -> 60)
                            .serialize(Codec.INT.fieldOf("value"))   // 关键！添加序列化
                            .build());

    // 上一 tick 的魔法值（原 dec_magic_store），默认 0
    public static final Supplier<AttachmentType<Float>> PREV_MAGIC =
            ATTACHMENT_TYPES.register("prev_magic",
                    () -> AttachmentType.builder(() -> 0f)
                            .serialize(Codec.FLOAT.fieldOf("value"))   // 关键！添加序列化
                            .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
