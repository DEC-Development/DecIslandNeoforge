package com.dec.decisland.api;


public interface CustomInertia {
    /**
     * @return 空气中的阻力系数（默认 0.99）
     */
    default float getAirInertia() {
        return 0.99F;
    }

    /**
     * @return 水中的阻力系数（默认 0.8）
     */
    default float getWaterInertia() {
        return 0.8F;
    }
}