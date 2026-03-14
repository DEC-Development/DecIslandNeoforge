package com.dec.decisland.client.model

import com.dec.decisland.DecIsland
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.resources.Identifier

class EmptyModel<T : LivingEntityRenderState>(root: ModelPart) : EntityModel<T>(root) {
    companion object {
        @JvmField
        val LAYER_LOCATION: ModelLayerLocation =
            ModelLayerLocation(Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "empty"), "main")

        @JvmStatic
        fun createBodyLayer(): LayerDefinition {
            val meshDefinition = MeshDefinition()
            return LayerDefinition.create(meshDefinition, 1, 1)
        }
    }
}
