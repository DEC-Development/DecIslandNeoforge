package com.dec.decisland.item.custom;

import com.dec.decisland.attachment.ModAttachments;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;

public class MagicCrystal extends ManaGainBoostItem {
    public MagicCrystal(Properties properties) {
        super(properties);
    }
    @Override
    public float getMinAttachmentLevel() {
        return 0.0f;
    }
    @Override
    public float getMaxAttachmentLevel() {
        return 19.0f;
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
    public float getBoostValue() {return 1.0f; }
}
