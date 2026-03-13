package com.dec.decisland.item.custom;

public class EnderEgg extends HeartEgg{
    public EnderEgg(Properties properties) {
        super(properties);
    }
    public int getMinAttachmentLevel() {
        return 5;
    }
    public int getMaxAttachmentLevel() {
        return 6;
    }
}
