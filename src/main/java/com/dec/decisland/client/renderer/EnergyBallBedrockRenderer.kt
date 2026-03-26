package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.projectile.EnergyBall
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.Identifier

class EnergyBallBedrockRenderer(context: EntityRendererProvider.Context) :
    BedrockProjectileRenderer<EnergyBall>(
        context,
        GEOMETRY_LOCATION,
        ANIMATION_LOCATION,
        FLY_ANIMATION,
        TEXTURE_LOCATION,
        SCALE,
    ) {

    companion object {
        private const val SCALE: Float = 0.8f
        private const val FLY_ANIMATION: String = "animation.energy_ball.fly"

        private val GEOMETRY_LOCATION: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/entity/energy_ball.geometry.json")
        private val ANIMATION_LOCATION: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/animations/entity/energy_ball.animation.json")
        private val TEXTURE_LOCATION: Identifier =
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/energy_ball.png")
    }
}
