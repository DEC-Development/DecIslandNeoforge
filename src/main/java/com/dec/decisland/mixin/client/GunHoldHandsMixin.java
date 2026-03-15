package com.dec.decisland.mixin.client;

import com.dec.decisland.client.RecoilClient;
import com.dec.decisland.item.gun.GunItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInHandRenderer.class)
public class GunHoldHandsMixin {
    private static Object decisland$bothHandsSelection;

    @Inject(method = "evaluateWhichHandsToRender(Lnet/minecraft/client/player/LocalPlayer;)Lnet/minecraft/client/renderer/ItemInHandRenderer$HandRenderSelection;", at = @At("HEAD"), cancellable = true)
    private static void decisland$renderBothHandsForGun(LocalPlayer player, CallbackInfoReturnable<Object> cir) {
        if (player == null) {
            return;
        }

        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        boolean mainGun = main.getItem() instanceof GunItem;
        boolean offGun = off.getItem() instanceof GunItem;
        boolean mainPistol = mainGun && isShortFlintlock(main);
        boolean offPistol = offGun && isShortFlintlock(off);

        if ((mainPistol && !offGun) || (offPistol && !mainGun)) {
            return;
        }

        if ((mainGun && (off.isEmpty() || offGun)) || (offGun && (main.isEmpty() || mainGun))) {
            Object selection = decisland$getBothHandsSelection();
            if (selection != null) {
                cir.setReturnValue(selection);
            }
        }
    }

    @Inject(
        method = "renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V",
        at = @At("HEAD")
    )
    private void decisland$gunFirstPersonPose(
        AbstractClientPlayer player,
        float partialTick,
        float pitch,
        InteractionHand hand,
        float swingProgress,
        ItemStack stack,
        float equipProgress,
        PoseStack poseStack,
        SubmitNodeCollector nodeCollector,
        int packedLight,
        CallbackInfo ci
    ) {
        if (player == null) {
            return;
        }

        ItemStack mainStack = player.getMainHandItem();
        ItemStack offStack = player.getOffhandItem();
        boolean mainGun = mainStack.getItem() instanceof GunItem;
        boolean offGun = offStack.getItem() instanceof GunItem;
        if (!mainGun && !offGun) {
            return;
        }

        boolean aiming = player.isCrouching();
        boolean dualWield = mainGun && offGun;
        boolean gunInMainOnly = mainGun && !offGun;
        boolean gunInOffOnly = offGun && !mainGun;
        boolean mainIsPistol = mainGun && isShortFlintlock(mainStack);
        boolean offIsPistol = offGun && isShortFlintlock(offStack);
        float raise = RecoilClient.getGunRaise(partialTick);

        if (hand == InteractionHand.MAIN_HAND && mainGun) {
            poseStack.translate(0.0F, 0.0F, 0.12F);
            if (raise > 0.0001f) {
                poseStack.translate(0.0F, -0.03F * raise, 0.08F * raise);
                poseStack.mulPose(Axis.XP.rotationDegrees(-10.0F * raise));
            }
        }
        if (hand == InteractionHand.OFF_HAND && offGun) {
            poseStack.translate(0.0F, 0.0F, 0.12F);
            if (raise > 0.0001f) {
                poseStack.translate(0.0F, -0.03F * raise, 0.08F * raise);
                poseStack.mulPose(Axis.XP.rotationDegrees(-10.0F * raise));
            }
        }

        if (!dualWield) {
            if (hand == InteractionHand.MAIN_HAND && gunInMainOnly && mainIsPistol) {
                poseStack.translate(-0.02F, 0.02F, 0.10F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(-5.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(-pitch * 0.35F));
                return;
            }
            if (hand == InteractionHand.OFF_HAND && gunInOffOnly && offIsPistol) {
                poseStack.translate(0.02F, 0.02F, 0.10F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(5.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(-pitch * 0.35F));
                return;
            }
        }

        if (dualWield) {
            if (hand == InteractionHand.MAIN_HAND && mainGun) {
                if (aiming) {
                    poseStack.translate(-0.10F, -0.04F, 0.08F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-8.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(-4.0F));
                } else {
                    poseStack.translate(-0.08F, -0.02F, 0.06F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-6.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(-2.5F));
                }
            }
            if (hand == InteractionHand.OFF_HAND && offGun) {
                if (aiming) {
                    poseStack.translate(0.10F, -0.04F, 0.08F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(8.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(-4.0F));
                } else {
                    poseStack.translate(0.08F, -0.02F, 0.06F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(6.0F));
                    poseStack.mulPose(Axis.XP.rotationDegrees(-2.5F));
                }
            }
            return;
        }

        if (hand == InteractionHand.MAIN_HAND && gunInMainOnly && aiming) {
            poseStack.translate(-0.10F, -0.02F, 0.02F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-2.5F));
            poseStack.mulPose(Axis.XP.rotationDegrees(-2.0F));
        }

        if (hand == InteractionHand.OFF_HAND && gunInOffOnly && aiming) {
            poseStack.translate(0.10F, -0.02F, 0.02F);
            poseStack.mulPose(Axis.YP.rotationDegrees(2.5F));
            poseStack.mulPose(Axis.XP.rotationDegrees(-2.0F));
        }

        if (gunInMainOnly && hand == InteractionHand.OFF_HAND && !mainIsPistol) {
            applySupportPose(poseStack, aiming, false);
        }
        if (gunInOffOnly && hand == InteractionHand.MAIN_HAND && !offIsPistol) {
            applySupportPose(poseStack, aiming, true);
        }
    }

    private static Object decisland$getBothHandsSelection() {
        if (decisland$bothHandsSelection != null) {
            return decisland$bothHandsSelection;
        }

        try {
            Class<?> cls = Class.forName("net.minecraft.client.renderer.ItemInHandRenderer$HandRenderSelection");
            @SuppressWarnings("unchecked")
            Class<? extends Enum<?>> enumCls = (Class<? extends Enum<?>>) cls;
            Object value = Enum.valueOf((Class) enumCls, "RENDER_BOTH_HANDS");
            decisland$bothHandsSelection = value;
            return value;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static void applySupportPose(PoseStack poseStack, boolean aiming, boolean mirror) {
        float sign = mirror ? -1.0F : 1.0F;
        if (aiming) {
            poseStack.translate(sign * 0.18F, -0.22F, 0.28F);
            poseStack.mulPose(Axis.YP.rotationDegrees(sign * 20.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(-18.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(sign * 10.0F));
        } else {
            poseStack.translate(sign * 0.14F, -0.18F, 0.24F);
            poseStack.mulPose(Axis.YP.rotationDegrees(sign * 14.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(-12.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(sign * 6.0F));
        }
    }

    private static boolean isShortFlintlock(ItemStack stack) {
        try {
            Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            return id != null && "short_flintlock".equals(id.getPath());
        } catch (Throwable ignored) {
            return false;
        }
    }
}
