package com.dec.decisland.item.custom

class RedGem(properties: Properties) : BlueGem(properties) {
    override fun getMinAttachmentLevel(): Int = 40

    override fun getMaxAttachmentLevel(): Int = 59

    override fun getBoostValue(): Float = 2.0f
}
