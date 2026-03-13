package com.dec.decisland.client.renderer;

import com.dec.decisland.DecIsland;
import com.dec.decisland.item.custom.Mask;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class MaskRenderer<R extends HumanoidRenderState & GeoRenderState> extends GeoArmorRenderer<Mask, R> {

    public MaskRenderer(Mask mask) {
        super(new ClothesGeoModel(mask));
    }

    public static class ClothesGeoModel extends GeoModel<Mask> {

        private final Mask maskItem;

        public ClothesGeoModel(Mask mask) {
            this.maskItem = mask;
        }

        @Override
        public Identifier getModelResource(GeoRenderState geoRenderState) {
            return Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "armor/clothes");
        }

        @Override
        public Identifier getTextureResource(GeoRenderState geoRenderState) {
            // 从关联的maskItem获取物品ID来构建纹理路径
            String itemName = maskItem.getDescriptionId().replace("item." + DecIsland.MOD_ID + ".", ""); // 移除"item.decisland."前缀，只保留实际的物品ID
            return Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/armor/mask/" + itemName + ".png");
        }

        @Override
        public Identifier getAnimationResource(Mask animatable) {
            // 如果有动画文件，返回对应的路径
            return null;
//            return Identifier.fromNamespaceAndPath(DecIsland.MOD_ID, "animations/item/clothes.animation.json");
        }
    }
}