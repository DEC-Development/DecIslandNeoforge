package net.minecraft.client.renderer;

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.shaders.ShaderType;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.jtracy.TracyClient;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.SharedConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.Screenshot;
import net.minecraft.client.TextureFilteringMethod;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.font.ActiveArea;
import net.minecraft.client.gui.font.EmptyArea;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.pip.GuiBannerResultRenderer;
import net.minecraft.client.gui.render.pip.GuiBookModelRenderer;
import net.minecraft.client.gui.render.pip.GuiEntityRenderer;
import net.minecraft.client.gui.render.pip.GuiProfilerChartRenderer;
import net.minecraft.client.gui.render.pip.GuiSignRenderer;
import net.minecraft.client.gui.render.pip.GuiSkinRenderer;
import net.minecraft.client.gui.render.state.ColoredRectangleRenderState;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.gui.render.state.GuiTextRenderState;
import net.minecraft.client.gui.screens.debug.DebugOptionsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.AtlasManager;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.Zone;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.waypoints.TrackedWaypoint;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class GameRenderer implements TrackedWaypoint.Projector, AutoCloseable {
    private static final Identifier BLUR_POST_CHAIN_ID = Identifier.withDefaultNamespace("blur");
    public static final int MAX_BLUR_RADIUS = 10;
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final float PROJECTION_Z_NEAR = 0.05F;
    public static final float PROJECTION_3D_HUD_Z_FAR = 100.0F;
    private static final float PORTAL_SPINNING_SPEED = 20.0F;
    private static final float NAUSEA_SPINNING_SPEED = 7.0F;
    private final Minecraft minecraft;
    private final RandomSource random = RandomSource.create();
    private float renderDistance;
    public final ItemInHandRenderer itemInHandRenderer;
    private final ScreenEffectRenderer screenEffectRenderer;
    private final RenderBuffers renderBuffers;
    private float spinningEffectTime;
    private float spinningEffectSpeed;
    private float fovModifier;
    private float oldFovModifier;
    private float darkenWorldAmount;
    private float darkenWorldAmountO;
    private boolean renderBlockOutline = true;
    private long lastScreenshotAttempt;
    private boolean hasWorldScreenshot;
    private long lastActiveTime = Util.getMillis();
    private final LightTexture lightTexture;
    private final OverlayTexture overlayTexture = new OverlayTexture();
    private @Nullable PanoramicScreenshotParameters panoramicScreenshotParameters;
    protected final CubeMap cubeMap = new CubeMap(Identifier.withDefaultNamespace("textures/gui/title/background/panorama"));
    protected final PanoramaRenderer panorama = new PanoramaRenderer(this.cubeMap);
    private final CrossFrameResourcePool resourcePool = new CrossFrameResourcePool(3);
    private final FogRenderer fogRenderer = new FogRenderer();
    private final GuiRenderer guiRenderer;
    final GuiRenderState guiRenderState;
    private final LevelRenderState levelRenderState = new LevelRenderState();
    private final SubmitNodeStorage submitNodeStorage;
    private final FeatureRenderDispatcher featureRenderDispatcher;
    private @Nullable Identifier postEffectId;
    private boolean effectActive;
    private final Camera mainCamera = new Camera();
    private final Lighting lighting = new Lighting();
    private final GlobalSettingsUniform globalSettingsUniform = new GlobalSettingsUniform();
    private final PerspectiveProjectionMatrixBuffer levelProjectionMatrixBuffer = new PerspectiveProjectionMatrixBuffer("level");
    private final CachedPerspectiveProjectionMatrixBuffer hud3dProjectionMatrixBuffer = new CachedPerspectiveProjectionMatrixBuffer("3d hud", 0.05F, 100.0F);

    public GameRenderer(Minecraft minecraft, ItemInHandRenderer itemInHandRenderer, RenderBuffers renderBuffers, BlockRenderDispatcher blockRenderDispatcher) {
        this.minecraft = minecraft;
        this.itemInHandRenderer = itemInHandRenderer;
        this.lightTexture = new LightTexture(this, minecraft);
        this.renderBuffers = renderBuffers;
        this.guiRenderState = new GuiRenderState();
        MultiBufferSource.BufferSource multibuffersource$buffersource = renderBuffers.bufferSource();
        AtlasManager atlasmanager = minecraft.getAtlasManager();
        this.submitNodeStorage = new SubmitNodeStorage();
        this.featureRenderDispatcher = new FeatureRenderDispatcher(
            this.submitNodeStorage,
            blockRenderDispatcher,
            multibuffersource$buffersource,
            atlasmanager,
            renderBuffers.outlineBufferSource(),
            renderBuffers.crumblingBufferSource(),
            minecraft.font
        );
        this.guiRenderer = new GuiRenderer(
            this.guiRenderState,
            multibuffersource$buffersource,
            this.submitNodeStorage,
            this.featureRenderDispatcher,
            net.neoforged.neoforge.client.ClientHooks.gatherPictureInPictureRenderers(List.of(
                new net.neoforged.neoforge.client.gui.PictureInPictureRendererRegistration<>(net.minecraft.client.gui.render.state.pip.GuiEntityRenderState.class, buffers -> new GuiEntityRenderer(buffers, minecraft.getEntityRenderDispatcher())),
                new net.neoforged.neoforge.client.gui.PictureInPictureRendererRegistration<>(net.minecraft.client.gui.render.state.pip.GuiSkinRenderState.class, GuiSkinRenderer::new),
                new net.neoforged.neoforge.client.gui.PictureInPictureRendererRegistration<>(net.minecraft.client.gui.render.state.pip.GuiBookModelRenderState.class, GuiBookModelRenderer::new),
                new net.neoforged.neoforge.client.gui.PictureInPictureRendererRegistration<>(net.minecraft.client.gui.render.state.pip.GuiBannerResultRenderState.class, buffers -> new GuiBannerResultRenderer(buffers, atlasmanager)),
                new net.neoforged.neoforge.client.gui.PictureInPictureRendererRegistration<>(net.minecraft.client.gui.render.state.pip.GuiSignRenderState.class, buffers -> new GuiSignRenderer(buffers, atlasmanager)),
                new net.neoforged.neoforge.client.gui.PictureInPictureRendererRegistration<>(net.minecraft.client.gui.render.state.pip.GuiProfilerChartRenderState.class, GuiProfilerChartRenderer::new)
            ))
        );
        this.screenEffectRenderer = new ScreenEffectRenderer(minecraft, atlasmanager, multibuffersource$buffersource);
    }

    @Override
    public void close() {
        this.globalSettingsUniform.close();
        this.lightTexture.close();
        this.overlayTexture.close();
        this.resourcePool.close();
        this.guiRenderer.close();
        this.levelProjectionMatrixBuffer.close();
        this.hud3dProjectionMatrixBuffer.close();
        this.lighting.close();
        this.cubeMap.close();
        this.fogRenderer.close();
        this.featureRenderDispatcher.close();
    }

    public SubmitNodeStorage getSubmitNodeStorage() {
        return this.submitNodeStorage;
    }

    public FeatureRenderDispatcher getFeatureRenderDispatcher() {
        return this.featureRenderDispatcher;
    }

    public LevelRenderState getLevelRenderState() {
        return this.levelRenderState;
    }

    public void setRenderBlockOutline(boolean renderBlockOutline) {
        this.renderBlockOutline = renderBlockOutline;
    }

    public void setPanoramicScreenshotParameters(@Nullable PanoramicScreenshotParameters panoramicScreenshotParameters) {
        this.panoramicScreenshotParameters = panoramicScreenshotParameters;
    }

    public @Nullable PanoramicScreenshotParameters getPanoramicScreenshotParameters() {
        return this.panoramicScreenshotParameters;
    }

    public boolean isPanoramicMode() {
        return this.panoramicScreenshotParameters != null;
    }

    public void clearPostEffect() {
        this.postEffectId = null;
        this.effectActive = false;
    }

    public void togglePostEffect() {
        this.effectActive = !this.effectActive;
    }

    /**
     * What shader to use when spectating this entity
     */
    public void checkEntityPostEffect(@Nullable Entity entity) {
        switch (entity) {
            case Creeper creeper:
                this.setPostEffect(Identifier.withDefaultNamespace("creeper"));
                break;
            case Spider spider:
                this.setPostEffect(Identifier.withDefaultNamespace("spider"));
                break;
            case EnderMan enderman:
                this.setPostEffect(Identifier.withDefaultNamespace("invert"));
                break;
            case null:
            default:
                if (!net.neoforged.neoforge.client.ClientHooks.loadEntityShader(entity, this))
                this.clearPostEffect();
        }
    }

    public void setPostEffect(Identifier postEffectId) {
        this.postEffectId = postEffectId;
        this.effectActive = true;
    }

    public void processBlurEffect() {
        PostChain postchain = this.minecraft.getShaderManager().getPostChain(BLUR_POST_CHAIN_ID, LevelTargetBundle.MAIN_TARGETS);
        if (postchain != null) {
            postchain.process(this.minecraft.getMainRenderTarget(), this.resourcePool);
        }
    }

    public void preloadUiShader(ResourceProvider resourceProvider) {
        GpuDevice gpudevice = RenderSystem.getDevice();
        ShaderSource shadersource = (p_465587_, p_465588_) -> {
            Identifier identifier = p_465588_.idConverter().idToFile(p_465587_);

            try {
                String s;
                try (Reader reader = resourceProvider.getResourceOrThrow(identifier).openAsReader()) {
                    s = IOUtils.toString(reader);
                }

                return s;
            } catch (IOException ioexception) {
                LOGGER.error("Coudln't preload {} shader {}: {}", p_465588_, p_465587_, ioexception);
                return null;
            }
        };
        gpudevice.precompilePipeline(RenderPipelines.GUI, shadersource);
        gpudevice.precompilePipeline(RenderPipelines.GUI_TEXTURED, shadersource);
        if (TracyClient.isAvailable()) {
            gpudevice.precompilePipeline(RenderPipelines.TRACY_BLIT, shadersource);
        }
    }

    public void tick() {
        this.tickFov();
        this.lightTexture.tick();
        LocalPlayer localplayer = this.minecraft.player;
        if (this.minecraft.getCameraEntity() == null) {
            this.minecraft.setCameraEntity(localplayer);
        }

        this.mainCamera.tick();
        this.itemInHandRenderer.tick();
        float f = localplayer.portalEffectIntensity;
        float f1 = localplayer.getEffectBlendFactor(MobEffects.NAUSEA, 1.0F);
        if (!(f > 0.0F) && !(f1 > 0.0F)) {
            this.spinningEffectSpeed = 0.0F;
        } else {
            this.spinningEffectSpeed = (f * 20.0F + f1 * 7.0F) / (f + f1);
            this.spinningEffectTime = this.spinningEffectTime + this.spinningEffectSpeed;
        }

        if (this.minecraft.level.tickRateManager().runsNormally()) {
            this.darkenWorldAmountO = this.darkenWorldAmount;
            if (this.minecraft.gui.getBossOverlay().shouldDarkenScreen()) {
                this.darkenWorldAmount += 0.05F;
                if (this.darkenWorldAmount > 1.0F) {
                    this.darkenWorldAmount = 1.0F;
                }
            } else if (this.darkenWorldAmount > 0.0F) {
                this.darkenWorldAmount -= 0.0125F;
            }

            this.screenEffectRenderer.tick();
            ProfilerFiller profilerfiller = Profiler.get();
            profilerfiller.push("levelRenderer");
            this.minecraft.levelRenderer.tick(this.mainCamera);
            profilerfiller.pop();
        }
    }

    public @Nullable Identifier currentPostEffect() {
        return this.postEffectId;
    }

    public void resize(int width, int height) {
        this.resourcePool.clear();
        this.minecraft.levelRenderer.resize(width, height);
    }

    /**
     * Gets the block or object that is being moused over.
     */
    public void pick(float partialTick) {
        Entity entity = this.minecraft.getCameraEntity();
        if (entity != null) {
            if (this.minecraft.level != null && this.minecraft.player != null) {
                Profiler.get().push("pick");
                this.minecraft.hitResult = this.minecraft.player.raycastHitResult(partialTick, entity);
                this.minecraft.crosshairPickEntity = this.minecraft.hitResult instanceof EntityHitResult entityhitresult ? entityhitresult.getEntity() : null;
                Profiler.get().pop();
            }
        }
    }

    private void tickFov() {
        float f;
        if (this.minecraft.getCameraEntity() instanceof AbstractClientPlayer abstractclientplayer) {
            Options options = this.minecraft.options;
            boolean flag = options.getCameraType().isFirstPerson();
            float f1 = options.fovEffectScale().get().floatValue();
            f = abstractclientplayer.getFieldOfViewModifier(flag, f1);
        } else {
            f = 1.0F;
        }

        this.oldFovModifier = this.fovModifier;
        this.fovModifier = this.fovModifier + (f - this.fovModifier) * 0.5F;
        this.fovModifier = Mth.clamp(this.fovModifier, 0.1F, 1.5F);
    }

    private float getFov(Camera camera, float partialTick, boolean useFovSetting) {
        if (this.isPanoramicMode()) {
            return 90.0F;
        } else {
            float f = 70.0F;
            if (useFovSetting) {
                f = this.minecraft.options.fov().get().intValue();
                f *= Mth.lerp(partialTick, this.oldFovModifier, this.fovModifier);
            }

            if (camera.entity() instanceof LivingEntity livingentity && livingentity.isDeadOrDying()) {
                float f1 = Math.min(livingentity.deathTime + partialTick, 20.0F);
                f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
            }

            FogType fogtype = camera.getFluidInCamera();
            if (fogtype == FogType.LAVA || fogtype == FogType.WATER) {
                float f2 = this.minecraft.options.fovEffectScale().get().floatValue();
                f *= Mth.lerp(f2, 1.0F, 0.85714287F);
            }

            return net.neoforged.neoforge.client.ClientHooks.getFieldOfView(this, camera, partialTick, f, useFovSetting);
        }
    }

    private void bobHurt(PoseStack poseStack, float partialTicks) {
        if (this.minecraft.getCameraEntity() instanceof LivingEntity livingentity) {
            float f2 = livingentity.hurtTime - partialTicks;
            if (livingentity.isDeadOrDying()) {
                float f = Math.min(livingentity.deathTime + partialTicks, 20.0F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(40.0F - 8000.0F / (f + 200.0F)));
            }

            if (f2 < 0.0F) {
                return;
            }

            // Neo: Prevent screen shake if the damage type is marked as "forge:no_flinch"
            var lastSrc = livingentity.getLastDamageSource();
            if (lastSrc != null && lastSrc.is(net.neoforged.neoforge.common.Tags.DamageTypes.NO_FLINCH)) return;

            f2 /= livingentity.hurtDuration;
            f2 = Mth.sin(f2 * f2 * f2 * f2 * (float) Math.PI);
            float f3 = livingentity.getHurtDir();
            poseStack.mulPose(Axis.YP.rotationDegrees(-f3));
            float f1 = (float)(-f2 * 14.0 * this.minecraft.options.damageTiltStrength().get());
            poseStack.mulPose(Axis.ZP.rotationDegrees(f1));
            poseStack.mulPose(Axis.YP.rotationDegrees(f3));
        }
    }

    private void bobView(PoseStack poseStack, float partialTicks) {
        if (this.minecraft.getCameraEntity() instanceof AbstractClientPlayer abstractclientplayer) {
            ClientAvatarState clientavatarstate = abstractclientplayer.avatarState();
            float $$5 = clientavatarstate.getBackwardsInterpolatedWalkDistance(partialTicks);
            float $$6 = clientavatarstate.getInterpolatedBob(partialTicks);
            poseStack.translate(Mth.sin($$5 * (float) Math.PI) * $$6 * 0.5F, -Math.abs(Mth.cos($$5 * (float) Math.PI) * $$6), 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin($$5 * (float) Math.PI) * $$6 * 3.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(Math.abs(Mth.cos($$5 * (float) Math.PI - 0.2F) * $$6) * 5.0F));
        }
    }

    private void renderItemInHand(float partialTick, boolean sleeping, Matrix4f projectionMatrix) {
        if (!this.isPanoramicMode()) {
            this.featureRenderDispatcher.renderAllFeatures();
            this.renderBuffers.bufferSource().endBatch();
            PoseStack posestack = new PoseStack();
            posestack.pushPose();
            posestack.mulPose(projectionMatrix.invert(new Matrix4f()));
            Matrix4fStack matrix4fstack = RenderSystem.getModelViewStack();
            matrix4fstack.pushMatrix().mul(projectionMatrix);
            this.bobHurt(posestack, partialTick);
            if (this.minecraft.options.bobView().get()) {
                this.bobView(posestack, partialTick);
            }

            if (this.minecraft.options.getCameraType().isFirstPerson()
                && !sleeping
                && !this.minecraft.options.hideGui
                && this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
                this.itemInHandRenderer
                    .renderHandsWithItems(
                        partialTick,
                        posestack,
                        this.minecraft.gameRenderer.getSubmitNodeStorage(),
                        this.minecraft.player,
                        this.minecraft.getEntityRenderDispatcher().getPackedLightCoords(this.minecraft.player, partialTick)
                    );
            }

            matrix4fstack.popMatrix();
            posestack.popPose();
        }
    }

    public Matrix4f getProjectionMatrix(float fov) {
        Matrix4f matrix4f = new Matrix4f();
        return matrix4f.perspective(
            fov * (float) (Math.PI / 180.0),
            (float)this.minecraft.getWindow().getWidth() / this.minecraft.getWindow().getHeight(),
            0.05F,
            this.getDepthFar()
        );
    }

    public float getDepthFar() {
        return Math.max(this.renderDistance * 4.0F, (float)(this.minecraft.options.cloudRange().get() * 16));
    }

    public static float getNightVisionScale(LivingEntity livingEntity, float nanoTime) {
        MobEffectInstance mobeffectinstance = livingEntity.getEffect(MobEffects.NIGHT_VISION);
        return !mobeffectinstance.endsWithin(200) ? 1.0F : 0.7F + Mth.sin((mobeffectinstance.getDuration() - nanoTime) * (float) Math.PI * 0.2F) * 0.3F;
    }

    public void render(DeltaTracker deltaTracker, boolean renderLevel) {
        if (!this.minecraft.isWindowActive()
            && this.minecraft.options.pauseOnLostFocus
            && (!this.minecraft.options.touchscreen().get() || !this.minecraft.mouseHandler.isRightPressed())) {
            if (Util.getMillis() - this.lastActiveTime > 500L) {
                this.minecraft.pauseGame(false);
            }
        } else {
            this.lastActiveTime = Util.getMillis();
        }

        if (!this.minecraft.noRender) {
            ProfilerFiller profilerfiller = Profiler.get();
            profilerfiller.push("camera");
            this.updateCamera(deltaTracker);
            profilerfiller.pop();
            this.globalSettingsUniform
                .update(
                    this.minecraft.getWindow().getWidth(),
                    this.minecraft.getWindow().getHeight(),
                    this.minecraft.options.glintStrength().get(),
                    this.minecraft.level == null ? 0L : this.minecraft.level.getGameTime(),
                    deltaTracker,
                    this.minecraft.options.getMenuBackgroundBlurriness(),
                    this.mainCamera,
                    this.minecraft.options.textureFiltering().get() == TextureFilteringMethod.RGSS
                );
            boolean flag = this.minecraft.isGameLoadFinished();
            int i = (int)this.minecraft.mouseHandler.getScaledXPos(this.minecraft.getWindow());
            int j = (int)this.minecraft.mouseHandler.getScaledYPos(this.minecraft.getWindow());
            if (flag && renderLevel && this.minecraft.level != null) {
                profilerfiller.push("world");
                this.renderLevel(deltaTracker);
                this.tryTakeScreenshotIfNeeded();
                this.minecraft.levelRenderer.doEntityOutline();
                if (this.postEffectId != null && this.effectActive) {
                    PostChain postchain = this.minecraft.getShaderManager().getPostChain(this.postEffectId, LevelTargetBundle.MAIN_TARGETS);
                    if (postchain != null) {
                        postchain.process(this.minecraft.getMainRenderTarget(), this.resourcePool);
                    }
                }

                profilerfiller.pop();
            }

            this.fogRenderer.endFrame();
            RenderTarget rendertarget = this.minecraft.getMainRenderTarget();
            RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(rendertarget.getDepthTexture(), 1.0);
            this.minecraft.gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_3D);
            this.guiRenderState.reset();
            profilerfiller.push("guiExtraction");
            GuiGraphics guigraphics = new GuiGraphics(this.minecraft, this.guiRenderState, i, j);
            if (flag && renderLevel && this.minecraft.level != null) {
                this.minecraft.gui.render(guigraphics, deltaTracker);
            }

            if (this.minecraft.getOverlay() != null) {
                try {
                    this.minecraft.getOverlay().render(guigraphics, i, j, deltaTracker.getGameTimeDeltaTicks());
                } catch (Throwable throwable2) {
                    CrashReport crashreport = CrashReport.forThrowable(throwable2, "Rendering overlay");
                    CrashReportCategory crashreportcategory = crashreport.addCategory("Overlay render details");
                    crashreportcategory.setDetail("Overlay name", () -> this.minecraft.getOverlay().getClass().getCanonicalName());
                    throw new ReportedException(crashreport);
                }
            } else if (flag && this.minecraft.screen != null) {
                try {
                    // Neo: Wrap Screen#render to allow for GUI Layers and ScreenEvent.Render.[Pre/Post]
                    net.neoforged.neoforge.client.ClientHooks.drawScreen(this.minecraft.screen, guigraphics, i, j, deltaTracker.getGameTimeDeltaTicks());
                } catch (Throwable throwable1) {
                    CrashReport crashreport1 = CrashReport.forThrowable(throwable1, "Rendering screen");
                    CrashReportCategory crashreportcategory1 = crashreport1.addCategory("Screen render details");
                    crashreportcategory1.setDetail("Screen name", () -> this.minecraft.screen.getClass().getCanonicalName());
                    this.minecraft.mouseHandler.fillMousePositionDetails(crashreportcategory1, this.minecraft.getWindow());
                    throw new ReportedException(crashreport1);
                }

                if (SharedConstants.DEBUG_CURSOR_POS) {
                    this.minecraft.mouseHandler.drawDebugMouseInfo(this.minecraft.font, guigraphics);
                }

                try {
                    if (this.minecraft.screen != null) {
                        this.minecraft.screen.handleDelayedNarration();
                    }
                } catch (Throwable throwable) {
                    CrashReport crashreport2 = CrashReport.forThrowable(throwable, "Narrating screen");
                    CrashReportCategory crashreportcategory2 = crashreport2.addCategory("Screen details");
                    crashreportcategory2.setDetail("Screen name", () -> this.minecraft.screen.getClass().getCanonicalName());
                    throw new ReportedException(crashreport2);
                }
            }

            if (flag && renderLevel && this.minecraft.level != null) {
                this.minecraft.gui.renderSavingIndicator(guigraphics, deltaTracker);
            }

            if (flag) {
                try (Zone zone = profilerfiller.zone("toasts")) {
                    this.minecraft.getToastManager().render(guigraphics);
                }
            }

            if (!(this.minecraft.screen instanceof DebugOptionsScreen)) {
                this.minecraft.gui.renderDebugOverlay(guigraphics);
            }

            this.minecraft.gui.renderDeferredSubtitles();
            if (SharedConstants.DEBUG_ACTIVE_TEXT_AREAS) {
                this.renderActiveTextDebug();
            }

            profilerfiller.popPush("guiRendering");
            this.guiRenderer.render(this.fogRenderer.getBuffer(FogRenderer.FogMode.NONE));
            this.guiRenderer.incrementFrameNumber();
            profilerfiller.pop();
            guigraphics.applyCursor(this.minecraft.getWindow());
            this.submitNodeStorage.endFrame();
            this.featureRenderDispatcher.endFrame();
            this.resourcePool.endFrame();
        }
    }

    private void renderActiveTextDebug() {
        this.guiRenderState.nextStratum();
        this.guiRenderState
            .forEachText(
                p_457350_ -> p_457350_.ensurePrepared()
                    .visit(
                        new Font.GlyphVisitor() {
                            private int index;

                            @Override
                            public void acceptGlyph(TextRenderable.Styled p_457901_) {
                                this.renderDebugMarkers(p_457901_, false);
                            }

                            @Override
                            public void acceptEmptyArea(EmptyArea p_457941_) {
                                this.renderDebugMarkers(p_457941_, true);
                            }

                            private void renderDebugMarkers(ActiveArea p_457687_, boolean p_458132_) {
                                int i = (p_458132_ ? 128 : 255) - (this.index++ & 1) * 64;
                                Style style = p_457687_.style();
                                int j = style.getClickEvent() != null ? i : 0;
                                int k = style.getHoverEvent() != null ? i : 0;
                                int l = j != 0 && k != 0 ? 0 : i;
                                int i1 = ARGB.color(128, j, k, l);
                                GameRenderer.this.guiRenderState
                                    .submitGuiElement(
                                        new ColoredRectangleRenderState(
                                            RenderPipelines.GUI,
                                            TextureSetup.noTexture(),
                                            p_457350_.pose,
                                            (int)p_457687_.activeLeft(),
                                            (int)p_457687_.activeTop(),
                                            (int)p_457687_.activeRight(),
                                            (int)p_457687_.activeBottom(),
                                            i1,
                                            i1,
                                            p_457350_.scissor
                                        )
                                    );
                            }
                        }
                    )
            );
    }

    private void tryTakeScreenshotIfNeeded() {
        if (!this.hasWorldScreenshot && this.minecraft.isLocalServer()) {
            long i = Util.getMillis();
            if (i - this.lastScreenshotAttempt >= 1000L) {
                this.lastScreenshotAttempt = i;
                IntegratedServer integratedserver = this.minecraft.getSingleplayerServer();
                if (integratedserver != null && !integratedserver.isStopped()) {
                    integratedserver.getWorldScreenshotFile().ifPresent(p_234239_ -> {
                        if (Files.isRegularFile(p_234239_)) {
                            this.hasWorldScreenshot = true;
                        } else {
                            this.takeAutoScreenshot(p_234239_);
                        }
                    });
                }
            }
        }
    }

    private void takeAutoScreenshot(Path path) {
        if (this.minecraft.levelRenderer.countRenderedSections() > 10 && this.minecraft.levelRenderer.hasRenderedAllSections()) {
            Screenshot.takeScreenshot(this.minecraft.getMainRenderTarget(), p_465585_ -> Util.ioPool().execute(() -> {
                int i = p_465585_.getWidth();
                int j = p_465585_.getHeight();
                int k = 0;
                int l = 0;
                if (i > j) {
                    k = (i - j) / 2;
                    i = j;
                } else {
                    l = (j - i) / 2;
                    j = i;
                }

                try (NativeImage nativeimage = new NativeImage(64, 64, false)) {
                    p_465585_.resizeSubRectTo(k, l, i, j, nativeimage);
                    nativeimage.writeToFile(path);
                } catch (IOException ioexception) {
                    LOGGER.warn("Couldn't save auto screenshot", (Throwable)ioexception);
                } finally {
                    p_465585_.close();
                }
            }));
        }
    }

    private boolean shouldRenderBlockOutline() {
        if (!this.renderBlockOutline) {
            return false;
        } else {
            Entity entity = this.minecraft.getCameraEntity();
            boolean flag = entity instanceof Player && !this.minecraft.options.hideGui;
            if (flag && !((Player)entity).getAbilities().mayBuild) {
                ItemStack itemstack = ((LivingEntity)entity).getMainHandItem();
                HitResult hitresult = this.minecraft.hitResult;
                if (hitresult != null && hitresult.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
                    BlockState blockstate = this.minecraft.level.getBlockState(blockpos);
                    if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
                        flag = blockstate.getMenuProvider(this.minecraft.level, blockpos) != null;
                    } else {
                        BlockInWorld blockinworld = new BlockInWorld(this.minecraft.level, blockpos, false);
                        Registry<Block> registry = this.minecraft.level.registryAccess().lookupOrThrow(Registries.BLOCK);
                        flag = !itemstack.isEmpty()
                            && (itemstack.canBreakBlockInAdventureMode(blockinworld) || itemstack.canPlaceOnBlockInAdventureMode(blockinworld));
                    }
                }
            }

            return flag;
        }
    }

    public void updateCamera(DeltaTracker deltaTracker) {
        float f = deltaTracker.getGameTimeDeltaPartialTick(true);
        LocalPlayer localplayer = this.minecraft.player;
        if (localplayer != null && this.minecraft.level != null) {
            if (this.minecraft.getCameraEntity() == null) {
                this.minecraft.setCameraEntity(localplayer);
            }

            Entity entity = (Entity)(this.minecraft.getCameraEntity() == null ? localplayer : this.minecraft.getCameraEntity());
            float f1 = this.minecraft.level.tickRateManager().isEntityFrozen(entity) ? 1.0F : f;
            this.mainCamera
                .setup(
                    this.minecraft.level,
                    entity,
                    !this.minecraft.options.getCameraType().isFirstPerson(),
                    this.minecraft.options.getCameraType().isMirrored(),
                    f1
                );
        }
    }

    public void renderLevel(DeltaTracker deltaTracker) {
        float f = deltaTracker.getGameTimeDeltaPartialTick(true);
        LocalPlayer localplayer = this.minecraft.player;
        this.lightTexture.updateLightTexture(1.0F);
        this.pick(f);
        ProfilerFiller profilerfiller = Profiler.get();
        boolean flag = this.shouldRenderBlockOutline();
        this.extractCamera(f);
        this.renderDistance = this.minecraft.options.getEffectiveRenderDistance() * 16;
        profilerfiller.push("matrices");
        float f1 = this.getFov(this.mainCamera, f, true);
        Matrix4f matrix4f = this.getProjectionMatrix(f1);
        PoseStack posestack = new PoseStack();
        this.bobHurt(posestack, this.mainCamera.getPartialTickTime());
        if (this.minecraft.options.bobView().get()) {
            this.bobView(posestack, this.mainCamera.getPartialTickTime());
        }

        matrix4f.mul(posestack.last().pose());
        float f2 = this.minecraft.options.screenEffectScale().get().floatValue();
        float f3 = Mth.lerp(f, localplayer.oPortalEffectIntensity, localplayer.portalEffectIntensity);
        float f4 = localplayer.getEffectBlendFactor(MobEffects.NAUSEA, f);
        float f5 = Math.max(f3, f4) * (f2 * f2);
        if (f5 > 0.0F) {
            float f6 = 5.0F / (f5 * f5 + 5.0F) - f5 * 0.04F;
            f6 *= f6;
            Vector3f vector3f = new Vector3f(0.0F, Mth.SQRT_OF_TWO / 2.0F, Mth.SQRT_OF_TWO / 2.0F);
            float f7 = (this.spinningEffectTime + f * this.spinningEffectSpeed) * (float) (Math.PI / 180.0);
            matrix4f.rotate(f7, vector3f);
            matrix4f.scale(1.0F / f6, 1.0F, 1.0F);
            matrix4f.rotate(-f7, vector3f);
        }

        RenderSystem.setProjectionMatrix(this.levelProjectionMatrixBuffer.getBuffer(matrix4f), ProjectionType.PERSPECTIVE);
        Quaternionf quaternionf = this.mainCamera.rotation().conjugate(new Quaternionf());
        Matrix4f matrix4f1 = new Matrix4f().rotation(quaternionf);
        profilerfiller.popPush("fog");
        Vector4f vector4f = this.fogRenderer
            .setupFog(this.mainCamera, this.minecraft.options.getEffectiveRenderDistance(), deltaTracker, this.getDarkenWorldAmount(f), this.minecraft.level);
        GpuBufferSlice gpubufferslice = this.fogRenderer.getBuffer(FogRenderer.FogMode.WORLD);
        profilerfiller.popPush("level");
        boolean flag1 = this.minecraft.gui.getBossOverlay().shouldCreateWorldFog();
        this.minecraft
            .levelRenderer
            .renderLevel(
                this.resourcePool,
                deltaTracker,
                flag,
                this.mainCamera,
                matrix4f1,
                matrix4f,
                this.getProjectionMatrixForCulling(f1),
                gpubufferslice,
                vector4f,
                !flag1
            );
        profilerfiller.popPush("neoforge_render_after_level");
        LevelRenderer levelRenderer = this.minecraft.levelRenderer;
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(new net.neoforged.neoforge.client.event.RenderLevelStageEvent.AfterLevel(levelRenderer, levelRenderer.levelRenderState, null, matrix4f1, levelRenderer.getRenderableSections()));
        profilerfiller.popPush("hand");
        boolean flag2 = this.minecraft.getCameraEntity() instanceof LivingEntity && ((LivingEntity)this.minecraft.getCameraEntity()).isSleeping();
        RenderSystem.setProjectionMatrix(
            this.hud3dProjectionMatrixBuffer
                .getBuffer(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), this.getFov(this.mainCamera, f, false)),
            ProjectionType.PERSPECTIVE
        );
        RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(this.minecraft.getMainRenderTarget().getDepthTexture(), 1.0);
        this.renderItemInHand(f, flag2, matrix4f1);
        profilerfiller.popPush("screenEffects");
        MultiBufferSource.BufferSource multibuffersource$buffersource = this.renderBuffers.bufferSource();
        this.screenEffectRenderer.renderScreenEffect(flag2, f, this.submitNodeStorage);
        this.featureRenderDispatcher.renderAllFeatures();
        multibuffersource$buffersource.endBatch();
        profilerfiller.pop();
        RenderSystem.setShaderFog(this.fogRenderer.getBuffer(FogRenderer.FogMode.NONE));
        if (this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.THREE_DIMENSIONAL_CROSSHAIR)
            && this.minecraft.options.getCameraType().isFirstPerson()
            && !this.minecraft.options.hideGui) {
            this.minecraft.getDebugOverlay().render3dCrosshair(this.mainCamera);
        }
    }

    private void extractCamera(float partialTick) {
        CameraRenderState camerarenderstate = this.levelRenderState.cameraRenderState;
        camerarenderstate.initialized = this.mainCamera.isInitialized();
        camerarenderstate.pos = this.mainCamera.position();
        camerarenderstate.blockPos = this.mainCamera.blockPosition();
        camerarenderstate.entityPos = this.mainCamera.entity().getPosition(partialTick);
        camerarenderstate.orientation = new Quaternionf(this.mainCamera.rotation());
    }

    private Matrix4f getProjectionMatrixForCulling(float fov) {
        float f = Math.max(fov, (float)this.minecraft.options.fov().get().intValue());
        return this.getProjectionMatrix(f);
    }

    public void resetData() {
        this.screenEffectRenderer.resetItemActivation();
        this.minecraft.getMapTextureManager().resetData();
        this.mainCamera.reset();
        this.hasWorldScreenshot = false;
    }

    public void displayItemActivation(ItemStack stack) {
        this.screenEffectRenderer.displayItemActivation(stack, this.random);
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    public float getDarkenWorldAmount(float partialTicks) {
        return Mth.lerp(partialTicks, this.darkenWorldAmountO, this.darkenWorldAmount);
    }

    public float getRenderDistance() {
        return this.renderDistance;
    }

    public Camera getMainCamera() {
        return this.mainCamera;
    }

    public LightTexture lightTexture() {
        return this.lightTexture;
    }

    public OverlayTexture overlayTexture() {
        return this.overlayTexture;
    }

    @Override
    public Vec3 projectPointToScreen(Vec3 p_415717_) {
        Matrix4f matrix4f = this.getProjectionMatrix(this.getFov(this.mainCamera, 0.0F, true));
        Quaternionf quaternionf = this.mainCamera.rotation().conjugate(new Quaternionf());
        Matrix4f matrix4f1 = new Matrix4f().rotation(quaternionf);
        Matrix4f matrix4f2 = matrix4f.mul(matrix4f1);
        Vec3 vec3 = this.mainCamera.position();
        Vec3 vec31 = p_415717_.subtract(vec3);
        Vector3f vector3f = matrix4f2.transformProject(vec31.toVector3f());
        return new Vec3(vector3f);
    }

    @Override
    public double projectHorizonToScreen() {
        float f = this.mainCamera.xRot();
        if (f <= -90.0F) {
            return Double.NEGATIVE_INFINITY;
        } else if (f >= 90.0F) {
            return Double.POSITIVE_INFINITY;
        } else {
            float f1 = this.getFov(this.mainCamera, 0.0F, true);
            return Math.tan(f * (float) (Math.PI / 180.0)) / Math.tan(f1 / 2.0F * (float) (Math.PI / 180.0));
        }
    }

    public GlobalSettingsUniform getGlobalSettingsUniform() {
        return this.globalSettingsUniform;
    }

    public Lighting getLighting() {
        return this.lighting;
    }

    public void setLevel(@Nullable ClientLevel level) {
        if (level != null) {
            this.lighting.updateLevel(level.dimensionType().cardinalLightType());
        }
    }

    public PanoramaRenderer getPanorama() {
        return this.panorama;
    }
}
