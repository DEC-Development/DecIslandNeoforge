package com.dec.decisland.events

import com.dec.decisland.DecIsland
import com.dec.decisland.client.DizzinessClient
import com.dec.decisland.client.RecoilClient
import com.dec.decisland.client.bedrock.BedrockEmitterManager
import com.dec.decisland.client.gui.ClientManaOverlay
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RenderGuiEvent

@EventBusSubscriber(modid = DecIsland.MOD_ID, value = [Dist.CLIENT])
object ModClientGameEvents {
    @SubscribeEvent
    @JvmStatic
    fun onRenderGuiOverlay(event: RenderGuiEvent.Pre) {
        val mc = Minecraft.getInstance()
        RecoilClient.tick(mc)
        DizzinessClient.tick(mc)
        ClientManaOverlay.render(
            event.guiGraphics,
            mc.window.guiScaledWidth,
            mc.window.guiScaledHeight,
        )
    }

    @SubscribeEvent
    @JvmStatic
    fun onClientTick(event: ClientTickEvent.Post) {
        val mc = Minecraft.getInstance()
        mc.level?.let(BedrockEmitterManager::tick)
        mc.player?.let(AccessoryCombatEffects::tickClientPlayer)
    }
}
