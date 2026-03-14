package com.dec.decisland.api


interface CustomInertia {
    val airInertia: Float
        /**
         * @return 空气中的阻力系数（默认 0.99）
         */
        get() = 0.99f

    val waterInertia: Float
        /**
         * @return 水中的阻力系数（默认 0.8）
         */
        get() = 0.8f
}