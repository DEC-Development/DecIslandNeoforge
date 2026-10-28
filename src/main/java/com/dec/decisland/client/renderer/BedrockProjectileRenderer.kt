package com.dec.decisland.client.renderer

import com.dec.decisland.client.bedrock.model.BedrockAnimatedEntityModel
import com.dec.decisland.client.bedrock.model.BedrockEntityAssets
import com.dec.decisland.client.bedrock.model.BedrockTextureMeshSupport
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

open class BedrockProjectileRenderer<T : Entity>(
    context: EntityRendererProvider.Context,
    geometryLocation: ResourceLocation,
    animationLocation: ResourceLocation?,
    animationName: String?,
    private val textureLocation: ResourceLocation,
    private val scale: Float,
    private val useEnergySwirl: Boolean = true,
    private val renderWithoutCulling: Boolean = false,
    private val additionalAnimations: List<Pair<ResourceLocation, String>> = emptyList(),
    private val materialTextures: Map<String, ResourceLocation> = emptyMap(),
) : EntityRenderer<T>(context) {
    /**
     * Constructor for a static Bedrock geometry.  Bedrock client entities do
     * not require an animation file; the geometry must still go through the
     * same model/material pipeline as animated entities.
     */
    constructor(
        context: EntityRendererProvider.Context,
        geometryLocation: ResourceLocation,
        textureLocation: ResourceLocation,
        scale: Float,
        useEnergySwirl: Boolean = true,
        renderWithoutCulling: Boolean = false,
        additionalAnimations: List<Pair<ResourceLocation, String>> = emptyList(),
        materialTextures: Map<String, ResourceLocation> = emptyMap(),
    ) : this(
        context,
        geometryLocation,
        null,
        null,
        textureLocation,
        scale,
        useEnergySwirl,
        renderWithoutCulling,
        additionalAnimations,
        materialTextures,
    )

    private val geometry = BedrockEntityAssets.geometry(geometryLocation)
    private val itemRenderer = context.itemRenderer
    private val textureMeshItem: ItemStack? =
        BedrockTextureMeshSupport.itemStackForTextureMesh(geometry, textureLocation, materialTextures)
    private val model = BedrockAnimatedEntityModel<T>(
        geometry,
        listOfNotNull(
            animationLocation?.let { location ->
                animationName?.let { name -> BedrockEntityAssets.animation(location, name) }
            },
        ) +
            additionalAnimations.map { (location, name) -> BedrockEntityAssets.animation(location, name) },
        textureLocation,
        materialTextures,
    )

    init {
        shadowRadius = 0.0f
        shadowStrength = 0.0f
    }

    override fun getTextureLocation(entity: T): ResourceLocation = textureLocation

    override fun render(
        entity: T,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
    ) {
        poseStack.pushPose()

        if (textureMeshItem != null) {
            renderTextureMeshItem(
                entity,
                partialTick,
                poseStack,
                bufferSource,
                packedLight,
                textureMeshItem,
            )
            poseStack.popPose()
            super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
            return
        }

        // Bedrock geometry uses a 24-pixel model origin. Keep this correction
        // inside the projectile transform so it follows the projectile pitch.
        // 模型烘焙锚点为脚底（y=24），直接渲染的实体按缩放比例补偿对齐
        poseStack.translate(0.0, -1.5 * scale, 0.0)
        poseStack.scale(scale, scale, scale)
        val yaw = Mth.rotLerp(partialTick, entity.yRotO, entity.yRot)
        val pitch = Mth.rotLerp(partialTick, entity.xRotO, entity.xRot)
        model.applyAnimation(
            (entity.tickCount + partialTick) / 20.0f,
            mapOf(
                "query.target_x_rotation" to pitch.toDouble(),
                "query.body_y_rotation" to yaw.toDouble(),
                "query.ground_speed" to entity.deltaMovement.horizontalDistance(),
            ),
        )
        val vertexConsumer = if (useEnergySwirl) {
            val swirlOffset = ((entity.tickCount + partialTick) * 0.01f) % 1.0f
            bufferSource.getBuffer(RenderType.energySwirl(textureLocation, swirlOffset, swirlOffset))
        } else if (renderWithoutCulling) {
            bufferSource.getBuffer(RenderType.entityCutoutNoCull(textureLocation))
        } else {
            bufferSource.getBuffer(RenderType.entityCutout(textureLocation))
        }
        model.renderWithMaterials(
            poseStack,
            vertexConsumer,
            bufferSource,
            packedLight,
            OverlayTexture.NO_OVERLAY,
            -1,
        )
        poseStack.popPose()
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
    }

    private fun renderTextureMeshItem(
        entity: T,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        itemStack: ItemStack,
    ) {
        // Match the proven DartRenderer path used by amethyst_dart. The
        // generated item model supplies its own correct extrusion and render
        // type, so the Bedrock texture_mesh polygons are not rebuilt here.
        val yaw = Mth.rotLerp(partialTick, entity.yRotO, entity.yRot)
        val pitch = Mth.rotLerp(partialTick, entity.xRotO, entity.xRot)
        val speed = entity.deltaMovement.length().toFloat()
        val spin = ((entity.tickCount.toFloat() + partialTick) * speed * 45.0f) % 360.0f

        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 90.0f))
        poseStack.mulPose(Axis.ZP.rotationDegrees(pitch - spin))
        itemRenderer.renderStatic(
            itemStack,
            ItemDisplayContext.FIXED,
            packedLight,
            OverlayTexture.NO_OVERLAY,
            poseStack,
            bufferSource,
            entity.level(),
            entity.id,
        )
    }

    override fun getBlockLightLevel(entity: T, pos: BlockPos): Int = 15
}
