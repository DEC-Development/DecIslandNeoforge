package net.minecraft.client.renderer.entity.player;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.BeeStingerLayer;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ParrotOnShoulderLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwingAnimationType;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AvatarRenderer<AvatarlikeEntity extends Avatar & ClientAvatarEntity>
    extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {
    public AvatarRenderer(EntityRendererProvider.Context context, boolean slim) {
        super(context, new PlayerModel(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.5F);
        this.addLayer(
            new HumanoidArmorLayer<>(
                this,
                ArmorModelSet.bake(
                    slim ? ModelLayers.PLAYER_SLIM_ARMOR : ModelLayers.PLAYER_ARMOR,
                    context.getModelSet(),
                    p_477740_ -> new PlayerModel(p_477740_, slim)
                ),
                context.getEquipmentRenderer()
            )
        );
        this.addLayer(new PlayerItemInHandLayer<>(this));
        this.addLayer(new ArrowLayer<>(this, context));
        this.addLayer(new Deadmau5EarsLayer(this, context.getModelSet()));
        this.addLayer(new CapeLayer(this, context.getModelSet(), context.getEquipmentAssets()));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
        this.addLayer(new WingsLayer<>(this, context.getModelSet(), context.getEquipmentRenderer()));
        this.addLayer(new ParrotOnShoulderLayer(this, context.getModelSet()));
        this.addLayer(new SpinAttackEffectLayer(this, context.getModelSet()));
        this.addLayer(new BeeStingerLayer<>(this, context));
    }

    protected boolean shouldRenderLayers(AvatarRenderState p_447057_) {
        return !p_447057_.isSpectator;
    }

    public Vec3 getRenderOffset(AvatarRenderState p_446468_) {
        Vec3 vec3 = super.getRenderOffset(p_446468_);
        return p_446468_.isCrouching ? vec3.add(0.0, p_446468_.scale * -2.0F / 16.0, 0.0) : vec3;
    }

    private static HumanoidModel.ArmPose getArmPose(Avatar avatar, HumanoidArm arm) {
        ItemStack itemstack = avatar.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack itemstack1 = avatar.getItemInHand(InteractionHand.OFF_HAND);
        HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(avatar, itemstack, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(avatar, itemstack1, InteractionHand.OFF_HAND);
        if (humanoidmodel$armpose.isTwoHanded()) {
            humanoidmodel$armpose1 = itemstack1.isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        }

        return avatar.getMainArm() == arm ? humanoidmodel$armpose : humanoidmodel$armpose1;
    }

    private static HumanoidModel.ArmPose getArmPose(Avatar avatar, ItemStack handItem, InteractionHand hand) {
        var extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(handItem);
        var armPose = extensions.getArmPose(avatar, hand, handItem);
        if (armPose != null) {
            return armPose;
        }
        if (handItem.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else if (!avatar.swinging && handItem.is(Items.CROSSBOW) && CrossbowItem.isCharged(handItem)) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        } else {
            if (avatar.getUsedItemHand() == hand && avatar.getUseItemRemainingTicks() > 0) {
                ItemUseAnimation itemuseanimation = handItem.getUseAnimation();
                if (itemuseanimation == ItemUseAnimation.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (itemuseanimation == ItemUseAnimation.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (itemuseanimation == ItemUseAnimation.TRIDENT) {
                    return HumanoidModel.ArmPose.THROW_TRIDENT;
                }

                if (itemuseanimation == ItemUseAnimation.CROSSBOW) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (itemuseanimation == ItemUseAnimation.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if (itemuseanimation == ItemUseAnimation.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if (itemuseanimation == ItemUseAnimation.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                }

                if (itemuseanimation == ItemUseAnimation.SPEAR) {
                    return HumanoidModel.ArmPose.SPEAR;
                }
            }

            SwingAnimation swinganimation = handItem.get(DataComponents.SWING_ANIMATION);
            if (swinganimation != null && swinganimation.type() == SwingAnimationType.STAB && avatar.swinging) {
                return HumanoidModel.ArmPose.SPEAR;
            } else {
                return handItem.is(ItemTags.SPEARS) ? HumanoidModel.ArmPose.SPEAR : HumanoidModel.ArmPose.ITEM;
            }
        }
    }

    public Identifier getTextureLocation(AvatarRenderState p_469499_) {
        return p_469499_.skin.body().texturePath();
    }

    protected void scale(AvatarRenderState p_447098_, PoseStack p_445727_) {
        float f = 0.9375F;
        p_445727_.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    public void submit(AvatarRenderState p_433493_, PoseStack p_434615_, SubmitNodeCollector p_433768_, CameraRenderState p_450931_) {
        if (net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(new net.neoforged.neoforge.client.event.RenderPlayerEvent.Pre<>(p_433493_, this, p_433493_.partialTick, p_434615_, p_433768_)).isCanceled()) return;
        super.submit(p_433493_, p_434615_, p_433768_, p_450931_);
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(new net.neoforged.neoforge.client.event.RenderPlayerEvent.Post<>(p_433493_, this, p_433493_.partialTick, p_434615_, p_433768_));
    }

    protected void submitNameTag(AvatarRenderState p_447013_, PoseStack p_446358_, SubmitNodeCollector p_446248_, CameraRenderState p_451056_) {
        p_446358_.pushPose();
        int i = p_447013_.showExtraEars ? -10 : 0;
        if (p_447013_.scoreText != null) {
            p_446248_.submitNameTag(
                p_446358_,
                p_447013_.nameTagAttachment,
                i,
                p_447013_.scoreText,
                !p_447013_.isDiscrete,
                p_447013_.lightCoords,
                p_447013_.distanceToCameraSq,
                p_451056_
            );
            p_446358_.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
        }

        if (p_447013_.nameTag != null) {
            var event = new net.neoforged.neoforge.client.event.RenderNameTagEvent.DoRender(p_447013_, p_447013_.nameTag, this, p_446358_, p_446248_, p_451056_, p_447013_.partialTick);
            if (!net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(event).isCanceled())
            p_446248_.submitNameTag(
                p_446358_,
                p_447013_.nameTagAttachment,
                i,
                p_447013_.nameTag,
                !p_447013_.isDiscrete,
                p_447013_.lightCoords,
                p_447013_.distanceToCameraSq,
                p_451056_
            );
        }

        p_446358_.popPose();
    }

    public AvatarRenderState createRenderState() {
        return new AvatarRenderState();
    }

    public void extractRenderState(AvatarlikeEntity p_445469_, AvatarRenderState p_446472_, float p_445702_) {
        super.extractRenderState(p_445469_, p_446472_, p_445702_);
        HumanoidMobRenderer.extractHumanoidRenderState(p_445469_, p_446472_, p_445702_, this.itemModelResolver);
        p_446472_.leftArmPose = getArmPose(p_445469_, HumanoidArm.LEFT);
        p_446472_.rightArmPose = getArmPose(p_445469_, HumanoidArm.RIGHT);
        p_446472_.skin = p_445469_.getSkin();
        p_446472_.arrowCount = p_445469_.getArrowCount();
        p_446472_.stingerCount = p_445469_.getStingerCount();
        p_446472_.isSpectator = p_445469_.isSpectator();
        p_446472_.showHat = p_445469_.isModelPartShown(PlayerModelPart.HAT);
        p_446472_.showJacket = p_445469_.isModelPartShown(PlayerModelPart.JACKET);
        p_446472_.showLeftPants = p_445469_.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
        p_446472_.showRightPants = p_445469_.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
        p_446472_.showLeftSleeve = p_445469_.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
        p_446472_.showRightSleeve = p_445469_.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
        p_446472_.showCape = p_445469_.isModelPartShown(PlayerModelPart.CAPE);
        this.extractFlightData(p_445469_, p_446472_, p_445702_);
        this.extractCapeState(p_445469_, p_446472_, p_445702_);
        if (p_446472_.distanceToCameraSq < 100.0) {
            p_446472_.scoreText = p_445469_.belowNameDisplay();
        } else {
            p_446472_.scoreText = null;
        }

        p_446472_.parrotOnLeftShoulder = p_445469_.getParrotVariantOnShoulder(true);
        p_446472_.parrotOnRightShoulder = p_445469_.getParrotVariantOnShoulder(false);
        p_446472_.id = p_445469_.getId();
        p_446472_.showExtraEars = p_445469_.showExtraEars();
        p_446472_.heldOnHead.clear();
        if (p_446472_.isUsingItem) {
            ItemStack itemstack = p_445469_.getItemInHand(p_446472_.useItemHand);
            if (itemstack.canPerformAction(net.neoforged.neoforge.common.ItemAbilities.SPYGLASS_SCOPE)) {
                this.itemModelResolver.updateForLiving(p_446472_.heldOnHead, itemstack, ItemDisplayContext.HEAD, p_445469_);
            }
        }
    }

    protected boolean shouldShowName(AvatarlikeEntity p_451069_, double p_451150_) {
        return super.shouldShowName(p_451069_, p_451150_)
            && (p_451069_.shouldShowName() || p_451069_.hasCustomName() && p_451069_ == this.entityRenderDispatcher.crosshairPickEntity);
    }

    private void extractFlightData(AvatarlikeEntity entity, AvatarRenderState reusedState, float partialTick) {
        reusedState.fallFlyingTimeInTicks = entity.getFallFlyingTicks() + partialTick;
        Vec3 vec3 = entity.getViewVector(partialTick);
        Vec3 vec31 = entity.avatarState().deltaMovementOnPreviousTick().lerp(entity.getDeltaMovement(), partialTick);
        if (vec31.horizontalDistanceSqr() > 1.0E-5F && vec3.horizontalDistanceSqr() > 1.0E-5F) {
            reusedState.shouldApplyFlyingYRot = true;
            double d0 = vec31.horizontal().normalize().dot(vec3.horizontal().normalize());
            double d1 = vec31.x * vec3.z - vec31.z * vec3.x;
            reusedState.flyingYRot = (float)(Math.signum(d1) * Math.acos(Math.min(1.0, Math.abs(d0))));
        } else {
            reusedState.shouldApplyFlyingYRot = false;
            reusedState.flyingYRot = 0.0F;
        }
    }

    private void extractCapeState(AvatarlikeEntity entity, AvatarRenderState renderState, float partialTick) {
        ClientAvatarState clientavatarstate = entity.avatarState();
        double d0 = clientavatarstate.getInterpolatedCloakX(partialTick) - Mth.lerp((double)partialTick, entity.xo, entity.getX());
        double d1 = clientavatarstate.getInterpolatedCloakY(partialTick) - Mth.lerp((double)partialTick, entity.yo, entity.getY());
        double d2 = clientavatarstate.getInterpolatedCloakZ(partialTick) - Mth.lerp((double)partialTick, entity.zo, entity.getZ());
        float f = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
        double d3 = Mth.sin(f * (float) (Math.PI / 180.0));
        double d4 = -Mth.cos(f * (float) (Math.PI / 180.0));
        renderState.capeFlap = (float)d1 * 10.0F;
        renderState.capeFlap = Mth.clamp(renderState.capeFlap, -6.0F, 32.0F);
        renderState.capeLean = (float)(d0 * d3 + d2 * d4) * 100.0F;
        renderState.capeLean = renderState.capeLean * (1.0F - renderState.fallFlyingScale());
        renderState.capeLean = Mth.clamp(renderState.capeLean, 0.0F, 150.0F);
        renderState.capeLean2 = (float)(d0 * d4 - d2 * d3) * 100.0F;
        renderState.capeLean2 = Mth.clamp(renderState.capeLean2, -20.0F, 20.0F);
        float f1 = clientavatarstate.getInterpolatedBob(partialTick);
        float f2 = clientavatarstate.getInterpolatedWalkDistance(partialTick);
        renderState.capeFlap = renderState.capeFlap + Mth.sin(f2 * 6.0F) * 32.0F * f1;
    }

    /**
     * @deprecated Neo: use {@link #renderRightHand(PoseStack, SubmitNodeCollector,
     *             int, Identifier, boolean,
     *             net.minecraft.client.player.AbstractClientPlayer)} instead
     */
    @Deprecated
    public void renderRightHand(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, Identifier skinTexture, boolean renderSleeve) {
        this.renderRightHand(poseStack, nodeCollector, packedLight, skinTexture, renderSleeve, net.minecraft.client.Minecraft.getInstance().player);
    }

    public void renderRightHand(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, Identifier skinTexture, boolean renderSleeve, net.minecraft.client.player.AbstractClientPlayer player) {
        if(!net.neoforged.neoforge.client.ClientHooks.renderSpecificFirstPersonArm(poseStack, nodeCollector, packedLight, player, HumanoidArm.RIGHT))
        this.renderHand(poseStack, nodeCollector, packedLight, skinTexture, this.model.rightArm, renderSleeve);
    }

    /**
     * @deprecated Neo: use {@link #renderLeftHand(PoseStack, SubmitNodeCollector, int
     *             , Identifier, boolean,
     *             net.minecraft.client.player.AbstractClientPlayer)} instead
     */
    @Deprecated
    public void renderLeftHand(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, Identifier skinTexture, boolean renderSleeve) {
        this.renderLeftHand(poseStack, nodeCollector, packedLight, skinTexture, renderSleeve, net.minecraft.client.Minecraft.getInstance().player);
    }

    public void renderLeftHand(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, Identifier skinTexture, boolean renderSleeve, net.minecraft.client.player.AbstractClientPlayer player) {
        if(!net.neoforged.neoforge.client.ClientHooks.renderSpecificFirstPersonArm(poseStack, nodeCollector, packedLight, player, HumanoidArm.LEFT))
        this.renderHand(poseStack, nodeCollector, packedLight, skinTexture, this.model.leftArm, renderSleeve);
    }

    private void renderHand(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, Identifier skinTexture, ModelPart arm, boolean renderSleeve) {
        PlayerModel playermodel = this.getModel();
        arm.resetPose();
        arm.visible = true;
        playermodel.leftSleeve.visible = renderSleeve;
        playermodel.rightSleeve.visible = renderSleeve;
        playermodel.leftArm.zRot = -0.1F;
        playermodel.rightArm.zRot = 0.1F;
        nodeCollector.submitModelPart(arm, poseStack, RenderTypes.entityTranslucent(skinTexture), packedLight, OverlayTexture.NO_OVERLAY, null);
    }

    protected void setupRotations(AvatarRenderState p_446425_, PoseStack p_446166_, float p_445813_, float p_446015_) {
        float f = p_446425_.swimAmount;
        float f1 = p_446425_.xRot;
        if (p_446425_.isFallFlying) {
            super.setupRotations(p_446425_, p_446166_, p_445813_, p_446015_);
            float f2 = p_446425_.fallFlyingScale();
            if (!p_446425_.isAutoSpinAttack) {
                p_446166_.mulPose(Axis.XP.rotationDegrees(f2 * (-90.0F - f1)));
            }

            if (p_446425_.shouldApplyFlyingYRot) {
                p_446166_.mulPose(Axis.YP.rotation(p_446425_.flyingYRot));
            }
        } else if (f > 0.0F) {
            super.setupRotations(p_446425_, p_446166_, p_445813_, p_446015_);
            float f4 = p_446425_.isInWater ? -90.0F - f1 : -90.0F;
            float f3 = Mth.lerp(f, 0.0F, f4);
            p_446166_.mulPose(Axis.XP.rotationDegrees(f3));
            if (p_446425_.isVisuallySwimming) {
                p_446166_.translate(0.0F, -1.0F, 0.3F);
            }
        } else {
            super.setupRotations(p_446425_, p_446166_, p_445813_, p_446015_);
        }
    }

    public boolean isEntityUpsideDown(AvatarlikeEntity p_451232_) {
        if (p_451232_.isModelPartShown(PlayerModelPart.CAPE)) {
            return p_451232_ instanceof Player player ? isPlayerUpsideDown(player) : super.isEntityUpsideDown(p_451232_);
        } else {
            return false;
        }
    }

    public static boolean isPlayerUpsideDown(Player player) {
        return isUpsideDownName(player.getGameProfile().name());
    }
}
