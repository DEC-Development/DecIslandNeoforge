package com.dec.decisland.item.custom;

import com.dec.decisland.attachment.ModAttachments;
import com.dec.decisland.entity.projectile.EnergyRay;
import com.dec.decisland.mana.ManaManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class EnergyRayStaff extends MagicWeapon {
    public EnergyRayStaff(Properties properties) {
        super(properties);
    }

    @Override
    public void shoot(int attackCounter, ServerLevel serverLevel, LivingEntity source) {
        Projectile.spawnProjectileUsingShoot(EnergyRay::new, serverLevel, source.getUseItem(), source,
                source.getViewVector(0.0f).x, source.getViewVector(0.0f).y, source.getViewVector(0.0f).z,
                1.4f, 2);
    }

    @Override
    public boolean judge(Level level, Player player) {
        float currentMana = player.getData(ModAttachments.CURRENT_MANA.get());
        if (currentMana >= 3) {
            ManaManager.reduceMana(player, 3);
            return true;
        } else {
            return false;
        }
    }
}