package com.dec.decisland.item.custom;

import com.dec.decisland.attachment.ModAttachments;
import net.neoforged.neoforge.attachment.AttachmentType;

public class RedGem extends BlueGem {
    public RedGem(Properties properties) {
        super(properties);
    }
    @Override
    public int getMinAttachmentLevel() {
        return 40;
    }
    @Override
    public int getMaxAttachmentLevel() {
        return 59;
    }
    @Override
    public float getBoostValue() {return 2.0f; }
}
