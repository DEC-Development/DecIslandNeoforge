package com.dec.decisland.item.custom

import com.dec.decisland.attachment.ModAttachments
import net.neoforged.neoforge.attachment.AttachmentType

open class BlueGem(properties: Properties) : MaxManaBoostItem(properties) {
    override fun getMinAttachmentLevel(): Int = 20

    override fun getMaxAttachmentLevel(): Int = 39

    override fun getAttachment(): AttachmentType<Float> = ModAttachments.MAX_MANA.get()

    override fun getNutrition(): Int = 0

    override fun getSaturationModifier(): Float = 0.0f

    override fun getBoostValue(): Float = 2.0f
}
