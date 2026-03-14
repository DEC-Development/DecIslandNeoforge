package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.client.model.EmptyModel
import com.dec.decisland.client.renderer.state.NormalRenderState
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity

class EmptyRenderer(context: EntityRendererProvider.Context) : EntityRenderer<Entity, NormalRenderState>(context) {
    private val model = EmptyModel<LivingEntityRenderState>(context.bakeLayer(EmptyModel.LAYER_LOCATION))

    override fun createRenderState(): NormalRenderState = NormalRenderState()

    fun getTextureLocation(entity: Entity): Identifier =
        Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/empty.png")
}
