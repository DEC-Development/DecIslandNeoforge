package com.dec.decisland.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

/**
 * 对应基岩版粒子效果：dec:absolute_zero_smoke_small_particle
 * 纹理：textures/particle/blizzard_wake.png
 * 注册时粒子类型 ID 应使用 "absolute_zero_smoke_small_particle"
 */
public class AbsoluteZeroSmokeSingleParticle extends SingleQuadParticle {
    private final SpriteSet sprites;
    private final float rotationRateRadPerTick;   // 弧度/刻
    private static final float MAX_LIFETIME_SEC = 3.0f;     // 粒子最大寿命（秒）
    private static final float GRAVITY = -0.2f;              // y 轴加速度（格/秒²）

    // 贝塞尔曲线控制点（对应 JSON 中 nodes: [-0.05, 2.09, 0.69, 0]）
    private static final float P0 = -0.05f;
    private static final float P1 =  2.09f;
    private static final float P2 =  0.69f;
    private static final float P3 =  0.0f;

    public AbsoluteZeroSmokeSingleParticle(ClientLevel level, double x, double y, double z,
                                           double xSpeed, double ySpeed, double zSpeed,
                                           SpriteSet sprites) {
        super(level, x, y, z, sprites.get(0, 1));  // 临时 sprite，稍后重新设置
        this.sprites = sprites;

        this.quadSize = 0;

        // 初始速度（由发射器 direction 和 initial_speed 决定）
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;

        // 寿命：3 秒 = 60 刻
        this.lifetime = (int) (MAX_LIFETIME_SEC * 20);

        // 初始旋转角度（随机 0~360°）转换为弧度
        this.roll = level.random.nextFloat() * (float) (2 * Math.PI);

        // 旋转速率：-30~30 度/秒 → 弧度/刻
        float rotRateDegPerSec = level.random.nextFloat() * 60 - 30;
        this.rotationRateRadPerTick = (float) Math.toRadians(rotRateDegPerSec);

        // 设置正确的 sprite（静态纹理，无动画）
        this.setSprite(sprites.get(0, this.lifetime));
    }

    @Override
    public void tick() {
        super.tick();
        this.oRoll = this.roll;

        // 应用重力（线性加速度 [0, -0.2, 0]）
        this.xd += 0;  // 无水平加速度
        this.yd += GRAVITY / 20.0;  // 每秒加速度转换为每刻增量
        this.zd += 0;

        // 更新旋转角度
        this.roll = this.roll + rotationRateRadPerTick;

        // 计算当前粒子年龄（秒）
        float ageSec = this.age / 20.0f;
        // 归一化时间 t ∈ [0, 1]
        float t = ageSec / MAX_LIFETIME_SEC;

        // 三次贝塞尔曲线计算 variable.size
        float sizeFactor = cubicBezier(t, P0, P1, P2, P3);
        // 最终大小 = 0.3 * sizeFactor，并限制非负
        this.quadSize = Math.max(0, 0.3f * sizeFactor);
    }

    /**
     * 三次贝塞尔曲线求值：B(t) = (1-t)³·P0 + 3(1-t)²·t·P1 + 3(1-t)·t²·P2 + t³·P3
     */
    private static float cubicBezier(float t, float p0, float p1, float p2, float p3) {
        float u = 1 - t;
        return u * u * u * p0 +
                3 * u * u * t * p1 +
                3 * u * t * t * p2 +
                t * t * t * p3;
    }

    @Override
    protected @NotNull Layer getLayer() {
        // JSON 使用 particles_alpha → 透明渲染
        return Layer.TRANSLUCENT;
    }

    @Override
    public @NotNull FacingCameraMode getFacingCameraMode() {
        // JSON 中 "facing_camera_mode": "rotate_xyz"
        return FacingCameraMode.LOOKAT_XYZ;
    }

    // ========== Provider 内部类，用于注册粒子 ==========
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType particleType,
                                       @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed,
                                       @NotNull RandomSource random) {
            // 注意：x,y,z 和 xSpeed,ySpeed,zSpeed 应由调用者根据发射器逻辑提供
            // （球形发射器、方向向量等）
            return new AbsoluteZeroSmokeSingleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
        }
    }
}