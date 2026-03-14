package com.dec.decisland.item.custom

class EnderEgg(properties: Properties) : HeartEgg(properties) {
    override fun getMinAttachmentLevel(): Int = 5

    override fun getMaxAttachmentLevel(): Int = 6
}
