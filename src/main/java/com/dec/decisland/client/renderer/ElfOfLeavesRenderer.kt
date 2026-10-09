package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.VexRenderer
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.monster.Vex

/**
 * 绿叶精灵渲染器：复用原版 VexRenderer（飞行 + 俯冲姿态），仅替换贴图。
 * VexRenderer 默认 scale 0.4，与基岩版 geometry.vex + scale 0.4 一致。
 */
class ElfOfLeavesRenderer(context: EntityRendererProvider.Context) : VexRenderer(context) {
    override fun getTextureLocation(entity: Vex): ResourceLocation = TEXTURE_LOCATION

    companion object {
        private val TEXTURE_LOCATION: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/elf_of_leaves.png")
    }
}
