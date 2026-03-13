package com.dec.decisland.item.custom;

import com.dec.decisland.entity.projectile.BlizzardEnergy;
import com.dec.decisland.particles.ModParticles;
import net.minecraft.client.renderer.item.properties.numeric.Damage;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.IItemExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AbsoluteZero extends Katana {
    public AbsoluteZero(Properties properties) {
        super(properties);
    }
    @Override
    public float getUseSkillRadius() {
        return 2.0f;
    }
    @Override
    public int getUseSkillBreakAmount() {
        return 5;
    }
    @Override
    public float getUseSkillKnockback() {
        return 0.4f;
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
        serverLevel.sendParticles(ModParticles.ABSOLUTE_ZERO_PARTICLE.get(), x, y, z, 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ModParticles.ABSOLUTE_ZERO_SMOKE_BIG_PARTICLE.get(), x, y, z, 1, 0, 0, 0, 0);
    }

    @Override
    public void useServer(ServerLevel serverLevel, LivingEntity source) {
        for (int i = 0; i < 5; i++) {
            Projectile.spawnProjectileUsingShoot(BlizzardEnergy::new, serverLevel, source.getUseItem(), source,
                    source.getViewVector(0.0f).x, source.getViewVector(0.0f).y, source.getViewVector(0.0f).z,
                    0.02f, 30);
        }
    }
    @Override
    public float getAttackSkillRadius() {
        return 1.1f;
    }
    @Override
    public int getAttackSkillBreakAmount() {
        return 3;
    }
    @Override
    public float getAttackSkillKnockback() {
        return 0.4f;
    }
    @Override
    public float getUseSkillBonusDamage() {
        return 11f;
    }
    @Override
    public float getAttackSkillBonusDamage() {
        return 5f;
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
        serverLevel.sendParticles(ModParticles.ABSOLUTE_ZERO_PARTICLE.get(), x, y, z, 1, 0, 0, 0, 0);
        serverLevel.sendParticles(ModParticles.ABSOLUTE_ZERO_SMOKE_SMALL_PARTICLE.get(), x, y, z, 1, 0, 0, 0, 0);
    }

    @Override
    public void attackServer(ServerLevel serverLevel, LivingEntity source) {
        for (int i = 0; i < 3; i++) {
            Projectile.spawnProjectileUsingShoot(BlizzardEnergy::new, serverLevel, source.getUseItem(), source,
                    source.getViewVector(0.0f).x, source.getViewVector(0.0f).y, source.getViewVector(0.0f).z,
                    0.02f, 30);
        }
    }

    @Override
    public boolean onAttackTriggerSweep(ItemStack stack) {
        Random random = new Random();
        return random.nextInt(3) == 0;
    }
}
