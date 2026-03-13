package com.dec.decisland.entity.projectile;

import com.dec.decisland.entity.ModEntities;
import com.dec.decisland.item.ModItems;
import com.dec.decisland.api.CustomInertia;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownAshPufferfish extends ThrowableItemProjectile implements CustomInertia {
    public ThrownAshPufferfish(EntityType<ThrownAshPufferfish> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownAshPufferfish(Level level, LivingEntity owner, ItemStack item) {
        super(ModEntities.THROWN_ASH_PUFFERFISH.get(), owner, level, item);
    }

    public ThrownAshPufferfish(Level level, double x, double y, double z, ItemStack item) {
        super(ModEntities.THROWN_ASH_PUFFERFISH.get(), x, y, z, level, item);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ASH_PUFFERFISH.get();
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 4);
        if (!this.level().isClientSide()) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.5f, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2f, true, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    @Override
    public float getWaterInertia() {
        return 0.99F; // 修改为你的目标值
    }
}
