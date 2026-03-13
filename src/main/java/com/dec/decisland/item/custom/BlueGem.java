package com.dec.decisland.item.custom;

import com.dec.decisland.attachment.ModAttachments;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;

public class BlueGem extends MaxManaBoostItem {
    public BlueGem(Properties properties) {
        super(properties);
    }
    @Override
    public int getMinAttachmentLevel() {
        return 20;
    }
    @Override
    public int getMaxAttachmentLevel() {
        return 39;
    }
    @Override
    public AttachmentType<Float> getAttachment() {
        return ModAttachments.MAX_MANA.get();
    }
    @Override
    public int getNutrition() {
        return 0;
    }
    @Override
    public float getSaturationModifier() {
        return 0.0f;
    }
    @Override
    public float getBoostValue() {return 2.0f; }
}
