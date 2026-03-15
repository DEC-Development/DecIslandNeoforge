package com.dec.decisland.mixin.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class GunThirdPersonStraightArmMixin {
    @Shadow public ModelPart head;
    @Shadow public ModelPart rightArm;
    @Shadow public ModelPart leftArm;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    private void decisland$straightGunArms(HumanoidRenderState state, CallbackInfo ci) {
        if (state == null) {
            return;
        }

        boolean rightGun = state.rightArmPose == HumanoidModel.ArmPose.BOW_AND_ARROW;
        boolean leftGun = state.leftArmPose == HumanoidModel.ArmPose.BOW_AND_ARROW;
        boolean rightSupport = state.rightArmPose == HumanoidModel.ArmPose.CROSSBOW_HOLD;
        boolean leftSupport = state.leftArmPose == HumanoidModel.ArmPose.CROSSBOW_HOLD;
        if (!rightGun && !leftGun && !rightSupport && !leftSupport) {
            return;
        }

        float base = -1.55F;
        float pitch = this.head.xRot;
        float yaw = this.head.yRot;

        if (rightGun && leftGun) {
            applyStraightHold(false, base, pitch, yaw);
            applyStraightHold(true, base, pitch, yaw);
            return;
        }

        if (rightGun) {
            applyStraightHold(false, base, pitch, yaw);
            if (leftSupport) {
                applySupportHold(true, pitch, yaw);
            }
            return;
        }

        if (leftGun) {
            applyStraightHold(true, base, pitch, yaw);
            if (rightSupport) {
                applySupportHold(false, pitch, yaw);
            }
        }
    }

    private void applyStraightHold(boolean left, float base, float pitch, float yaw) {
        ModelPart arm = left ? this.leftArm : this.rightArm;
        float side = left ? 1.0F : -1.0F;
        arm.xRot = base + pitch;
        arm.yRot = yaw + (side * 0.10F);
        arm.zRot = side * -0.06F;
    }

    private void applySupportHold(boolean left, float pitch, float yaw) {
        ModelPart arm = left ? this.leftArm : this.rightArm;
        float side = left ? 1.0F : -1.0F;
        arm.xRot = -1.22F + (pitch * 0.85F);
        arm.yRot = (side * 0.70F) + (yaw * 0.55F);
        arm.zRot = side * -0.12F;
    }
}
