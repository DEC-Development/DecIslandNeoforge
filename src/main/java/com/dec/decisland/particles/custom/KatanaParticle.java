package com.dec.decisland.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class KatanaParticle extends SingleQuadParticle {
    private final SpriteSet sprites;

    public KatanaParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites.first()); // 临时精灵，稍后更新
        this.sprites = sprites;

        // 速度强制为零（JSON 中 initial_speed = 0）
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        // 寿命：0.3 秒 = 6 游戏刻
        this.lifetime = 6;

        // 初始大小：1.0（JSON 中 size = [1, 1]）
        this.quadSize = 1.0f;

        // 设置正确的初始精灵（基于 lifetime）
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        // 根据年龄更新精灵，实现翻页书动画（stretch_to_lifetime）
        this.setSpriteFromAge(sprites);
    }

    @Override
    protected @NotNull Layer getLayer() {
        // 使用不透明层，纹理的透明度由纹理本身控制
        return Layer.OPAQUE;
    }

    @Override
    public @NotNull FacingCameraMode getFacingCameraMode() {
        // 对应基岩版 "rotate_xyz"：始终面向相机
        return FacingCameraMode.LOOKAT_XYZ;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed,
                                       RandomSource random) {
            // 应用发射点偏移：offset [0, 1.7, 0]
            double offsetY = 0; // 1.7
            return new KatanaParticle(level, x, y + offsetY, z, this.sprite);
        }
    }
}