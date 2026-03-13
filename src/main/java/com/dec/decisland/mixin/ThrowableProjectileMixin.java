package com.dec.decisland.mixin;

import com.dec.decisland.api.CustomInertia; // 接口移到了 api 包
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ThrowableProjectile.class)
public abstract class ThrowableProjectileMixin {

    /**
     * 修改水中阻力系数（对应原版 0.8F）
     * 在 f = 0.8F 的 store 指令后注入，用接口提供的值替换
     */
    @ModifyVariable(
            method = "applyInertia",
            at = @At(value = "STORE", ordinal = 1), // 第一个 store 指令（水中分支）
            ordinal = 0 // 修改第一个 float 类型变量
    )
    private float modifyWaterInertia(float original) {
        if (this instanceof CustomInertia) {
            return ((CustomInertia) this).getWaterInertia();
        }
        return original;
    }

    /**
     * 修改空气阻力系数（对应原版 0.99F）
     * 在 f = 0.99F 的 store 指令后注入，用接口提供的值替换
     */
    @ModifyVariable(
            method = "applyInertia",
            at = @At(value = "STORE", ordinal = 2), // 第二个 store 指令（空气分支）
            ordinal = 0 // 修改第一个 float 类型变量
    )
    private float modifyAirInertia(float original) {
        if (this instanceof CustomInertia) {
            return ((CustomInertia) this).getAirInertia();
        }
        return original;
    }
}