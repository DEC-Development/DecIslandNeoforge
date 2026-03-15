package com.dec.decisland.mixin.client;

import com.dec.decisland.item.gun.GunItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AvatarRenderer.class)
public class GunHoldThirdPersonPoseMixin {
    @Inject(
        method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void decisland$gunHoldArmPose(
        Avatar player,
        ItemStack stack,
        InteractionHand hand,
        CallbackInfoReturnable<HumanoidModel.ArmPose> cir
    ) {
        if (stack.getItem() instanceof GunItem) {
            cir.setReturnValue(isShortFlintlock(stack) ? HumanoidModel.ArmPose.ITEM : HumanoidModel.ArmPose.BOW_AND_ARROW);
            return;
        }

        ItemStack other = hand == InteractionHand.OFF_HAND ? player.getMainHandItem() : player.getOffhandItem();
        if (stack.isEmpty() && other.getItem() instanceof GunItem && !isShortFlintlock(other)) {
            cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
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
