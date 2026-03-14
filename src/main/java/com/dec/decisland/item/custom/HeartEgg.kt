package com.dec.decisland.item.custom

import com.dec.decisland.attachment.ModAttachments
import net.minecraft.core.Holder
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.neoforge.attachment.AttachmentType

open class HeartEgg(properties: Properties) : HealthBoostItem(properties) {
    override fun getMinAttachmentLevel(): Int = 0

    override fun getMaxAttachmentLevel(): Int = 2

    override fun getAttachment(): AttachmentType<Int> = ModAttachments.MAX_HEALTH_LEVEL.get()

    override fun getAttribute(): Holder<Attribute> = Attributes.MAX_HEALTH

    override fun calAttributeValue(newLevel: Int): Int = 10 + 2 * newLevel
}
