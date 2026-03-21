package com.dec.decisland.particles.bedrock

import kotlin.math.max
import kotlin.math.min

object BedrockCurves {
    fun evaluateAll(
        curves: Map<String, BedrockCurve>,
        baseContext: MolangContext,
        mutableVariables: MutableMap<String, Double>,
    ): Map<String, Double> {
        if (curves.isEmpty()) {
            return emptyMap()
        }

        val resolved = LinkedHashMap<String, Double>(curves.size)
        curves.forEach { (rawName, curve) ->
            val normalizedName = normalizeCurveName(rawName)
            val curveContext = baseContext.copyWithVariables(
                buildMap {
                    putAll(baseContext.variables)
                    putAll(resolved)
                    putAll(mutableVariables)
                },
            )
            val value = evaluate(curve, curveContext, mutableVariables)
            resolved[normalizedName] = value
        }
        return resolved
    }

    private fun evaluate(curve: BedrockCurve, ctx: MolangContext, mutableVariables: MutableMap<String, Double>): Double {
        val inputValue = Molang.evalDouble(curve.input, ctx, mutableVariables) ?: 0.0
        val rangeValue = Molang.evalDouble(curve.horizontalRange, ctx, mutableVariables)
        val t = normalizeInput(inputValue, rangeValue)

        return when (curve) {
            is BedrockCurve.Linear -> evaluateLinear(curve.nodes, t)
            is BedrockCurve.CatmullRom -> evaluateCatmullRom(curve.nodes, t)
            is BedrockCurve.BezierChain -> evaluateBezierChain(curve.nodes, t)
        }
    }

    private fun normalizeInput(inputValue: Double, horizontalRange: Double?): Double {
        if (horizontalRange == null) {
            return inputValue
        }
        if (horizontalRange == 0.0) {
            return 0.0
        }
        return inputValue / horizontalRange
    }

    private fun evaluateLinear(nodes: List<Double>, rawT: Double): Double {
        if (nodes.isEmpty()) return 0.0
        if (nodes.size == 1) return nodes.first()

        val t = rawT.coerceIn(0.0, 1.0)
        val scaled = t * (nodes.size - 1).toDouble()
        val leftIndex = scaled.toInt().coerceIn(0, nodes.lastIndex)
        val rightIndex = min(leftIndex + 1, nodes.lastIndex)
        val localT = (scaled - leftIndex.toDouble()).coerceIn(0.0, 1.0)
        return lerp(nodes[leftIndex], nodes[rightIndex], localT)
    }

    private fun evaluateCatmullRom(nodes: List<Double>, rawT: Double): Double {
        if (nodes.isEmpty()) return 0.0
        if (nodes.size == 1) return nodes.first()

        val t = rawT.coerceIn(0.0, 1.0)
        val scaled = t * (nodes.size - 1).toDouble()
        val i1 = scaled.toInt().coerceIn(0, nodes.lastIndex)
        val i2 = min(i1 + 1, nodes.lastIndex)
        val i0 = max(i1 - 1, 0)
        val i3 = min(i2 + 1, nodes.lastIndex)
        val localT = (scaled - i1.toDouble()).coerceIn(0.0, 1.0)

        val p0 = nodes[i0]
        val p1 = nodes[i1]
        val p2 = nodes[i2]
        val p3 = nodes[i3]
        val tt = localT * localT
        val ttt = tt * localT

        return 0.5 * (
            (2.0 * p1) +
                (-p0 + p2) * localT +
                (2.0 * p0 - 5.0 * p1 + 4.0 * p2 - p3) * tt +
                (-p0 + 3.0 * p1 - 3.0 * p2 + p3) * ttt
            )
    }

    private fun evaluateBezierChain(nodes: List<BedrockBezierNode>, rawT: Double): Double {
        if (nodes.isEmpty()) return 0.0
        if (nodes.size == 1) return nodes.first().value

        val sorted = nodes.sortedBy(BedrockBezierNode::position)
        val t = rawT.coerceIn(sorted.first().position, sorted.last().position)
        var left = sorted.first()
        for (index in 1 until sorted.size) {
            val right = sorted[index]
            if (t <= right.position) {
                val span = (right.position - left.position).coerceAtLeast(1.0E-9)
                val localT = ((t - left.position) / span).coerceIn(0.0, 1.0)
                return hermite(
                    left.value,
                    right.value,
                    left.slope * span,
                    right.slope * span,
                    localT,
                )
            }
            left = right
        }
        return sorted.last().value
    }

    private fun hermite(p0: Double, p1: Double, m0: Double, m1: Double, t: Double): Double {
        val tt = t * t
        val ttt = tt * t
        val h00 = 2 * ttt - 3 * tt + 1
        val h10 = ttt - 2 * tt + t
        val h01 = -2 * ttt + 3 * tt
        val h11 = ttt - tt
        return h00 * p0 + h10 * m0 + h01 * p1 + h11 * m1
    }

    private fun lerp(a: Double, b: Double, t: Double): Double =
        a + (b - a) * t

    private fun normalizeCurveName(rawName: String): String =
        rawName.lowercase()
            .removePrefix("variable.")
            .removePrefix("v.")
}
