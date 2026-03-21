package com.dec.decisland.mixin

import com.dec.decisland.api.CustomInertia
import net.minecraft.world.entity.projectile.ThrowableProjectile
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.ModifyVariable

@Mixin(ThrowableProjectile::class)
abstract class ThrowableProjectileMixin {
    @ModifyVariable(
        method = ["applyInertia"],
        at = At(value = "STORE", ordinal = 1),
        ordinal = 0,
    )
    private fun modifyWaterInertia(original: Float): Float =
        if (this is CustomInertia) resolveInertia(this.waterInertia, original) else original

    @ModifyVariable(
        method = ["applyInertia"],
        at = At(value = "STORE", ordinal = 2),
        ordinal = 0,
    )
    private fun modifyAirInertia(original: Float): Float =
        if (this is CustomInertia) resolveInertia(this.airInertia, original) else original

    private fun resolveInertia(configured: Float, vanilla: Float): Float {
        if (configured <= 1.0f) {
            return configured
        }

        // Bedrock projectile inertia values above 1 mean "retain more velocity",
        // not "multiply velocity by >1 each tick" like vanilla would.
        return (vanilla + ((1.0f - vanilla) * (1.0f - (1.0f / configured)))).coerceAtMost(0.9999f)
    }
}
