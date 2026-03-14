package com.dec.decisland.item.custom

class SpurtEgg(properties: Properties) : HeartEgg(properties) {
    override fun getMinAttachmentLevel(): Int = 3

    override fun getMaxAttachmentLevel(): Int = 4
}
