package com.dec.decisland.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class BlizzardWakeParticle extends SingleQuadParticle {
    private final float initialSize;
    private final SpriteSet sprites;

    public BlizzardWakeParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        // 调用父类构造函数，传入一个临时精灵（先使用 age=0, lifetime=0 获取）
        super(level, x, y, z, sprites.get(0, 1));
        this.sprites = sprites;

        // 速度强制为零（JSON 中 initial_speed = 0）
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        // 寿命：0.6 ~ 2.0 秒，转换为刻
        this.lifetime = (int) (20.0 * (0.6 + level.random.nextDouble() * 1.4));

        // 初始大小：0.05 + random*0.05
        this.initialSize = 0.05f + (float) level.random.nextDouble() * 0.05f;
        this.quadSize = initialSize;

        // 重新设置正确的精灵（基于实际 lifetime）
        this.setSprite(sprites.get(0, this.lifetime));
    }

    @Override
    public void tick() {
        super.tick();
        // 根据年龄（秒）更新 quadSize：线性减小，速率 0.05/秒
        float ageSec = this.age / 20.0f;
        this.quadSize = Math.max(0, initialSize - ageSec * 0.05f);

        // 如果需要动画帧，可取消下一行注释
        // this.setSpriteFromAge(sprites);
    }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
        // 使用不透明粒子层，纹理来自 particles 图集，受光照影响
        return SingleQuadParticle.Layer.OPAQUE;
    }

    @Override
    public SingleQuadParticle.FacingCameraMode getFacingCameraMode() {
        // JSON 中指定了 "facing_camera_mode": "lookat_xyz"
        return SingleQuadParticle.FacingCameraMode.LOOKAT_XYZ;
    }

    @Override
    public ParticleRenderType getGroup() {
        return ParticleRenderType.SINGLE_QUADS;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            BlizzardWakeParticle blizzardWakeParticle = new BlizzardWakeParticle(level, x, y, z, this.sprite);
            return blizzardWakeParticle;
        }
    }
}