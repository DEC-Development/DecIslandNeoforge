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
        if (this is CustomInertia) this.waterInertia else original

    @ModifyVariable(
        method = ["applyInertia"],
        at = At(value = "STORE", ordinal = 2),
        ordinal = 0,
    )
    private fun modifyAirInertia(original: Float): Float =
        if (this is CustomInertia) this.airInertia else original
}
