package com.dec.decisland.item.custom;

import com.dec.decisland.attachment.ModAttachments;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.attachment.AttachmentType;

public class SpurtEgg extends HeartEgg{
    public SpurtEgg(Properties properties) {
        super(properties);
    }
    public int getMinAttachmentLevel() {
        return 3;
    }
    public int getMaxAttachmentLevel() {
        return 4;
    }
}
