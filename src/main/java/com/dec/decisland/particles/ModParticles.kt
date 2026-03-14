package com.dec.decisland.particles

import com.dec.decisland.DecIsland
import com.dec.decisland.particles.custom.AbsoluteZeroSmokeSeedParticle
import com.dec.decisland.particles.custom.AbsoluteZeroSmokeSingleParticle
import com.dec.decisland.particles.custom.BlizzardWakeParticle
import com.dec.decisland.particles.custom.KatanaParticle
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModParticles {
    @JvmField
    val PARTICLE_TYPES: DeferredRegister<ParticleType<*>> =
        DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, DecIsland.MOD_ID)

    private val particleConfigs = mutableListOf<ParticleConfig>()

    @JvmField
    val BLIZZARD_WAKE_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("blizzard_wake_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, BlizzardWakeParticle::Provider) }
            .build(),
    )

    @JvmField
    val ABSOLUTE_ZERO_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("absolute_zero_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, KatanaParticle::Provider) }
            .build(),
    )

    @JvmField
    val BAMBOO_KATANA_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("bamboo_katana_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, KatanaParticle::Provider) }
            .build(),
    )

    @JvmField
    val HARD_BAMBOO_KATANA_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("hard_bamboo_katana_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, KatanaParticle::Provider) }
            .build(),
    )

    @JvmField
    val ABSOLUTE_ZERO_SMOKE_SINGLE_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("absolute_zero_smoke_single_particle")
            .factoryProvider { event, particleType -> event.registerSpriteSet(particleType, AbsoluteZeroSmokeSingleParticle::Provider) }
            .build(),
    )

    @JvmField
    val ABSOLUTE_ZERO_SMOKE_SMALL_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("absolute_zero_smoke_small_particle")
            .factoryProvider { event, particleType ->
                event.registerSpecial(particleType, AbsoluteZeroSmokeSeedParticle.Provider(10, 3.0, 0.1))
            }
            .build(),
    )

    @JvmField
    val ABSOLUTE_ZERO_SMOKE_BIG_PARTICLE: Supplier<SimpleParticleType> = registerParticle(
        ParticleConfig.Builder("absolute_zero_smoke_big_particle")
            .factoryProvider { event, particleType ->
                event.registerSpecial(particleType, AbsoluteZeroSmokeSeedParticle.Provider(30, 5.0, 0.1))
            }
            .build(),
    )

    @JvmStatic
    fun registerParticle(config: ParticleConfig): Supplier<SimpleParticleType> {
        particleConfigs.add(config)
        val holder: DeferredHolder<ParticleType<*>, ParticleType<*>> = PARTICLE_TYPES.register(config.name, config.sup)
        config.registeredType = Supplier { holder.get() as SimpleParticleType }
        return config.registeredType!!
    }

    @JvmStatic
    fun register(eventBus: IEventBus) {
        PARTICLE_TYPES.register(eventBus)
    }

    @JvmStatic
    fun getParticleConfigs(): List<ParticleConfig> = particleConfigs
}
