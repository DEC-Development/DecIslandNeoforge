package com.dec.decisland.client.renderer;

import com.dec.decisland.DecIsland;
import com.dec.decisland.entity.projectile.GeoParticleRayProjectile;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.specialty.DirectionalProjectileRenderer;

public class MagicBallGeckoRenderer<T extends GeoParticleRayProjectile>
    extends DirectionalProjectileRenderer<T, MagicBallGeckoRenderer.MagicBallRenderState> {
    private final int renderTint;
    private final Identifier texture;
    private final MaterialStyle materialStyle;

    public MagicBallGeckoRenderer(EntityRendererProvider.Context context, Identifier texture, float scale) {
        this(context, Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "energy_ball"), texture, scale);
    }

    public MagicBallGeckoRenderer(
        EntityRendererProvider.Context context,
        Identifier modelName,
        Identifier texture,
        float scale
    ) {
        this(context, modelName, texture, scale, MaterialStyle.ENERGY_SWIRL, 0xFFFFFFFF);
    }

    public MagicBallGeckoRenderer(
        EntityRendererProvider.Context context,
        Identifier texture,
        float scale,
        MaterialStyle materialStyle,
        int renderTint
    ) {
        this(
            context,
            Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "energy_ball"),
            texture,
            scale,
            materialStyle,
            renderTint
        );
    }

    public MagicBallGeckoRenderer(
        EntityRendererProvider.Context context,
        Identifier modelName,
        Identifier texture,
        float scale,
        MaterialStyle materialStyle,
        int renderTint
    ) {
        super(context, new MagicBallGeoModel<>(modelName, texture));
        this.shadowRadius = 0.0F;
        this.shadowStrength = 0.0F;
        this.withScale(scale);
        this.texture = texture;
        this.materialStyle = materialStyle;
        this.renderTint = renderTint;
    }

    @Override
    public MagicBallRenderState createRenderState(T animatable, Void relatedObject) {
        return new MagicBallRenderState();
    }

    @Override
    public void extractRenderState(T entity, MagicBallRenderState reusedState, float partialTick) {
        reusedState.getDataMap().clear();
        reusedState.entityType = entity.getType();
        reusedState.x = Mth.lerp((double)partialTick, entity.xOld, entity.getX());
        reusedState.y = Mth.lerp((double)partialTick, entity.yOld, entity.getY());
        reusedState.z = Mth.lerp((double)partialTick, entity.zOld, entity.getZ());
        reusedState.isInvisible = entity.isInvisible();
        reusedState.partialTick = partialTick;
        reusedState.ageInTicks = entity.tickCount + partialTick;
        reusedState.boundingBoxWidth = entity.getBbWidth();
        reusedState.boundingBoxHeight = entity.getBbHeight();
        reusedState.eyeHeight = entity.getEyeHeight();
        reusedState.passengerOffset = null;
        reusedState.isDiscrete = entity.isDiscrete();
        reusedState.nameTag = null;
        reusedState.leashStates = null;

        if (this.entityRenderDispatcher.camera != null) {
            reusedState.distanceToCameraSq = this.entityRenderDispatcher.distanceToSqr(entity);
        }

        long instanceId = this.getInstanceId(entity, null);

        reusedState.addGeckolibData(DataTickets.ANIMATABLE_INSTANCE_ID, instanceId);
        reusedState.addGeckolibData(
            DataTickets.ANIMATABLE_MANAGER,
            Objects.requireNonNull(entity.getAnimatableInstanceCache().getManagerForId(instanceId))
        );
        reusedState.addGeckolibData(DataTickets.PARTIAL_TICK, partialTick);
        reusedState.addGeckolibData(DataTickets.TICK, (double)reusedState.ageInTicks);
        reusedState.addGeckolibData(DataTickets.ANIMATABLE_CLASS, entity.getClass());
        reusedState.addGeckolibData(DataTickets.RENDER_COLOR, this.getRenderColor(entity, null, partialTick));
        reusedState.addGeckolibData(DataTickets.PACKED_OVERLAY, this.getPackedOverlay(entity, null, 0.0F, partialTick));
        reusedState.addGeckolibData(DataTickets.INVISIBLE_TO_PLAYER, false);
        reusedState.addGeckolibData(DataTickets.IS_SHAKING, entity.isFullyFrozen());
        reusedState.addGeckolibData(DataTickets.ENTITY_POSE, entity.getPose());
        reusedState.addGeckolibData(DataTickets.ENTITY_PITCH, entity.getXRot(partialTick));
        reusedState.addGeckolibData(DataTickets.ENTITY_YAW, this.calculateYRot(entity, 0.0F, partialTick));
        reusedState.addGeckolibData(
            DataTickets.ENTITY_BODY_YAW,
            reusedState.getOrDefaultGeckolibData(DataTickets.ENTITY_YAW, 0.0F)
        );
        reusedState.addGeckolibData(DataTickets.VELOCITY, entity.getDeltaMovement());
        reusedState.addGeckolibData(DataTickets.BLOCKPOS, entity.blockPosition());
        reusedState.addGeckolibData(DataTickets.SPRINTING, entity.isSprinting());
        reusedState.addGeckolibData(DataTickets.IS_CROUCHING, entity.isCrouching());
        reusedState.addGeckolibData(DataTickets.POSITION, entity.position());
        reusedState.addGeckolibData(
            DataTickets.IS_MOVING,
            entity.getDeltaMovement().lengthSqr() >= (double)this.getMotionAnimThreshold(entity)
        );

        this.addRenderData(entity, null, reusedState, partialTick);
        this.getGeoModel().addAdditionalStateData(entity, null, reusedState);
        this.fireCompileRenderStateEvent(entity, null, reusedState, partialTick);
        this.setMolangQueryValues(entity, null, reusedState, partialTick);
        AnimationProcessor.extractControllerStates(entity, reusedState, this.getGeoModel());
    }

    @Override
    public RenderType getRenderType(MagicBallRenderState renderState, Identifier texture) {
        if (this.materialStyle == MaterialStyle.TRANSLUCENT) {
            return RenderTypes.entityTranslucent(this.texture);
        }

        float offset = (renderState.ageInTicks * 0.01F) % 1.0F;
        return RenderTypes.energySwirl(this.texture, offset, offset);
    }

    @Override
    public int getRenderColor(T animatable, Void relatedObject, float partialTick) {
        return this.renderTint;
    }

    @Override
    public int getBlockLightLevel(T entity, BlockPos pos) {
        return 15;
    }

    public static final class MagicBallRenderState extends EntityRenderState implements GeoRenderState {
        private final Map<DataTicket<?>, Object> dataMap = new Reference2ObjectOpenHashMap<>();

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return this.dataMap;
        }

        @Override
        public <D> void addGeckolibData(DataTicket<D> dataTicket, D data) {
            this.dataMap.put(dataTicket, data);
        }

        @Override
        public boolean hasGeckolibData(DataTicket<?> dataTicket) {
            return this.dataMap.containsKey(dataTicket);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <D> D getGeckolibData(DataTicket<D> dataTicket) {
            return (D)this.dataMap.get(dataTicket);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <D> D getOrDefaultGeckolibData(DataTicket<D> dataTicket, D defaultValue) {
            return (D)this.dataMap.getOrDefault(dataTicket, defaultValue);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <D> D getOrDefaultGeckolibData(DataTicket<D> dataTicket, Supplier<D> defaultValueSupplier) {
            return (D)this.dataMap.computeIfAbsent(dataTicket, ignored -> defaultValueSupplier.get());
        }

        @Override
        public int getPackedLight() {
            return this.lightCoords;
        }

        @Override
        public float getPartialTick() {
            return this.partialTick;
        }

        @Override
        public double getAnimatableAge() {
            return this.ageInTicks;
        }
    }

    public enum MaterialStyle {
        ENERGY_SWIRL,
        TRANSLUCENT
    }

    private static final class MagicBallGeoModel<T extends GeoParticleRayProjectile> extends DefaultedEntityGeoModel<T> {
        private final Identifier texture;

        private MagicBallGeoModel(Identifier modelName, Identifier texture) {
            super(modelName);
            this.texture = texture;
        }

        @Override
        public Identifier getTextureResource(GeoRenderState geoRenderState) {
            return this.texture;
        }
    }
}
