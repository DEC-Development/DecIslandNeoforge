package com.dec.decisland.events

import com.dec.decisland.DecIsland
import com.dec.decisland.client.DizzinessClient
import com.dec.decisland.client.RecoilClient
import com.dec.decisland.client.bedrock.BedrockEmitterManager
import com.dec.decisland.client.renderer.DartRenderer
import com.dec.decisland.client.renderer.EnergyBallGeckoRenderer
import com.dec.decisland.client.renderer.EmptyRenderer
import com.dec.decisland.client.renderer.MagicBallGeckoRenderer
import com.dec.decisland.client.gui.ClientManaOverlay
import com.dec.decisland.entity.ModEntities
import com.dec.decisland.entity.projectile.AmethystEnergyBall
import com.dec.decisland.entity.projectile.ConcentratedSoulBullet
import com.dec.decisland.entity.projectile.DeepEnergy
import com.dec.decisland.entity.projectile.FrozenEnergyBall
import com.dec.decisland.entity.projectile.GoldenEnergyBall
import com.dec.decisland.entity.projectile.JellyfishStaffProjectile
import com.dec.decisland.entity.projectile.LapisBullet
import com.dec.decisland.entity.projectile.PureEnergyBall
import com.dec.decisland.entity.projectile.SoulWakeBullet
import com.dec.decisland.entity.projectile.SpotsByBook
import com.dec.decisland.entity.projectile.StreamEnergyBall
import com.dec.decisland.entity.projectile.SpotsOverflow
import com.dec.decisland.entity.projectile.ThunderBall
import com.dec.decisland.entity.projectile.WaveEnergy
import com.dec.decisland.entity.projectile.WinterEnergy
import com.dec.decisland.entity.projectile.dart.ModDarts
import com.dec.decisland.particles.ModParticles
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.resources.Identifier
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent
import net.neoforged.neoforge.client.event.RenderGuiEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent

@EventBusSubscriber(modid = DecIsland.MOD_ID, value = [Dist.CLIENT])
object ModClientEvents {
    @SubscribeEvent
    @JvmStatic
    fun onClientSetup(event: FMLClientSetupEvent) {
        EntityRenderers.register(ModEntities.SNOW_ENERGY.get(), ::EmptyRenderer)
        ModDarts.ALL.forEach { definition ->
            EntityRenderers.register(definition.entityType(), ::DartRenderer)
        }
        EntityRenderers.register(ModEntities.THROWN_ASH_PUFFERFISH.get(), ::ThrownItemRenderer)
        EntityRenderers.register(ModEntities.STICKY_ASH.get()) { context ->
            ThrownItemRenderer(context, 1.35f, false)
        }
        EntityRenderers.register(ModEntities.FROZEN_BALL.get()) { context ->
            ThrownItemRenderer(context, 1.2f, false)
        }
        EntityRenderers.register(ModEntities.MIND_CONTROLLER.get()) { context ->
            ThrownItemRenderer(context, 1.15f, false)
        }
        EntityRenderers.register(ModEntities.MUDDY_BALL.get()) { context ->
            ThrownItemRenderer(context, 1.15f, false)
        }
        EntityRenderers.register(ModEntities.FIREFLY_BOTTLE.get()) { context ->
            ThrownItemRenderer(context, 1.0f, false)
        }
        EntityRenderers.register(ModEntities.SMOKE_BOMB.get()) { context ->
            ThrownItemRenderer(context, 1.0f, false)
        }
        EntityRenderers.register(ModEntities.GAS_BOMB.get()) { context ->
            ThrownItemRenderer(context, 1.0f, false)
        }
        EntityRenderers.register(ModEntities.THROWABLE_BOMB.get()) { context ->
            ThrownItemRenderer(context, 1.0f, false)
        }
        EntityRenderers.register(ModEntities.ENERGY_BALL.get(), ::EnergyBallGeckoRenderer)
        EntityRenderers.register(ModEntities.AMETHYST_ENERGY_BALL.get()) { context ->
            MagicBallGeckoRenderer<AmethystEnergyBall>(context, entityTexture("amethyst_energy_ball"), 0.8f)
        }
        EntityRenderers.register(ModEntities.GOLDEN_ENERGY_BALL.get()) { context ->
            MagicBallGeckoRenderer<GoldenEnergyBall>(context, entityTexture("golden_energy_ball"), 0.8f)
        }
        EntityRenderers.register(ModEntities.LAPIS_BULLET.get()) { context ->
            ThrownItemRenderer<LapisBullet>(context, 1.0f, false)
        }
        EntityRenderers.register(ModEntities.CONCENTRATED_SOUL_BULLET.get()) { context ->
            MagicBallGeckoRenderer<ConcentratedSoulBullet>(context, entityTexture("concentrated_soul_bullet"), 0.8f)
        }
        EntityRenderers.register(ModEntities.JELLYFISH_BY_JELLYFISH_STAFF.get()) { context ->
            ThrownItemRenderer<JellyfishStaffProjectile>(context, 1.0f, false)
        }
        EntityRenderers.register(ModEntities.SOUL_WAKE_BULLET.get()) { context ->
            ThrownItemRenderer<SoulWakeBullet>(context, 0.5f, false)
        }
        EntityRenderers.register(ModEntities.STREAM_ENERGY_BALL.get()) { context ->
            MagicBallGeckoRenderer<StreamEnergyBall>(context, entityTexture("stream_energy_ball"), 0.8f)
        }
        EntityRenderers.register(ModEntities.PURE_ENERGY_BALL.get()) { context ->
            MagicBallGeckoRenderer<PureEnergyBall>(context, entityTexture("pure_energy_ball"), 0.8f)
        }
        EntityRenderers.register(ModEntities.SPOTS_BY_BOOK.get()) { context ->
            MagicBallGeckoRenderer<SpotsByBook>(
                context,
                Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "spots"),
                entityTexture("spots"),
                1.0f,
            )
        }
        EntityRenderers.register(ModEntities.SPOTS_OVERFLOW.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.DEEP_ENERGY.get()) { context ->
            MagicBallGeckoRenderer<DeepEnergy>(context, entityTexture("deep_energy"), 0.8f)
        }
        EntityRenderers.register(ModEntities.FROZEN_ENERGY_BALL.get()) { context ->
            MagicBallGeckoRenderer<FrozenEnergyBall>(context, entityTexture("frozen_energy_ball"), 0.6f)
        }
        EntityRenderers.register(ModEntities.WINTER_ENERGY.get()) { context ->
            MagicBallGeckoRenderer<WinterEnergy>(context, entityTexture("winter_energy"), 0.8f)
        }
        EntityRenderers.register(ModEntities.THUNDER_BALL.get()) { context ->
            MagicBallGeckoRenderer<ThunderBall>(context, entityTexture("thunder_ball"), 0.9f)
        }
        EntityRenderers.register(ModEntities.ENERGY_RAY.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.AMETHYST_ENERGY_RAY.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.FROZEN_RAY.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.GROWING_ENERGY_RAY.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.NIGHTMARE_SPORE.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.NIGHTMARE_RAY.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.WAVE_ENERGY.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.BLIZZARD_ENERGY.get(), ::EmptyRenderer)
        EntityRenderers.register(ModEntities.STORM_FUSE.get(), ::EmptyRenderer)
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

    private fun entityTexture(path: String): Identifier =
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/$path.png")
}
