package com.dec.decisland.item.custom;

import com.dec.decisland.particles.ModParticles;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class HardBambooKatana extends BambooKatana {
    public HardBambooKatana(Properties properties) {
        super(properties);
    }
    @Override
    protected int getMaxAttackCount(){
        return 6;
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
        serverLevel.sendParticles(ModParticles.HARD_BAMBOO_KATANA_PARTICLE.get(), x, y, z, 1, 0, 0, 0, 0);
    }
    @Override
    public void attackSpawnParticle(ServerLevel serverLevel, double x, double y, double z) {
        serverLevel.sendParticles(ModParticles.HARD_BAMBOO_KATANA_PARTICLE.get(), x, y, z, 1, 0, 0, 0, 0);
    }
}
