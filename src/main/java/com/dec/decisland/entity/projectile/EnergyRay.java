package com.dec.decisland.entity.projectile;

import com.dec.decisland.entity.ModEntities;
import com.dec.decisland.particles.ModParticles;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EnergyRay extends Projectile {
    public EnergyRay(EntityType<EnergyRay> entityType, Level level) {
        super(entityType, level);
    }
    public EnergyRay(Level level, LivingEntity owner, ItemStack spawnedFrom) {
        this(ModEntities.ENERGY_RAY.get(), level);
        this.setOwner(owner);
        this.setPos(
                owner.getX() + 0.5 * (owner.getBbWidth()) * owner.getViewVector(0.0f).x,
                owner.getEyeY() + 0.5 * (owner.getBbWidth()) * owner.getViewVector(0.0f).y,
                owner.getZ() + 0.5 * (owner.getBbWidth()) * owner.getViewVector(0.0f).z
        );
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }
    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    @Override
    public void tick() {
        if (this.tickCount > 60) this.discard();
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !net.neoforged.neoforge.event.EventHooks.onProjectileImpact(this, hitresult))
            this.hitTargetOrDeflectSelf(hitresult);
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3.scale(1));
            this.applyGravity();
            this.setPos(d0, d1, d2);
        }
        this.level().addParticle(ModParticles.BLIZZARD_WAKE_PARTICLE.get(), this.position().x, this.position().y, this.position().z, 0, 0, 0);
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.getOwner() instanceof LivingEntity livingentity) {
            Entity entity = result.getEntity();
            DamageSource damagesource = this.damageSources().spit(this, livingentity);
            if (this.level() instanceof ServerLevel serverlevel && entity.hurtServer(serverlevel, damagesource, 7)) {
                EnchantmentHelper.doPostAttackEffects(serverlevel, entity, damagesource);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }
}
