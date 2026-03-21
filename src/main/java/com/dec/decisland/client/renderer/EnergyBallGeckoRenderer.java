package com.dec.decisland.client.renderer;

import com.dec.decisland.DecIsland;
import com.dec.decisland.entity.projectile.EnergyBall;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public final class EnergyBallGeckoRenderer extends MagicBallGeckoRenderer<EnergyBall> {
    public EnergyBallGeckoRenderer(EntityRendererProvider.Context context) {
        super(
            context,
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/energy_ball.png"),
            0.8F,
            MaterialStyle.ENERGY_SWIRL,
            0xFF808080
        );
    }
}
