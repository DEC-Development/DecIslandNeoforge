package com.dec.decisland.item.custom;

import com.dec.decisland.attachment.ModAttachments;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentType;

public class ManaGainBoostItem extends Item {
    public ManaGainBoostItem(Properties properties) {
        super(properties);
    }
    public float getMinAttachmentLevel() {
        return 0.0f;
    }
    public float getMaxAttachmentLevel() {
        return 19.0f;
    }
    public AttachmentType<Float> getAttachment() {
        return ModAttachments.MANA_GAIN_LEVEL.get();
    }
    public int getNutrition() {
        return 0;
    }
    public float getSaturationModifier() {
        return 0.0f;
    }
    public float getBoostValue() {return 1.0f; }


    // 食用完成时调用
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            // 只在服务端执行逻辑
            if (!level.isClientSide()) {
                float currentLevel = player.getData(getAttachment());
                if (currentLevel >= getMinAttachmentLevel() && currentLevel <= getMaxAttachmentLevel()) {
                    // 获取当前等级并+1
                    float newLevel = currentLevel + getBoostValue();
                    player.setData(getAttachment(), newLevel);

                    // --- 添加饱食度 ---
                    player.getFoodData().eat(getNutrition(), getSaturationModifier());

                    // --- 消耗物品 ---
                    stack.shrink(1);

                    // 播放声音
                    player.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F);
                } else {
                    // 条件不满足，不消耗物品，也不提升
                    stack.shrink(0);
                    return stack; // 直接返回原物品（未消耗）
                }
            }
        }
        // 返回剩余的物品栈（服务端已 shrink，客户端原样返回，但会被服务端同步覆盖）
        return stack;
    }
}
