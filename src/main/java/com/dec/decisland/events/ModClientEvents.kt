package com.dec.decisland.events

import com.dec.decisland.DecIsland
import com.dec.decisland.client.RecoilClient
import com.dec.decisland.client.bedrock.BedrockEmitterManager
import com.dec.decisland.client.gui.ClientManaOverlay
import com.dec.decisland.client.renderer.EmptyRenderer
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.particles.ModParticles
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent
import net.neoforged.neoforge.client.event.RenderGuiEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent

@EventBusSubscriber(modid = DecIsland.MOD_ID, value = [Dist.CLIENT])
object ModClientEvents {
    @SubscribeEvent
    @JvmStatic
    fun onClientSetup(event: FMLClientSetupEvent) {
        EntityRenderers.register(ModEntities.BLIZZARD_ENERGY.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.THROWN_ASH_PUFFERFISH.get(), ::ThrownItemRenderer)
        EntityRenderers.register(ModEntities.ENERGY_RAY.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.BULLET_BY_FLINTLOCK.get(), ::ThrownItemRenderer)
        EntityRenderers.register(ModEntities.BULLET_BY_FLINTLOCK_PRO.get(), ::ThrownItemRenderer)
        EntityRenderers.register(ModEntities.BULLET_BY_SHORT_FLINTLOCK.get(), ::ThrownItemRenderer)
        EntityRenderers.register(ModEntities.BULLET_BY_EVERLASTING_WINTER_FLINTLOCK.get(), ::ThrownItemRenderer)
        EntityRenderers.register(ModEntities.BULLET_BY_GHOST_FLINTLOCK.get(), ::ThrownItemRenderer)
        EntityRenderers.register(ModEntities.BULLET_BY_LAVA_FLINTLOCK.get(), ::ThrownItemRenderer)
        EntityRenderers.register(ModEntities.BULLET_BY_STAR_FLINTLOCK.get(), ::ThrownItemRenderer)
        EntityRenderers.register(ModEntities.BULLET_BY_STORM_FLINTLOCK.get(), ::ThrownItemRenderer)
    }

    @SubscribeEvent
    @JvmStatic
    fun registerParticleFactories(event: RegisterParticleProvidersEvent) {
        ModParticles.getParticleConfigs().forEach { config ->
            if (config.factoryProvider != null && config.registeredType != null) {
                config.factoryProvider!!.register(event, config.registeredType!!.get())
            }
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
    }

    @SubscribeEvent
    @JvmStatic
    fun registerRenderers(event: EntityRenderersEvent.RegisterRenderers) {
    }

    @SubscribeEvent
    @JvmStatic
    fun onRenderGuiOverlay(event: RenderGuiEvent.Pre) {
        val mc = Minecraft.getInstance()
        RecoilClient.tick(mc)
        mc.level?.let(BedrockEmitterManager::tick)
        ClientManaOverlay.render(
            event.guiGraphics,
            mc.window.guiScaledWidth,
            mc.window.guiScaledHeight,
        )
    }
}
