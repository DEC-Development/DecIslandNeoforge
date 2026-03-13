package com.dec.decisland.item.custom;

import com.dec.decisland.attachment.ModAttachments;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.attachment.AttachmentType;

public class HeartEgg extends HealthBoostItem {
    public HeartEgg(Properties properties) {
        super(properties);
    }
    public int getMinAttachmentLevel() {
        return 0;
    }
    public int getMaxAttachmentLevel() {
        return 2;
    }
    public AttachmentType<Integer> getAttachment() {
        return ModAttachments.MAX_HEALTH_LEVEL.get();
    }
    public Holder<Attribute> getAttribute() {
        return Attributes.MAX_HEALTH;
    }
    public int calAttributeValue(int newLevel) {
        int new_value = 10 + 2 * newLevel;
        return new_value;
    }
}
