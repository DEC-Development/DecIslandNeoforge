package com.dec.decisland.events;

import com.dec.decisland.DecIsland;
import com.dec.decisland.client.gui.ClientManaOverlay;
import com.dec.decisland.client.renderer.EmptyRenderer;
import com.dec.decisland.client.renderer.MaskRenderer;
import com.dec.decisland.entity.ModEntities;
import com.dec.decisland.item.ModItems;
import com.dec.decisland.item.custom.Mask;
import com.dec.decisland.particles.ModParticles;
import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@EventBusSubscriber(modid = DecIsland.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {
    public ModClientEvents(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.BLIZZARD_ENERGY.get(), EmptyRenderer::new);
        EntityRenderers.register(ModEntities.THROWN_ASH_PUFFERFISH.get(), ThrownItemRenderer::new);
        EntityRenderers.register(ModEntities.ENERGY_RAY.get(), EmptyRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        // 批量注册粒子工厂
        ModParticles.getParticleConfigs().forEach(config -> {
            if (config.factoryProvider != null && config.registeredType != null) {
                // 使用注册后的粒子类型
                config.factoryProvider.register(event, config.registeredType.get());
            }
        });
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        // 获取屏幕尺寸并渲染mana overlay
        ClientManaOverlay.render(event.getGuiGraphics(), mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
    }
}
