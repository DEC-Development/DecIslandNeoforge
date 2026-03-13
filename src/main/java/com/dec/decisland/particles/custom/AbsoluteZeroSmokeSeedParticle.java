package com.dec.decisland.particles.custom;

import com.dec.decisland.particles.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * 种子粒子，用于模拟基岩版粒子效果 dec:absolute_zero_smoke_small_particle 的发射器。
 * 在存活的第一 tick 一次性生成多个子粒子（AbsoluteZeroSmokeSingleParticle），
 * 位置为球形随机分布，速度由方向向量 (random(-1,1), 5, random(-1,1)) 乘以初始速度决定。
 * <p>
 * 参数可通过 Provider 传入，以支持不同变体：
 * - numParticles: 生成粒子数量 (对应 JSON 的 num_particles)
 * - radius: 球形半径 (对应 JSON 的 radius)
 * - initialSpeed: 初始速度标量 (对应 JSON 的 initial_speed)
 * 其他参数固定：偏移 [0, 1, 0]，方向 Y 分量 = 5。
 */
public class AbsoluteZeroSmokeSeedParticle extends NoRenderParticle {
    // 固定参数（不通过 Provider 传入）
    private static final double OFFSET_Y = 1.0;
    private static final double DIRECTION_Y = 2.0;

    // 可配置参数
    private final int numParticles;
    private final double radius;
    private final double initialSpeed;

    public AbsoluteZeroSmokeSeedParticle(ClientLevel level, double x, double y, double z,
                                         int numParticles, double radius, double initialSpeed) {
        super(level, x, y, z, 0.0, 0.0, 0.0); // 种子粒子本身不移动
        this.numParticles = numParticles;
        this.radius = radius;
        this.initialSpeed = initialSpeed;
        this.lifetime = 1; // 只存活 1 tick，在第一次 tick 时生成子粒子后立即移除
    }

    @Override
    public void tick() {
        // 第一次 tick 时生成子粒子（age == 0）
        if (this.age == 0) {
            RandomSource random = this.random;
            for (int i = 0; i < numParticles; i++) {
                // 生成球体内均匀随机点（半径 radius）
                double dx, dy, dz;
                do {
                    dx = random.nextDouble() * 2 - 1; // [-1, 1]
                    dy = random.nextDouble() * 2 - 1;
                    dz = random.nextDouble() * 2 - 1;
                } while (dx * dx + dy * dy + dz * dz > 1.0); // 拒绝采样，保证球内均匀

                // 乘以半径并加上偏移（偏移 Y = OFFSET_Y）
                double particleX = this.x + dx * radius;          // offset x = 0
                double particleY = this.y + dy * radius + OFFSET_Y;
                double particleZ = this.z + dz * radius;          // offset z = 0

                // 速度向量 = 方向向量 * initialSpeed
                // 方向向量 x/z 随机 [-1,1]，y 固定为 DIRECTION_Y
                double xSpeed = (random.nextDouble() * 2 - 1) * initialSpeed;
                double ySpeed = DIRECTION_Y * initialSpeed;      // 5 * initialSpeed
                double zSpeed = (random.nextDouble() * 2 - 1) * initialSpeed;

                // 添加子粒子（请确保粒子类型已正确注册）
                this.level.addParticle(ModParticles.ABSOLUTE_ZERO_SMOKE_SINGLE_PARTICLE.get(),
                        particleX, particleY, particleZ,
                        xSpeed, ySpeed, zSpeed);
            }
        }
        // 调用父类 tick 处理 age 递增和移除（lifetime = 1 确保在此次 tick 后移除）
        super.tick();
    }

    /**
     * Provider 用于注册种子粒子（无需 SpriteSet，因为不渲染）。
     * 注册时应使用 event.registerSpecial() 而非 registerSpriteSet()。
     * 可通过构造函数传入发射器参数。
     */
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final int numParticles;
        private final double radius;
        private final double initialSpeed;

        /**
         * @param numParticles 每次发射生成的粒子数（对应 JSON num_particles）
         * @param radius       球形发射半径（对应 JSON radius）
         * @param initialSpeed 初始速度标量（对应 JSON initial_speed）
         */
        public Provider(int numParticles, double radius, double initialSpeed) {
            this.numParticles = numParticles;
            this.radius = radius;
            this.initialSpeed = initialSpeed;
        }

        @Override
        public Particle createParticle(@NotNull SimpleParticleType particleType,
                                       @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed,
                                       @NotNull RandomSource random) {
            // 忽略传入的速度参数，种子粒子本身不移动，使用构造函数传入的参数创建种子粒子
            return new AbsoluteZeroSmokeSeedParticle(level, x, y, z,
                    numParticles, radius, initialSpeed);
        }
    }
}