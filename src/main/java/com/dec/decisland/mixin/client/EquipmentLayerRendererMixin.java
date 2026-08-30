package com.dec.decisland.mixin.client;

import com.dec.decisland.client.model.FashionArmorModel;
import com.dec.decisland.item.category.Fashion;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EquipmentLayerRenderer.class)
public class EquipmentLayerRendererMixin {
    @Inject(
        method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
        at = @At("TAIL")
    )
    private <S> void decisland$renderFashionTextureMeshSides(
        EquipmentClientInfo.LayerType layerType,
        ResourceKey<EquipmentAsset> assetKey,
        Model<? super S> model,
        S renderState,
        ItemStack stack,
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
        int packedLight,
        Identifier playerTexture,
        int outlineColor,
        int order,
        CallbackInfo ci
    ) {
        Fashion.Definition definition = Fashion.definitionOf(stack);
        if (definition == null || definition.getModelKind() != Fashion.ModelKind.GIANT_BAT_WINGS) {
            return;
        }
        Model<?> resolvedModel = IClientItemExtensions.of(stack).getGenericArmorModel(stack, layerType, model);
        if (!(resolvedModel instanceof FashionArmorModel<?> fashionModel)) {
            return;
        }

        fashionModel.submitTextureMeshSides(
            poseStack,
            submitNodeCollector,
            packedLight,
            stack
        );
    }
}
