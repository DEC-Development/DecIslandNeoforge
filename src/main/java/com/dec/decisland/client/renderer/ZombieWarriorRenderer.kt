package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.ZombieRenderer
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.resources.ResourceLocation

// 复用原版 ZombieRenderer（人形模型 + 行走/挥击动画），仅替换为僵尸战士贴图
class ZombieWarriorRenderer(context: EntityRendererProvider.Context) : ZombieRenderer(context) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation = TEXTURE_LOCATION

    companion object {
        val TEXTURE_LOCATION: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/zombie_warrior.png")
    }
}
