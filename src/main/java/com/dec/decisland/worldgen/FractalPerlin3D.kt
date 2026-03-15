package com.dec.decisland.worldgen

import kotlin.math.floor

class FractalPerlin3D(seed: Long) {
    private val perm: IntArray = IntArray(512)

    init {
        val p = IntArray(256) { it }
        var s = seed xor 0xD1B54A32D192ED03UL.toLong()
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

    fun perlin(x: Double, y: Double, z: Double): Double {
        val xi = fastFloor(x) and 255
        val yi = fastFloor(y) and 255
        val zi = fastFloor(z) and 255

        val xf = x - floor(x)
        val yf = y - floor(y)
        val zf = z - floor(z)

        val u = fade(xf)
        val v = fade(yf)
        val w = fade(zf)

        val aaa = perm[perm[perm[xi] + yi] + zi]
        val aab = perm[perm[perm[xi] + yi] + zi + 1]
        val aba = perm[perm[perm[xi] + yi + 1] + zi]
        val abb = perm[perm[perm[xi] + yi + 1] + zi + 1]
        val baa = perm[perm[perm[xi + 1] + yi] + zi]
        val bab = perm[perm[perm[xi + 1] + yi] + zi + 1]
        val bba = perm[perm[perm[xi + 1] + yi + 1] + zi]
        val bbb = perm[perm[perm[xi + 1] + yi + 1] + zi + 1]

        val x1 = lerp(u, grad(aaa, xf, yf, zf), grad(baa, xf - 1.0, yf, zf))
        val x2 = lerp(u, grad(aba, xf, yf - 1.0, zf), grad(bba, xf - 1.0, yf - 1.0, zf))
        val y1 = lerp(v, x1, x2)

        val x3 = lerp(u, grad(aab, xf, yf, zf - 1.0), grad(bab, xf - 1.0, yf, zf - 1.0))
        val x4 = lerp(u, grad(abb, xf, yf - 1.0, zf - 1.0), grad(bbb, xf - 1.0, yf - 1.0, zf - 1.0))
        val y2 = lerp(v, x3, x4)

        return lerp(w, y1, y2)
    }

    private fun fastFloor(v: Double): Int = if (v >= 0.0) v.toInt() else v.toInt() - 1

    private fun fade(t: Double): Double = t * t * t * (t * (t * 6.0 - 15.0) + 10.0)

    private fun lerp(t: Double, a: Double, b: Double): Double = a + t * (b - a)

    private fun grad(hash: Int, x: Double, y: Double, z: Double): Double =
        when (hash and 15) {
            0 -> x + y
            1 -> -x + y
            2 -> x - y
            3 -> -x - y
            4 -> x + z
            5 -> -x + z
            6 -> x - z
            7 -> -x - z
            8 -> y + z
            9 -> -y + z
            10 -> y - z
            11 -> -y - z
            12 -> x + y
            13 -> -x + y
            14 -> y - z
            else -> -y - z
        }
}
