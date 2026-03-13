package com.dec.decisland.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class MagicWeapon extends Item {
    protected static final String ATTACK_COUNTER_KEY = "AttackCounter";
    protected static final String LAST_ATTACK_TIME_KEY = "LastAttackTime";
    public MagicWeapon(Properties properties) {
        super(properties);
    }
    protected int getMaxAttackCount() {
        return 7;
    }
    protected long getResetTimeMs() {
        return 5000;
    }
    public void shoot(int attackCounter, ServerLevel serverLevel, LivingEntity source) {

    }
    public boolean judge(Level level, Player player) {
        return true;
    }
    public void shootTrigger(ItemStack stack, ServerLevel serverLevel, Player player) {

        // 获取或创建CustomData组件
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag tag;

        if (customData == null) {
            tag = new CompoundTag();
        } else {
            tag = customData.copyTag();
        }

        // 获取当前时间
        long currentTime = System.currentTimeMillis();

        // 检查是否需要重置计数器
        boolean shouldReset = false;
        if (tag.contains(LAST_ATTACK_TIME_KEY)) {
            long lastAttackTime = tag.getLong(LAST_ATTACK_TIME_KEY).get();
            if (currentTime - lastAttackTime > this.getResetTimeMs()) {
                shouldReset = true;
            }
        }

        // 获取当前攻击计数
        int attackCount = shouldReset ? 0 : (tag.contains(ATTACK_COUNTER_KEY) ? tag.getInt(ATTACK_COUNTER_KEY).get() : 0);

        // 增加攻击计数
        attackCount++;
        tag.putInt(ATTACK_COUNTER_KEY, attackCount);
        tag.putLong(LAST_ATTACK_TIME_KEY, currentTime);

        // 创建新的CustomData并设置回物品
        CustomData updatedData = CustomData.of(tag);
        stack.set(DataComponents.CUSTOM_DATA, updatedData);



        shoot(attackCount, serverLevel, player);

        // 检查是否达到最大次
        if (attackCount >= this.getMaxAttackCount()) {
            // 重置计数器
            stack.set(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        }
    }
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            if (judge(level, player)) {
                shootTrigger(player.getItemInHand(hand), serverLevel, player);
                return InteractionResult.SUCCESS;
            };
        };
        return InteractionResult.PASS;
    }
}
