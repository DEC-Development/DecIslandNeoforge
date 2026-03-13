package com.dec.decisland.client.renderer;

import com.dec.decisland.DecIsland;
import com.dec.decisland.client.model.EmptyModel;
import com.dec.decisland.client.renderer.state.NormalRenderState;
import com.dec.decisland.entity.projectile.BlizzardEnergy;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public class EmptyRenderer extends EntityRenderer<Entity, NormalRenderState> {
    private EmptyModel model;
    public EmptyRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new EmptyModel(context.bakeLayer(EmptyModel.LAYER_LOCATION));
    }

    @Override
    public NormalRenderState createRenderState() {
        return new NormalRenderState();
    }

    public Identifier getTextureLocation(Entity entity) {
        return Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/empty.png");
    }
}
