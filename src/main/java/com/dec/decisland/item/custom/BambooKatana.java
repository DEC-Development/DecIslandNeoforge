package com.dec.decisland.item.custom;

import com.dec.decisland.entity.projectile.BlizzardEnergy;
import com.dec.decisland.particles.ModParticles;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.Random;

public class BambooKatana extends Katana {
    public BambooKatana(Properties properties) {
        super(properties);
    }
    @Override
    protected int getMaxAttackCount(){
        return 7;
    }
    @Override
    protected long getResetTimeMs() {
        return 5000;
    }
    @Override
    public float getUseSkillRadius() {
        return 1.2f;
    }
    @Override
    public int getUseSkillBreakAmount() {
        return 1;
    }
    /**
     * 在视野前方生成粒子
     *
     * @param serverLevel
     * @param x
     * @param y
     * @param z
     */
    @Override
    public void useSpawnParticle(ServerLevel serverLevel, double x, double y, double z) {
        serverLevel.sendParticles(ModParticles.BAMBOO_KATANA_PARTICLE.get(), x, y, z, 1, 0, 0, 0, 0);
    }
    @Override
    public float getAttackSkillRadius() {
        return 1.2f;
    }
    @Override
    public int getAttackSkillBreakAmount() {
        return 1;
    }
    @Override
    public float getUseSkillBonusDamage() {
        return 3f;
    }
    @Override
    public float getAttackSkillBonusDamage() {
        return 3f;
    }
    /**
     * 在视野前方生成粒子
     *
     * @param serverLevel
     * @param x
     * @param y
     * @param z
     */
    @Override
    public void attackSpawnParticle(ServerLevel serverLevel, double x, double y, double z) {
        serverLevel.sendParticles(ModParticles.BAMBOO_KATANA_PARTICLE.get(), x, y, z, 1, 0, 0, 0, 0);
    }

    @Override
    public boolean onAttackTriggerSweep(ItemStack stack) {
        // 获取或创建CustomData组件
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag tag;

        if (customData == null) {
            tag = new CompoundTag();
        } else {
            tag = customData.copyTag();
        }
        // 获取当前攻击计数
        int attackCount = tag.contains(ATTACK_COUNTER_KEY) ? tag.getInt(ATTACK_COUNTER_KEY).get() : 0;
        if (attackCount == this.getMaxAttackCount()) {
            return true;
        } else {
            return false;
        }
    }
}
