package com.dec.decisland.attachment

import com.dec.decisland.DecIsland
import com.mojang.serialization.Codec
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModAttachments {
    @JvmField
    val ATTACHMENT_TYPES: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DecIsland.MOD_ID)

    @JvmField
    val MAX_HEALTH_LEVEL: Supplier<AttachmentType<Int>> =
        ATTACHMENT_TYPES.register("max_health_level", Supplier {
            AttachmentType.builder(Supplier { 0 })
                .serialize(Codec.INT.fieldOf("value"))
                .build()
        })

    @JvmField
    val MAX_MANA: Supplier<AttachmentType<Float>> =
        ATTACHMENT_TYPES.register("max_mana", Supplier {
            AttachmentType.builder(Supplier { 20.0f })
                .serialize(Codec.FLOAT.fieldOf("value"))
                .build()
        })

    @JvmField
    val MANA_GAIN_LEVEL: Supplier<AttachmentType<Float>> =
        ATTACHMENT_TYPES.register("mana_level", Supplier {
            AttachmentType.builder(Supplier { 0.0f })
                .serialize(Codec.FLOAT.fieldOf("value"))
                .build()
        })

    @JvmField
    val MANA_RECKON: Supplier<AttachmentType<Int>> =
        ATTACHMENT_TYPES.register("mana_regen_level", Supplier {
            AttachmentType.builder(Supplier { 0 })
                .serialize(Codec.INT.fieldOf("value"))
                .build()
        })

    @JvmField
    val CURRENT_MANA: Supplier<AttachmentType<Float>> =
        ATTACHMENT_TYPES.register("current_mana", Supplier {
            AttachmentType.builder(Supplier { 20.0f })
                .serialize(Codec.FLOAT.fieldOf("value"))
                .build()
        })

    @JvmField
    val MAGIC_GAP: Supplier<AttachmentType<Int>> =
        ATTACHMENT_TYPES.register("magic_gap", Supplier {
            AttachmentType.builder(Supplier { 60 })
                .serialize(Codec.INT.fieldOf("value"))
                .build()
        })

    @JvmField
    val PREV_MAGIC: Supplier<AttachmentType<Float>> =
        ATTACHMENT_TYPES.register("prev_magic", Supplier {
            AttachmentType.builder(Supplier { 0.0f })
                .serialize(Codec.FLOAT.fieldOf("value"))
                .build()
        })

    @JvmStatic
    fun register(eventBus: IEventBus) {
        ATTACHMENT_TYPES.register(eventBus)
    }
}
