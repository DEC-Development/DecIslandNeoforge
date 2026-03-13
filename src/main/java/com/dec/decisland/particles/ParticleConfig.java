package com.dec.decisland.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.function.Supplier;

public class ParticleConfig {
    public String name;
    public Supplier<ParticleType<?>> sup;
    public Supplier<SimpleParticleType> registeredType; // 添加字段保存注册后的粒子类型

    // 粒子工厂提供者配置
    public ParticleFactoryProvider factoryProvider;

    public ParticleConfig(String name, Supplier<ParticleType<?>> sup, ParticleFactoryProvider factoryProvider) {
        this.name = name;
        this.sup = sup;
        this.factoryProvider = factoryProvider;
    }

    public static class Builder {
        private String name;
        private Supplier<ParticleType<?>> sup = () -> new SimpleParticleType(true);
        private ParticleFactoryProvider factoryProvider;

        public Builder(String name) {
            this.name = name;
        }

        public Builder sup(Supplier<ParticleType<?>> sup) {
            this.sup = sup;
            return this;
        }

        public Builder factoryProvider(ParticleFactoryProvider factoryProvider) {
            this.factoryProvider = factoryProvider;
            return this;
        }

        public ParticleConfig build() {
            return new ParticleConfig(name, sup, factoryProvider);
        }
    }

    // 粒子工厂提供者接口
    public interface ParticleFactoryProvider {
        void register(RegisterParticleProvidersEvent event, SimpleParticleType particleType);
    }
}
