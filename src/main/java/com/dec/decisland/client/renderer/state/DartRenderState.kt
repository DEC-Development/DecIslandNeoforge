package com.dec.decisland.client.renderer.state

import net.minecraft.client.renderer.entity.state.ThrownItemRenderState

class DartRenderState : ThrownItemRenderState() {
    var xRot: Float = 0.0f
    var yRot: Float = 0.0f
    var spin: Float = 0.0f
    var randomTilt: Float = 0.0f
}
