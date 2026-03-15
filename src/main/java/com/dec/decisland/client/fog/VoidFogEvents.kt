package com.dec.decisland.client.fog

import com.dec.decisland.worldgen.ModDimensions
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.world.level.material.FogType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.ViewportEvent
import kotlin.math.max

object VoidFogEvents {
    @SubscribeEvent
    fun onRenderFog(event: ViewportEvent.RenderFog) {
        if (event.type != FogType.NONE) {
            return
        }

        val camera = event.camera
        val entity = camera.entity() ?: return
        val level = entity.level()
        if (level !is ClientLevel || level.dimension() != ModDimensions.VOID_LEVEL) {
            return
        }

        VoidFogConfig.ensureLoaded(Minecraft.getInstance().resourceManager)

        val biomeId = level.getBiome(camera.blockPosition())
            .unwrapKey()
            .map { it.identifier() }
            .orElse(null)
        val global = VoidFogConfig.globalSpec()
        val biome = VoidFogConfig.specFor(biomeId)

        val near = max(0f, event.nearPlaneDistance * global.startMul * biome.startMul)
        val far = max(near + 0.001f, event.farPlaneDistance * global.endMul * biome.endMul)
        event.setNearPlaneDistance(near)
        event.setFarPlaneDistance(far)
    }

    @SubscribeEvent
    fun onComputeFogColor(event: ViewportEvent.ComputeFogColor) {
        val camera = event.camera
        val entity = camera.entity() ?: return
        val level = entity.level()
        if (level !is ClientLevel || level.dimension() != ModDimensions.VOID_LEVEL) {
            return
        }

        VoidFogConfig.ensureLoaded(Minecraft.getInstance().resourceManager)

        val biomeId = level.getBiome(camera.blockPosition())
            .unwrapKey()
            .map { it.identifier() }
            .orElse(null)
        val rgb = VoidFogConfig.tintRgb(event.red, event.green, event.blue, biomeId)
        event.red = rgb[0]
        event.green = rgb[1]
        event.blue = rgb[2]
    }
}
