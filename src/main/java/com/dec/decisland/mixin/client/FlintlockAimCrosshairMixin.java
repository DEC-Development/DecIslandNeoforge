package com.dec.decisland.mixin.client;

import com.dec.decisland.DecIsland;
import com.dec.decisland.item.gun.GunItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class FlintlockAimCrosshairMixin {
    private static final Identifier CROSSHAIR_TEX =
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/gui/flintlock_crosshair.png");

    @Inject(method = "renderCrosshair(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", at = @At("HEAD"), cancellable = true)
    private void decisland$flintlockAimCrosshair(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !mc.player.isCrouching()) {
            return;
        }

        ItemStack stack = mc.player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) {
            stack = mc.player.getOffhandItem();
            if (!(stack.getItem() instanceof GunItem)) {
                return;
            }
        }

        int x = (graphics.guiWidth() - 16) / 2;
        int y = (graphics.guiHeight() - 16) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, CROSSHAIR_TEX, x, y, 0.0f, 0.0f, 16, 16, 16, 16);
        ci.cancel();
    }
}
