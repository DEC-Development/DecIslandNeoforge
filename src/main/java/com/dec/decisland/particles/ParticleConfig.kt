package com.dec.decisland.particles

import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent
import java.util.function.Supplier

class ParticleConfig(
    @JvmField val name: String,
    @JvmField val sup: Supplier<ParticleType<*>>,
    @JvmField val factoryProvider: ParticleFactoryProvider?,
) {
    @JvmField
    var registeredType: Supplier<SimpleParticleType>? = null

    class Builder(@JvmField val name: String) {
        private var sup: Supplier<ParticleType<*>> = Supplier { SimpleParticleType(true) }
        private var factoryProvider: ParticleFactoryProvider? = null

        fun sup(sup: Supplier<ParticleType<*>>): Builder = apply {
            this.sup = sup
        }

        fun factoryProvider(factoryProvider: ParticleFactoryProvider): Builder = apply {
            this.factoryProvider = factoryProvider
        }

        fun build(): ParticleConfig = ParticleConfig(name, sup, factoryProvider)
    }

    fun interface ParticleFactoryProvider {
        fun register(event: RegisterParticleProvidersEvent, particleType: SimpleParticleType)
    }
}
