package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.client.bedrock.model.BedrockTextureMeshSupport
import com.dec.decisland.client.model.FashionArmorModel
import com.dec.decisland.item.category.Fashion
import net.minecraft.client.Minecraft
import net.minecraft.client.model.HumanoidModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

object FashionArmorClientItemExtensions : IClientItemExtensions {
    private val cachedModels = mutableMapOf<Fashion.ModelKind, FashionArmorModel<LivingEntity>>()

    @JvmStatic
    fun textureFor(stack: ItemStack): ResourceLocation = ResourceLocation.fromNamespaceAndPath(
        DecIsland.MOD_ID,
        "textures/armor/fashion/${stack.itemDescriptionIdPath()}.png",
    )

    override fun getHumanoidArmorModel(
        livingEntity: LivingEntity,
        itemStack: ItemStack,
        equipmentSlot: EquipmentSlot,
        original: HumanoidModel<*>,
    ): HumanoidModel<*> {
        val definition = Fashion.definitionOf(itemStack) ?: return original
        if (definition.modelKind == Fashion.ModelKind.VANILLA) {
            return original
        }

        val layerLocation = when (definition.modelKind) {
            Fashion.ModelKind.CLOTHES -> FashionArmorModel.CLOTHES_LAYER_LOCATION
            Fashion.ModelKind.CLOTHES_WITH_HOOD -> FashionArmorModel.CLOTHES_WITH_HOOD_LAYER_LOCATION
            Fashion.ModelKind.HAT -> FashionArmorModel.HAT_LAYER_LOCATION
            Fashion.ModelKind.WITCH_HAT -> FashionArmorModel.WITCH_HAT_LAYER_LOCATION
            Fashion.ModelKind.CHRISTMAS_CAP -> FashionArmorModel.CHRISTMAS_CAP_LAYER_LOCATION
            Fashion.ModelKind.WINGS_FROM_DEEP -> FashionArmorModel.WINGS_FROM_DEEP_LAYER_LOCATION
            Fashion.ModelKind.GIANT_BAT_WINGS -> FashionArmorModel.GIANT_BAT_WINGS_LAYER_LOCATION
            Fashion.ModelKind.FOLLOWING_PARTICLE -> FashionArmorModel.FOLLOWING_PARTICLE_LAYER_LOCATION
            Fashion.ModelKind.VANILLA -> return original
        }

        if (definition.modelKind == Fashion.ModelKind.GIANT_BAT_WINGS) {
            val baked = BedrockTextureMeshSupport.ensureHumanoidRoot(
                Minecraft.getInstance().entityModels.bakeLayer(layerLocation),
            )
            FashionArmorModel.attachTextureMeshes(baked, definition.modelKind)
            return FashionArmorModel<LivingEntity>(baked)
        }

        return cachedModels.getOrPut(definition.modelKind) {
            val baked = BedrockTextureMeshSupport.ensureHumanoidRoot(
                Minecraft.getInstance().entityModels.bakeLayer(layerLocation),
            )
            FashionArmorModel.attachTextureMeshes(baked, definition.modelKind)
            FashionArmorModel<LivingEntity>(baked)
        }
    }

    private fun ItemStack.itemDescriptionIdPath(): String =
        net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item).path
}
