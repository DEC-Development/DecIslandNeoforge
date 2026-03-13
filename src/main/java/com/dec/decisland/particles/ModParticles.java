package com.dec.decisland.particles;

import com.dec.decisland.DecIsland;
import com.dec.decisland.particles.custom.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, DecIsland.MOD_ID);

    private static final List<ParticleConfig> PARTICLE_CONFIGS = new ArrayList<>();

    // 使用配置方式注册粒子
    public static final Supplier<SimpleParticleType> BLIZZARD_WAKE_PARTICLE = registerParticle(
            new ParticleConfig.Builder("blizzard_wake_particle")
                    .factoryProvider((event, particleType) ->
                            event.registerSpriteSet(particleType, BlizzardWakeParticle.Provider::new))
                    .build()
    );

    public static final Supplier<SimpleParticleType> ABSOLUTE_ZERO_PARTICLE = registerParticle(
            new ParticleConfig.Builder("absolute_zero_particle")
                    .factoryProvider((event, particleType) ->
                            event.registerSpriteSet(particleType, KatanaParticle.Provider::new))
                    .build()
    );

    public static final Supplier<SimpleParticleType> BAMBOO_KATANA_PARTICLE = registerParticle(
            new ParticleConfig.Builder("bamboo_katana_particle")
                    .factoryProvider((event, particleType) ->
                            event.registerSpriteSet(particleType, KatanaParticle.Provider::new))
                    .build()
    );

    public static final Supplier<SimpleParticleType> HARD_BAMBOO_KATANA_PARTICLE = registerParticle(
            new ParticleConfig.Builder("hard_bamboo_katana_particle")
                    .factoryProvider((event, particleType) ->
                            event.registerSpriteSet(particleType, KatanaParticle.Provider::new))
                    .build()
    );

    public static final Supplier<SimpleParticleType> ABSOLUTE_ZERO_SMOKE_SINGLE_PARTICLE = registerParticle(
            new ParticleConfig.Builder("absolute_zero_smoke_single_particle")
                    .factoryProvider((event, particleType) ->
                            event.registerSpriteSet(particleType, AbsoluteZeroSmokeSingleParticle.Provider::new))
                    .build()
    );

    public static final Supplier<SimpleParticleType> ABSOLUTE_ZERO_SMOKE_SMALL_PARTICLE = registerParticle(
            new ParticleConfig.Builder("absolute_zero_smoke_small_particle")
                    .factoryProvider((event, particleType) ->
                            event.registerSpecial(particleType, new AbsoluteZeroSmokeSeedParticle.Provider(10, 3.0, 0.1)))
                    .build()
    );

    public static final Supplier<SimpleParticleType> ABSOLUTE_ZERO_SMOKE_BIG_PARTICLE = registerParticle(
            new ParticleConfig.Builder("absolute_zero_smoke_big_particle")
                    .factoryProvider((event, particleType) ->
                            event.registerSpecial(particleType, new AbsoluteZeroSmokeSeedParticle.Provider(30, 5.0, 0.1)))
                    .build()
    );

    /**
     * 使用配置注册粒子
     * @param config 粒子配置
     * @return 注册后的粒子类型
     */
    public static Supplier<SimpleParticleType> registerParticle(ParticleConfig config) {
        PARTICLE_CONFIGS.add(config);
        DeferredHolder<ParticleType<?>, ParticleType<?>> holder = PARTICLE_TYPES.register(config.name, config.sup);
        // 保存注册的粒子类型到配置中，确保工厂注册时使用的是同一个实例
        config.registeredType = () -> (SimpleParticleType) holder.get();
        return config.registeredType;
    }

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    public static List<ParticleConfig> getParticleConfigs() {
        return PARTICLE_CONFIGS;
    }
}
