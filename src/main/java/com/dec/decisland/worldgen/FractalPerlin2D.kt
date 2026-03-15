package com.dec.decisland.worldgen

import kotlin.math.floor

class FractalPerlin2D(seed: Long) {
    private val perm: IntArray = IntArray(512)

    init {
        val p = IntArray(256) { it }
        var s = seed xor 0x9E3779B97F4A7C15UL.toLong()
        for (i in 255 downTo 0) {
            s = s * 6364136223846793005L + 1442695040888963407L
            val j = ((s ushr 33).toInt() and Int.MAX_VALUE) % (i + 1)
            val tmp = p[i]
            p[i] = p[j]
            p[j] = tmp
        }
        for (i in 0 until 512) {
            perm[i] = p[i and 255]
        }
    }

    fun perlin(x: Double, z: Double): Double {
        val xi = fastFloor(x) and 255
        val zi = fastFloor(z) and 255
        val xf = x - floor(x)
        val zf = z - floor(z)

        val u = fade(xf)
        val v = fade(zf)

        val aa = perm[perm[xi] + zi]
        val ab = perm[perm[xi] + zi + 1]
        val ba = perm[perm[xi + 1] + zi]
        val bb = perm[perm[xi + 1] + zi + 1]

        val x1 = lerp(u, grad(aa, xf, zf), grad(ba, xf - 1.0, zf))
        val x2 = lerp(u, grad(ab, xf, zf - 1.0), grad(bb, xf - 1.0, zf - 1.0))
        return lerp(v, x1, x2)
    }

    private fun fastFloor(v: Double): Int = if (v >= 0.0) v.toInt() else v.toInt() - 1

    private fun fade(t: Double): Double = t * t * t * (t * (t * 6.0 - 15.0) + 10.0)

    private fun lerp(t: Double, a: Double, b: Double): Double = a + t * (b - a)

    private fun grad(hash: Int, x: Double, z: Double): Double =
        when (hash and 7) {
            0 -> x + z
            1 -> -x + z
            2 -> x - z
            3 -> -x - z
            4 -> x
            5 -> -x
            6 -> z
            else -> -z
        }
}
